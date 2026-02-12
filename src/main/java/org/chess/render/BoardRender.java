package org.chess.render;

import org.chess.entities.Board;
import org.chess.entities.Piece;
import org.chess.gui.Colors;
import org.chess.service.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardRender {
    private final RenderContext render;
    private final GUIService guiService;
    private final PieceService pieceService;
    private final BoardService boardService;
    private final PromotionService promotionService;

    public BoardRender(RenderContext render, GUIService guiService,
                       PieceService pieceService,
                       BoardService boardService,
                       PromotionService promotionService) {
        this.render = render;
        this.guiService = guiService;
        this.pieceService = pieceService;
        this.boardService = boardService;
        this.promotionService = promotionService;

    }

    public int getBoardOriginX() {
        int leftPanelWidth = render.scale(RenderContext.BASE_WIDTH/2);
        int totalBoardWidth = Board.getSquare() * boardService.getBoard().getCOL();
        int scaledBoardWidth = render.scale(totalBoardWidth);
        int middlePanelWidth =
                render.scale(RenderContext.BASE_WIDTH - 2 * RenderContext.BASE_WIDTH/2);
        int centerOffset = (middlePanelWidth - scaledBoardWidth)/2;
        return render.getOffsetX() + leftPanelWidth + centerOffset;
    }

    public int getBoardOriginY() {
        int totalBoardHeight = Board.getSquare() * boardService.getBoard().getROW();
        int scaledBoardHeight = render.scale(totalBoardHeight);
        int panelHeight = render.scale(RenderContext.BASE_HEIGHT);
        int centerOffset = (panelHeight - scaledBoardHeight) / 2;
        return render.getOffsetY() + centerOffset;
    }

    public void drawBoard(Graphics2D g2) {
        Piece currentPiece = PieceService.getPiece();
        Piece hoveredPiece = pieceService.getHoveredPieceKeyboard();
        int hoverX = pieceService.getHoveredSquareX();
        int hoverY = pieceService.getHoveredSquareY();

        drawBaseBoard(g2);

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        if (hoverX >= 0 && hoverY >= 0) {
            int squareSize = render.scale(Board.getSquare());
            guiService.drawBox(g2, 4, getBoardOriginX() + hoverX * squareSize,
                    getBoardOriginY() + hoverY * squareSize, squareSize,
                    squareSize, 16, 16, false);
        }

        Piece selectedPiece = pieceService.getMoveManager() != null
                ? pieceService.getMoveManager().getSelectedPiece() : null;

        for (Piece p : pieceService.getPieces()) {
            if (p != currentPiece) {
                BufferedImage img = (p == hoveredPiece) ? hoveredPiece.getHovered() : p.getImage();
                drawPiece(g2, p, Colorblindness.filter(img));
            }
        }

        if (currentPiece != null) {
            if (!BooleanService.isDragging) {
                currentPiece.setScale(currentPiece.getDEFAULT_SCALE());
            }
            drawPiece(g2, currentPiece);
        }
    }

    public void drawBaseBoard(Graphics2D g2) {
        g2.setColor(Colorblindness.filter(Colors.getEVEN()));
        g2.fillRect(0, 0, RenderContext.BASE_WIDTH, RenderContext.BASE_HEIGHT);
        final int ROW = boardService.getBoard().getROW();
        final int COL = boardService.getBoard().getCOL();
        final int SQUARE = render.scale(Board.getSquare());
        final int PADDING = render.scale(Board.getPadding());

        int square = render.scale(Board.getSquare());
        float edgePadding = 0.15f;
        int boardSize = square * boardService.getBoard().getCOL();
        int edgeSize = (int) (boardSize * (1 + edgePadding));

        int originX = getBoardOriginX() - (edgeSize - boardSize)/2;
        int originY = getBoardOriginY() - (edgeSize - boardSize)/2;

        g2.setColor(Colorblindness.filter(Colors.getEDGE()));
        g2.fillRect(originX, originY, edgeSize, edgeSize);

        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                boolean isEven = (row + col) % 2 == 0;
                g2.setColor(isEven ? Colorblindness.filter(Colors.getEVEN())
                        : Colorblindness.filter(Colors.getODD()));
                g2.fillRect(
                        getBoardOriginX() + col * SQUARE,
                        getBoardOriginY() + row * SQUARE,
                        SQUARE,
                        SQUARE
                );

                g2.setFont(GUIService.getFont(16));
                g2.setColor(isEven ? Colorblindness.filter(Colors.getODD())
                        : Colorblindness.filter(Colors.getEVEN()));
                g2.drawString(
                        BoardService.getSquareName(col, row),
                        getBoardOriginX() + col * SQUARE + PADDING,
                        getBoardOriginY() + row * SQUARE + SQUARE - PADDING
                );
            }
        }
    }

    public void drawPiece(Graphics2D g2, Piece piece) {
        drawPiece(g2, piece, null);
    }

    public void drawPiece(Graphics2D g2, Piece piece, BufferedImage override) {
        int square = render.scale(Board.getSquare());
        int drawSize = (int) (square * piece.getScale());
        int offset = (square - drawSize)/2;

        g2.drawImage(
                override != null ? override : piece.getImage(),
                getBoardOriginX() + render.scale(piece.getX()) + offset,
                getBoardOriginY() + render.scale(piece.getY()) + offset,
                drawSize,
                drawSize,
                null
        );
    }
}