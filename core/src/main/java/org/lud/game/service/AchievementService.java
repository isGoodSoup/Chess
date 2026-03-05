package org.lud.game.service;

import org.lud.engine.data.Achievement;
import org.lud.engine.interfaces.Service;
import org.lud.engine.service.AchievementPersistence;
import org.lud.engine.service.EventBus;
import org.lud.engine.service.ServiceFactory;
import org.lud.game.actors.Piece;
import org.lud.game.enums.Achievements;
import org.lud.game.events.CaptureEvent;

import java.util.*;

public class AchievementService implements Service {
    private final EventBus eventBus;
    private final ServiceFactory service;
    private final AchievementPersistence persistence;
    private final Map<Achievements, Achievement<Achievements>> achievements;

    public AchievementService(EventBus eventBus, ServiceFactory service,
                              AchievementPersistence persistence) {
        this.eventBus = eventBus;
        this.service = service;
        this.persistence = persistence;
        this.achievements = new LinkedHashMap<>();

        for(Achievements id : Achievements.values()) {
            achievements.put(id, new Achievement<>(id));
        }

        eventBus.register(CaptureEvent.class, this::onCapture);
    }

    public void unlock(Achievements id){
        achievements.computeIfPresent(id,(k, v) -> v.unlocked() ? v : v.unlock());
        persistence.save(achievements.values());
        eventBus.fire(new AchievementUnlockedEvent(id));
    }

    public Collection<Achievement<Achievements>> getAllAchievements() {
        return achievements.values();
    }

    public List<Achievement<Achievements>> listOfAchievements() {
        return new ArrayList<>(achievements.values());
    }

    public List<Achievement<Achievements>> getSortedAchievements(Collection<Achievement<Achievements>> list) {
        return list.stream()
            .sorted(Comparator.comparingInt(a -> a.id().ordinal()))
            .toList();
    }

    public List<Achievement<Achievements>> getUnlockedAchievements() {
        return achievements.values().stream()
            .filter(Achievement::unlocked)
            .toList();
    }

    public List<Achievement<Achievements>> getLockedAchievements() {
        return achievements.values().stream()
            .filter(a -> !a.unlocked())
            .toList();
    }

    public boolean isUnlocked(Achievements id) {
        return achievements.get(id).unlocked();
    }

    public void onCapture(CaptureEvent event) {
        Piece captured = event.captured();

        if(captured != null && !isUnlocked(Achievements.FIRST_CAPTURE)) {
            unlock(Achievements.FIRST_CAPTURE);
        }
    }
}
