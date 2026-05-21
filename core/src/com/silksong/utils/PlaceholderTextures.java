package com.silksong.utils;

import com.badlogic.gdx.graphics.Texture;
import com.silksong.utils.BackgroundArt;

/**
 * Runtime-generated pixel art sprites.
 * All art is drawn via PixelArt.java using Pixmap — no files needed.
 */
public class PlaceholderTextures {

    private static PlaceholderTextures instance;

    private final Texture player;
    private final Texture skeleton;
    private final Texture bat;
    private final Texture expOrb;
    private final Texture lever;
    private final Texture bossP1;
    private final Texture bossP2;
    private final Texture bossP3;
    private final Texture ghostSword;
    private final Texture arrow;
    private final Texture bgGates;
    private final Texture bgGreatHall;
    private final Texture bgThroneRoom;

    private PlaceholderTextures() {
        player       = PixelArt.drawPlayer();
        skeleton     = PixelArt.drawSkeleton();
        bat          = PixelArt.drawBat();
        expOrb       = PixelArt.drawExpOrb();
        lever        = PixelArt.drawLever();
        bossP1       = PixelArt.drawBossPhase(1);
        bossP2       = PixelArt.drawBossPhase(2);
        bossP3       = PixelArt.drawBossPhase(3);
        ghostSword   = PixelArt.drawGhostSword();
        arrow        = PixelArt.drawArrow();
        bgGates      = BackgroundArt.drawBgGates();
        bgGreatHall  = BackgroundArt.drawBgGreatHall();
        bgThroneRoom = BackgroundArt.drawBgThroneRoom();
    }

    public static PlaceholderTextures getInstance() {
        if (instance == null) instance = new PlaceholderTextures();
        return instance;
    }

    public Texture getPlayer()      { return player; }
    public Texture getSkeleton()    { return skeleton; }
    public Texture getBat()         { return bat; }
    public Texture getExpOrb()      { return expOrb; }
    public Texture getLever()       { return lever; }
    public Texture getBossP1()      { return bossP1; }
    public Texture getBossP2()      { return bossP2; }
    public Texture getBossP3()      { return bossP3; }
    public Texture getGhostSword()  { return ghostSword; }
    public Texture getArrow()       { return arrow; }
    public Texture getBgGates()     { return bgGates; }
    public Texture getBgGreatHall() { return bgGreatHall; }
    public Texture getBgThroneRoom(){ return bgThroneRoom; }

    public void dispose() {
        player.dispose(); skeleton.dispose(); bat.dispose();
        expOrb.dispose(); lever.dispose(); bossP1.dispose();
        bossP2.dispose(); bossP3.dispose(); ghostSword.dispose();
        arrow.dispose(); bgGates.dispose(); bgGreatHall.dispose();
        bgThroneRoom.dispose();
        instance = null;
    }
}
