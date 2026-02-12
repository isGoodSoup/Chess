package org.chess.gui;

import org.chess.service.BooleanService;

import java.awt.*;
import java.io.Serial;

import javax.swing.JFrame;

public class ChessFrame extends JFrame {
	@Serial
    private static final long serialVersionUID = -3130387824420425271L;
	private final static String TITLE = "Chess";
	private final BoardPanel panel;
	private final GraphicsDevice gd;
	private Rectangle windowedBounds;

    public ChessFrame() {
		super(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(false);
		setResizable(false);
		panel = new BoardPanel(this);
		add(panel);
		pack();
		gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		setLocationRelativeTo(null);
		toggleFullscreen();
		setVisible(true);
		panel.launch();
	}

	public void toggleFullscreen() {
		dispose();
		if (BooleanService.isFullscreen) {
			windowedBounds = getBounds();
			setUndecorated(true);
			setResizable(false);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			setUndecorated(false);
			setResizable(true);
			setExtendedState(JFrame.NORMAL);
			if (windowedBounds != null) {
				setBounds(windowedBounds);
			}
		}
		BooleanService.isFullscreen = !BooleanService.isFullscreen;
		setVisible(true);
	}
}
