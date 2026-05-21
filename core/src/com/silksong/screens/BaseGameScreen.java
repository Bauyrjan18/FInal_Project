package com.silksong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.silksong.SilkSongGame;
import com.silksong.entities.player.Player;
import com.silksong.patterns.command.InputHandler;
import com.silksong.systems.level.LevelSystem;
import com.silksong.systems.sound.SoundManager;
import com.silksong.ui.UIHud;

public abstract class BaseGameScreen implements Screen {

    protected static final float VIEWPORT_WIDTH  = 800f;
    protected static final float VIEWPORT_HEIGHT = 450f;

    protected final SilkSongGame       game;
    protected final SpriteBatch        batch;
    protected final OrthographicCamera camera;
    protected final Viewport           viewport;

    protected Player       player;
    protected InputHandler inputHandler;
    protected LevelSystem  levelSystem;
    protected UIHud        hud;

    protected BaseGameScreen(SilkSongGame game, Player player,
                              LevelSystem levelSystem, UIHud hud) {
        this.game        = game;
        this.player      = player;
        this.levelSystem = levelSystem;
        this.hud         = hud;
        this.batch       = new SpriteBatch();
        this.camera      = new OrthographicCamera();
        this.viewport    = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        this.inputHandler = new InputHandler(player);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            openPauseMenu(); return;
        }
        SoundManager.getInstance().update(delta);
        hud.update(delta);
        inputHandler.handleInput();
    }

    @Override
    public void resize(int width, int height) { viewport.update(width, height); }

    protected void openPauseMenu() {
        game.setScreen(new PauseMenuScreen(game, this, this::restartScreen));
    }

    protected void restartScreen() {
        game.setScreen(new MainMenuScreen(game));
    }

    @Override public void show()   {}
    @Override public void hide()   {}
    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void dispose() { batch.dispose(); }
}
