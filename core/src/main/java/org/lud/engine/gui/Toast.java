package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class Toast extends Actor {
    private final BitmapFont font;
    private String title;
    private String description;
    private final Texture tex;
    private TextureRegion tl, tr, bl, br;
    private TextureRegion top, bottom, left, right, center;
    private final GlyphLayout layout;

    private float x, y;
    private float width, height;
    private final float padding = 16f;
    private final float duration = 2f;
    private float elapsedTime;
    private final int cell = 4;
    private final float scale = 4f;
    private final float scaledCell = cell * scale;

    private boolean isFinished;

    public Toast(String description, String title, BitmapFont font, float targetX, float targetY) {
        this.description = description;
        this.title = title;
        this.font = font;
        this.tex = new Texture("tooltip.png");
        this.tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.layout = new GlyphLayout();
        layout.setText(font, description);
        this.width = Gdx.graphics.getWidth()/3f;
        this.height = 250f;

        this.tl = new TextureRegion(tex, 0, tex.getHeight() - cell, cell, cell);
        this.tr = new TextureRegion(tex, tex.getWidth() - cell, tex.getHeight() - cell, cell, cell);
        this.bl = new TextureRegion(tex, 0, 0, cell, cell);
        this.br = new TextureRegion(tex, tex.getWidth() - cell, 0, cell, cell);
        this.top = new TextureRegion(tex, cell, tex.getHeight() - cell, tex.getWidth() - 2 * cell, cell);
        this.bottom = new TextureRegion(tex, cell, 0, tex.getWidth() - 2 * cell, cell);
        this.left = new TextureRegion(tex, 0, cell, cell, tex.getHeight() - 2 * cell);
        this.right = new TextureRegion(tex, tex.getWidth() - cell, cell, cell, tex.getHeight() - 2 * cell);
        this.center = new TextureRegion(tex, cell, cell, tex.getWidth() - 2 * cell, tex.getHeight() - 2 * cell);

        top.flip(false, true);
        bottom.flip(false, true);
        left.flip(false, true);
        right.flip(false, true);
        center.flip(false, true);
        tl.flip(false, true);
        tr.flip(false, true);
        bl.flip(false, true);
        br.flip(false, true);

        setPosition(targetX, -height);
        setSize(width, height);

        float slideTime = 0.5f;
        float fadeTime = 1f;

        addAction(
            Actions.sequence(
                Actions.moveTo(targetX, targetY, slideTime),
                Actions.delay(duration),
                Actions.moveTo(targetX, -height, slideTime),
                Actions.run(() -> isFinished = true)
            )
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float l = getX();
        float b = getY();
        float r = l + width;
        float t = b + height;

        float innerWidth  = width  - 2 * scaledCell;
        float innerHeight = height - 2 * scaledCell;

        batch.draw(tl, l, t - scaledCell, scaledCell, scaledCell);
        batch.draw(tr, r - scaledCell, t - scaledCell, scaledCell, scaledCell);
        batch.draw(bl, l, b, scaledCell, scaledCell);
        batch.draw(br, r - scaledCell, b, scaledCell, scaledCell);

        batch.draw(top, l + scaledCell, t - scaledCell, innerWidth, scaledCell);
        batch.draw(bottom, l + scaledCell, b, innerWidth, scaledCell);
        batch.draw(left, l, b + scaledCell, scaledCell, innerHeight);
        batch.draw(right, r - scaledCell, b + scaledCell, scaledCell, innerHeight);

        batch.draw(center, l + scaledCell, b + scaledCell, innerWidth, innerHeight);

        float textX = l + padding * 2;
        float titleY = t - padding * 2;
        layout.setText(font, title);
        font.setColor(Colors.getHighlight());
        font.draw(batch, layout, textX, titleY);

        float descriptionY = titleY - layout.height - 10f;
        layout.setText(font, description, Color.WHITE,
            width - padding * 2, Align.left,
            true);
        font.draw(batch, layout, textX, descriptionY);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
