package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.lud.engine.enums.Difficulty;
import org.lud.engine.enums.Direction;
import org.lud.engine.enums.GameState;
import org.lud.engine.enums.Lang;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Menu;
import org.lud.engine.service.ServiceFactory;
import org.lud.game.service.AudioService;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;

public class GlobalScreen extends Menu {
    private final ServiceFactory service;

    public GlobalScreen(ServiceFactory service) {
        this.service = service;
    }

    @Override public void setup() {}
    @Override public void checkInput() {}
    @Override public void playFX(int i) {}

    @Override
    public void loadKeys() {
        getActions().put(Input.Keys.ESCAPE, () -> {
            service.get(GameService.class).showMainMenu();
            playFX(0);
        });
        getActions().put(Input.Keys.UP, () -> cursor(Direction.UP));
        getActions().put(Input.Keys.DOWN, () -> cursor(Direction.DOWN));
        getActions().put(Input.Keys.M, () -> service.get(AudioService.class).toggleMusic());
        getActions().put(Input.Keys.NUMPAD_ADD, () -> service.get(AudioService.class).setMusicVolume(0.1f));
        getActions().put(Input.Keys.NUMPAD_SUBTRACT, () -> service.get(AudioService.class).setMusicVolume(-0.1f));

        getCombos().put(Input.Keys.T, () -> {
            if(service.get(GameService.class).getGameState() == GameState.BOARD) { return; }
            Colors.nextTheme();
            playFX(1);
        });
        getCombos().put(Input.Keys.L, Lang::nextLang);
        getCombos().put(Input.Keys.D, () -> {
            Difficulty next = service.get(BoardService.class).getDifficulty().nextDifficulty();
            service.get(BoardService.class).switchDifficulties(next);
            createToast(next);
            playFX(2);
        });
        getCombos().put(Input.Keys.Q, Gdx.app::exit);
    }
}
