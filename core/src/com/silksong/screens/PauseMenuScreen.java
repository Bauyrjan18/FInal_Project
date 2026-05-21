package com.silksong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.silksong.SilkSongGame;

public class PauseMenuScreen implements Screen {

    private final SilkSongGame game;
    private final Screen       previousScreen;
    private final Runnable     restartAction;

    private final SpriteBatch   batch;
    private final ShapeRenderer shape;
    private final BitmapFont    font;
    private final GlyphLayout   layout;

    private static final float VW = 800f;
    private static final float VH = 450f;

    private static final String[] LABELS = {"Resume", "Restart", "Main Menu"};
    private final Rectangle[] buttons = new Rectangle[3];
    private int   hovered = -1;
    private float time    = 0f;

    public PauseMenuScreen(SilkSongGame game, Screen previousScreen, Runnable restartAction) {
        this.game           = game;
        this.previousScreen = previousScreen;
        this.restartAction  = restartAction;
        this.batch          = new SpriteBatch();
        this.shape          = new ShapeRenderer();
        this.font           = new BitmapFont();
        this.layout         = new GlyphLayout();

        float btnW = 220f, btnH = 44f, gap = 18f;
        float totalH = 3 * btnH + 2 * gap;
        float startY = VH / 2f + totalH / 2f;
        float btnX   = VW / 2f - btnW / 2f;
        for (int i = 0; i < 3; i++)
            buttons[i] = new Rectangle(btnX, startY - i * (btnH + gap) - btnH, btnW, btnH);
    }

    // Called by Resume button and ESC key
    private void doResume() {
        game.setScreen(previousScreen);
    }

    @Override
    public void render(float delta) {
        time += delta;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            doResume(); return;
        }

        float mx = Gdx.input.getX() * (VW / Gdx.graphics.getWidth());
        float my = (Gdx.graphics.getHeight() - Gdx.input.getY()) * (VH / Gdx.graphics.getHeight());
        hovered = -1;
        for (int i = 0; i < buttons.length; i++)
            if (buttons[i].contains(mx, my)) { hovered = i; break; }

        if (Gdx.input.isButtonJustPressed(0)) {
            if (hovered == 0) { doResume(); return; }
            if (hovered == 1) { restartAction.run(); dispose(); return; }
            if (hovered == 2) { game.setScreen(new MainMenuScreen(game)); dispose(); return; }
        }

        // Draw
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.getProjectionMatrix().setToOrtho2D(0, 0, VW, VH);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Dark overlay
        shape.setColor(0f, 0f, 0f, 0.72f);
        shape.rect(0, 0, VW, VH);

        // Panel
        float panW = 280f, panH = 280f;
        float panX = VW / 2f - panW / 2f, panY = VH / 2f - panH / 2f;
        shape.setColor(0f, 0f, 0f, 0.5f);
        shape.rect(panX + 6f, panY - 6f, panW, panH);
        shape.setColor(0.08f, 0.04f, 0.10f, 0.97f);
        shape.rect(panX, panY, panW, panH);

        // Top/bottom accent
        float ga = 0.7f + 0.3f * MathUtils.sin(time * 2.5f);
        shape.setColor(0.55f * ga, 0.15f * ga, 0.95f * ga, 1f);
        shape.rect(panX, panY + panH - 3f, panW, 3f);
        shape.rect(panX, panY, panW, 3f);

        // Buttons (filled)
        for (int i = 0; i < buttons.length; i++) {
            Rectangle b = buttons[i];
            boolean hov = (hovered == i);
            shape.setColor(0f, 0f, 0f, 0.4f);
            shape.rect(b.x + 3f, b.y - 3f, b.width, b.height);
            if (hov) {
                float ha = 0.8f + 0.2f * MathUtils.sin(time * 6f);
                shape.setColor(0.40f * ha, 0.08f * ha, 0.80f * ha, 1f);
            } else {
                shape.setColor(0.16f, 0.08f, 0.22f, 1f);
            }
            shape.rect(b.x, b.y, b.width, b.height);
        }
        shape.end();

        // Button borders (line)
        shape.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < buttons.length; i++) {
            Rectangle b = buttons[i];
            if (hovered == i) shape.setColor(0.75f, 0.3f, 1f, 1f);
            else              shape.setColor(0.35f, 0.15f, 0.55f, 0.8f);
            shape.rect(b.x, b.y, b.width, b.height);
        }
        shape.end();

        // Text
        batch.getProjectionMatrix().setToOrtho2D(0, 0, VW, VH);
        batch.begin();

        font.getData().setScale(2.2f);
        float tp = 0.85f + 0.15f * MathUtils.sin(time * 2f);
        font.setColor(0.70f * tp, 0.20f * tp, 1f * tp, 1f);
        layout.setText(font, "PAUSED");
        font.draw(batch, "PAUSED", VW / 2f - layout.width / 2f, panY + panH - 24f);

        font.getData().setScale(0.75f);
        font.setColor(0.5f, 0.4f, 0.6f, 0.8f);
        layout.setText(font, "Press ESC to resume");
        font.draw(batch, "Press ESC to resume", VW / 2f - layout.width / 2f, panY + panH - 54f);

        font.getData().setScale(1.3f);
        for (int i = 0; i < buttons.length; i++) {
            Rectangle b = buttons[i];
            font.setColor(hovered == i ? new com.badlogic.gdx.graphics.Color(1f, 0.85f, 1f, 1f)
                                       : new com.badlogic.gdx.graphics.Color(0.75f, 0.65f, 0.85f, 1f));
            layout.setText(font, LABELS[i]);
            font.draw(batch, LABELS[i],
                b.x + b.width / 2f - layout.width / 2f,
                b.y + b.height / 2f + layout.height / 2f + 2f);
        }
        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override public void show()                  {}
    @Override public void hide()                  {}
    @Override public void pause()                 {}
    @Override public void resume()                {}   // Screen interface — intentionally empty
    @Override public void resize(int w, int h)    {}
    @Override public void dispose() {
        batch.dispose();
        shape.dispose();
        font.dispose();
    }
}
