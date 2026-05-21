package com.silksong.systems.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * PATTERN: Singleton
 *
 * Only loads files that ACTUALLY EXIST in the assets folder.
 * If a file is missing — it is silently skipped.
 * The game uses colored rectangles as placeholders for missing textures.
 *
 * To add real assets: place files in assets/ and the paths below will
 * match automatically.
 */
public class AssetManagerSingleton {

    private static AssetManagerSingleton instance;
    private final AssetManager manager;
    private boolean loadingStarted = false;
    private boolean loadingDone    = false;

    // ===== Texture paths =====
    public static final String PLAYER_IDLE      = "textures/player/idle.png";
    public static final String PLAYER_RUN       = "textures/player/run.png";
    public static final String PLAYER_JUMP      = "textures/player/jump.png";
    public static final String PLAYER_ATTACK    = "textures/player/attack.png";
    public static final String PLAYER_DASH      = "textures/player/dash.png";
    public static final String ARROW_TEXTURE    = "textures/player/arrow.png";
    public static final String SKELETON_TEXTURE = "textures/enemies/skeleton.png";
    public static final String BAT_TEXTURE      = "textures/enemies/bat.png";
    public static final String EXP_ORB_TEXTURE  = "textures/misc/exp_orb.png";
    public static final String LEVER_TEXTURE    = "textures/misc/lever.png";
    public static final String BOSS_PHASE1      = "textures/boss/boss_phase1.png";
    public static final String BOSS_PHASE2      = "textures/boss/boss_phase2.png";
    public static final String BOSS_PHASE3      = "textures/boss/boss_phase3.png";
    public static final String GHOST_SWORD      = "textures/boss/ghost_sword.png";
    public static final String BOSS_SHIELD      = "textures/boss/boss_shield.png";
    public static final String BG_GATES         = "textures/backgrounds/bg_gates.png";
    public static final String BG_GREAT_HALL    = "textures/backgrounds/bg_great_hall.png";
    public static final String BG_THRONE_ROOM   = "textures/backgrounds/bg_throne_room.png";
    public static final String UI_HEALTH_BAR    = "textures/ui/health_bar.png";
    public static final String UI_BOSS_BAR      = "textures/ui/boss_health_bar.png";

    // ===== Audio paths =====
    public static final String MUSIC_MENU       = "audio/music/menu_theme.mp3";
    public static final String MUSIC_GATES      = "audio/music/gates_theme.mp3";
    public static final String MUSIC_GREAT_HALL = "audio/music/great_hall_theme.mp3";
    public static final String MUSIC_BOSS       = "audio/music/boss_theme.mp3";

    public static final String SFX_SWORD_SWING  = "audio/sfx/sword_swing.wav";
    public static final String SFX_ARROW_SHOOT  = "audio/sfx/arrow_shoot.wav";
    public static final String SFX_PLAYER_HIT   = "audio/sfx/player_hit.wav";
    public static final String SFX_PLAYER_DEATH = "audio/sfx/player_death.wav";
    public static final String SFX_ENEMY_HIT    = "audio/sfx/enemy_hit.wav";
    public static final String SFX_ENEMY_DEATH  = "audio/sfx/enemy_death.wav";
    public static final String SFX_EXP_COLLECT  = "audio/sfx/exp_collect.wav";
    public static final String SFX_LEVER        = "audio/sfx/lever_pull.wav";
    public static final String SFX_BOSS_ROAR    = "audio/sfx/boss_roar.wav";
    public static final String SFX_BOSS_SLAM    = "audio/sfx/boss_slam.wav";
    public static final String SFX_DASH         = "audio/sfx/dash.wav";
    public static final String SFX_JUMP         = "audio/sfx/jump.wav";
    public static final String SFX_LEVEL_UP     = "audio/sfx/level_up.wav";

    private AssetManagerSingleton() {
        manager = new AssetManager();
    }

    public static AssetManagerSingleton getInstance() {
        if (instance == null) instance = new AssetManagerSingleton();
        return instance;
    }

