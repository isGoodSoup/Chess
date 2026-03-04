package org.lud.engine.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

public class WindowToast extends Toast {
    public WindowToast(String description, String title, BitmapFont font, float targetX, float targetY) {
        super(description, title, font, targetX, targetY);
        clearActions();
        setPosition(targetX, targetY);
    }

    @Override
    public float getY() {
        return super.getY();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public static WindowToast addWindowToStage(Stage stage, List<WindowToast> existingWindows,
                                               String description, String title, BitmapFont font,
                                               float x, float startY, float spacing) {
        float yOffset = 0;
        for (WindowToast w : existingWindows) {
            yOffset += w.getHeight() + spacing;
        }
        WindowToast newWindow = new WindowToast(description, title, font, x, startY - yOffset);
        existingWindows.add(newWindow);
        stage.addActor(newWindow);
        return newWindow;
    }
}
