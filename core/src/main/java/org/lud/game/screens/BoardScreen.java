package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.lud.engine.enums.Direction;
import org.lud.engine.enums.LastInput;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Menu;
import org.lud.engine.gui.Toast;
import org.lud.engine.input.InputContext;
import org.lud.engine.input.InputManager;
import org.lud.engine.service.EventBus;
import org.lud.game.input.BoardInput;
import org.lud.engine.input.Coordinator;
import org.lud.engine.interfaces.Moves;
import org.lud.game.actors.BackgroundTile;
import org.lud.game.actors.Piece;
import org.lud.game.actors.Tile;
import org.lud.engine.data.ButtonData;
import org.lud.engine.enums.UIButton;
import org.lud.game.moves.MovePiece;
import org.lud.game.service.AudioService;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;
import org.lud.game.service.PieceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BoardScreen extends Menu {
    private static final float DURATION = 1f;
    private static final int TILE_SIZE = 64;
    private static final int BOARD_SIZE = TILE_SIZE * 8;
    private static final int PADDING = 32;
    private static final Logger log = LoggerFactory.getLogger(BoardScreen.class);

    private Coordinator coordinator;
    private final BoardInput boardInput;
    private InputMultiplexer multiplexer;

    private final BoardService boardService;
    private final GameService gameService;
    private final PieceService pieceService;
    private final AudioService audioService;
    private final EventBus eventBus;

    private final List<ButtonData> data;

    private Piece selectedPiece;

    private Group boardGroup;
    private Group uiGroup;
    private Texture baseButton;
    private Texture frame;

    private final float startX;
    private final float startY;

    private int lastAnimatedDark = -1;
    private int lastAnimatedLight = -1;

    public BoardScreen(BoardService boardService, GameService gameService, PieceService pieceService,
                       AudioService audioService, EventBus eventBus) {
        super(eventBus);
        this.boardService = boardService;
        this.gameService = gameService;
        this.pieceService = pieceService;
        this.audioService = audioService;
        this.eventBus = eventBus;
        this.data = new ArrayList<>();

        this.startX = (Gdx.graphics.getWidth() - BOARD_SIZE)/2f;
        this.startY = (Gdx.graphics.getHeight() - BOARD_SIZE)/2f;

        this.boardInput = new BoardInput(pieceService, gameService, boardService,
            audioService, startX, startY, TILE_SIZE);

        addMenu(this);
        loadSprites();
    }

    public BoardInput getBoardInput() {
        return boardInput;
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");
        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, this::slideOut, () -> playFX(0)));
        data.add(new ButtonData(UIButton.RESET, gameService::resetBoard, () -> playFX(0)));
        data.add(new ButtonData(UIButton.UNDO, boardService::undoMove, () -> playFX(0)));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float buttonX = 50f;
        float buttonY = 50f;

        boardGroup = new Group();
        uiGroup = new Group();

        for(ButtonData data : data) {
            Texture icon = getButton(data, false);
            Texture highlighted = getButton(data, true);

            Button b = new Button(buttonX, buttonY, baseButton.getWidth(),
                baseButton.getHeight(), baseButton, icon, frame,
                highlighted, data.soundPath(), data.action()
            );

            addButton(b);
            buttonX += baseButton.getWidth() + spacing;
        }

        for(Button b : getButtons()) {
            uiGroup.addActor(b);
        }

        for(Toast t : getToasts()) {
            getToastGroup().addActor(t);
        }
    }

    @Override
    public void show() {
        super.show();
        pieceService.clearBoard();

        boardGroup.setPosition(startX, startY + Gdx.graphics.getHeight());
        uiGroup.setPosition(25f, startY + Gdx.graphics.getHeight());
        getToastGroup().setPosition(0, 0);

        getStage().addActor(boardGroup);
        getStage().addActor(uiGroup);
        getStage().addActor(getToastGroup());

        if(gameService.isFirstBoardEntry()) {
            boardGroup.addAction(Actions.moveTo(startX, startY, DURATION, Interpolation.pow5Out));
            uiGroup.addAction(Actions.moveTo(25f, 25f, DURATION, Interpolation.pow5Out));
        } else {
            boardGroup.setPosition(startX, startY);
            uiGroup.setPosition(25f, 25f);
        }

        BackgroundTile bg = new BackgroundTile(Colors.getEdge(), -PADDING, -PADDING,
            BOARD_SIZE + PADDING * 2, BOARD_SIZE + PADDING * 2, getShaper());
        boardGroup.addActor(bg);

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                Color color = (row + col) % 2 == 0 ? Colors.getBackground() : Colors.getForeground();
                Tile tile = new Tile(getShaper(), color, col * TILE_SIZE, row * TILE_SIZE,
                    TILE_SIZE);
                boardGroup.addActor(tile);
            }
        }

        pieceService.setPieces();

        for(Piece p : pieceService.getPieces()) {
            p.setSprite(pieceService.getSprite(p));
            p.setPosition(p.getCol() * TILE_SIZE, p.getRow() * TILE_SIZE);
            p.setSize(TILE_SIZE, TILE_SIZE);
            boardGroup.addActor(p);
        }

        InputContext menu = new InputContext("BoardScreen");
        menu.bindKey(Input.Keys.TAB, () -> setCursorActive(!isCursorActive()));
        menu.bindKey(Input.Keys.UP, () -> {
            if(isCursorActive()) { cursor(Direction.UP, true); }
            else { cursor(Direction.UP); }
        });

        menu.bindKey(Input.Keys.DOWN, () -> {
            if(isCursorActive()) { cursor(Direction.DOWN, true); }
            else { cursor(Direction.DOWN); }
        });

        menu.bindKey(Input.Keys.LEFT, () -> {
            if(isCursorActive()) { cursor(Direction.LEFT, true); }
            else { cursor(Direction.LEFT); }
        });

        menu.bindKey(Input.Keys.RIGHT, () -> {
            if(isCursorActive()) { cursor(Direction.RIGHT, true); }
            else { cursor(Direction.RIGHT); }
        });

        menu.bindKey(Input.Keys.ENTER, () -> {
            if(isCursorActive()) { activate(); }
            else { super.activate(); }
        });

        InputManager manager = InputManager.get();
        manager.addContext(menu);
        manager.setActiveContext(menu);

        coordinator = new Coordinator();
        multiplexer = new InputMultiplexer(getStage(), coordinator);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);

        List<Moves> dark = boardService.getMoveAIPieces();
        if (!dark.isEmpty() && dark.size() - 1 > lastAnimatedDark) {
            Moves last = dark.getLast();
            if (last instanceof MovePiece move && move.color() == Turn.DARK) {
                animateMove(move);
                lastAnimatedDark = dark.size() - 1;
            }
        }

        List<Moves> light = boardService.getMovePieces();
        if (!light.isEmpty() && light.size() - 1 > lastAnimatedLight) {
            Moves last = light.getLast();
            if (last instanceof MovePiece move && move.color() == Turn.LIGHT) {
                animateMove(move);
                lastAnimatedLight = light.size() - 1;
            }
        }

        drawCursor();
        drawTooltip(delta);

        boardInput.update(boardGroup);
    }

    public void animateMove(MovePiece move) {
        Piece piece = move.piece();
        float toX = move.targetCol() * TILE_SIZE;
        float toY = move.targetRow() * TILE_SIZE;
        piece.clearActions();
        piece.addAction(Actions.sequence(
            Actions.moveTo(toX, toY, 0.2f),
            Actions.run(move::apply)
        ));
    }

    @Override
    public void hide() {
        super.hide();
        gameService.resetFirstBoardEntry();
    }

    private void drawCursor() {
        if(Coordinator.getLastInput() == LastInput.KEYBOARD && isCursorActive()) {
            getCursor().setPosition(boardGroup.getX() + getMoveX() * TILE_SIZE + TILE_SIZE/2f,
                boardGroup.getY() + getMoveY() * TILE_SIZE + 16f);
        }
    }

    private void drawTooltip(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        Piece hoveredPiece = null;
        for(Piece p : pieceService.getPieces()) {
            float px = boardGroup.getX() + p.getX();
            float py = boardGroup.getY() + p.getY();
            if(mouseX >= px && mouseX <= px + TILE_SIZE && mouseY >= py && mouseY <= py + TILE_SIZE) {
                hoveredPiece = p;
                break;
            }
        }

        if(hoveredPiece != null) {
            getTooltip().setText("tooltip." + hoveredPiece.getTypeID().getLabelKey());
        }

        getTooltip().update(delta, hoveredPiece != null, mouseX, mouseY);
        getTooltip().render(getBatch());
    }

    @Override
    public void playFX(int i) {
        audioService.playFX(i);
    }

    @Override @SuppressWarnings("StatementWithEmptyBody")
    public void activate() {
        if(isCursorActive()) {
            int cursorCol = getMoveX();
            int cursorRow = getMoveY();

            Piece underCursor = null;
            for(Piece p : pieceService.getPieces()) {
                if(p.getCol() == cursorCol && p.getRow() == cursorRow) {
                    underCursor = p;
                    break;
                }
            }

            if(selectedPiece == null) {
                if(underCursor != null) {
                    selectedPiece = underCursor;
                }
            } else {
                boolean hasMoved = boardService.attemptMove(selectedPiece, cursorCol, cursorRow);
                if(hasMoved) {
                    playFX(4);
                    if(Turn.getTurn() == Turn.DARK) {
                        boardService.executeAIMove();
                    }
                } else {}
                selectedPiece = null;
            }
        } else {
            super.activate();
        }
    }

    public void slideOut() {
        boardGroup.addAction(Actions.sequence(
                Actions.moveTo(startX, -Gdx.graphics.getHeight(), DURATION, Interpolation.pow5Out),
                Actions.run(gameService::showMainMenu)
        ));
        uiGroup.addAction(Actions.moveTo(25f, -Gdx.graphics.getHeight(),
            DURATION, Interpolation.pow5Out));
    }

    @Override
    public void dispose() {}
}
