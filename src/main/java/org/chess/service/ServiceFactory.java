package org.chess.service;

import org.chess.input.Keyboard;
import org.chess.input.Mouse;
import org.chess.input.MoveManager;
import org.chess.render.RenderContext;

public class ServiceFactory {
    private final RenderContext render;
    private final PieceService piece;
    private final BoardService board;
    private final Mouse mouse;
    private final Keyboard keyboard;
    private final GUIService gui;
    private final GameService gs;
    private final PromotionService promotion;
    private final MoveManager manager;
    private final ModelService model;
    private final AnimationService animation;
    private final TimerService timer;

    public ServiceFactory(RenderContext render) {
        this.render = render;
        this.mouse = new Mouse(render);
        this.keyboard = new Keyboard();
        this.animation = new AnimationService();
        this.piece = new PieceService(mouse);
        this.promotion = new PromotionService(piece, mouse);
        this.model = new ModelService(piece, animation, promotion);
        this.manager = new MoveManager();
        this.piece.setMoveManager(manager);
        this.board = new BoardService(piece, mouse, promotion,
                model, manager);
        this.board.setServiceFactory(this);
        this.model.setBoardService(board);
        this.gs = new GameService(render, board, mouse);
        this.gs.setServiceFactory(this);
        this.timer = new TimerService();
        this.gui = new GUIService(render, piece, board, gs, promotion,
                model, manager, timer, mouse);
        this.manager.init(this);
    }

    public RenderContext getRender() {
        return render;
    }

    public PieceService getPieceService() {
        return piece;
    }

    public BoardService getBoardService() {
        return board;
    }

    public Mouse getMouseService() {
        return mouse;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public GUIService getGuiService() {
        return gui;
    }

    public PromotionService getPromotionService() {
        return promotion;
    }

    public MoveManager getManager() {
        return manager;
    }

    public ModelService getModelService() {
        return model;
    }

    public GameService getGameService() {
        return gs;
    }

    public AnimationService getAnimationService() {
        return animation;
    }

    public TimerService getTimerService() { return timer; }
}
