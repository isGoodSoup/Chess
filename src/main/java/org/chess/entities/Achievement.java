package org.chess.entities;

import org.chess.enums.Achievements;

public class Achievement {
    private Achievements id;
    private boolean isUnlocked;

    public Achievement(Achievements id) {
        this.id = id;
    }

    public Achievements getId() {
        return id;
    }

    public void setId(Achievements id) {
        this.id = id;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.isUnlocked = unlocked;
    }
}
