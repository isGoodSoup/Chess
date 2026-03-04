package org.lud.engine.data;

import org.lud.engine.enums.Lang;
import org.lud.engine.enums.UIButton;

public record ButtonData(UIButton type, Runnable action, Runnable soundPath, Lang lang) {
    public ButtonData(UIButton type, Runnable action, Runnable soundPath) {
        this(type, action, soundPath, null);
    }
}
