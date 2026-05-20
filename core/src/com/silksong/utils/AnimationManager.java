package com.silksong.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Manages player animations.
 *
 * Priority:
 *   1. Real sprite sheets (NightBorne PNG converted from GIF) — if found in assets/
 *   2. Programmatic pixel art animations from SpriteFrames — always available
 *
 * HOW TO ADD YOUR GIF ANIMATIONS:
 *   1. Convert GIF to sprite sheet: https://ezgif.com/gif-to-sprite
 *   2. Place in assets/textures/player/ as NightBorne_idle_sheet.png etc.
 *   3. Set FRAMES_* constants below to match your frame counts.
 */
public class AnimationManager {

    // ── Paths for real sprite sheets ─────────────────────────────
    public static final String SHEET_IDLE   = "textures/player/NightBorne_idle_sheet.png";
    public static final String SHEET_RUN    = "textures/player/NightBorne_run_sheet.png";
    public static final String SHEET_ATTACK = "textures/player/NightBorne_attack_sheet.png";
    public static final String SHEET_DEATH  = "textures/player/NightBorne_death_sheet.png";
    public static final String SHEET_HURT   = "textures/player/NightBorne_hurt_sheet.png";
    public static final String SHEET_JUMP   = "textures/player/NightBorne_jump_sheet.png";

    // ── Frame counts for real sheets — change to match your GIFs ─
    public static final int FRAMES_IDLE   = 6;
    public static final int FRAMES_RUN    = 8;
    public static final int FRAMES_ATTACK = 6;
    public static final int FRAMES_DEATH  = 8;
    public static final int FRAMES_HURT   = 3;
    public static final int FRAMES_JUMP   = 4;

    // ── Frame speed ───────────────────────────────────────────────
    public static final float FPS_IDLE   = 0.13f;
    public static final float FPS_RUN    = 0.08f;
    public static final float FPS_ATTACK = 0.055f;
    public static final float FPS_DEATH  = 0.10f;
    public static final float FPS_HURT   = 0.07f;
    public static final float FPS_JUMP   = 0.12f;
    public static final float FPS_DASH   = 0.06f;

    // ── Animations ────────────────────────────────────────────────
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> runAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> deathAnim;
    private Animation<TextureRegion> hurtAnim;
    private Animation<TextureRegion> jumpAnim;
    private Animation<TextureRegion> dashAnim;
    private Animation<TextureRegion> bowAnim;

    // Stored textures (for disposal)
    private final java.util.List<Texture> ownedTextures = new java.util.ArrayList<>();

    private boolean usingRealSprites = false;
    private float stateTime = 0f;

