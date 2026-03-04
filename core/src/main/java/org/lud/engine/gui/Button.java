package org.lud.engine.gui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import org.lud.engine.enums.LastInput;
import org.lud.engine.input.Coordinator;
import org.lud.engine.interfaces.Clickable;

public class Button extends Actor implements Clickable {
    private final Runnable action;
    private final Texture baseTexture;
    private final Texture iconNormal;
    private final Texture iconHighlighted;
    private final Texture frame;
    private final Runnable sound;
    private boolean isHovered = false;
    private boolean isSelected = false;

    public Button(float x, float y, float width, float height,
                  Texture baseTexture, Texture iconNormal, Texture frame,
                  Texture iconHighlighted, Runnable sound, Runnable action) {

        this.baseTexture = baseTexture;
        this.iconNormal = iconNormal;
        this.iconHighlighted = iconHighlighted;
        this.frame = frame;
        this.action = action;
        this.sound = sound;

        setSize(width, height);
        setPosition(x, y);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Buttons.LEFT) {
                    onClick();
                    return true;
                }
                return false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if(Coordinator.getLastInput() != LastInput.KEYBOARD) {
                    isHovered = true;
                    Coordinator.setLastInput(LastInput.MOUSE);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(Coordinator.getLastInput() != LastInput.KEYBOARD) {
                    isHovered = false;
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color col = getColor();
        float alpha = col.a * parentAlpha;
        batch.setColor(col.r, col.g, col.b, alpha);

        Texture iconToDraw = (isSelected || isHovered) && iconHighlighted != null ? iconHighlighted : iconNormal;
        batch.draw(baseTexture, getX(), getY(), getWidth(), getHeight());
        batch.draw(iconToDraw, getX(), getY(), getWidth(), getHeight());
        if((isSelected || isHovered) && frame != null) {
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void onClick() {
        if(action != null) { action.run(); }
        if(sound != null) { sound.run(); }
    }

    public Runnable getAction() {
        return action;
    }
    public boolean isHovered() {
        return isHovered;
    }
    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void dispose() {
        baseTexture.dispose();
        iconNormal.dispose();
        iconHighlighted.dispose();
        if(frame != null) { frame.dispose(); }
    }
}
