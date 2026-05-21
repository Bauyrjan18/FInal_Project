package com.silksong.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.silksong.systems.assets.AssetManagerSingleton;
import com.silksong.utils.PlaceholderTextures;

public class Bat extends Enemy {
    private float waveTimer   = 0f;
    private float attackTimer = 0f;
    private float attackCooldown = 2f;
    private float baseY;
    private static final float AGGRO_RANGE  = 250f;
    private static final float ATTACK_RANGE = 40f;

    public Bat(float x, float y) { super(x, y, 28f, 20f, 40f, 4f, 120f, 15); this.baseY = y; }

    @Override
    public void update(float delta, float playerX, float playerY) {
        if (!alive) return;
        float dist = dist(playerX, playerY);
        if (dist < AGGRO_RANGE) {
            moveTowards(playerX, playerY, speed, delta);
        } else {
            waveTimer += delta * 2f;
            position.y = baseY + MathUtils.sin(waveTimer) * 20f;
            position.x += (facingRight ? 1 : -1) * speed * 0.5f * delta;
            bounds.setPosition(position.x, position.y);
        }
        if (attackTimer > 0) attackTimer -= delta;
    }

    public boolean canAttack(float px, float py) {
        if (attackTimer > 0 || !alive) return false;
        if (dist(px, py) < ATTACK_RANGE) { attackTimer = attackCooldown; return true; }
        return false;
    }

    private float dist(float px, float py) {
        return (float)Math.sqrt(Math.pow(px-position.x,2)+Math.pow(py-position.y,2));
    }

    @Override
    protected Texture getTexture() {
        Texture t = AssetManagerSingleton.getInstance().getTexture(AssetManagerSingleton.BAT_TEXTURE);
        return t != null ? t : PlaceholderTextures.getInstance().getBat();
    }
}