    public AnimationManager() {
        // Try to load real sprite sheets first
        Texture idleTex = tryLoad(SHEET_IDLE);
        if (idleTex != null) {
            // Real sprites found — load all
            usingRealSprites = true;
            idleAnim   = buildFromSheet(idleTex,           FRAMES_IDLE,   FPS_IDLE,   Animation.PlayMode.LOOP);
            runAnim    = buildFromSheetOpt(SHEET_RUN,       FRAMES_RUN,    FPS_RUN,    Animation.PlayMode.LOOP);
            attackAnim = buildFromSheetOpt(SHEET_ATTACK,    FRAMES_ATTACK, FPS_ATTACK, Animation.PlayMode.NORMAL);
            deathAnim  = buildFromSheetOpt(SHEET_DEATH,     FRAMES_DEATH,  FPS_DEATH,  Animation.PlayMode.NORMAL);
            hurtAnim   = buildFromSheetOpt(SHEET_HURT,      FRAMES_HURT,   FPS_HURT,   Animation.PlayMode.NORMAL);
            jumpAnim   = buildFromSheetOpt(SHEET_JUMP,      FRAMES_JUMP,   FPS_JUMP,   Animation.PlayMode.NORMAL);
            Gdx.app.log("AnimationManager", "NightBorne sprite sheets loaded!");
        } else {
            // Fallback — draw pixel art animations in code
            usingRealSprites = false;
            Texture idleSheet   = SpriteFrames.buildIdle();
            Texture runSheet    = SpriteFrames.buildRun();
            Texture attackSheet = SpriteFrames.buildAttack();
            Texture jumpSheet   = SpriteFrames.buildJump();
            Texture hurtSheet   = SpriteFrames.buildHurt();
            Texture deathSheet  = SpriteFrames.buildDeath();
            Texture dashSheet   = SpriteFrames.buildDash();
            Texture bowSheet    = SpriteFrames.buildBow();

            ownedTextures.add(idleSheet);   ownedTextures.add(runSheet);
            ownedTextures.add(attackSheet); ownedTextures.add(jumpSheet);
            ownedTextures.add(hurtSheet);   ownedTextures.add(deathSheet);
            ownedTextures.add(dashSheet); ownedTextures.add(bowSheet);

            idleAnim   = SpriteFrames.toAnim(idleSheet,   6, FPS_IDLE,   Animation.PlayMode.LOOP);
            runAnim    = SpriteFrames.toAnim(runSheet,    6, FPS_RUN,    Animation.PlayMode.LOOP);
            attackAnim = SpriteFrames.toAnim(attackSheet, 5, FPS_ATTACK, Animation.PlayMode.NORMAL);
            jumpAnim   = SpriteFrames.toAnim(jumpSheet,   4, FPS_JUMP,   Animation.PlayMode.NORMAL);
            hurtAnim   = SpriteFrames.toAnim(hurtSheet,   3, FPS_HURT,   Animation.PlayMode.NORMAL);
            deathAnim  = SpriteFrames.toAnim(deathSheet,  5, FPS_DEATH,  Animation.PlayMode.NORMAL);
            dashAnim   = SpriteFrames.toAnim(dashSheet,   4, FPS_DASH,   Animation.PlayMode.NORMAL);
            bowAnim    = SpriteFrames.toAnim(bowSheet,    4, 0.10f,      Animation.PlayMode.NORMAL);
            Gdx.app.log("AnimationManager", "Using built-in pixel art animations.");
        }
    }

    private Texture tryLoad(String path) {
        try {
            if (Gdx.files.internal(path).exists()) {
                Texture t = new Texture(Gdx.files.internal(path));
                ownedTextures.add(t);
                return t;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private Animation<TextureRegion> buildFromSheet(Texture sheet, int frames, float fps, Animation.PlayMode mode) {
        return SpriteFrames.toAnim(sheet, frames, fps, mode);
    }

    private Animation<TextureRegion> buildFromSheetOpt(String path, int frames, float fps, Animation.PlayMode mode) {
        Texture t = tryLoad(path);
        if (t == null) return idleAnim;
        return SpriteFrames.toAnim(t, frames, fps, mode);
    }

    public void update(float delta) { stateTime += delta; }

    public void resetStateTime() { stateTime = 0f; }

    public boolean isLoaded() { return true; } // always true now

    public TextureRegion getFrame(String state) {
        Animation<TextureRegion> anim = pick(state);
        return anim.getKeyFrame(stateTime);
    }

    public boolean isAnimFinished(String state) {
        return pick(state).isAnimationFinished(stateTime);
    }

    private Animation<TextureRegion> pick(String state) {
        switch (state) {
            case "run":    return runAnim    != null ? runAnim    : idleAnim;
            case "jump":   return jumpAnim   != null ? jumpAnim   : idleAnim;
            case "attack": return attackAnim != null ? attackAnim : idleAnim;
            case "dash":   return dashAnim   != null ? dashAnim   : runAnim != null ? runAnim : idleAnim;
            case "bow":    return bowAnim    != null ? bowAnim    : idleAnim;
            case "hurt":   return hurtAnim   != null ? hurtAnim   : idleAnim;
            case "death":  return deathAnim  != null ? deathAnim  : idleAnim;
            default:       return idleAnim;
        }
    }

    public boolean isUsingRealSprites() { return usingRealSprites; }

    public void dispose() {
        for (Texture t : ownedTextures) t.dispose();
        ownedTextures.clear();
    }
}