    /**
     * Queues only files that physically exist on disk.
     * Missing files are skipped — game uses colored placeholders instead.
     */
    public void loadAll() {
        if (loadingStarted) return;
        loadingStarted = true;

        tryTexture(PLAYER_IDLE);   tryTexture(PLAYER_RUN);
        tryTexture(PLAYER_JUMP);   tryTexture(PLAYER_ATTACK);
        tryTexture(PLAYER_DASH);   tryTexture(ARROW_TEXTURE);
        tryTexture(SKELETON_TEXTURE); tryTexture(BAT_TEXTURE);
        tryTexture(EXP_ORB_TEXTURE);  tryTexture(LEVER_TEXTURE);
        tryTexture(BOSS_PHASE1);   tryTexture(BOSS_PHASE2);
        tryTexture(BOSS_PHASE3);   tryTexture(GHOST_SWORD);
        tryTexture(BOSS_SHIELD);   tryTexture(BG_GATES);
        tryTexture(BG_GREAT_HALL); tryTexture(BG_THRONE_ROOM);
        tryTexture(UI_HEALTH_BAR); tryTexture(UI_BOSS_BAR);

        tryMusic(MUSIC_MENU);  tryMusic(MUSIC_GATES);
        tryMusic(MUSIC_GREAT_HALL); tryMusic(MUSIC_BOSS);

        trySfx(SFX_SWORD_SWING); trySfx(SFX_ARROW_SHOOT);
        trySfx(SFX_PLAYER_HIT);  trySfx(SFX_PLAYER_DEATH);
        trySfx(SFX_ENEMY_HIT);   trySfx(SFX_ENEMY_DEATH);
        trySfx(SFX_EXP_COLLECT); trySfx(SFX_LEVER);
        trySfx(SFX_BOSS_ROAR);   trySfx(SFX_BOSS_SLAM);
        trySfx(SFX_DASH);        trySfx(SFX_JUMP);
        trySfx(SFX_LEVEL_UP);

        int queued = manager.getQueuedAssets();
        Gdx.app.log("AssetManager", "Queued " + queued + " assets.");

        // If nothing was queued, mark as done immediately
        if (queued == 0) {
            loadingDone = true;
            Gdx.app.log("AssetManager", "No asset files found — using placeholders. Add files to assets/");
        }
    }

    /**
     * Call every frame in LoadingScreen.
     * Returns true when loading is complete (or nothing to load).
     */
    public boolean update() {
        if (loadingDone) return true;
        if (manager.getQueuedAssets() == 0 && manager.getLoadedAssets() == 0) {
            loadingDone = true;
            return true;
        }
        boolean done = manager.update();
        if (done) loadingDone = true;
        return done;
    }

    /** 0.0 – 1.0 progress. Returns 1.0 if nothing to load. */
    public float getProgress() {
        if (loadingDone) return 1f;
        if (manager.getQueuedAssets() == 0) return 1f;
        return manager.getProgress();
    }

    private void tryTexture(String path) {
        try {
            if (Gdx.files.internal(path).exists()) {
                manager.load(path, Texture.class);
            }
        } catch (Exception e) {
            Gdx.app.log("AssetManager", "Skip texture: " + path);
        }
    }

    private void tryMusic(String path) {
        try {
            if (Gdx.files.internal(path).exists()) {
                manager.load(path, Music.class);
            }
        } catch (Exception e) {
            Gdx.app.log("AssetManager", "Skip music: " + path);
        }
    }

    private void trySfx(String path) {
        try {
            if (Gdx.files.internal(path).exists()) {
                manager.load(path, Sound.class);
            }
        } catch (Exception e) {
            Gdx.app.log("AssetManager", "Skip sfx: " + path);
        }
    }

    public Texture getTexture(String path) {
        if (manager.isLoaded(path)) return manager.get(path, Texture.class);
        return null; // Caller draws a colored rectangle instead
    }

    public Music getMusic(String path) {
        if (manager.isLoaded(path)) return manager.get(path, Music.class);
        return null;
    }

    public Sound getSound(String path) {
        if (manager.isLoaded(path)) return manager.get(path, Sound.class);
        return null;
    }

    public boolean isLoaded(String path) { return manager.isLoaded(path); }

    public void dispose() { manager.dispose(); }
}
