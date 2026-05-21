package com.silksong.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Pixel art drawn programmatically via Pixmap.
 * Every sprite is hand-crafted pixel by pixel.
 *
 * Color helper: setC(pm, hex) sets color from 0xRRGGBBAA integer.
 */
public class PixelArt {

    // ── Color palette constants ─────────────────────────────────
    // Player
    private static final int P_SKIN   = 0xF5CBA7FF;
    private static final int P_HAIR   = 0x2C1A0EFF;
    private static final int P_CAPE   = 0x6B2FA0FF;
    private static final int P_ARMOR  = 0x3A3A5AFF;
    private static final int P_ARMOR2 = 0x5A5A8AFF;
    private static final int P_SWORD  = 0xC0C0D0FF;
    private static final int P_SWORD2 = 0xFFD700FF;
    private static final int P_EYE    = 0x00FFFFFF;
    private static final int P_BOOT   = 0x2A1A0AFF;
    private static final int P_BELT   = 0x8B6914FF;

    // Skeleton
    private static final int SK_BONE  = 0xE8DEC8FF;
    private static final int SK_BONE2 = 0xBFB49AFF;
    private static final int SK_EYE   = 0xFF3300FF;
    private static final int SK_DARK  = 0x181008FF;
    private static final int SK_RUST  = 0x7A4A1EFF;

    // Bat
    private static final int BAT_BODY  = 0x3A1A5AFF;
    private static final int BAT_WING  = 0x5A2A7AFF;
    private static final int BAT_EYE   = 0xFF2200FF;
    private static final int BAT_FANG  = 0xFFFFFFFF;

    // Boss
    private static final int BS_BONE   = 0xD4C9A8FF;
    private static final int BS_BONE2  = 0xA89880FF;
    private static final int BS_ROBE   = 0x1A0A2AFF;
    private static final int BS_ROBE2  = 0x2A1040FF;
    private static final int BS_GOLD   = 0xCFAE2EFF;
    private static final int BS_CROWN  = 0xFFD700FF;
    private static final int BS_COMP1  = 0x4488FFFF; // phase1 glow blue
    private static final int BS_COMP2  = 0x9944FFFF; // phase2 glow purple
    private static final int BS_COMP3  = 0xFF2222FF; // phase3 glow red
    private static final int BS_SHIELD = 0x2244AAFF;
    private static final int BS_CIRK   = 0x888888FF;
    private static final int BS_CIRK2  = 0xAAAACCFF;

    // Misc
    private static final int ORB_G    = 0x22FF55FF;
    private static final int ORB_G2   = 0x00CC33FF;
    private static final int ORB_WH   = 0xAAFFBBFF;
    private static final int LEV_WOOD = 0x6B3A1FFF;
    private static final int LEV_META = 0x888899FF;
    private static final int LEV_ON   = 0xFFAA00FF;
    private static final int ARR_WOOD = 0xC8A060FF;
    private static final int ARR_META = 0xD0D8E8FF;
    private static final int ARR_FETH = 0x4488CCFF;

    private static final int TRANS    = 0x00000000;

    // ── Helper ─────────────────────────────────────────────────

    private static void c(Pixmap pm, int rgba) {
        pm.setColor(
            ((rgba >> 24) & 0xFF) / 255f,
            ((rgba >> 16) & 0xFF) / 255f,
            ((rgba >>  8) & 0xFF) / 255f,
            ( rgba        & 0xFF) / 255f
        );
    }

    private static void px(Pixmap pm, int x, int y, int rgba) {
        c(pm, rgba); pm.drawPixel(x, y);
    }

    private static void rect(Pixmap pm, int x, int y, int w, int h, int rgba) {
        c(pm, rgba); pm.fillRectangle(x, y, w, h);
    }

    private static void hline(Pixmap pm, int x, int y, int len, int rgba) {
        for (int i = 0; i < len; i++) px(pm, x+i, y, rgba);
    }

