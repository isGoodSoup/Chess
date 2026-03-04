package org.lud.engine.enums;

public enum LangKeys {
    TOOLTIPS("tooltip.play", "tooltip.settings", "tooltip.achievements",
        "tooltip.lang", "tooltip.exit");

    private final String[] keys;

    LangKeys(String... keys) {
        this.keys = keys;
    }

    public String[] getKeys() {
        return keys;
    }
}
