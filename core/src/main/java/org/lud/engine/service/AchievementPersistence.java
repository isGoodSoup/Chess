package org.lud.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lud.engine.data.Achievement;
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

    public void save(Collection<Achievement> achievements){
        try{
            mapper.writeValue(file,achievements);
        } catch(IOException e){
            log.error(e.getMessage());
        }
    }

    public List<Achievement> load(){
        if(!file.exists()) return new ArrayList<>();
        try {
            Achievement[] array = mapper.readValue(file,Achievement[].class);
            return Arrays.asList(array);
        } catch(IOException e){
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
