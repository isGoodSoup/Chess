package org.chess.service;

import org.chess.enums.GameState;
import org.chess.enums.PlayState;
import org.chess.enums.Tint;
import org.chess.input.Mouse;
import org.chess.render.MenuRender;
import org.chess.render.RenderContext;

public class GameService {
    private static GameState state;
    private static PlayState mode;
    private static Tint currentTurn;

    private static RenderContext render;
    private static BoardService boardService;
    private static Mouse mouse;

    private static ServiceFactory service;

    public GameService(RenderContext render, BoardService boardService,
                       Mouse mouse) {
        GameService.render = render;
        GameService.boardService = boardService;
        GameService.mouse = mouse;
    }

    public static ServiceFactory getServiceFactory() {
        return service;
    }

    public void setServiceFactory(ServiceFactory service) {
        GameService.service = service;
    }

    public static GameState getState() {
        return state;
    }

    public static PlayState getMode() {
        return mode;
    }

    public static void setState(GameState state) {
        GameService.state = state;
    }

    public static void setMode() {
        if(!mouse.wasPressed()) {
            return;
        }

        if(state != GameState.MODE) {
            return;
        }

        int startY =
                render.scale(RenderContext.BASE_HEIGHT)/2 + GUIService.getMENU_START_Y();
        int spacing = GUIService.getMENU_SPACING();

        for(int i = 0; i < MenuRender.optionsMode.length; i++) {
            int y = startY + i * spacing;
            boolean isHovered =
                    GUIService.getHITBOX(render.getOffsetX(), y, 200, 40).contains(mouse.getX(),
                    mouse.getY());
            if(isHovered) {
                switch(i) {
                    case 0 -> mode = PlayState.PLAYER;
                    case 1 -> mode = PlayState.AI;
                }
                boardService.startBoard();
                return;
            }
        }
    }

    public static Tint getCurrentTurn() {
        return currentTurn;
    }

    public static void setCurrentTurn(Tint tint) {
        currentTurn = tint;
    }

    public static boolean isBlackTurn() {
        return currentTurn == Tint.BLACK;
    }

    public void startNewGame() {
        setState(GameState.MODE);
    }

    public void optionsMenu() {
        setState(GameState.RULES);
    }
}
