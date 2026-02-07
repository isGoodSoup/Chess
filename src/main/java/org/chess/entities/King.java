package org.chess.entities;

import org.chess.enums.Tint;
import org.chess.enums.Type;
import org.chess.gui.BoardPanel;

public class King extends Piece {

	public King(Tint color, int col, int row) {
		super(color, col, row);
		this.id = Type.KING;
		if(color == Tint.WHITE) {
			image = getImage("/pieces/king");
		} else {
			image = getImage("/pieces/king_b");
		}
	}

	@Override
	public boolean canMove(int targetCol, int targetRow, BoardPanel board) {
		if(isWithinBoard(targetCol, targetRow)) {
			int colDiff = Math.abs(targetCol - getPreCol());
			int rowDiff = Math.abs(targetRow - getPreRow());

			if((colDiff + rowDiff == 1) || (colDiff * rowDiff == 1)) {
				if(isValidSquare(targetCol, targetRow, board)) {
					return true;
				}
			}

			if(hasMoved()) {
				if(targetCol == getPreCol() + 2 && targetRow == getPreRow()
						&& isPieceOnTheWay(targetCol, targetRow)) {
					for(Piece p : board.getPieces()) {
						BoardPanel boardPanel = new BoardPanel();
						boardPanel.setCastlingPiece(p);
						return true;
					}
				}

				if(targetCol == getPreCol() - 2 && targetRow == getPreRow()
						&& isPieceOnTheWay(targetCol, targetRow)) {
					Piece[] ps = new Piece[2];
					for(Piece p : board.getPieces()) {
						if(p.getCol() == getPreCol() - 3 && p.getRow() == targetRow) { 
							ps[0] = p;
						}
						
						if(p.getCol() == getPreCol() - 4 && p.getRow() == targetRow) { 
							ps[1] = p;
						}
						
						if(ps[0] == null && ps[1] != null && ps[1].hasMoved()) {
							BoardPanel boardPanel2 = new BoardPanel();
							boardPanel2.setCastlingPiece(ps[1]);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
