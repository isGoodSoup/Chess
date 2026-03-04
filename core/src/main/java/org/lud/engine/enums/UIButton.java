package org.lud.engine.enums;

public enum UIButton {
    PLAY("play"),
    SETTINGS("settings"),
    ACHIEVEMENTS("achievements"),
    LANG("lang"),
    EXIT("exit"),

    UNDO("undo"),
    RESET("reset"),
    NEXT_PAGE("next_page"),
    PREVIOUS_PAGE("previous_page"),
    PAUSE("pause"),
    THEME("theme");

    private final String suffix;

    UIButton(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getSuffix(Lang lang) {
        if(this == LANG) {
            return "lang_" + lang.name().toLowerCase();
        }
        return suffix;
    }
}
