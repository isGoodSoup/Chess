package org.vertex.engine.rulesets;

import org.vertex.engine.entities.Piece;
import org.vertex.engine.enums.Tint;
import org.vertex.engine.interfaces.Ruleset;
import org.vertex.engine.records.Move;
import org.vertex.engine.records.MoveScore;
import org.vertex.engine.service.BoardService;
import org.vertex.engine.service.PieceService;

import java.util.List;

public class CheckersRuleset implements Ruleset {
    private final PieceService pieceService;
    private final BoardService boardService;

    public CheckersRuleset(PieceService pieceService, BoardService boardService) {
        this.pieceService = pieceService;
        this.boardService = boardService;
    }

    @Override
    public boolean isLegalMove(Piece p, int col, int row) {
        return false;
    }

    @Override
    public int evaluateMove(Move move) {
        return 0;
    }

    @Override
    public List<MoveScore> getAllLegalMoves(Tint color) {
        return List.of();
    }
}
