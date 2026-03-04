package org.lud.engine.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.lud.game.core.Chess;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if(StartupHelper.startNewJvmIfRequired()) { return; }
        createApplication();
    }

    public static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        config.setWindowedMode(displayMode.width, displayMode.height);
        config.setTitle("LudEngine v1.3");
        config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        config.setWindowedMode(displayMode.width, displayMode.height);
        config.setDecorated(false);
        config.setWindowPosition(0, 0);
        config.useVsync(true);
        config.setWindowIcon("logo_altx128.png", "logo_altx64.png", "logo_altx32.png");
        return config;
    }

    private static void createApplication() {
        new Lwjgl3Application(new Chess(), getDefaultConfiguration());
    }
}
