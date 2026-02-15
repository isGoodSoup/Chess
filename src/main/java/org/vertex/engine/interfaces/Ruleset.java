package org.vertex.engine.interfaces;

import org.vertex.engine.entities.Piece;
import org.vertex.engine.enums.Tint;
import org.vertex.engine.records.Move;
import org.vertex.engine.records.MoveScore;

import java.util.List;

public interface Ruleset {
    boolean isLegalMove(Piece p, int col, int row);
    int evaluateMove(Move move);
    List<MoveScore> getAllLegalMoves(Tint color);
}
