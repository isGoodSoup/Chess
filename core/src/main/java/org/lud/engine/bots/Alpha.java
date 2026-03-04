package org.lud.engine.bots;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;

import java.util.List;

public class Alpha implements AI {
    @Override
    public Moves chooseMove(List<Moves> legalMoves) {
        if(legalMoves.isEmpty()) { return null; }

        List<Moves> safe = legalMoves.stream()
            .filter(m -> m.getScoreImpact() >= 0)
            .toList();
        if(!safe.isEmpty()) { legalMoves = safe; }

        int index = (int) (Math.random() * legalMoves.size());
        return legalMoves.get(index);
    }

    @Override
    public void switchTurns() {
        Turn.nextTurn();
    }
}
