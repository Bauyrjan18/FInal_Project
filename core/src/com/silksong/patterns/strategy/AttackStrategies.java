package com.silksong.patterns.strategy;

import com.badlogic.gdx.math.Vector2;
import com.silksong.entities.player.Arrow;
import com.silksong.systems.sound.SoundManager;
import com.silksong.systems.level.LevelSystem;

import java.util.List;

/**
 * ПАТТЕРН: Strategy
 *
 * Стратегии атаки игрока — меч и лук.
 * Позволяет менять тип атаки без изменения Player.
 * В будущем легко добавить магию или другое оружие.
 */
public class AttackStrategies {

    // ========== Базовый интерфейс ==========

    public interface AttackStrategy {
        /**
         * @param x         позиция X игрока
         * @param y         позиция Y игрока
         * @param facingRight направление взгляда
         * @param arrows    список стрел для добавления
         * @param level     система уровней для получения урона
         */
        void performAttack(float x, float y, boolean facingRight,
                           List<Arrow> arrows, LevelSystem level);

        /** Задержка перезарядки в секундах */
        float getCooldown();
    }

    // ========== Ближний бой — Меч ==========

    public static class SwordStrategy implements AttackStrategy {
        private static final float COOLDOWN    = 0.4f;
        private static final float RANGE       = 60f;

        @Override
        public void performAttack(float x, float y, boolean facingRight,
                                  List<Arrow> arrows, LevelSystem level) {
            // Меч бьёт в hitbox вокруг игрока — реальная проверка коллизий
            // в Player.update() через attackHitbox
            SoundManager.getInstance().playSwordSwing();
            // Визуальный hitbox задаётся в Player
        }

        @Override public float getCooldown() { return COOLDOWN; }
        public float getRange() { return RANGE; }
    }

    // ========== Дальний бой — Лук ==========

    public static class BowStrategy implements AttackStrategy {
        private static final float COOLDOWN      = 0.6f;
        private static final float ARROW_SPEED   = 400f;

        @Override
        public void performAttack(float x, float y, boolean facingRight,
                                  List<Arrow> arrows, LevelSystem level) {
            float damage = level != null ? level.getArrowDamage() : 8f;
            float dirX   = facingRight ? 1f : -1f;
            // Fire at ~15 degrees upward — arrow peaks about 40px above launch
            // then arcs down. Gravity in Arrow = 28f/s keeps it gentle.
            float velX = dirX * ARROW_SPEED;
            float velY = 0f;  // perfectly horizontal
            Arrow arrow = new Arrow(x, y, new Vector2(velX, velY), damage);
            arrows.add(arrow);
            SoundManager.getInstance().playArrowShoot();
        }

        @Override public float getCooldown() { return COOLDOWN; }
    }
}
