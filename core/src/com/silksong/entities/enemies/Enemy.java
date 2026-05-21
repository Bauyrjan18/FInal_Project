package com.silksong.entities.enemies;
import com.silksong.utils.GameSettings;
import com.silksong.utils.Difficulty;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Базовый класс врага. Skeleton и Bat его расширяют.
 */
public abstract class Enemy {

    protected Vector2    position;
    protected Vector2    velocity;
    protected Rectangle  bounds;
    protected float      maxHealth;
    protected float      currentHealth;
    protected float      damage;
    protected float      speed;
    protected float      width;
    protected float      height;
    protected boolean    alive = true;
    protected boolean    facingRight = true;

    // Сколько опыта выпадает при смерти
    protected int expReward;

    public Enemy(float x, float y, float w, float h, float health, float damage, float speed, int expReward) {
        // Apply difficulty multipliers
        Difficulty diff = GameSettings.getInstance().getDifficulty();
        float hpMult  = diff.enemyHealthMult;
        float dmgMult = diff.enemyDamageMult;

        this.position      = new Vector2(x, y);
        this.velocity      = new Vector2(0, 0);
        this.bounds        = new Rectangle(x, y, w, h);
        this.maxHealth     = health * hpMult;
        this.currentHealth = health * hpMult;
        this.damage        = damage * dmgMult;
        this.speed         = speed;
        this.width         = w;
        this.height        = h;
        this.expReward     = expReward;
    }

    public abstract void update(float delta, float playerX, float playerY);

    public void render(SpriteBatch batch) {
        if (!alive) return;
        Texture tex = getTexture();
        if (tex != null) {
            if (!facingRight) {
                batch.draw(tex, position.x + width, position.y, -width, height);
            } else {
                batch.draw(tex, position.x, position.y, width, height);
            }
        }
    }

    protected abstract Texture getTexture();

    public void takeDamage(float dmg) {
        if (!alive) return;
        currentHealth -= dmg;
        if (currentHealth <= 0) {
            currentHealth = 0;
            alive = false;
        }
    }

    protected void moveTowards(float targetX, float targetY, float spd, float delta) {
        float dx = targetX - position.x;
        float dy = targetY - position.y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist > 5f) {
            velocity.x    = (dx / dist) * spd;
            velocity.y    = (dy / dist) * spd;
            facingRight   = dx > 0;
        } else {
            velocity.set(0, 0);
        }
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);
    }

    public Rectangle getBounds()         { return bounds; }
    public Vector2   getPosition()       { return position; }
    public boolean   isAlive()           { return alive; }
    public float     getDamage()         { return damage; }
    public int       getExpReward()      { return expReward; }
    public float     getHpPercent()      { return currentHealth / maxHealth; }
}
