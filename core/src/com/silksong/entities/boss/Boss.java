package com.silksong.entities.boss;
import com.silksong.utils.GameSettings;
import com.silksong.utils.Difficulty;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.silksong.entities.enemies.Bat;
import com.silksong.patterns.factory.EntityFactory;
import com.silksong.patterns.state.BossStates;
import com.silksong.systems.assets.AssetManagerSingleton;
import com.silksong.systems.sound.SoundManager;
import com.silksong.utils.PlaceholderTextures;

import java.util.ArrayList;
import java.util.List;

/**
 * Босс — Скелет-Король в порванной королевской мантии.
 * В одной руке — сломанный циркуль-копьё, в другой — призрачный щит.
 *
 * ПАТТЕРН: State — три фазы боя, каждая со своим поведением и цветом свечения.
 *
 * Фаза 1 (100-70%): синий,     ходит + рывок
 * Фаза 2 (70-30%):  фиолетовый, призрачные мечи
 * Фаза 3 (<30%):    красный,    ярость + летучие мыши
 */
public class Boss {

    // ===== Физика =====
    private final Vector2   position;
    private final Vector2   velocity;
    private final Rectangle bounds;

    private static final float WIDTH  = 80f;
    private static final float HEIGHT = 120f;

    // ===== Статы =====
    private float maxHealth     = 500f;
    private float currentHealth = 500f;
    private float contactDamage = 20f;

    // ===== Паттерн State =====
    private BossStates.BossState currentState;

    // ===== Проджектайлы и спауны =====
    private final List<GhostSword> ghostSwords;
    private final List<Bat>        spawnedBats;

    // ===== Ссылка на игрока (для AI) =====
    private Vector2 playerPosition;

    // ===== Визуал =====
    private String  currentTexture = "phase1";
    private float   glowTimer      = 0f;
    private boolean alive          = true;
    private boolean dead           = false;
    private boolean slaming        = false;
    private boolean summoning      = false;
    private float   slamTimer      = 0f;
    private float   summonTimer2   = 0f;

    // ===== Camera shake =====
    private float   shakeTimer     = 0f;
    private float   shakeMagnitude = 0f;

    // Центр арены
    private float ARENA_CENTER_X = 1200f; // updated in constructor
    private static final float ARENA_CENTER_Y = 120f;

    public Boss(float x, float y) {
        ARENA_CENTER_X = x + WIDTH / 2f;
        Difficulty diff = GameSettings.getInstance().getDifficulty();
        maxHealth     = 500f * diff.enemyHealthMult;
        currentHealth = maxHealth;
        this.position    = new Vector2(x, y);
        this.velocity    = new Vector2(0, 0);
        this.bounds      = new Rectangle(x, y, WIDTH, HEIGHT);
        this.ghostSwords = new ArrayList<>();
        this.spawnedBats = new ArrayList<>();

        // Начинаем с фазы 1
        setState(new BossStates.DefenderPhase());
    }

    // ============================================================
    //  UPDATE
    // ============================================================

    public void update(float delta, Vector2 playerPos) {
        if (!alive) return;
        this.playerPosition = playerPos;

        // Обновляем таймеры
        glowTimer += delta;
        if (shakeTimer > 0) shakeTimer -= delta;

        // Обновляем текущую фазу
        currentState.update(this, delta);

        // Обновляем позицию
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);

        // Обновляем призрачные мечи
        for (int i = ghostSwords.size() - 1; i >= 0; i--) {
            ghostSwords.get(i).update(delta);
            if (!ghostSwords.get(i).isAlive()) ghostSwords.remove(i);
        }

        // Обновляем летучих мышей
        for (int i = spawnedBats.size() - 1; i >= 0; i--) {
            Bat b = spawnedBats.get(i);
            b.update(delta, playerPos.x, playerPos.y);
            if (!b.isAlive()) spawnedBats.remove(i);
        }

