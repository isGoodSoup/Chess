package org.vertex.engine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertex.engine.entities.*;
import org.vertex.engine.enums.Tint;
import org.vertex.engine.input.Mouse;

public class PromotionService {
    private Tint promotionColor;
    private Piece promotingPawn;
    private int promotionTracker;

    private static Mouse mouse;
    private final PieceService pieceService;

    private static final Logger log = LoggerFactory.getLogger(PromotionService.class);

    public PromotionService(PieceService pieceService, Mouse mouse) {
        this.pieceService = pieceService;
        PromotionService.mouse = mouse;
    }

    public Tint getPromotionColor() {
        return promotionColor;
    }

    public void setPromotionColor(Tint promotionColor) {
        this.promotionColor = promotionColor;
    }

    public Piece getPromotingPawn() {
        return promotingPawn;
    }

    public void setPromotingPawn(Piece promotingPawn) {
        this.promotingPawn = promotingPawn;
    }

    public int getPromotionTracker() {
        return promotionTracker;
    }

    public boolean checkPromotion(Piece p) {
        if (p instanceof Pawn) {
            if ((p.getColor() == Tint.LIGHT && p.getRow() == 0) ||
                    (p.getColor() == Tint.DARK && p.getRow() == 7)) {
                return true;
            }
        }

        if (!(p instanceof King)) {
            if ((p.getColor() == Tint.LIGHT && p.getRow() == 0) ||
                    (p.getColor() == Tint.DARK && p.getRow() == 7)) {
                return true;
            }
        }
        return false;
    }

    public Piece autoPromote(Piece piece) {
        if(piece == null || !BooleanService.canPromote) return piece;
        if(!checkPromotion(piece)) return piece;
        log.info("Promotion: {}, {}", piece.getRow(), piece.getCol());

        Piece promotedPiece = null;
        if(piece instanceof Pawn) {
            promotedPiece = new Queen(piece.getColor(), piece.getRow(),
                    piece.getCol());
        }
        else {
            promotedPiece = new King(pieceService, piece.getColor(),
                    piece.getRow(), piece.getCol());
        }

        pieceService.getPieces().remove(piece);
        pieceService.getPieces().add(promotedPiece);
        promotedPiece.setX(promotedPiece.getRow() * Board.getSquare());
        promotedPiece.setY(promotedPiece.getCol() * Board.getSquare());

        BoardService.getBoardState()
                [promotedPiece.getRow()][promotedPiece.getCol()] =
                promotedPiece;

        if(pieceService.getHeldPiece() == piece) {
            PieceService.nullThisPiece();
        }

        BooleanService.isPromotionActive = false;
        promotionTracker++;
        pieceService.switchTurns();

        if(promotionTracker == 10 && !BooleanService.doKingPromoter) {
            BooleanService.doKingPromoter = true;
        }
        return promotedPiece;
    }
}
