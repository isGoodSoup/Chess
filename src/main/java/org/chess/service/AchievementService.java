package org.chess.service;

import org.chess.entities.Achievement;
import org.chess.enums.AchievementType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AchievementService {
    private final Map<AchievementType, Achievement> achievements;

    public AchievementService() {
        this.achievements = new HashMap<>();
        for(AchievementType type : AchievementType.values()) {
            achievements.put(type, new Achievement(type));
        }
    }

    public void unlockAchievement(AchievementType type) {
        Achievement achievement = achievements.get(type);
        if (achievement != null && !achievement.isUnlocked()) {
            achievement.unlock();
            System.out.println("Achievement Unlocked: " + type.getTitle());
        }
    }

    public Collection<Achievement> getAllAchievements() {
        return achievements.values();
    }
}