        // Трение (торможение)
        velocity.x *= 0.85f;
    }

    // ============================================================
    //  RENDER
    // ============================================================

    public void render(SpriteBatch batch) {
        if (!alive) return;

        // Получаем цвет свечения из текущего состояния
        float[] glow = currentState.getGlowColor();

        // Пульсирующий эффект через sin
        float pulse = 0.7f + 0.3f * MathUtils.sin(glowTimer * 4f);

        // Рендерим босса с цветовым тинтом
        Texture tex = getBossTexture();
        if (tex != null) {
            batch.setColor(
                glow[0] * pulse + (1f - pulse) * 1f,
                glow[1] * pulse + (1f - pulse) * 1f,
                glow[2] * pulse + (1f - pulse) * 1f,
                1f
            );
            batch.draw(tex, position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(Color.WHITE);
        }

        // Рендерим призрачные мечи
        for (GhostSword gs : ghostSwords) {
            gs.render(batch);
        }

        // Рендерим летучих мышей
        for (Bat bat : spawnedBats) {
            bat.render(batch);
        }
    }

    private Texture getBossTexture() {
        AssetManagerSingleton am = AssetManagerSingleton.getInstance();
        PlaceholderTextures ph   = PlaceholderTextures.getInstance();
        Texture t;
        switch (currentTexture) {
            case "phase2": t = am.getTexture(AssetManagerSingleton.BOSS_PHASE2); return t != null ? t : ph.getBossP2();
            case "phase3": t = am.getTexture(AssetManagerSingleton.BOSS_PHASE3); return t != null ? t : ph.getBossP3();
            default:       t = am.getTexture(AssetManagerSingleton.BOSS_PHASE1); return t != null ? t : ph.getBossP1();
        }
    }

    // ============================================================
    //  МЕТОДЫ AI (вызываются из BossStates)
    // ============================================================

    /** Медленно идёт к игроку */
    public void walkTowardsPlayer(float delta, float speed) {
        if (playerPosition == null) return;
        float dx    = playerPosition.x - position.x;
        velocity.x  = (dx > 0 ? 1f : -1f) * speed;
    }

    /** Рывок с циркулем в сторону игрока */
    public void dashTowardsPlayer(float speed) {
        if (playerPosition == null) return;
        float dx   = playerPosition.x - position.x;
        velocity.x = (dx > 0 ? 1f : -1f) * speed;
    }

    /** Отступает в центр арены. Возвращает true когда добрался. */
    public boolean retreatToCenter(float delta, float speed) {
        float dx   = ARENA_CENTER_X - position.x;
        float dy   = ARENA_CENTER_Y - position.y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < 20f) return true;

        velocity.x = (dx / dist) * speed;
        velocity.y = (dy / dist) * speed;
        return false;
    }

    /** Призвать N призрачных мечей в сторону игрока */
    public void summonGhostSwords(int count) {
        summoning = true;
        if (playerPosition == null) return;
        SoundManager.getInstance().playBossRoar();

        for (int i = 0; i < count; i++) {
            // Небольшой разброс по углу
            float offsetX = MathUtils.random(-60f, 60f);
            float offsetY = MathUtils.random(-30f, 30f);
            float targetX = playerPosition.x + offsetX;
            float targetY = playerPosition.y + offsetY;

            float speed   = 180f + i * 30f;
            GhostSword gs = new GhostSword(
                position.x + WIDTH / 2f,
                position.y + HEIGHT / 2f,
                targetX, targetY, speed
            );
            ghostSwords.add(gs);
        }
    }

    /** Быстрый удар-серия (фаза 3) */
    public void triggerSlam() { slaming = true; }
    public void triggerSummon() { summoning = true; }

    public void performComboAttack(int hitNum) {
        slaming = true;
        velocity.x = (playerPosition != null && playerPosition.x > position.x) ? 250f : -250f;
        if (hitNum % 3 == 0) {
            triggerCameraShake(0.15f, 5f);
            SoundManager.getInstance().playBossSlam();
        }
    }

    /** Призвать летучих мышей (фаза 3) */
    public void spawnBats(int count) {
        summoning = true;
        for (int i = 0; i < count; i++) {
            float bx = position.x + MathUtils.random(-100f, 100f);
            float by = position.y + HEIGHT + 20f + i * 30f;
            spawnedBats.add(EntityFactory.createBat(bx, by));
        }
    }

    /** Триггер тряски камеры — данные читает Screen */
    public void triggerCameraShake(float duration, float magnitude) {
        this.shakeTimer     = duration;
        this.shakeMagnitude = magnitude;
    }

    public void playDeathAnimation() {
        // В реальной реализации — проигрывать анимацию смерти
        alive = false;
        dead  = true;
    }

    // ============================================================
    //  ПОЛУЧЕНИЕ УРОНА
    // ============================================================

    public void takeDamage(float damage) {
        if (!alive) return;
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            setState(new BossStates.DeadState());
        }
    }

    // ============================================================
    //  ПАТТЕРН STATE
    // ============================================================

    public void setState(BossStates.BossState newState) {
        if (currentState != null) currentState.exit(this);
        currentState = newState;
        currentState.enter(this);
    }

    // ============================================================
    //  ГЕТТЕРЫ
    // ============================================================

    public void setCurrentTexture(String key) { this.currentTexture = key; }
    public float getHpPercent()       { return maxHealth > 0 ? currentHealth / maxHealth : 0f; }
    public float getMaxHealth()       { return maxHealth; }
    public float getCurrentHealth()   { return currentHealth; }
    public boolean isAlive()          { return alive; }
    public boolean isDead()           { return dead; }
    public boolean isSlaming()        { boolean v=slaming; slaming=false; return v; }
    public boolean isSummoning()      { boolean v=summoning; summoning=false; return v; }
    public Rectangle getBounds()      { return bounds; }
    public Vector2   getPosition()    { return position; }
    public float getContactDamage()   { return contactDamage; }
    public List<GhostSword> getGhostSwords() { return ghostSwords; }
    public List<Bat>        getSpawnedBats() { return spawnedBats; }
    public boolean isShaking()        { return shakeTimer > 0; }
    public float   getShakeMagnitude(){ return shakeMagnitude; }
    public BossStates.BossState getCurrentState() { return currentState; }
}
