package org.chess.entities;

import org.chess.enums.Tint;
import org.chess.enums.Type;
import org.chess.gui.BoardPanel;

public class Queen extends Piece {

	public Queen(Tint color, int col, int row) {
		super(color, col, row);
		this.id = Type.QUEEN;
		if(color == Tint.WHITE) {
			image = getImage("/pieces/queen");
		} else {
			image = getImage("/pieces/queen_b");
		}
	}

	@Override
	public boolean canMove(int targetCol, int targetRow, BoardPanel board) {
	    if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
	    	 if(targetCol == getPreCol() || targetRow == getPreRow()) {
	    		 if(isValidSquare(targetCol, targetRow, board) && isPieceOnTheWay(targetCol, targetRow)) {
	    			 return true;
	    		 }
	    	 }
	    	 if(Math.abs(targetCol - getPreCol()) == Math.abs(targetRow - getPreRow())) {
	    		 if(isValidSquare(targetCol, targetRow, board)) {
	    			 return true;
	    		 }
		     }
	    }
	    return false;
	}
}
