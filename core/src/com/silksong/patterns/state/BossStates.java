package com.silksong.patterns.state;

import com.badlogic.gdx.math.MathUtils;
import com.silksong.entities.boss.Boss;
import com.silksong.entities.enemies.Bat;
import com.silksong.entities.boss.GhostSword;
import com.silksong.systems.sound.SoundManager;

/**
 * ПАТТЕРН: State
 *
 * Три фазы босса — Огромный Скелет-Король.
 *
 * Фаза 1 «Защитник»   (100–70% HP) — синее свечение, ходит + рывок с циркулем
 * Фаза 2 «Стрелок»    (70–30% HP)  — фиолетовое свечение, призывает призрачные мечи
 * Фаза 3 «Безумие»    (<30% HP)    — красное свечение, быстрые удары + летучие мыши
 */
public class BossStates {

    // ========== Базовый интерфейс ==========

    public interface BossState {
        void enter(Boss boss);
        void update(Boss boss, float delta);
        void exit(Boss boss);
        String getName();
        /** Цвет свечения как RGBA float (для ShaderProgram или ColorFilter) */
        float[] getGlowColor();
    }

    // ========== ФАЗА 1: Защитник ==========

    public static class DefenderPhase implements BossState {
        private float actionTimer   = 0f;
        private float dashCooldown  = 3f;

        // Синее свечение
        private static final float[] GLOW = {0.2f, 0.5f, 1.0f, 0.8f};

        @Override
        public void enter(Boss boss) {
            boss.setCurrentTexture("phase1");
            SoundManager.getInstance().playBossRoar();
            actionTimer = 0f;
        }

        @Override
        public void update(Boss boss, float delta) {
            actionTimer += delta;

            // Медленное движение к игроку
            boss.walkTowardsPlayer(delta, 80f); // скорость 80

            // Рывок каждые dashCooldown секунд
            if (actionTimer >= dashCooldown) {
                actionTimer = 0f;
                boss.dashTowardsPlayer(300f);
                SoundManager.getInstance().playBossSlam();
            }

            // Переход в фазу 2 при 70% HP
            if (boss.getHpPercent() <= 0.70f) {
                boss.setState(new ShooterPhase());
            }
        }

        @Override public void exit(Boss boss) {}
        @Override public String getName() { return "DEFENDER"; }
        @Override public float[] getGlowColor() { return GLOW; }
    }

    // ========== ФАЗА 2: Стрелок ==========

    public static class ShooterPhase implements BossState {
        private float summonTimer    = 0f;
        private float summonCooldown = 2.5f;
        private float moveTimer      = 0f;

        private static final float[] GLOW = {0.6f, 0.1f, 0.9f, 0.85f};

        @Override
        public void enter(Boss boss) {
            boss.setCurrentTexture("phase2");
            SoundManager.getInstance().playBossRoar();
            summonTimer = summonCooldown * 0.5f; // first attack sooner
            moveTimer   = 0f;
        }

        @Override
        public void update(Boss boss, float delta) {
            // Slow walk toward player
            boss.walkTowardsPlayer(delta, 60f);

            // Summon ghost swords regularly
            summonTimer += delta;
            if (summonTimer >= summonCooldown) {
                summonTimer = 0f;
                boss.summonGhostSwords(3);
            }

            // Phase 3 at 30% HP
            if (boss.getHpPercent() <= 0.30f) {
                boss.setState(new RagePhase());
            }
        }

        @Override public void exit(Boss boss) {}
        @Override public String getName() { return "SHOOTER"; }
        @Override public float[] getGlowColor() { return GLOW; }
    }

    // ========== ФАЗА 3: Безумие ==========

    public static class RagePhase implements BossState {
        private float actionTimer   = 0f;
        private float comboTimer    = 0f;
        private float batTimer      = 0f;
        private int   comboCount    = 0;

        private static final float COMBO_INTERVAL  = 0.25f;
        private static final float BAT_INTERVAL    = 5f;
        private static final int   COMBO_MAX       = 5;

        // Красное свечение
        private static final float[] GLOW = {1.0f, 0.1f, 0.1f, 0.9f};

        @Override
        public void enter(Boss boss) {
            boss.setCurrentTexture("phase3");
            // Втыкаем циркуль в землю — эффект землетрясения
            boss.triggerCameraShake(0.5f, 10f);
            SoundManager.getInstance().playBossRoar();
            SoundManager.getInstance().playBossSlam();
        }

        @Override
        public void update(Boss boss, float delta) {
            // Быстрые удары-серией
            comboTimer += delta;
            if (comboTimer >= COMBO_INTERVAL) {
                comboTimer = 0f;
                comboCount++;
                boss.performComboAttack(comboCount);
                if (comboCount >= COMBO_MAX) {
                    comboCount = 0;
                    boss.walkTowardsPlayer(delta, 160f); // Быстрое движение
                }
            }

            // Призыв летучих мышей
            batTimer += delta;
            if (batTimer >= BAT_INTERVAL) {
                batTimer = 0f;
                boss.spawnBats(3);
                boss.triggerCameraShake(0.3f, 6f);
            }
        }

        @Override public void exit(Boss boss) {}
        @Override public String getName() { return "RAGE"; }
        @Override public float[] getGlowColor() { return GLOW; }
    }

    // ========== СМЕРТЬ ==========

    public static class DeadState implements BossState {
        private static final float[] GLOW = {1f, 1f, 1f, 0f};

        @Override
        public void enter(Boss boss) {
            boss.playDeathAnimation();
        }

        @Override
        public void update(Boss boss, float delta) {
            // В BossScreen отслеживается boss.isDead() → переход к победному экрану
        }

        @Override public void exit(Boss boss) {}
        @Override public String getName() { return "DEAD"; }
        @Override public float[] getGlowColor() { return GLOW; }
    }
}
