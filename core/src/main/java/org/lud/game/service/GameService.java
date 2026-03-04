package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.GameState;
import org.lud.engine.enums.Lang;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Menu;
import org.lud.engine.input.InputContext;
import org.lud.engine.input.InputManager;
import org.lud.engine.interfaces.Moves;
import org.lud.engine.interfaces.Service;
import org.lud.engine.service.ServiceFactory;
import org.lud.game.actors.Piece;
import org.lud.game.entities.Board;
import org.lud.game.enums.TypeID;
import org.lud.game.moves.MovePiece;
import org.lud.game.screens.AchievementsMenu;
import org.lud.game.screens.BoardScreen;
import org.lud.game.screens.MainMenu;
import org.lud.game.screens.SettingsMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameService implements Service {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameFrame gameFrame;
    private final ServiceFactory service;

    private Menu activeMenu;
    private final Menu mainMenu;
    private final Menu settingsMenu;
    private final Menu achievementsMenu;
    private final BoardScreen boardScreen;
    private GameState gameState;

    private Piece checkingPiece;

    private boolean isLegal;
    private boolean isFirstBoardEntry = true;
    private boolean isInputLocked;

    public GameService(GameFrame gameFrame, ServiceFactory service) {
        this.gameFrame = gameFrame;
        this.service = service;
        this.gameState = GameState.MENU;

        AudioService audio = service.get(AudioService.class);
        PieceService piece = service.get(PieceService.class);
        BoardService board = service.get(BoardService.class);
        AchievementService achievement = service.get(AchievementService.class);

        this.mainMenu = new MainMenu(this, audio, board, piece);
        this.settingsMenu = new SettingsMenu(this, audio, board, piece);
        this.achievementsMenu = new AchievementsMenu(this, audio, board, piece, achievement);
        this.boardScreen = new BoardScreen(board, this, piece, audio);

        InputContext global = new InputContext("Global");
        global.bindKey(Input.Keys.ESCAPE, () -> service.get(GameService.class).showMainMenu());
        global.bindKey(Input.Keys.M, audio::toggleMusic);
        global.bindKey(Input.Keys.NUMPAD_ADD, () -> audio.setMusicVolume(0.1f));
        global.bindKey(Input.Keys.NUMPAD_SUBTRACT, () -> audio.setMusicVolume(-0.1f));
        global.bindCombo(Input.Keys.T, Colors::nextTheme);
        global.bindCombo(Input.Keys.L, Lang::nextLang);
        global.bindCombo(Input.Keys.Q, Gdx.app::exit);
        InputManager.get().setGlobalContext(global);

        activeMenu = mainMenu;
    }

    public boolean isFirstBoardEntry() { return isFirstBoardEntry; }
    public void resetFirstBoardEntry() { isFirstBoardEntry = !isFirstBoardEntry; }

    public GameState getGameState() { return gameState; }
    public void setGameState(GameState gameState) { this.gameState = gameState; }

    public void showMainMenu() {
        activeMenu = mainMenu;
        gameState = GameState.MENU;
        gameFrame.setScreen(activeMenu);
    }
    public void showBoard() {
        activeMenu = boardScreen;
        gameState = GameState.BOARD;
        gameFrame.setScreen(activeMenu);
    }
    public void resetBoard() {
        service.get(PieceService.class).clearBoard();
        showBoard();
        Turn.setTurn(Turn.LIGHT);
    }
    public void newGame() {
        service.get(PieceService.class).clearBoard();
        showBoard();
        Turn.setTurn(Turn.LIGHT);
    }
    public void showSettings() {
        activeMenu = settingsMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void showAchievements() {
        activeMenu = achievementsMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void exit() {
        log.info("EoS (End of session)");
        Gdx.app.exit();
    }

    public void getActiveMenu(int index) {
        switch(index) {
            case 0 -> newGame();
            case 1 -> showSettings();
            case 2 -> showAchievements();
            case 3 -> exit();
        }
    }

    public List<Moves> newLegalMoves(Turn turn) {
        List<Moves> legalMoves = new ArrayList<>();
        List<Piece> pieces = service.get(PieceService.class).getPieces();

        for(Piece piece : new ArrayList<>(pieces)) {
            if(piece.getTurn() != turn) continue;
            for(int row = 0; row < 8; row++) {
                for(int col = 0; col < 8; col++) {
                    if(piece.getRow() == row && piece.getCol() == col) { continue; }
                    if(!BoardService.isWithinBoard(col, row)) { continue; }
                    if(!BoardService.isPathClear(piece, col, row, pieces)) { continue; }
                    Piece targetPiece = BoardService.getPieceAt(col, row, pieces);
                    if(targetPiece != null && targetPiece.getTurn() == turn) { continue; }

                    MovePiece move = new MovePiece(piece, piece.getCol(), piece.getRow(), col, row,
                        piece.getTurn(), targetPiece);

                    Piece king = null;
                    for(Piece p : pieces) {
                        if(p.getTypeID() == TypeID.KING) {
                            king = p;
                        }
                    }

                    if (canMove(piece, col, row, pieces) &&
                        !wouldLeaveKingInCheck(piece, col, row, pieces)
                        && service.get(BoardService.class).saveKing(piece, col, row, king)) {
                        legalMoves.add(move);
                    }
                }
            }
        }
        return legalMoves;
    }

    public boolean wouldLeaveKingInCheck(Piece piece, int targetCol, int targetRow, List<Piece> list) {

        List<Piece> simulated = list.stream()
            .map(p -> p.copy(p))
            .collect(Collectors.toList());

        Piece simPiece = simulated.stream()
            .filter(p -> p.getCol() == piece.getCol()
                && p.getRow() == piece.getRow()
                && p.getTurn() == piece.getTurn())
            .findFirst()
            .orElse(null);

        if(simPiece == null) {
            return true;
        }

        simulated.stream()
            .filter(p -> p.getCol() == targetCol && p.getRow() == targetRow)
            .findFirst().ifPresent(simulated::remove);

        simPiece.setCol(targetCol);
        simPiece.setRow(targetRow);

        Piece king = simulated.stream()
            .filter(p -> p.getTypeID() == TypeID.KING
                && p.getTurn() == piece.getTurn())
            .findFirst()
            .orElse(null);

        if (king == null) return true;

        for(Piece enemy : simulated) {
            if(enemy.getTurn() != piece.getTurn()) {
                if(canMove(enemy, king.getCol(), king.getRow(), simulated)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Piece findPiece(Piece piece, List<Piece> sim) {
        for(Piece simp : sim) {
            if(simp == piece) {
                return simp;
            }
        }
        return sim.getFirst();
    }

    public boolean isKingInCheck(Turn kingColor, List<Piece> list) {
        Piece king = null;
        for(Piece p : list) {
            if(p.getTypeID() == TypeID.KING && p.getTurn() == kingColor) {
                king = p;
                break;
            }
        }

        if(king == null) { return false; }
        for(Piece p : list) {
            if(p.getTurn() != kingColor) {
                if(canMove(p, king.getCol(), king.getRow(), list)) {
                    checkingPiece = p;
                    return true;
                }
            }
        }

        checkingPiece = null;
        return false;
    }

    public boolean isCheckmate() {
        Turn currentTurn = Turn.getTurn();
        Piece king = service.get(PieceService.class).getKing(currentTurn);
        if(king == null) { return false; }
        if(!isKingInCheck(currentTurn, service.get(PieceService.class).getPieces())) { return false; }

        List<Piece> pieces = service.get(PieceService.class).getPieces();

        for(Piece piece : pieces) {
            if(piece.getTurn() != currentTurn) { continue; }
            for(int col = 0; col < Board.getSIZE(); col++) {
                for(int row = 0; row < Board.getSIZE(); row++) {
                    if(canMove(piece, col, row, pieces) &&
                        !wouldLeaveKingInCheck(piece, col, row, pieces)) {
                        return false;
                    }
                }
            }
        }

        log.debug("Checkmate for {}", currentTurn);
        service.get(AudioService.class).stopMusic();
        service.get(AudioService.class).playFX(3);
        return true;
    }

    public boolean canMove(Piece p, int targetCol, int targetRow, List<Piece> list) {
        switch(p.getTypeID()) {
            case PAWN -> {
                int direction = (p.getTurn() == Turn.LIGHT) ? 1 : -1;
                Piece pieceAtTarget = BoardService.getPieceAt(targetCol, targetRow, list);

                if(targetCol == p.getCol() && targetRow == p.getRow() + direction) {
                    return pieceAtTarget == null;
                }
                if(targetCol == p.getCol() && targetRow == p.getRow() + 2 * direction
                    && !p.hasMoved() && BoardService.isPathClear(p, targetCol, targetRow, list)) {
                    return pieceAtTarget == null;
                }
                if(Math.abs(targetCol - p.getCol()) == 1 && targetRow == p.getRow() + direction) {
                    if(pieceAtTarget != null && pieceAtTarget.getTurn() != p.getTurn()) {
                        return true;
                    }
                    return service.get(BoardService.class).canEnPassant(p, targetCol, targetRow, list);
                }
            }
            case KNIGHT -> {
                int colDiff = Math.abs(targetCol - p.getCol());
                int rowDiff = Math.abs(targetRow - p.getRow());

                if((colDiff == 2 && rowDiff == 1) || (colDiff == 1 && rowDiff == 2)) {
                    return BoardService.isValidSquare(p, targetCol, targetRow, list);
                }
            }
            case BISHOP -> {
                int colDiff = targetCol - p.getCol();
                int rowDiff = targetRow - p.getRow();

                if(Math.abs(colDiff) != Math.abs(rowDiff)) {
                    return false;
                }

                if(!BoardService.isPathClear(p, targetCol, targetRow, list)) {
                    return false;
                }

                Piece target = null;
                for(Piece piece : list) {
                    if(piece.getCol() == targetCol && piece.getRow() == targetRow) {
                        target = piece;
                        break;
                    }
                }
                return target == null || target.getTurn() != p.getTurn();
            }
            case ROOK -> {
                if(targetCol == p.getCol() || targetRow == p.getRow()) {
                    return BoardService.isValidSquare(p, targetCol, targetRow, list)
                        && BoardService.isPathClear(p, targetCol, targetRow, list);
                }
            }
            case QUEEN -> {
                if(targetCol == p.getCol() || targetRow == p.getRow()) {
                    return BoardService.isValidSquare(p, targetCol, targetRow,
                        list)
                        && BoardService.isPathClear(p, targetCol, targetRow, list);
                }

                int colDiff = targetCol - p.getCol();
                int rowDiff = targetRow - p.getRow();

                if(Math.abs(colDiff) != Math.abs(rowDiff)) {
                    return false;
                }

                if(!BoardService.isPathClear(p, targetCol, targetRow, list)) {
                    return false;
                }

                Piece target = null;
                for(Piece piece : list) {
                    if(piece.getCol() == targetCol && piece.getRow() == targetRow) {
                        target = piece;
                        break;
                    }
                }
                return target == null || target.getTurn() != p.getTurn();
            }
            case KING -> {
                int colDiff = Math.abs(targetCol - p.getCol());
                int rowDiff = Math.abs(targetRow - p.getRow());

                if((colDiff + rowDiff == 1) || (colDiff * rowDiff == 1)) {
                    return BoardService.isValidSquare(p, targetCol, targetRow, list);
                }
            }
        }
        return false;
    }

    public boolean isInputLocked() { return isInputLocked; }
    public void setInputLocked(boolean inputLocked) { this.isInputLocked = inputLocked; }
}
