package org.vertex.engine.enums;

import org.vertex.engine.service.GameService;

public enum Games {
    CHESS("CHESS", "") {
        @Override
        public void setup(GameService gameService) {
            GameService.setGame(this);
            GameService.setState(GameState.BOARD);
            gameService.startNewGame();
        }

        @Override
        public int getBoardSize() {
            return 8;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    },
    CHECKERS("CHECKERS", "checker_") {
        @Override
        public void setup(GameService gameService) {
            GameService.setGame(this);
            GameService.setState(GameState.BOARD);
            gameService.startNewGame(); // TODO
        }

        @Override
        public int getBoardSize() {
            return 8;
        }

        @Override
        public boolean isEnabled() {
            return true;
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

    public abstract boolean isEnabled();

    public abstract int getBoardSize();

    public abstract void setup(GameService gameService);
}
