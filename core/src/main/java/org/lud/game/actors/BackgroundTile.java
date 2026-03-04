package org.lud.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundTile extends Actor {
    private final Color color;
    private final float width, height;
    private final ShapeRenderer shaper;

    public BackgroundTile(Color color, float x, float y, float width, float height,
                          ShapeRenderer shaper) {
        this.color = color;
        this.shaper = shaper;
        this.width = width;
        this.height = height;
        setPosition(x, y);
        setSize(width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shaper.setProjectionMatrix(getStage().getCamera().combined);
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.setColor(color);
        float worldX = getX() + getParent().getX();
        float worldY = getY() + getParent().getY();
        shaper.rect(worldX, worldY, getWidth(), getHeight());
        shaper.end();
        batch.begin();
    }
}
