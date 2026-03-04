package org.lud.engine.data;

import org.lud.game.enums.Achievements;

public record Achievement(Achievements id, boolean unlocked) {
    public Achievement(Achievements id) {
        this(id, false);
    }

    public Achievement unlock() {
        return new Achievement(id, true);
    }
}
