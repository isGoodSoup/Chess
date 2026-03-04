package org.lud.engine.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cursor {
    public float x, y;
    public boolean isVisible = true;
    private final Texture sprite;

    public Cursor() {
        this.x = Gdx.input.getX();
        this.y = Gdx.input.getY();
        this.sprite = new Texture("cursor.png");
        setPosition(Gdx.graphics.getHeight()/2f,
            Gdx.graphics.getWidth()/2f);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(sprite, x - sprite.getWidth()/2f + 32f,
            y - sprite.getHeight()/2f - 16f);
        batch.end();
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public void setPosition(float nx, float ny) {
        x = nx;
        y = ny;
    }
}
