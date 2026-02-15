package org.vertex.engine.entities;

import org.vertex.engine.enums.Tint;
import org.vertex.engine.enums.Type;

import java.util.List;

public class Checker extends Piece {

    public Checker(Tint color, int col, int row) {
        super(color, col, row);
        this.id = Type.CHECKERS;
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, List<Piece> board) {
        return false;
    }

    @Override
    public Piece copy() {
        return null;
    }
}
