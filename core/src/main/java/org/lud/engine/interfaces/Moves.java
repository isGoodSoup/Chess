package org.lud.engine.interfaces;

import org.lud.engine.enums.Turn;

public interface Moves {
    void apply();
    void undo();
    int getScoreImpact();
    int isCapture();
    int isUnique();
    Turn getTurn();
    boolean isTurn();
}
