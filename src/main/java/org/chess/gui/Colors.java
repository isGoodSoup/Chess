package org.chess.gui;

import org.chess.service.BooleanService;

import java.awt.Color;
import java.util.List;

public class Colors {
    public final static Color DEFAULT_EVEN = new Color(210, 165, 125);
    public final static Color DEFAULT_ODD = new Color(175, 115, 70);
    public final static Color DEFAULT_EDGE = new Color(120, 70, 40);
    public static final Color DARK_MODE = new Color(20, 20, 40);
    public static final Color BLACK_EVEN = new Color(40, 40, 60);
    public static final Color WHITE_ODD = new Color(255, 255, 255);
    public static final Color BLACK_EDGE = new Color(20, 20, 40);
    public static final Color OCEAN_EVEN = new Color(100, 140, 200);
    public static final Color OCEAN_ODD = new Color(100, 90, 200);
    public static final Color OCEAN_EDGE = new Color(70, 60, 170);
    public static final Color FOREST_EVEN = new Color(100, 180, 100);
    public static final Color FOREST_ODD = new Color(30, 120, 60);
    public static final Color FOREST_EDGE = new Color(10, 100, 40);
    public static final Color FAIRY_EVEN = new Color(180, 140, 200);
    public static final Color FAIRY_ODD = new Color(180, 70, 200);
    public static final Color FAIRY_EDGE = new Color(140, 40, 170);

    public static Color EVEN = DEFAULT_EVEN;
    public static Color ODD = DEFAULT_ODD;
    public static Color EDGE = DEFAULT_EDGE;

    public static final List<Color[]> THEMES_LIST = List.of(
            new Color[]{DEFAULT_EVEN, DEFAULT_ODD, DEFAULT_EDGE},
            new Color[]{BLACK_EVEN, WHITE_ODD, BLACK_EDGE},
            new Color[]{OCEAN_EVEN, OCEAN_ODD, OCEAN_EDGE},
            new Color[]{FOREST_EVEN, FOREST_ODD, FOREST_EDGE},
            new Color[]{FAIRY_EVEN, FAIRY_ODD, FAIRY_EDGE}
    );
    private static int themeIndex = 0;

    public static void nextTheme() {
        themeIndex = (themeIndex + 1) % THEMES_LIST.size();
        Color[] theme = THEMES_LIST.get(themeIndex);
        EVEN = theme[0];
        ODD = theme[1];
        EDGE = theme[2];
    }

    public static void toggleDarkTheme() {
        if (BooleanService.isDarkMode) {
            EVEN = DARK_MODE;
            ODD = WHITE_ODD;
            EDGE = DARK_MODE;
        } else {
            setDefaultTheme();
        }
    }

    public static void setDefaultTheme() {
        EVEN = DEFAULT_EVEN;
        ODD = DEFAULT_ODD;
        EDGE = DEFAULT_EDGE;
    }

    public static Color getEVEN() {
        return EVEN;
    }

    public static Color getODD() {
        return ODD;
    }

    public static Color getEDGE() {
        return EDGE;
    }
}
