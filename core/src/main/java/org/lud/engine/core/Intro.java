package org.lud.engine.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

@SuppressWarnings("ALL")
public class Intro {
    private int logoSize = 0;
    private int logoDelta = 2;
    private float alpha = 0f;
    private boolean isFadingIn = true;
    private final float FADE_SPEED = 0.01f;
    private Texture logo;
    private float timer = 0f;
    private final float DURATION = 3.0f;
    private final int MIN_SIZE = 25;
    private final int MAX_SIZE = 100;

    public Intro() {
        logo = new Texture("logo.png");
    }

    public void draw(SpriteBatch batch, float delta, int screenWidth, int screenHeight) {
        timer += delta;

        logoSize += logoDelta;
        if(logoSize > MAX_SIZE) {
            logoSize = MAX_SIZE;
            logoDelta = 0;
        } else if(logoSize < MIN_SIZE) {
            logoSize = MIN_SIZE;
        }

        if(isFadingIn) {
            alpha += FADE_SPEED;
            if(alpha >= 1f) {
                alpha = 1f;
                isFadingIn = false;
            }
        } else if (timer > 1.5f) {
            alpha -= FADE_SPEED;
            if(alpha < 0f) { alpha = 0f; }
        }

        float scale = 1f + logoSize / 1000f;
        float width = logo.getWidth() * scale;
        float height = logo.getHeight() * scale;
        float x = (screenWidth - width) / 2f;
        float y = (screenHeight - height) / 2f;

        Color originalColor = batch.getColor();
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(logo, x, y, width, height);
        batch.setColor(originalColor);
    }

    public boolean isFinished() {
        return timer > DURATION;
    }

    public void dispose() {
        logo.dispose();
    }
}
