package org.chess.render;

import org.chess.entities.Board;
import org.chess.enums.Tint;
import org.chess.records.Move;
import org.chess.service.BoardService;
import org.chess.service.BooleanService;
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
        if(!BooleanService.canToggleMoves) { return; }
        int boardWidth = render.scale(RenderContext.BASE_WIDTH) - Board.getSquare() * 8;
        int totalHeight = render.scale(RenderContext.BASE_HEIGHT);

        g2.setFont(GUIService.getFontBold(24));
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = render.scale(fm.getHeight() + 8);

        int stroke = 4;
        int boardX = guiService.getBoardRender().getBoardOriginX();
        int boardY = guiService.getBoardRender().getBoardOriginY();
        int boardSize = Board.getSquare() * boardService.getBoard().getCOL();
        int arcWidth = 25;
        int arcHeight = 25;
        boolean hasBackground = true;

        int padding = render.scale(GUIService.getPADDING() - 30);
        int innerPadding = render.scale(24);

        int availableWidth = render.scale(RenderContext.BASE_WIDTH) - boardSize - padding * 4;
        int boxWidth = availableWidth/7;
        int boxHeight = boardSize - 10;

        int leftX = boardX - boxWidth - padding + innerPadding;
        int rightX = boardX + boardSize + padding + innerPadding;
        int leftY = boardY + innerPadding + fm.getAscent();
        int rightY = boardY + innerPadding + fm.getAscent();

        guiService.drawBox(g2, stroke, boardX - boxWidth - padding,
                boardY, boxWidth, boxHeight, arcWidth, arcHeight, hasBackground);
        guiService.drawBox(g2, stroke, boardX + boardSize + padding,
                boardY, boxWidth, boxHeight, arcWidth, arcHeight, hasBackground);

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
                rightY += fm.getHeight() + render.scale(4);
            } else {
                g2.setColor(isLast ? Color.CYAN : Color.WHITE);
                g2.drawString(moveText, leftX, leftY);
                leftY += fm.getHeight() + render.scale(4);
            }
        }
    }

    public void hideMoves() {
        BooleanService.canToggleMoves = !BooleanService.canToggleMoves;
    }
}