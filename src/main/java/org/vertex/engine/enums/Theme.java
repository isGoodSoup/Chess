package org.vertex.engine.enums;

public enum Theme {
    DEFAULT("white", "black"),
    LEGACY("creme", "brown");

    private final String lightName;
    private final String darkName;

    Theme(String lightName, String darkName) {
        this.lightName = lightName;
        this.darkName = darkName;
    }

    public String getColorName(Tint tint) {
        return tint == Tint.LIGHT ? lightName : darkName;
    }
}
