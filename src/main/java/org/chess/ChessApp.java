package org.chess;

import org.chess.gui.ChessFrame;

public class ChessApp {
	private final ChessFrame frame;

	public ChessApp() {
		super();
		this.frame = new ChessFrame();
	}
	
	public static void main(String[] args) {
		ChessApp app = new ChessApp();
		app.init();
	}

	public ChessFrame getFrame() {
		return frame;
	}
	
	public void init() {
		
	}
}
