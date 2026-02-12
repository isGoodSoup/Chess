package org.chess.entities;

import org.chess.enums.AchievementType;

public class Achievement {
    private AchievementType id;
    private boolean isUnlocked;

    public Achievement(AchievementType id) {
        this.id = id;
    }

    public AchievementType getId() {
        return id;
    }

    public void setId(AchievementType id) {
        this.id = id;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.isUnlocked = unlocked;
    }

    public void unlock() {
        
    }
}
