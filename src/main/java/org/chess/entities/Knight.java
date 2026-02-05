package org.chess.entities;

import org.chess.enums.Tint;
import org.chess.enums.Type;
import org.chess.gui.BoardPanel;

public class Knight extends Piece {

	public Knight(Tint color, int col, int row) {
		super(color, col, row);
		this.id = Type.KNIGHT;
		if(color == Tint.WHITE) {
			image = getImage("/pieces/knight");
		} else {
			image = getImage("/pieces/knight_b");
		}
	}

	@Override
	public boolean canMove(int targetCol, int targetRow, BoardPanel board) {
	    if(isWithinBoard(targetCol, targetRow)) {
	        if(Math.abs(targetCol - getPreCol()) * Math.abs(targetRow - getPreRow()) == 2) {
	        	if(isValidSquare(targetCol, targetRow, board)) {
	        		return true;
	        	}
	        }
	    }
	    return false;
	}
}
