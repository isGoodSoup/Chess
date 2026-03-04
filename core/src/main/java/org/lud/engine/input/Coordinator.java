package org.lud.engine.input;

import com.badlogic.gdx.InputAdapter;
import org.lud.engine.enums.LastInput;

public class Coordinator extends InputAdapter {
    private static LastInput lastInput;

    public static LastInput getLastInput() {
        return lastInput;
    }

    public static void setLastInput(LastInput lastInput) {
        Coordinator.lastInput = lastInput;
    }

    @Override public boolean keyDown(int keycode) { lastInput = LastInput.KEYBOARD; return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { lastInput = LastInput.MOUSE; return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { lastInput = LastInput.MOUSE; return false; }
    @Override public boolean scrolled(float amountX, float amountY) { lastInput = LastInput.MOUSE; return false; }
}
