package org.lud.engine.bots;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;

import java.util.ArrayList;
import java.util.List;

public class Beta implements AI {
    @Override
    public Moves chooseMove(List<Moves> legalMoves) {
        if(legalMoves.isEmpty()) { return null; }

        int bestScore = Integer.MIN_VALUE;
        List<Moves> bestMoves = new ArrayList<>();

        for(Moves move : legalMoves) {
            int score = move.getScoreImpact();
            if(score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if(score == bestScore) {
                bestMoves.add(move);
            }
        }

        int index = (int) (Math.random() * bestMoves.size());
        return bestMoves.get(index);
    }

    @Override
    public void switchTurns() {
        Turn.nextTurn();
    }
}
