package org.lud.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Logo extends Actor {
    private final Texture logo;

    public Logo(Texture logo, float x, float y) {
        this.logo = logo;
        setPosition(x, y);
        setSize(logo.getWidth(), logo.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color col = getColor();
        float alpha = col.a * parentAlpha;
        batch.setColor(col.r, col.g, col.b, alpha);

        batch.draw(logo, getX(), getY(), getWidth(), getHeight());
    }
}
