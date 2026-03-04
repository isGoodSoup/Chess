package org.lud.engine.bots;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;

import java.util.*;

public class Coronel implements AI {
    private final Set<String> history = new HashSet<>();

    @Override
    public Moves chooseMove(List<Moves> legalMoves) {
        if(legalMoves.isEmpty()) return null;

        int bestScore = Integer.MIN_VALUE;
        List<Moves> bestMoves = new ArrayList<>();

        for(Moves move : legalMoves) {
            int score = move.getScoreImpact();
            score += move.isCapture();
            score += history.contains(moveKey(move)) ? 0 : 5;

            int opponentMax = simulateOpponentResponse(move, legalMoves);
            score -= opponentMax;

            if(score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if(score == bestScore) {
                bestMoves.add(move);
            }
        }

        if(!bestMoves.isEmpty()) {
            Moves chosen = bestMoves.get((int) (Math.random() * bestMoves.size()));
            history.add(moveKey(chosen));
            return chosen;
        }

        return legalMoves.get((int) (Math.random() * legalMoves.size()));
    }

    private int simulateOpponentResponse(Moves move, List<Moves> allMoves) {
        int maxImpact = 0;
        Turn opponentTurn = (move.isTurn()) ? Turn.DARK : Turn.LIGHT;
        for(Moves oppMove : allMoves) {
            if(oppMove.getTurn() != opponentTurn) continue;

            int impact = oppMove.getScoreImpact();
            impact += oppMove.isCapture();
            maxImpact = Math.max(maxImpact, impact);
        }

        return maxImpact;
    }

    @Override
    public void switchTurns() {
        Turn.nextTurn();
    }

    private String moveKey(Moves move) {
        return move.getScoreImpact() + ":" + move.isCapture();
    }
}
