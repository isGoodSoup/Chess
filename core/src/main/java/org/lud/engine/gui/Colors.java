package org.lud.engine.gui;

import com.badlogic.gdx.graphics.Color;
import org.lud.engine.enums.Theme;

public class Colors {
    public static final Color PROGRESS_BAR = new Color(61f/255f, 187f/255f, 47f/255f, 1f);
    public static final Color SETTINGS = new Color(0f, 0f, 0f, 180f/255f);

    private static Theme currentTheme = Theme.DEFAULT;

    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    public static Theme getTheme() {
        return currentTheme;
    }

    public static void nextTheme() {
        Theme[] themes = Theme.values();
        int nextIndex = (currentTheme.ordinal() + 1) % themes.length;
        setTheme(themes[nextIndex]);
    }

    public static Color getBackground() {
        return currentTheme.getBackground();
    }

    public static Color getForeground() {
        return currentTheme.getForeground();
    }

    public static Color getEdge() {
        return currentTheme.getEdge();
    }

    public static Color getHighlight() {
        return currentTheme.getHighlight();
    }
}
