package org.chess.gui;

import java.awt.HeadlessException;

import javax.swing.JFrame;

public class ChessFrame extends JFrame {
	private static final long serialVersionUID = -3130387824420425271L;
	private final static String TITLE = "Chess";
	private final BoardPanel panel;
	
	public ChessFrame() throws HeadlessException {
		super(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		panel = new BoardPanel();
		add(panel);
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		panel.launch();
	}
	
	
}
