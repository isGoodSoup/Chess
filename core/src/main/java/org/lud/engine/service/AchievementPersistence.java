package org.lud.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lud.engine.data.Achievement;
import org.lud.engine.interfaces.Achieveable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ALL")
public class AchievementPersistence{
    private static final Logger log = LoggerFactory.getLogger(AchievementPersistence.class);
    private final ObjectMapper mapper=new ObjectMapper();
    private final File file;

    public AchievementPersistence(){
        String home=System.getProperty("user.home");
        File dir=new File(home, ".lud");
        if(!dir.exists()) { dir.mkdirs(); }
        this.file=new File(dir,"achievements.json");
    }

    public <T extends Achieveable> void save(Collection<Achievement<T>> achievements) {
        try {
            mapper.writeValue(file, achievements);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public <T extends Achieveable> List<Achievement<T>> load(Class<T> c) {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            Achievement<T>[] array = mapper.readValue(file, Achievement[].class);
            return Arrays.asList(array);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
