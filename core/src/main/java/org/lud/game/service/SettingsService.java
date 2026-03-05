package org.lud.game.service;

import org.lud.engine.enums.GameSettings;
import org.lud.engine.interfaces.Service;
import org.lud.engine.service.ServiceFactory;

import java.util.EnumMap;
import java.util.Map;

public class SettingsService implements Service {
    private static final Map<GameSettings, Boolean> settings = new EnumMap<>(GameSettings.class);
    static {
        for (GameSettings s : GameSettings.values()) {
            settings.put(s, s.get());
        }
    }

    private final ServiceFactory service;

    public SettingsService(ServiceFactory service) {
        this.service = service;
    }

    public static boolean get(GameSettings setting) {
        return settings.get(setting);
    }

    public static void toggle(GameSettings setting) {
        settings.put(setting, !settings.get(setting));
    }
}
