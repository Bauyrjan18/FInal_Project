package com.silksong.entities.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.silksong.patterns.observer.ExpObserver;
import com.silksong.patterns.observer.ExpSubject;
import com.silksong.patterns.state.PlayerStates;
import com.silksong.patterns.strategy.AttackStrategies;
import com.silksong.systems.assets.AssetManagerSingleton;
import com.silksong.systems.level.LevelSystem;
import com.silksong.systems.sound.SoundManager;
import com.silksong.utils.AnimationManager;
import com.silksong.utils.PlaceholderTextures;

import java.util.ArrayList;
import java.util.List;

public class Player implements ExpSubject {

    // ── Physics ──────────────────────────────────────────────────
    public static final float WIDTH       = 48f;
    public static final float HEIGHT      = 64f;
    private static final float GRAVITY    = -980f;
    private static final float JUMP_FORCE = 540f;
    private static final float MOVE_SPEED = 220f;
    private static final float MAX_FALL   = -700f;
    private static final float COYOTE_TIME = 0.10f;
    private static final float JUMP_BUFFER = 0.12f;

    private final Vector2   position;
    private final Vector2   velocity;
    private final Rectangle bounds;
    private Rectangle       swordHitbox;

    private boolean onGround    = false;
    private float   coyoteTimer = 0f;
    private float   jumpBuffer  = 0f;
    private boolean facingRight = true;
    private boolean invincible  = false;
    private boolean swordActive = false;
    private boolean alive       = true;
    private String  animKey     = "idle";

    // ── Stats ────────────────────────────────────────────────────
    private int   maxHealth;
    private int   currentHealth;
    private float dashCooldownLeft   = 0f;
    private static final float DASH_COOLDOWN = 1.0f;
    private float attackCooldownLeft = 0f;
    private float invTimer           = 0f;
    private static final float INV_DURATION = 0.55f;
    private float hurtFlashTimer     = 0f;

    // ── XP ───────────────────────────────────────────────────────
    private int totalXp = 0;

    // ── Super bar ─────────────────────────────────────────────────
    private float superCharge          = 0f;
    private static final float SUPER_MAX    = 10f;
    private static final float SUPER_DAMAGE = 30f;  // ~6% of boss HP per use
    private final List<SoulWave> soulWaves  = new ArrayList<>();

    // ── Patterns ─────────────────────────────────────────────────
    private PlayerStates.PlayerState          currentState;
    private final AttackStrategies.SwordStrategy swordStrategy;
    private final AttackStrategies.BowStrategy   bowStrategy;
    private final List<Arrow>                    arrows    = new ArrayList<>();
    private final List<ExpObserver>              observers = new ArrayList<>();
    private final LevelSystem                    levelSystem;
    private final AnimationManager               animManager;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────
    public Player(float x, float y, LevelSystem levelSystem) {
        this.levelSystem   = levelSystem;
        this.position      = new Vector2(x, y);
        this.velocity      = new Vector2(0, 0);
        this.bounds        = new Rectangle(x + WIDTH * 0.2f, y, WIDTH * 0.6f, HEIGHT * 0.9f);
        this.swordHitbox   = new Rectangle(0, 0, 58f, HEIGHT * 0.7f);
        this.maxHealth     = levelSystem.getMaxHealth();
        this.currentHealth = maxHealth;
        this.swordStrategy = new AttackStrategies.SwordStrategy();
        this.bowStrategy   = new AttackStrategies.BowStrategy();
        this.animManager   = new AnimationManager();
        setState(new PlayerStates.IdleState());
    }

    // ─────────────────────────────────────────────────────────────
    //  UPDATE
    // ─────────────────────────────────────────────────────────────
    public void update(float delta) {
        animManager.update(delta);
        if (!alive) return;

        // Timers
        if (dashCooldownLeft   > 0) dashCooldownLeft   -= delta;
        if (attackCooldownLeft > 0) attackCooldownLeft -= delta;
        if (hurtFlashTimer     > 0) hurtFlashTimer     -= delta;
        if (jumpBuffer         > 0) jumpBuffer         -= delta;
        if (invTimer           > 0) { invTimer -= delta; if (invTimer <= 0) invincible = false; }

        // Coyote time
        if (!onGround) coyoteTimer -= delta;

        // Gravity
        if (!(currentState instanceof PlayerStates.DashState)) {
            velocity.y += GRAVITY * delta;
            velocity.y  = Math.max(velocity.y, MAX_FALL);
        }

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        // Hitboxes
        bounds.setPosition(position.x + WIDTH * 0.2f, position.y);
        if (swordActive) {
            float sx = facingRight
                ? position.x + WIDTH * 0.8f
                : position.x - swordStrategy.getRange();
            swordHitbox.setPosition(sx, position.y + HEIGHT * 0.15f);
        }

        // Arrows
        for (int i = arrows.size()-1; i >= 0; i--) {
            arrows.get(i).update(delta);
            if (!arrows.get(i).isAlive()) arrows.remove(i);
        }

        // Soul Waves
        for (int i = soulWaves.size()-1; i >= 0; i--) {
            soulWaves.get(i).update(delta);
            if (!soulWaves.get(i).isAlive()) soulWaves.remove(i);
        }

        // Super bar charges passively (1 per second → full in SUPER_MAX seconds)
        if (superCharge < SUPER_MAX) {
            superCharge = Math.min(SUPER_MAX, superCharge + delta);
        }

        // Jump buffer: landed while jump was buffered
        if (jumpBuffer > 0 && onGround) {
            jumpBuffer = 0;
            doJump();
        }

        currentState.update(this, delta);
    }

