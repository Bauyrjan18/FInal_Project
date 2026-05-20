package com.silksong.entities.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Arrow projectile.
 * position = CENTER of arrow for collision.
 * Rendered with tail behind, tip in front.
 * Collision: pure distance check against enemy center.
 */
public class Arrow {

    private final Vector2   position;   // CENTER of arrow
    private final Vector2   velocity;
    private final Rectangle hitbox;
    private final float     damage;
    private boolean alive    = true;
    private float   traveled = 0f;

    // Hit effect
    private boolean justHit   = false;
    private float   hitTimer  = 0f;
    private static final float HIT_FLASH = 0.06f; // show flash before disappear

    private static Texture arrowTex;
    private static Texture hitTex;

    public static final float LENGTH    = 36f;
    public static final float THICKNESS =  7f;
    private static final float MAX_RANGE = 750f;
    private static final float SPEED     = 420f;

    public Arrow(float spawnX, float spawnY, boolean goRight, float damage) {
        this.velocity = new Vector2(goRight ? SPEED : -SPEED, 0f);
        // spawn at hand, position = center of arrow
        this.position = new Vector2(spawnX, spawnY);
        this.damage   = damage;
        this.hitbox   = new Rectangle(spawnX - LENGTH/2f, spawnY - THICKNESS, LENGTH, THICKNESS*2f);
        if (arrowTex == null) buildTextures();
    }

    // Legacy constructor for BowStrategy compatibility
    public Arrow(float x, float y, Vector2 vel, float damage) {
        this.velocity = new Vector2(vel.x, 0f); // force horizontal
        this.position = new Vector2(x, y);
        this.damage   = damage;
        this.hitbox   = new Rectangle(x - LENGTH/2f, y - THICKNESS, LENGTH, THICKNESS*2f);
        if (arrowTex == null) buildTextures();
    }

    private static void buildTextures() {
        // Arrow sprite 36x7
        Pixmap pm = new Pixmap(36, 7, Pixmap.Format.RGBA8888);
        pm.setColor(0,0,0,0); pm.fill();
        // Fletching (back, left side) - blue
        pm.setColor(0.2f, 0.5f, 1f, 1f);
        pm.fillRectangle(0, 0, 8, 3);
        pm.fillRectangle(0, 4, 8, 3);
        pm.setColor(0.4f, 0.7f, 1f, 0.8f);
        pm.fillRectangle(1, 1, 6, 2);
        pm.fillRectangle(1, 4, 6, 2);
        // Shaft - wood brown
        pm.setColor(0.68f, 0.46f, 0.16f, 1f);
        pm.fillRectangle(7, 2, 21, 3);
        pm.setColor(0.85f, 0.60f, 0.25f, 1f);
        pm.fillRectangle(7, 3, 21, 1);
        // Tip - bright metal (RIGHT side = front)
        pm.setColor(0.88f, 0.90f, 0.98f, 1f);
        pm.fillRectangle(28, 1, 7, 5);
        pm.fillRectangle(30, 0, 5, 1);
        pm.fillRectangle(30, 6, 5, 1);
        // Glow on tip
        pm.setColor(1f, 0.95f, 0.6f, 0.9f);
        pm.drawPixel(34, 3); pm.drawPixel(35, 3);
        arrowTex = new Texture(pm);
        arrowTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        pm.dispose();

        // Hit flash - small yellow burst
        Pixmap hp = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        hp.setColor(0,0,0,0); hp.fill();
        hp.setColor(1f, 0.9f, 0.3f, 0.8f); hp.fillCircle(8, 8, 7);
        hp.setColor(1f, 1f, 0.9f, 0.6f);   hp.fillCircle(8, 8, 4);
        hitTex = new Texture(hp); hp.dispose();
    }

    public void update(float delta) {
        if (!alive) return;
        if (justHit) {
            hitTimer += delta;
            if (hitTimer >= HIT_FLASH) alive = false;
            return; // don't move during flash
        }
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        hitbox.setPosition(position.x - LENGTH/2f, position.y - THICKNESS);
        traveled += Math.abs(velocity.x * delta);
        if (traveled >= MAX_RANGE) alive = false;
        if (position.y < -40f) alive = false;
    }

    public void render(SpriteBatch batch) {
        if (!alive) return;

        if (justHit) {
            // Hit flash burst
            float a = 1f - hitTimer / HIT_FLASH;
            batch.setColor(1f, 0.9f, 0.3f, a);
            batch.draw(hitTex, position.x - 8f, position.y - 8f, 16f, 16f);
            batch.setColor(Color.WHITE);
            return;
        }

        batch.setColor(Color.WHITE);
        boolean goRight = velocity.x > 0;
        if (goRight) {
            // tip at position.x + LENGTH/2, tail at position.x - LENGTH/2
            batch.draw(arrowTex,
                position.x - LENGTH/2f, position.y - THICKNESS/2f,
                0, THICKNESS/2f, LENGTH, THICKNESS,
                1f, 1f, 0f, 0, 0, 36, 7, false, false);
        } else {
            // flipped: tip on LEFT
            batch.draw(arrowTex,
                position.x + LENGTH/2f, position.y - THICKNESS/2f,
                0, THICKNESS/2f, -LENGTH, THICKNESS,
                1f, 1f, 0f, 0, 0, 36, 7, false, false);
        }
    }

    /** Called when arrow hits something — shows flash then disappears */
    public void hit() {
        if (justHit) return;
        justHit  = true;
        hitTimer = 0f;
        velocity.set(0, 0); // stop moving
    }

    public static void disposeShared() {
        if (arrowTex != null) { arrowTex.dispose(); arrowTex = null; }
        if (hitTex   != null) { hitTex.dispose();   hitTex   = null; }
    }

    public Rectangle getHitbox()   { return hitbox; }
    public float     getDamage()   { return damage; }
    public boolean   isAlive()     { return alive; }
    public Vector2   getPosition() { return position; }
}
