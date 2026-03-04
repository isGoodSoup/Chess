package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.lud.engine.interfaces.Service;

public class AudioService implements Service {
    private Sound[] fx;
    private Music music;
    private float masterGain = 1f;
    private float musicGain  = 1f;
    private float sfxGain    = 1f;

    public AudioService() {
        loadFx();
    }

    public float getMusicGain() {
        return musicGain;
    }

    public void loadFx() {
        this.fx = new Sound[10];
        fx[0] = loadSound("click", ".wav");
        fx[1] = loadSound("menu-select", ".wav");
        fx[2] = loadSound("reveal", ".wav");
        fx[3] = loadSound("checkmate", ".wav");
        fx[4] = loadSound("piece_fx", ".ogg");
    }

    public Sound loadSound(String file, String format) {
        return Gdx.audio.newSound(Gdx.files.internal("sounds/" + file + format));
    }
    public Music loadMusic(String file, String format) {
        return Gdx.audio.newMusic(Gdx.files.internal("sounds/" + file + format));
    }

    public void toggleMusic() {
        if(music.isPlaying()) {
            stopMusic();
        } else {
            playMusic();
        }
    }

    public void playMusic() {
        if(music == null) {
            music = loadMusic("main-theme", ".ogg");
            music.setLooping(true);
            music.setVolume(musicGain);
        }
        music.play();
    }

    public Sound playFX(int i) {
        Sound sound = fx[i];
        sound.play(masterGain * sfxGain);
        return sound;
    }

    private void apply() {
        if (music != null) {
            music.setVolume(musicGain);
        }
    }

    private float linearToDb(float linear) {
        return 20f * (float)Math.log10(Math.max(linear, 0.0001f));
    }

    private float dbToLinear(float db) {
        return (float)Math.pow(10f, db/20f);
    }

    public void setMasterGain(float delta) {
        float linear = masterGain;
        linear += delta;
        linear = Math.max(0f, Math.min(1f, linear));
        masterGain = linear;
        apply();
    }

    public void setMusicVolume(float delta) {
        float linear = musicGain;
        linear += delta;
        linear = Math.max(0f, Math.min(1f, linear));
        musicGain = linear;
        apply();
    }

    public void setFXVolume(float delta) {
        float linear = sfxGain;
        linear += delta;
        linear = Math.max(0f, Math.min(1f, linear));
        sfxGain = linear;
        apply();
    }

    public void stopMusic() {
        if(music != null) {
            music.stop();
        }
    }

    public void dispose() {
        if(music != null) {
            music.dispose();
        }
    }
}
