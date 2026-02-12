package org.chess.gui;

import org.chess.service.BooleanService;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Colors {
    public final static Color DEFAULT_EVEN = new Color(210, 165, 125);
    public final static Color DEFAULT_ODD = new Color(175, 115, 70);
    public static final Color DARK_MODE = new Color(20, 20, 40);
    public static final Color BLACK_EVEN = new Color(40, 40, 60);
    public static final Color WHITE_ODD = new Color(255, 255, 255);
    public static final Color OCEAN_EVEN = new Color(100, 140, 200);
    public static final Color OCEAN_ODD = new Color(100, 90, 200);
    public static final Color FOREST_EVEN = new Color(100, 180, 100);
    public static final Color FOREST_ODD = new Color(30, 120, 60);
    public static final Color FAIRY_EVEN = new Color(180, 140, 200);
    public static final Color FAIRY_ODD = new Color(180, 70, 200);
    public static final List<Color> THEMES =
            new ArrayList<>(List.of(DEFAULT_EVEN, DEFAULT_ODD,
            BLACK_EVEN, WHITE_ODD, OCEAN_EVEN, OCEAN_ODD, FOREST_EVEN,
            FOREST_ODD, FAIRY_EVEN, FAIRY_ODD));
    public static Color EVEN = DEFAULT_EVEN;
    public static Color ODD = DEFAULT_ODD;
    private static int themeCounter = 0;

    public static void nextTheme() {
        themeCounter = (themeCounter + 2) % THEMES.size();
        EVEN = THEMES.get(themeCounter);
        ODD = THEMES.get((themeCounter + 1) % THEMES.size());
    }

    public static void toggleDarkTheme() {
        if (BooleanService.isDarkMode) {
            EVEN = DARK_MODE;
            ODD = WHITE_ODD;
        } else {
            setDefaultTheme();
        }
    }

    public static void setDefaultTheme() {
        EVEN = DEFAULT_EVEN;
        ODD = DEFAULT_ODD;
    }

    public static Color getEVEN() {
        return EVEN;
    }

    public static Color getODD() {
        return ODD;
    }
}
