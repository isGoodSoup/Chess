package org.lud.engine.interfaces;

import java.util.List;

public interface AI {
    Moves chooseMove(List<Moves> legalMoves);
    void switchTurns();
}
