package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.enums.Difficulty;
import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;
import org.lud.engine.interfaces.Service;
import org.lud.engine.service.ServiceFactory;
import org.lud.game.actors.Piece;
import org.lud.game.entities.Board;
import org.lud.game.enums.TypeID;
import org.lud.game.moves.MovePiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class BoardService implements Service {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    private static Board board;
    private final ServiceFactory service;
    private final OrthographicCamera camera;

    private final List<Moves> movePieces;
    private final List<Moves> moveAIPieces;

    private Difficulty difficulty;
    private AI currentAI;
    private boolean canUndo;

    public BoardService(ServiceFactory service, OrthographicCamera camera) {
        this.camera = camera;
        this.service = service;
        board = new Board();
        this.movePieces = new ArrayList<>();
        this.moveAIPieces = new ArrayList<>();
        this.difficulty = Difficulty.ALPHA;
        this.currentAI = Difficulty.setDiff(this.difficulty);
        canUndo = true;
    }

    public OrthographicCamera getCamera() { return camera; }
    public static Board getBoard() { return board; }

    public static Piece getPieceAt(int col, int row, List<Piece> pieces) {
        try {
            if(isWithinBoard(col, row)) {
                for(Piece p : pieces) {
                    if(p.getCol() == col && p.getRow() == row) {
                        return p;
                    }
                }
            }
        } catch(NullPointerException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public AI switchDifficulties(Difficulty d) {
        this.difficulty = d;
        this.currentAI = Difficulty.setDiff(d);
        return this.currentAI;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public boolean attemptMove(Piece piece, int targetCol, int targetRow) {
        if(!isWithinBoard(targetCol, targetRow)) return false;

        MovePiece move = new MovePiece(piece, piece.getCol(), piece.getRow(), targetCol,
            targetRow, piece.getTurn(), getPieceAt(targetCol, targetRow, service.get(PieceService.class).getPieces()));

        if(!isValidSquare(piece, targetCol, targetRow, service.get(PieceService.class).getPieces()) ||
            (!service.get(GameService.class).canMove(piece, targetCol, targetRow, service.get(PieceService.class).getPieces()) &&
            !service.get(GameService.class).wouldLeaveKingInCheck(piece, targetCol, targetRow,
                service.get(PieceService.class).getPieces()))) {
            return false;
        }

        if(!isPathClear(piece, targetCol, targetRow, service.get(PieceService.class).getPieces())) {
            return false;
        }

        executeMove(move);
        return true;
    }

    public void executeMove(MovePiece move) {
        Piece piece = move.piece();
        int targetCol = move.targetCol();
        int targetRow = move.targetRow();

        Piece captured = getPieceAt(targetCol, targetRow,
            service.get(PieceService.class).getPieces());

        if(captured != null && captured != piece) {
            service.get(PieceService.class).removePiece(captured);
            log.debug("{} {} > {} {}", captured.getTurn(), captured.getTypeID(),
                piece.getTurn(), piece.getTypeID());
        }

        if(piece.getTurn() == Turn.LIGHT) { movePieces.add(move);
        } else { moveAIPieces.add(move); }

        logMove(piece, targetCol, targetRow);
        Turn.nextTurn();

        canUndo = !canUndo;

        if(service.get(GameService.class).isCheckmate()) {
            service.get(GameService.class).setInputLocked(true);
            log.info("Checkmate! Game over for {}", Turn.getTurn());
            return;
        }
    }

    public void executeAIMove() {
        service.get(GameService.class).setInputLocked(true);
        List<Moves> legalMoves = service.get(GameService.class).newLegalMoves(Turn.DARK);

        Moves aiMove = currentAI.chooseMove(legalMoves);
        if(aiMove instanceof MovePiece movePiece) {
            executeMove(movePiece);
        }

        if(!service.get(GameService.class).isCheckmate()) {
            service.get(GameService.class).setInputLocked(false);
        }
    }

    public void undoMove() {
        if(movePieces.isEmpty()) { return; }
        if(!canUndo) { return; }
        List<Moves> moves = movePieces;
        MovePiece move = (MovePiece) moves.getLast();
        move.undo();
        canUndo = false;
    }

    public List<Moves> getMovePieces() {
        return movePieces;
    }
    public List<Moves> getMoveAIPieces() {
        return moveAIPieces;
    }

    public static boolean isWithinBoard(int col, int row) {
        return col >= 0 && col < 8 && row >= 0 && row < 8;
    }

    public static boolean isValidSquare(Piece piece, int targetCol, int targetRow,
                                        List<Piece> board) {
        for(Piece p : board) {
            if(p.getCol() == targetCol && p.getRow() == targetRow) {
                if(p.getTypeID() == TypeID.KING) { return false; }
                return p.getTurn() != piece.getTurn();
            }
        }
        return true;
    }

    public static boolean isPathClear(Piece piece, int targetCol, int targetRow,
                                      List<Piece> list) {
        int colDiff = targetCol - piece.getCol();
        int rowDiff = targetRow - piece.getRow();

        switch(piece.getTypeID()) {
            case KNIGHT:
                return true;
            default:
                break;
        }

        int colStep = Integer.signum(colDiff);
        int rowStep = Integer.signum(rowDiff);

        if(colStep == 0 && rowStep == 0) { return false; }
        if(colStep != 0 && rowStep != 0 && Math.abs(colDiff) != Math.abs(rowDiff)) { return false; }

        int currentCol = piece.getCol() + colStep;
        int currentRow = piece.getRow() + rowStep;

        while(currentCol != targetCol || currentRow != targetRow) {
            if(getPieceAt(currentCol, currentRow, list) != null) { return false; }
            currentCol += colStep;
            currentRow += rowStep;
        }
        return true;
    }

    public boolean saveKing(Piece piece, int targetCol, int targetRow, Piece king) {
        List<Piece> copy = service.get(PieceService.class).getPieces().stream()
            .map(p -> p.copy(p))
            .toList();

        Piece simPiece = copy.stream()
            .filter(p -> p.equals(piece))
            .findFirst().orElseThrow();

        simPiece.setCol(targetCol);
        simPiece.setRow(targetRow);

        Piece simKing = copy.stream()
            .filter(p -> p.getTypeID() == TypeID.KING && p.getTurn() == king.getTurn())
            .findFirst().orElseThrow();

        return !service.get(GameService.class).isKingInCheck(simKing.getTurn(), copy);
    }

    public boolean canEnPassant(Piece piece, int targetCol, int targetRow, List<Piece> board) {
        for(Piece p : board) {
            if(p.getTypeID() == TypeID.PAWN && p.getTurn() != piece.getTurn()
                && p.getCol() == targetCol && p.getRow() == piece.getRow() && p.isTwoStepsAhead()) {
                p.setOther(p);
                return true;
            }
        }
        return false;
    }

    private MovePiece addMove(MovePiece movePiece) {
        movePieces.add(movePiece);
        return movePiece;
    }

    private boolean isEnPassantMove(Piece pa, int targetCol, int targetRow) {
        return false;
    }

    private void executeCastling(Piece king, int targetCol) {}

    private void executeEnPassant(Piece pa, int targetCol, int targetRow) {}

    public float getBoardStartX() {
        return (Gdx.graphics.getWidth() - Board.getSIZE())/2f;
    }

    public float getBoardStartY() {
        return (Gdx.graphics.getHeight() - Board.getSIZE()) / 2f;
    }

    private void logMove(Piece p, int targetCol, int targetRow) {
        log.info("{} {}: {} to {}", p.getTurn(), p.getTypeID(),
            board.getSquareNameAt(p.getPreCol(), p.getPreRow()),
            board.getSquareNameAt(targetCol, targetRow));
    }
}