    // ─────────────────────────────────────────────────────────────
    //  RENDER
    // ─────────────────────────────────────────────────────────────
    public void render(SpriteBatch batch) {
        // Invincibility flash
        if (invincible) {
            int flashFrame = (int)(invTimer * 12f);
            if (flashFrame % 2 == 0) {
                for (Arrow    a  : arrows)    a.render(batch);
                for (SoulWave sw : soulWaves) sw.render(batch);
                return;
            }
        }

        if (animManager.isLoaded()) {
            TextureRegion frame = animManager.getFrame(animKey);
            if (frame != null) {
                if ("hurt".equals(animKey)) batch.setColor(1f, 0.35f, 0.35f, 1f);
                else                        batch.setColor(Color.WHITE);
                if (!facingRight) batch.draw(frame, position.x + WIDTH, position.y, -WIDTH, HEIGHT);
                else              batch.draw(frame, position.x, position.y, WIDTH, HEIGHT);
            }
        } else {
            applyStateTint(batch);
            Texture tex = AssetManagerSingleton.getInstance().getTexture(getTexPath());
            if (tex == null) tex = PlaceholderTextures.getInstance().getPlayer();
            if (!facingRight) batch.draw(tex, position.x + WIDTH, position.y, -WIDTH, HEIGHT);
            else              batch.draw(tex, position.x, position.y, WIDTH, HEIGHT);
        }

        batch.setColor(Color.WHITE);
        for (Arrow    a  : arrows)    a.render(batch);
        for (SoulWave sw : soulWaves) sw.render(batch);
    }

    private void applyStateTint(SpriteBatch b) {
        if ("hurt".equals(animKey))   { b.setColor(1f, 0.2f, 0.2f, 1f);   return; }
        if ("attack".equals(animKey)) { b.setColor(1f, 0.9f, 0.3f, 1f);   return; }
        if ("dash".equals(animKey))   { b.setColor(0.3f, 1f, 1f, 0.85f);  return; }
        if ("jump".equals(animKey))   { b.setColor(0.6f, 0.8f, 1f, 1f);   return; }
        b.setColor(Color.WHITE);
    }

