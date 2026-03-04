package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tooltip {
    private String text;
    private final BitmapFont font;
    private float x, y;
    private boolean isVisible = false;
    private float timer = 0f;
    private final float delay = 0.25f;
    private float padding = 32f;
    private final Texture tex;
    private final TextureRegion tl, tr, bl, br;
    private final TextureRegion top, bottom, left, right, center;
    private final int cs;
    private final int cell = 4;
    private final float scale;

    private final float cursorOffsetY = 32f;
    private final float cursorOffsetX = 80f;

    public Tooltip(String text, BitmapFont font, Texture tex, int cs, float scale){
        this.text = text;
        this.font = font;
        this.tex = tex;
        this.cs = cs;
        this.scale = scale;

        int texW = tex.getWidth();
        int texH = tex.getHeight();
        int centerW = texW - 2 * cell;
        int centerH = texH - 2 * cell;

        this.tl = new TextureRegion(tex, 0, texH - cell, cell, cell);
        this.tr = new TextureRegion(tex, texW - cell, texH - cell, cell, cell);
        this.bl = new TextureRegion(tex, 0, 0, cell, cell);
        this.br = new TextureRegion(tex, texW - cell, 0, cell, cell);

        this.top = new TextureRegion(tex, cell, texH - cell, centerW, cell);
        this.bottom = new TextureRegion(tex, cell, 0, centerW, cell);
        this.left = new TextureRegion(tex, 0, cell, cell, centerH);
        this.right = new TextureRegion(tex, texW - cell, cell, cell, centerH);
        this.center = new TextureRegion(tex, cell, cell, centerW, centerH);

        tl.flip(false, true);
        tr.flip(false, true);
        bl.flip(false, true);
        br.flip(false, true);
        top.flip(false, true);
        bottom.flip(false, true);
        left.flip(false, true);
        right.flip(false, true);
        center.flip(false, true);
    }

    public void setText(String text){ this.text = text; }

    public void update(float delta, boolean isHovering, float mouseX, float mouseY){
        if(isHovering){
            timer += delta;
            if(timer >= delay){
                isVisible = true;
                x = mouseX + cursorOffsetX;
                y = mouseY + cursorOffsetY;
            }
        } else{
            timer = 0f;
            isVisible = false;
        }
    }

    public void render(SpriteBatch batch) {
        if(!isVisible) { return; }

        String localizedText = Localization.lang.t(text);
        String[] lines = localizedText.split("\n");

        float maxLineWidth = 0;
        GlyphLayout layout = new GlyphLayout();
        for(String line : lines) {
            if(lines.length == 1) {
                padding = 16f;
            } else {
                padding = 32f;
            }
            layout.setText(font, line);
            if(layout.width > maxLineWidth) maxLineWidth = layout.width;
        }

        float textHeight = layout.height * lines.length + (lines.length - 1) * 2;

        float hPadding = padding + 16;
        float w = Math.max(maxLineWidth + 2 * hPadding, cs * scale);
        float h = Math.max(textHeight + 2 * padding, cs * scale);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float drawX = x;
        float drawY = y;
        if(drawX + w > screenWidth) { drawX = screenWidth - w; }
        if(drawY - h < 0) { drawY = h; }

        float l = drawX;
        float r = drawX + w;
        float t = drawY;
        float b = drawY - h;

        batch.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.draw(tl, l, t - cell * scale, cell * scale, cell * scale);
        batch.draw(tr, r - cell * scale, t - cell * scale, cell * scale, cell * scale);
        batch.draw(bl, l, b, cell * scale, cell * scale);
        batch.draw(br, r - cell * scale, b, cell * scale, cell * scale);

        batch.draw(top, l + cell * scale, t - cell * scale, w - 2 * cell * scale, cell * scale);
        batch.draw(bottom, l + cell * scale, b, w - 2 * cell * scale, cell * scale);
        batch.draw(left, l, b + cell * scale, cell * scale, h - 2 * cell * scale);
        batch.draw(right, r - cell * scale, b + cell * scale, cell * scale, h - 2 * cell * scale);
        batch.draw(center, l + cell * scale, b + cell * scale, w - 2 * cell * scale, h - 2 * cell * scale);

        float textX = l + hPadding;
        float textY = t - padding;

        if (lines.length == 1) {
            textY -= layout.height/2f;
        }

        for(String line : lines) {
            layout.setText(font, line);
            font.setColor(Color.WHITE);
            font.draw(batch, layout, textX, textY);
            textY -= layout.height + 8;
        }
        batch.end();
    }
}
