package org.chess.service;

import org.chess.entities.Achievement;
import org.chess.enums.Achievements;

import java.util.*;

public class AchievementService {
    private static Map<Achievements, Achievement> achievements;
    private static List<Achievement> achievementList;

    public AchievementService() {
        achievements = new HashMap<>();
        achievementList = new ArrayList<>(achievements.values());
        for(Achievements type : Achievements.values()) {
            achievements.put(type, new Achievement(type));
        }
    }

    public void unlockAchievement(Achievements type) {
        Achievement achievement = achievements.get(type);
        if(achievement != null && !achievement.isUnlocked()) {
            unlock(achievement);
            System.out.println("Achievement Unlocked: " + type.getTitle());
        }
    }

    public static void unlockAllAchievements() {
        for(Map.Entry<Achievements, Achievement> a
                : achievements.entrySet()) {
            a.getValue().setUnlocked(true);
        }
    }

    public static void lockAllAchievements() {
        for(Map.Entry<Achievements, Achievement> a
                : achievements.entrySet()) {
            a.getValue().setUnlocked(false);
        }
    }

    public static Collection<Achievement> getAllAchievements() {
        return achievements.values();
    }

    public static List<Achievement> getAchievementList() {
        achievementList.sort(Comparator.comparingInt(a -> a.getId().ordinal()));
        return achievementList;
    }

    public Collection<Achievement> getUnlockedAchievements() {
        return achievements.values()
                .stream()
                .filter(Achievement::isUnlocked)
                .toList();
    }

    private void unlock(Achievement achievement) {
        achievement.setUnlocked(true);
    }
}
