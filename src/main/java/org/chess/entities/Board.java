package org.chess.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
	private final int COL = 8;
	private final int ROW = 8;
	private static final int SQUARE = 64;
	private static final int HALF_SQUARE = SQUARE / 2;

	public int getCOL() {
		return COL;
	}

	public int getROW() {
		return ROW;
	}

	public static int getSquare() {
		return SQUARE;
	}

	public static int getHalfSquare() {
		return HALF_SQUARE;
	}

	public void draw(Graphics2D g2) {
		int x = 0;
		for (int row = 0; row < ROW; row++) {
			for (int col = 0; col < COL; col++) {
				if (x == 0) {
					g2.setColor(new Color(210, 165, 125));
					x = 1;
				} else {
					g2.setColor(new Color(175, 115, 70));
					x = 0;
				}
				g2.fillRect(col * SQUARE, row * SQUARE, SQUARE, SQUARE);
			}

			if (x == 0) {
				x = 1;
			} else {
				x = 0;
			}
		}
	}
}