    private String getTexPath() {
        switch (animKey) {
            case "run": case "dash": return AssetManagerSingleton.PLAYER_RUN;
            case "jump":             return AssetManagerSingleton.PLAYER_JUMP;
            case "attack":           return AssetManagerSingleton.PLAYER_ATTACK;
            default:                 return AssetManagerSingleton.PLAYER_IDLE;
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  INPUT COMMANDS
    // ─────────────────────────────────────────────────────────────
    public void moveLeft() {
        if (isDashing() || isAttacking()) return;
        velocity.x = -MOVE_SPEED; facingRight = false;
    }

    public void moveRight() {
        if (isDashing() || isAttacking()) return;
        velocity.x = MOVE_SPEED; facingRight = true;
    }

    public void stopMoving() {
        if (isDashing() || isAttacking()) return;
        velocity.x = 0;
    }

    public void jump() {
        if (onGround || coyoteTimer > 0) doJump();
        else jumpBuffer = JUMP_BUFFER;
    }

    private void doJump() {
        velocity.y  = JUMP_FORCE;
        onGround    = false;
        coyoteTimer = 0f;
        SoundManager.getInstance().playJump();
        setState(new PlayerStates.JumpState());
    }

    public void dash() {
        if (dashCooldownLeft > 0 || isDashing()) return;
        dashCooldownLeft = DASH_COOLDOWN;
        SoundManager.getInstance().playDash();
        setState(new PlayerStates.DashState());
    }

    public void swordAttack() {
        if (attackCooldownLeft > 0 || isDashing()) return;
        attackCooldownLeft = swordStrategy.getCooldown();
        swordStrategy.performAttack(position.x, position.y, facingRight, arrows, levelSystem);
        setState(onGround ? new PlayerStates.AttackState() : new PlayerStates.AirAttackState());
    }

    public void superAttack() {
        if (superCharge < SUPER_MAX) return;
        if (isDashing()) return;
        superCharge = 0f;
        float dir    = facingRight ? 1f : -1f;
        float spawnX = position.x + (facingRight ? WIDTH * 0.9f : WIDTH * 0.1f);
        float spawnY = position.y + HEIGHT * 0.55f;
        soulWaves.add(new SoulWave(spawnX, spawnY, dir, SUPER_DAMAGE));
        animManager.resetStateTime();
        setState(new PlayerStates.AttackState());
    }

    public void bowAttack() {
        if (attackCooldownLeft > 0 || isDashing()) return;
        attackCooldownLeft = bowStrategy.getCooldown();
        // Spawn arrow center at chest height, just outside player sprite
        float spawnX = facingRight
            ? position.x + WIDTH + 4f          // right of player
            : position.x - 4f;                 // left of player
        float spawnY = position.y + HEIGHT * 0.60f; // chest/shoulder height
        bowStrategy.performAttack(spawnX, spawnY, facingRight, arrows, levelSystem);
        setState(new PlayerStates.BowState());
    }

    // ─────────────────────────────────────────────────────────────
    //  DAMAGE
    // ─────────────────────────────────────────────────────────────
    public void takeDamage(float damage) {
        if (invincible || !alive) return;
        currentHealth -= (int) damage;
        SoundManager.getInstance().playPlayerHit();
        setInvincible(true);
        hurtFlashTimer = INV_DURATION;
        if (!(currentState instanceof PlayerStates.DashState))
            setState(new PlayerStates.HurtState());
        if (currentHealth <= 0) { currentHealth = 0; die(); }
    }

    private void die() {
        alive = false;
        SoundManager.getInstance().playPlayerDeath();
        setState(new PlayerStates.DeadState());
    }

    public void collectExp(int amount) {
        totalXp += amount;
        SoundManager.getInstance().playExpCollect();
        notifyObservers(amount);
        maxHealth = levelSystem.getMaxHealth();
    }

    // ─────────────────────────────────────────────────────────────
    //  OBSERVER
    // ─────────────────────────────────────────────────────────────
    @Override public void addObserver(ExpObserver o)    { observers.add(o); }
    @Override public void removeObserver(ExpObserver o) { observers.remove(o); }
    @Override public void notifyObservers(int exp)      { for (ExpObserver o : observers) o.onExpCollected(exp); }

    // ─────────────────────────────────────────────────────────────
    //  STATE
    // ─────────────────────────────────────────────────────────────
    public void setState(PlayerStates.PlayerState s) {
        if (currentState != null) currentState.exit(this);
        currentState = s;
        currentState.enter(this);
    }

    // ─────────────────────────────────────────────────────────────
    //  GROUND COLLISION (called by Screen)
    // ─────────────────────────────────────────────────────────────
    public void setOnGround(boolean g, float groundY) {
        this.onGround = g;
        if (g) { position.y = groundY; velocity.y = 0f; coyoteTimer = COYOTE_TIME; }
    }

    // ─────────────────────────────────────────────────────────────
    //  GETTERS / SETTERS
    // ─────────────────────────────────────────────────────────────
    public void setAnimationKey(String key)      { this.animKey = key; }
    public void activateSwordHitbox(boolean a)   { this.swordActive = a; }
    public void setInvincible(boolean inv)        { this.invincible = inv; if (inv) invTimer = INV_DURATION; }

    public Vector2    getPosition()        { return position; }
    public Vector2    getVelocity()        { return velocity; }
    public Rectangle  getBounds()          { return bounds; }
    public Rectangle  getSwordHitbox()     { return swordHitbox; }
    public boolean    isSwordActive()      { return swordActive; }
    public boolean    isOnGround()         { return onGround; }
    public boolean    isFacingRight()      { return facingRight; }
    public boolean    isAlive()            { return alive; }
    public int        getCurrentHealth()   { return currentHealth; }
    public int        getMaxHealth()       { return maxHealth; }
    public float      getHpPercent()       { return (float) currentHealth / maxHealth; }
    public List<Arrow>     getArrows()     { return arrows; }
    public List<SoulWave>  getSoulWaves()  { return soulWaves; }
    public int        getTotalXp()         { return totalXp; }
    public String     getAnimKey()         { return animKey; }
    public AnimationManager getAnimManager() { return animManager; }

    public float  getSuperCharge()  { return superCharge; }
    public float  getSuperMax()     { return SUPER_MAX; }
    public boolean isSuperReady()   { return superCharge >= SUPER_MAX; }

    public boolean isDashing()   { return currentState instanceof PlayerStates.DashState; }
    public boolean isAttacking() { return currentState instanceof PlayerStates.AttackState
                                       || currentState instanceof PlayerStates.AirAttackState; }

    public void dispose() { animManager.dispose(); }
}
