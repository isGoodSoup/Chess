package org.chess.input;

import org.chess.entities.Board;
import org.chess.render.MenuRender;
import org.chess.render.RenderContext;
import org.chess.service.BoardService;
import org.chess.service.BooleanService;
import org.chess.service.GUIService;
import org.chess.service.GameService;

import java.awt.*;

public class MenuInput {
    private final RenderContext render;
    private final GameService gameService;
    private final BoardService boardService;
    private final MoveManager moveManager;
    private final GUIService guiService;
    private final Mouse mouse;
    private final MenuRender menuRender;

    public MenuInput(RenderContext render, MenuRender menuRender,
                     GUIService guiService,
                     GameService gameService, BoardService boardService,
                     MoveManager moveManager, Mouse mouse) {
        this.render = render;
        this.gameService = gameService;
        this.boardService = boardService;
        this.moveManager = moveManager;
        this.guiService = guiService;
        this.mouse = mouse;
        this.menuRender = menuRender;
    }

    public boolean updatePage() {
        int itemsPerPage = 8;
        int totalPages = (MenuRender.optionsTweaks.length - 1 + itemsPerPage - 1) / itemsPerPage;
        int newPage = moveManager.getSelectedIndexX() / itemsPerPage + 1;
        newPage = Math.max(1, Math.min(newPage, totalPages));

        if(newPage != menuRender.getCurrentPage()) {
            menuRender.setCurrentPage(newPage);
            moveManager.setSelectedIndexY(0);
            return true;
        }
        return false;
    }

    public void previousPage() {
        int itemsPerPage = 8;
        int currentIndex = moveManager.getSelectedIndexX();

        if(currentIndex >= itemsPerPage) {
            moveManager.setSelectedIndexX(currentIndex - itemsPerPage);
        }
        updatePage();
    }

    public void nextPage() {
        int itemsPerPage = 8;
        int currentIndex = moveManager.getSelectedIndexX();

        if(currentIndex + itemsPerPage < MenuRender.optionsTweaks.length) {
            moveManager.setSelectedIndexX(currentIndex + itemsPerPage);
        }
        updatePage();
    }


    public void handleOptionsInput() {
        if(!mouse.wasPressed()) { return; }

        int itemsPerPage = 8;
        int startIndex = (menuRender.getCurrentPage() - 1) * itemsPerPage + 1;
        int endIndex = Math.min(startIndex + itemsPerPage, MenuRender.optionsTweaks.length);

        FontMetrics fm = menuRender.getFontMetrics();
        int lineHeight = fm.getHeight() + render.scale(10);

        int headerY = render.getOffsetY() + render.scale(MenuRender.getOPTION_Y());
        int startY = headerY + render.scale(90);

        int gap = render.scale(100);
        int maxRowWidth = 0;

        for(int i = startIndex; i < endIndex; i++) {
            String enabledOption = MenuRender.ENABLE + MenuRender.optionsTweaks[i];
            int textWidth = fm.stringWidth(enabledOption.toUpperCase());
            int toggleWidth = render.scale(menuRender.getSprite(0).getWidth() / 2);
            int rowWidth = textWidth + gap + toggleWidth;
            if(rowWidth > maxRowWidth) maxRowWidth = rowWidth;
        }

        for(int i = startIndex; i < endIndex; i++) {
            String option = MenuRender.optionsTweaks[i];
            String enabledOption = MenuRender.ENABLE + option;

            int textWidth = fm.stringWidth(enabledOption);
            int toggleWidth = render.scale(menuRender.getSprite(0).getWidth()/2);
            int toggleHeight = render.scale(menuRender.getSprite(0).getHeight()/2);

            int blockX = MenuRender.getCenterX(menuRender.getTotalWidth(), maxRowWidth);
            int toggleX = blockX + maxRowWidth - toggleWidth;
            int toggleY = startY - toggleHeight;

            Rectangle toggleHitbox = new Rectangle(
                    toggleX,
                    toggleY,
                    toggleWidth,
                    toggleHeight
            );

            if(toggleHitbox.contains(mouse.getX(), mouse.getY())) {
                guiService.getFx().play(0);
                menuRender.toggleOption(option);
                break;
            }
            startY += lineHeight;
        }
    }

    public void handleMenuInput(String[] options) {
        if(!mouse.wasPressed()) { return; }

        int startY = render.scale(RenderContext.BASE_HEIGHT)/2 + render.scale(GUIService.getMENU_START_Y());
        int spacing = render.scale(GUIService.getMENU_SPACING());

        for(int i = 0; i < options.length; i++) {
            String optionText = options[i];
            FontMetrics fm = menuRender.getFontMetrics();
            int textWidth = fm.stringWidth(optionText);

            int x = MenuRender.getCenterX(menuRender.getTotalWidth(), textWidth);
            int y = render.getOffsetY() + startY + i * spacing;

            Rectangle hitbox = new Rectangle(
                    x,
                    y - fm.getAscent(),
                    textWidth,
                    fm.getHeight()
            );

            if(hitbox.contains(mouse.getX(), mouse.getY())) {
                guiService.getFx().play(0);
                switch(GameService.getState()) {
                    case MENU -> {
                        switch(i) {
                            case 0 -> gameService.startNewGame();
                            case 1 -> gameService.achievementsMenu();
                            case 2 -> gameService.optionsMenu();
                            case 3 -> System.exit(0);
                        }
                    }
                    case MODE -> {
                        switch(i) {
                            case 0 -> boardService.startBoard();
                            case 1 -> {
                                BooleanService.isAIPlaying = true;
                                boardService.startBoard();
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