    private static Pixmap transparent(int w, int h) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0,0,0,0); pm.fill();
        return pm;
    }

    // ── PLAYER  32×48 ──────────────────────────────────────────

    public static Texture drawPlayer() {
        Pixmap pm = transparent(32, 48);

        // Cape / back (drawn first, behind body)
        rect(pm,  6, 12, 4, 22, P_CAPE);
        rect(pm,  5, 16, 5, 18, P_CAPE);
        px(pm, 4, 20, P_CAPE); px(pm, 4, 21, P_CAPE);

        // Body — armor torso
        rect(pm,  9, 16, 14, 16, P_ARMOR);
        // Armor highlight
        rect(pm, 10, 17,  5,  6, P_ARMOR2);
        rect(pm, 16, 17,  5,  3, P_ARMOR2);
        // Belt
        rect(pm,  9, 29,  14, 3, P_BELT);

        // Head
        rect(pm, 10,  4, 12, 11, P_SKIN);
        // Hair
        rect(pm, 10,  4, 12,  4, P_HAIR);
        rect(pm, 10,  5,  2,  6, P_HAIR);
        rect(pm, 20,  5,  2,  5, P_HAIR);
        // Eyes
        px(pm, 13, 10, P_EYE); px(pm, 14, 10, P_EYE);
        px(pm, 17, 10, P_EYE); px(pm, 18, 10, P_EYE);
        px(pm, 14, 11, P_HAIR); px(pm, 17, 11, P_HAIR);

        // Neck
        rect(pm, 14, 15,  4,  2, P_SKIN);

        // Left arm
        rect(pm,  6, 17,  4, 12, P_ARMOR);
        rect(pm,  6, 27,  4,  3, P_SKIN);
        // Right arm (sword arm)
        rect(pm, 22, 17,  4, 12, P_ARMOR);
        rect(pm, 22, 27,  4,  3, P_SKIN);

        // Sword
        rect(pm, 25, 10,  2, 20, P_SWORD);
        rect(pm, 23, 18,  6,  2, P_SWORD2); // crossguard
        px(pm, 25,  9, P_SWORD2); px(pm, 26,  9, P_SWORD2); // tip glow

        // Legs
        rect(pm, 10, 32,  5, 12, P_ARMOR);
        rect(pm, 17, 32,  5, 12, P_ARMOR);
        // Boots
        rect(pm,  9, 41,  7,  7, P_BOOT);
        rect(pm, 16, 41,  7,  7, P_BOOT);
        // Boot highlight
        hline(pm, 10, 42, 5, P_ARMOR2);
        hline(pm, 17, 42, 5, P_ARMOR2);

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── SKELETON  32×48 ────────────────────────────────────────

    public static Texture drawSkeleton() {
        Pixmap pm = transparent(32, 48);

        // Skull
        rect(pm, 10,  2, 12, 11, SK_BONE);
        rect(pm, 11,  1, 10,  1, SK_BONE);
        rect(pm, 10, 13,  3,  2, SK_BONE); // jaw left
        rect(pm, 19, 13,  3,  2, SK_BONE); // jaw right
        rect(pm, 13, 12,  6,  2, SK_BONE2); // jaw
        // Eye sockets
        rect(pm, 12,  6,  3,  3, SK_DARK);
        rect(pm, 17,  6,  3,  3, SK_DARK);
        px(pm, 13,  7, SK_EYE); px(pm, 14,  7, SK_EYE); // red glow
        px(pm, 18,  7, SK_EYE); px(pm, 19,  7, SK_EYE);
        // Nose hole
        px(pm, 15, 10, SK_DARK); px(pm, 16, 10, SK_DARK);
        // Teeth
        for (int i = 0; i < 5; i++) {
            px(pm, 13 + i*2, 13, SK_BONE); px(pm, 13 + i*2, 14, SK_BONE2);
        }

        // Spine / ribcage
        rect(pm, 14, 15,  4, 14, SK_BONE2);
        // Ribs
        for (int i = 0; i < 4; i++) {
            int ry = 16 + i*3;
            rect(pm,  9, ry,  5, 2, SK_BONE);
            rect(pm, 18, ry,  5, 2, SK_BONE);
        }

        // Arms (bones)
        rect(pm,  6, 16,  4, 14, SK_BONE);
        rect(pm, 22, 16,  4, 14, SK_BONE);
        // Elbow joints
        rect(pm,  6, 22,  4,  2, SK_BONE2);
        rect(pm, 22, 22,  4,  2, SK_BONE2);
        // Hands (claw)
        px(pm,  6, 30, SK_BONE); px(pm,  7, 31, SK_BONE);
        px(pm,  8, 30, SK_BONE); px(pm,  9, 31, SK_BONE);
        px(pm, 22, 30, SK_BONE); px(pm, 23, 31, SK_BONE);
        px(pm, 24, 30, SK_BONE); px(pm, 25, 31, SK_BONE);

        // Pelvis
        rect(pm,  9, 30, 14,  4, SK_BONE2);

        // Leg bones
        rect(pm, 10, 34,  4, 10, SK_BONE);
        rect(pm, 18, 34,  4, 10, SK_BONE);
        // Knee
        rect(pm, 10, 39,  4,  2, SK_BONE2);
        rect(pm, 18, 39,  4,  2, SK_BONE2);
        // Feet
        rect(pm,  8, 44,  8,  3, SK_BONE);
        rect(pm, 16, 44,  8,  3, SK_BONE);

        // Rusty sword in hand
        rect(pm, 22, 12,  3, 18, SK_RUST);
        px(pm, 21, 18, SK_RUST); px(pm, 25, 18, SK_RUST); // guard

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── BAT  28×20 ─────────────────────────────────────────────

    public static Texture drawBat() {
        Pixmap pm = transparent(28, 20);

        // Left wing
        rect(pm,  0,  4,  8,  2, BAT_WING);
        rect(pm,  1,  3,  6,  1, BAT_WING);
        rect(pm,  0,  5, 10,  6, BAT_WING);
        rect(pm,  2, 11,  7,  3, BAT_WING);
        rect(pm,  3, 14,  4,  2, BAT_WING);
        // Wing membrane darker
        rect(pm,  1,  6,  8,  4, BAT_BODY);

        // Right wing
        rect(pm, 20,  4,  8,  2, BAT_WING);
        rect(pm, 21,  3,  6,  1, BAT_WING);
        rect(pm, 18,  5, 10,  6, BAT_WING);
        rect(pm, 19, 11,  7,  3, BAT_WING);
        rect(pm, 21, 14,  4,  2, BAT_WING);
        rect(pm, 19,  6,  8,  4, BAT_BODY);

        // Body
        rect(pm,  9,  4, 10, 10, BAT_BODY);
        rect(pm, 10,  3,  8,  1, BAT_BODY);
        rect(pm, 10, 14,  8,  3, BAT_BODY);

        // Head / face
        rect(pm, 10,  2,  8,  8, BAT_BODY);
        // Ears
        rect(pm, 10,  0,  2,  3, BAT_BODY);
        rect(pm, 16,  0,  2,  3, BAT_BODY);
        // Eyes
        px(pm, 12,  4, BAT_EYE); px(pm, 15,  4, BAT_EYE);
        // Fangs
        px(pm, 12,  9, BAT_FANG); px(pm, 15,  9, BAT_FANG);

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── EXP ORB  16×16 ─────────────────────────────────────────

    public static Texture drawExpOrb() {
        Pixmap pm = transparent(16, 16);
        // Outer glow ring
        rect(pm,  4,  1,  8,  1, ORB_G2);
        rect(pm,  2,  2, 12,  1, ORB_G2);
        rect(pm,  1,  3,  1, 10, ORB_G2);
        rect(pm, 14,  3,  1, 10, ORB_G2);
        rect(pm,  2, 13, 12,  1, ORB_G2);
        rect(pm,  4, 14,  8,  1, ORB_G2);
        // Main body
        rect(pm,  3,  3, 10, 10, ORB_G);
        rect(pm,  2,  4, 12,  8, ORB_G);
        rect(pm,  4,  2,  8, 12, ORB_G);
        // Highlight
        rect(pm,  5,  4,  3,  3, ORB_WH);
        px(pm,  4,  5, ORB_WH);
        // Dark center
        rect(pm,  7,  7,  3,  3, ORB_G2);

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── LEVER  24×40 ───────────────────────────────────────────

    public static Texture drawLever() {
        Pixmap pm = transparent(24, 40);
        // Base plate
        rect(pm,  2, 30, 20,  8, LEV_META);
        rect(pm,  3, 29, 18,  1, 0xAAAAAAFF);
        // Pivot
        rect(pm,  9, 24,  6,  8, LEV_META);
        rect(pm, 10, 23,  4,  2, 0xCCCCCCFF);
        // Handle rod (pulled right = OFF position)
        rect(pm, 13, 10,  3, 16, LEV_WOOD);
        rect(pm, 14,  9,  2,  2, LEV_WOOD);
        // Handle grip
        rect(pm, 12,  6,  6,  6, LEV_META);
        rect(pm, 13,  5,  4,  2, 0xCCCCCCFF);
        // Glow when OFF (grey)
        rect(pm, 12,  6,  6,  6, 0x88888888);

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── ARROW  24×6 ────────────────────────────────────────────

    public static Texture drawArrow() {
        Pixmap pm = transparent(24, 6);
        // Shaft
        rect(pm,  4,  2, 14,  2, ARR_WOOD);
        // Tip
        rect(pm, 18,  1,  4,  4, ARR_META);
        rect(pm, 20,  0,  4,  1, ARR_META);
        rect(pm, 20,  5,  4,  1, ARR_META);
        px(pm, 23,  2, ARR_META); px(pm, 23,  3, ARR_META);
        // Fletching
        rect(pm,  0,  0,  5,  3, ARR_FETH);
        rect(pm,  0,  3,  5,  3, ARR_FETH);
        px(pm,  3,  2, ARR_WOOD); px(pm,  3,  3, ARR_WOOD);

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── BOSS  80×120  (phase determines glow color) ────────────

    public static Texture drawBossPhase(int phase) {
        // phase: 1=blue, 2=purple, 3=red
        int glow = phase==1 ? BS_COMP1 : phase==2 ? BS_COMP2 : BS_COMP3;
        int glowD= phase==1 ? 0x224488FF : phase==2 ? 0x441188FF : 0x880000FF;

        Pixmap pm = transparent(80, 120);

        // ── Torn royal robe (wide, behind body) ──
        rect(pm, 10, 40, 60, 75, BS_ROBE);
        rect(pm,  8, 50, 64, 60, BS_ROBE);
        // Robe tears / shreds at bottom
        for (int i = 0; i < 8; i++) {
            int rx = 10 + i * 8;
            int ry = 100 + (i%3)*6;
            rect(pm, rx, ry, 5, 20 - (i%3)*4, BS_ROBE2);
        }
        // Robe highlight / trim — gold
        hline(pm, 10, 40, 60, BS_GOLD);
        hline(pm, 10, 42, 60, BS_GOLD);
        for (int i = 0; i < 6; i++) {
            rect(pm, 10 + i*10, 43, 4, 3, BS_GOLD);
        }

        // ── Ribcage / torso ──
        rect(pm, 20, 40, 40, 40, BS_BONE2);
        rect(pm, 22, 38, 36,  4, BS_BONE);
        // Ribs
        for (int i = 0; i < 5; i++) {
            int ry = 42 + i*7;
            rect(pm, 16, ry, 8, 4, BS_BONE);  // left
            rect(pm, 56, ry, 8, 4, BS_BONE);  // right
            rect(pm, 22, ry, 4, 3, BS_BONE2);
            rect(pm, 54, ry, 4, 3, BS_BONE2);
        }
        // Spine
        rect(pm, 37, 38, 6, 42, BS_BONE2);

        // ── Skull (big — boss scale) ──
        rect(pm, 18,  6, 44, 34, BS_BONE);
        rect(pm, 20,  4, 40,  4, BS_BONE);
        rect(pm, 16, 10, 48, 28, BS_BONE);
        // Skull shading
        rect(pm, 22, 10,  8,  8, BS_BONE2);
        rect(pm, 50, 10,  8,  8, BS_BONE2);
        // Crown
        for (int i = 0; i < 5; i++) {
            int cx = 20 + i * 10;
            rect(pm, cx, 0, 6, 8, BS_CROWN);
            px(pm, cx+2, 0, glow); px(pm, cx+3, 0, glow);
        }
        rect(pm, 18,  6, 44,  4, BS_GOLD);

        // Eye sockets
        rect(pm, 22, 14, 14, 12, 0x080408FF);
        rect(pm, 44, 14, 14, 12, 0x080408FF);
        // Glowing eyes
        rect(pm, 24, 15, 10,  8, glow);
        rect(pm, 46, 15, 10,  8, glow);
        rect(pm, 27, 17,  4,  4, 0xFFFFFFFF);
        rect(pm, 49, 17,  4,  4, 0xFFFFFFFF);

        // Nose cavity
        rect(pm, 36, 24,  8,  6, 0x080408FF);
        // Jaw / teeth
        rect(pm, 22, 36, 36,  6, BS_BONE);
        for (int i = 0; i < 8; i++) {
            rect(pm, 23 + i*5, 38, 3, 5, BS_BONE2);
        }

        // ── Left arm — Broken compass/spear ──
        // Upper arm bone
        rect(pm,  4, 42, 16, 10, BS_BONE);
        rect(pm,  2, 50, 12, 10, BS_BONE2);
        // Forearm
        rect(pm,  2, 58, 10, 12, BS_BONE);
        // Hand grip
        rect(pm,  4, 68,  8,  6, BS_BONE2);
        // Compass shaft (broken spear shape)
        rect(pm,  6, 20,  4, 52, BS_CIRK);
        rect(pm,  5, 22,  6, 48, BS_CIRK2);
        px(pm,  7, 20, 0xFFFFFFFF); px(pm,  8, 20, 0xFFFFFFFF); // tip
        // Compass head (broken circle)
        rect(pm,  2, 30, 12, 12, BS_CIRK);
        rect(pm,  4, 28, 8,  2,  BS_CIRK);
        rect(pm,  4, 40, 8,  2,  BS_CIRK2);
        rect(pm,  3, 32, 4,  6,  BS_CIRK2);
        // Glow on spear
        rect(pm,  7, 20,  2, 55, glowD);

        // ── Right arm — Ghost shield ──
        // Upper arm
        rect(pm, 60, 42, 16, 10, BS_BONE);
        rect(pm, 66, 50, 12, 10, BS_BONE2);
        // Forearm
        rect(pm, 68, 58, 10, 12, BS_BONE);
        // Shield (magical, translucent)
        rect(pm, 58, 38, 20, 26, BS_SHIELD);
        rect(pm, 56, 42, 24, 18, BS_SHIELD);
        // Shield glow border
        hline(pm, 58, 38, 20, glow);
        hline(pm, 58, 63, 20, glow);
        for (int y=38; y<64; y++) {
            px(pm, 58, y, glow);
            px(pm, 77, y, glow);
        }
        // Shield inner rune
        rect(pm, 63, 44, 12, 12, glowD);
        rect(pm, 65, 42,  8,  2, glow);
        rect(pm, 65, 56,  8,  2, glow);
        rect(pm, 62, 47,  2,  6, glow);
        rect(pm, 74, 47,  2,  6, glow);
        rect(pm, 66, 47,  6,  6, glow);

        // Overall glow aura at edges
        for (int x=0; x<80; x++) {
            int ga = 0x40;
            int glowA = ((glow >> 8) & 0xFFFFFF) << 8 | ga;
            if (x < 4 || x > 75) { for (int y=0;y<120;y++) px(pm,x,y,glowA); }
        }

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── GHOST SWORD  32×12 ─────────────────────────────────────

    public static Texture drawGhostSword() {
        Pixmap pm = transparent(32, 12);
        // Blade
        rect(pm,  4,  4, 24,  4, 0x8855FFFF);
        rect(pm,  6,  3, 22,  1, 0xAA77FFFF);
        rect(pm,  6,  8, 22,  1, 0xAA77FFFF);
        // Crossguard
        rect(pm,  4,  1,  4, 10, 0xCC99FFFF);
        rect(pm,  3,  2,  6,  8, 0xAA77FFFF);
        // Tip
        rect(pm, 26,  4,  6,  4, 0xDDBBFFFF);
        px(pm, 31,  5, 0xFFEEFFFF); px(pm, 31,  6, 0xFFEEFFFF);
        // Glow effect (semi-transparent)
        rect(pm,  4,  2, 24,  8, 0x6633FF44);

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    // ── BACKGROUNDS  160×90 (scaled up to 800×450 in draw) ────

    public static Texture drawBgGates() {
        int W = 160, H = 90;
        Pixmap pm = new Pixmap(W, H, Pixmap.Format.RGBA8888);

        // Night sky
        rect(pm, 0, 30, W, H-30, 0x0A061AFF);
        // Gradient sky darker at top
        rect(pm, 0, 55, W, H-55, 0x100828FF);
        // Ground
        rect(pm, 0, 0, W, 30, 0x1A1008FF);
        rect(pm, 0, 28, W, 3, 0x2A1A0AFF);

        // Moon
        rect(pm, 120, 65, 14, 14, 0xEEEECCFF);
        rect(pm, 122, 67, 10, 10, 0xFFFFDDFF);
        rect(pm, 125, 68,  4,  8, 0xDDDDBBFF); // crater

        // Stars
        int[] sx = {10,25,40,55,70,90,105,130,145,15,50,80,110,140,30,65,100,135,5,45};
        int[] sy = {80,75,85,70,82,78,72,  85, 68, 68,60,65,58, 62, 55,52, 50, 48,45,42};
        for (int i = 0; i < sx.length; i++) px(pm, sx[i], sy[i], 0xFFFFFFCC);

        // Distant castle silhouette
        rect(pm, 60, 35, 40, 25, 0x0D0820FF);
        rect(pm, 65, 55,  8, 10, 0x0D0820FF); // tower
        rect(pm, 87, 50, 10, 15, 0x0D0820FF); // tower
        rect(pm, 64, 59,  4,  4, 0x2244AAFF); // window glow
        rect(pm, 88, 54,  4,  4, 0x2244AAFF);

        // Ground cobblestone pattern
        for (int x = 0; x < W; x += 16) {
            for (int y = 0; y < 30; y += 8) {
                int shade = (x/16 + y/8)%2==0 ? 0x1E1208FF : 0x160E06FF;
                rect(pm, x, y, 14, 6, shade);
            }
        }

        // Left wall with torches
        rect(pm, 0, 20, 12, 70, 0x1A1208FF);
        rect(pm, 8, 50,  4,  8, 0x8B6914FF); // torch handle
        rect(pm, 7, 55,  6,  4, 0xFF8800FF); // flame
        rect(pm, 8, 56,  4,  3, 0xFFCC00FF);

        // Right wall / gate arch
        rect(pm, 130, 20, 30, 70, 0x1A1208FF);
        // Gate (closed)
        rect(pm, 132, 0, 26, 25, 0x1A1208FF);
        rect(pm, 134, 2, 22, 23, 0x2A1A10FF);
        // Gate bars
        for (int x = 135; x < 155; x += 5) {
            rect(pm, x, 2, 2, 23, 0x3A2A18FF);
        }
        // Gate glow (purple magic lock)
        rect(pm, 142, 10,  8,  8, 0x6600CCAA);

        // Foreground fog
        for (int x = 0; x < W; x++) {
            int fa = 60 + (x%20)*2;
            pm.setColor(0.04f, 0.02f, 0.08f, fa/255f);
            pm.drawPixel(x, 0); pm.drawPixel(x, 1); pm.drawPixel(x, 2);
        }

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    public static Texture drawBgGreatHall() {
        int W = 160, H = 90;
        Pixmap pm = new Pixmap(W, H, Pixmap.Format.RGBA8888);

        // Ceiling
        rect(pm, 0, 70, W, 20, 0x0E0C18FF);
        // Walls
        rect(pm, 0,  0, 14, H, 0x161020FF);
        rect(pm, W-14, 0, 14, H, 0x161020FF);
        // Floor
        rect(pm, 0, 0, W, 18, 0x1A1428FF);
        rect(pm, 0, 16, W, 3, 0x2A2040FF);
        // Floor tiles
        for (int x = 14; x < W-14; x += 18) {
            for (int y = 0; y < 16; y += 8) {
                int shade = (x/18+y/8)%2==0 ? 0x1E1830FF : 0x181428FF;
                rect(pm, x, y, 16, 6, shade);
            }
        }

        // Pillars
        int[] pillarX = {20, 60, 100, 140};
        for (int px2 : pillarX) {
            rect(pm, px2,  0, 10, H, 0x1C1830FF);
            rect(pm, px2+1, 0,  2, H, 0x2C2840FF); // highlight
            // Torch
            rect(pm, px2+3, 45, 4, 6, 0x8B6914FF);
            rect(pm, px2+2, 48, 6, 4, 0xFF8800AA);
            rect(pm, px2+3, 49, 4, 3, 0xFFCC00AA);
        }

        // Arched windows (purple glow)
        for (int wx = 30; wx < W-30; wx += 40) {
            rect(pm, wx, 65, 14, 15, 0x4400AAFF);
            rect(pm, wx+1, 70, 12, 10, 0x6622CCFF);
            rect(pm, wx+3, 72,  8,  8, 0x9944FFFF);
        }

        // Chains hanging from ceiling
        for (int cx = 40; cx < W-40; cx += 30) {
            for (int cy = 70; cy < 85; cy += 3) {
                rect(pm, cx, cy, 2, 2, 0x444455FF);
            }
        }

        // Skull pile in corner
        rect(pm, 15, 5, 10, 8, 0xC8BCB0FF);
        rect(pm, 18, 8,  6, 5, 0xE8DCC8FF);
        px(pm, 19, 10, 0x660000FF); px(pm, 22, 10, 0x660000FF);

        Texture t = new Texture(pm); pm.dispose(); return t;
    }

    public static Texture drawBgThroneRoom() {
        int W = 160, H = 90;
        Pixmap pm = new Pixmap(W, H, Pixmap.Format.RGBA8888);

        // Background wall — dark red stone
        rect(pm, 0, 0, W, H, 0x120A08FF);

        // Stone wall pattern
        for (int x = 0; x < W; x += 20) {
            for (int y = 20; y < H; y += 10) {
                int shade = (x/20+y/10)%2==0 ? 0x1A0E0CFF : 0x160A0AFF;
                rect(pm, x, y, 18, 8, shade);
            }
        }

        // Floor
        rect(pm, 0, 0, W, 18, 0x1E0E0AFF);
        rect(pm, 0,16, W,  3, 0x3A1A16FF);
        for (int x = 0; x < W; x += 16) {
            int shade = (x/16)%2==0 ? 0x22100EFF : 0x1A0C0AFF;
            rect(pm, x, 0, 14, 15, shade);
        }

        // THRONE center back
        rect(pm, 60, 20, 40, 55, 0x260A06FF);
        rect(pm, 62, 40, 36, 40, 0x1E0804FF);
        // Throne armrests
        rect(pm, 56, 26, 10, 30, 0x260A06FF);
        rect(pm, 74, 26, 10, 30, 0x260A06FF);
        // Throne top arch
        rect(pm, 58, 55, 44,  8, 0x340E08FF);
        rect(pm, 62, 58, 36,  8, 0x1E0804FF);
        // Gold trim on throne
        hline(pm, 60, 55, 40, 0xAA6600FF);
        hline(pm, 56, 26, 10, 0xAA6600FF);
        hline(pm, 74, 26, 10, 0xAA6600FF);
        // Throne skull decoration
        rect(pm, 74, 64,  8,  6, 0xC8BCB0FF);
        px(pm, 76, 66, 0xFF0000FF); px(pm, 80, 66, 0xFF0000FF);

        // Red magic circles on floor
        for (int x = 20; x < W-20; x += 24) {
            rect(pm, x, 4, 16, 10, 0x2A0808FF);
            rect(pm, x+4, 5,  8,  8, 0x440A0AFF);
            rect(pm, x+6, 6,  4,  6, 0x880808AA);
        }

        // Cracked pillars
        for (int px3 = 8; px3 < W-8; px3 += W-20) {
            rect(pm, px3, 0, 12, H, 0x1C0E0CFF);
            rect(pm, px3+1, 0, 2, H, 0x2C1818FF);
            // Cracks
            rect(pm, px3+4, 20, 1, 15, 0x0A0404FF);
            rect(pm, px3+6, 30, 1, 10, 0x0A0404FF);
        }

        // Lava glow strips at bottom
        rect(pm, 0, 0, W, 3, 0xFF440022);
        rect(pm, 0, 1, W, 1, 0xFF660044);

        // Wall torches with red flame
        for (int tx = 35; tx < W-35; tx += 45) {
            rect(pm, tx, 45, 4, 8, 0x6B3A1FFF);
            rect(pm, tx-1, 48, 6, 5, 0xFF4400AA);
            rect(pm, tx, 50, 4, 3, 0xFF8800AA);
        }

        Texture t = new Texture(pm); pm.dispose(); return t;
    }
}
