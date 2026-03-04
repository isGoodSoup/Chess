package org.lud.engine.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameFrame extends Game {

    @Override public void create() {}
    @Override public void render() {
        super.render();
    }
    @Override public void dispose() {
        getScreen().dispose();
    }
    @Override public void setScreen(Screen screen) {
        super.setScreen(screen);
    }
}
