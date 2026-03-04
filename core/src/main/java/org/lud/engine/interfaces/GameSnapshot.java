package org.lud.engine.interfaces;

import org.lud.engine.enums.Turn;

import java.util.List;

public interface GameSnapshot {
    List<Moves> getLegalMoves(Turn player);
    void applyMove(Moves move);
    void undoMove(Moves move);
    boolean isGameOver();
    int evaluate(Turn player);
}
