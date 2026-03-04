package org.lud.game.service;

import org.lud.engine.data.Achievement;
import org.lud.engine.interfaces.Service;
import org.lud.engine.service.AchievementPersistence;
import org.lud.engine.service.EventBus;
import org.lud.engine.service.ServiceFactory;
import org.lud.game.enums.Achievements;

import java.util.*;

public class AchievementService implements Service {
    private final EventBus eventBus;
    private final ServiceFactory service;
    private final AchievementPersistence persistence;
    private final Map<Achievements, Achievement> achievements;

    public AchievementService(EventBus eventBus, ServiceFactory service,
                              AchievementPersistence persistence) {
        this.eventBus = eventBus;
        this.service = service;
        this.persistence = persistence;
        this.achievements = new LinkedHashMap<>();

        for(Achievements id : Achievements.values()) {
            achievements.put(id, new Achievement(id));
        }
    }

    public void unlock(Achievements id){
        achievements.computeIfPresent(id,(k, v) -> v.unlocked() ? v : v.unlock());
        persistence.save(achievements.values());
    }

    public Collection<Achievement> getAllAchievements() {
        return achievements.values();
    }

    public List<Achievement> listOfAchievements() {
        return new ArrayList<>(achievements.values());
    }

    public List<Achievement> getSortedAchievements(Collection<Achievement> list) {
        return list.stream()
            .sorted(Comparator.comparingInt(a -> a.id().ordinal()))
            .toList();
    }

    public List<Achievement> getUnlockedAchievements() {
        return achievements.values().stream()
            .filter(Achievement::unlocked)
            .toList();
    }

    public List<Achievement> getLockedAchievements() {
        return achievements.values().stream()
            .filter(a -> !a.unlocked())
            .toList();
    }

    public boolean isUnlocked(Achievements id) {
        return achievements.get(id).unlocked();
    }
}
