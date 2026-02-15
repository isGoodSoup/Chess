package org.vertex.engine.enums;

import org.vertex.engine.service.GameService;

public enum Games {
    CHESS("CHESS", "") {
        @Override
        public void setup(GameService gameService) {
            GameService.setState(GameState.BOARD);
            gameService.startNewGame();
        }

        @Override
        public int getBoardSize() {
            return 8;
        }
    },
    CHECKERS("CHECKERS", "checkers_") {
        @Override
        public void setup(GameService gameService) {
            GameService.setState(GameState.BOARD);
            gameService.startNewGame(); // TODO
        }

        @Override
        public int getBoardSize() {
            return 8;
        }
    };

    private final String label;
    private final String spritePrefix;

    Games(String label, String spritePrefix) {
        this.label = label;
        this.spritePrefix = spritePrefix;
    }

    public String getLabel() {
        return label;
    }

    public String getSpritePrefix() {
        return spritePrefix;
    }

    public boolean isEnabled() {
        return true;
    }

    public abstract int getBoardSize();

    public abstract void setup(GameService gameService);
}
