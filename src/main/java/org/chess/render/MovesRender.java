package org.chess.render;

import org.chess.entities.Board;
import org.chess.enums.Tint;
import org.chess.records.Move;
import org.chess.service.BoardService;
import org.chess.service.GUIService;

import java.awt.*;
import java.util.List;

public class MovesRender {
    private final BoardService boardService;
    private final GUIService guiService;
    private final RenderContext render;

    public MovesRender(RenderContext render, BoardService boardService,
                       GUIService guiService) {
        this.boardService = boardService;
        this.guiService = guiService;
        this.render = render;
    }

    public void drawMoves(Graphics2D g2) {
        int extraWidth = render.scale(GUIService.getEXTRA_WIDTH());
        int boardWidth = render.scale(RenderContext.BASE_WIDTH);
        int panelHeight = render.scale(RenderContext.BASE_HEIGHT);

        // Draw background for left and right panels
        g2.setColor(GUIService.getNewBackground());
        g2.fillRect(0, 0, extraWidth, panelHeight);
        g2.fillRect(boardWidth, 0, extraWidth, panelHeight);

        // Set font
        g2.setFont(GUIService.getFontBold(20));
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = render.scale(fm.getHeight() + 8);

        // Offsets
        int offsetX = render.scale(25);
        int leftX = offsetX;
        int rightX = boardWidth + offsetX;

        int leftY = lineHeight;
        int rightY = lineHeight;

        List<Move> moves = boardService.getMoves();
        int startIndex = Math.max(0, moves.size() - GUIService.getMOVES_CAP());

        for (int i = startIndex; i < moves.size(); i++) {
            Move move = moves.get(i);
            boolean isLast = (i == moves.size() - 1);

            String moveText = BoardService.getSquareName(move.fromCol(), move.fromRow()) +
                    " > " +
                    BoardService.getSquareName(move.targetCol(), move.targetRow());

            if (move.color() == Tint.WHITE) {
                g2.setColor(isLast ? Color.YELLOW : Color.WHITE);
                g2.drawString(moveText, rightX, rightY);
                rightY += lineHeight;
            } else {
                g2.setColor(isLast ? Color.CYAN : Color.WHITE);
                g2.drawString(moveText, leftX, leftY);
                leftY += lineHeight;
            }
        }
    }
}