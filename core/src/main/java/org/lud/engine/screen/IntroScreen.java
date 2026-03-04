package org.lud.engine.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lud.engine.core.GameFrame;
import org.lud.engine.core.Intro;
import org.lud.engine.core.AudioService;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;
import org.lud.game.service.PieceService;
import org.lud.game.screens.MainMenu;

public class IntroScreen implements Screen {
    private final GameFrame gameFrame;
    private final GameService gameService;
    private final AudioService audioService;
    private final BoardService boardService;
    private final PieceService pieceService;
    private final Intro intro;
    private SpriteBatch batch;

    public IntroScreen(GameFrame gameFrame, GameService gameService, AudioService audioService,
                       BoardService boardService, PieceService pieceService) {
        this.gameFrame = gameFrame;
        this.gameService = gameService;
        this.audioService = audioService;
        this.boardService = boardService;
        this.pieceService = pieceService;
        this.intro = new Intro();
    }

    @Override
    public void show() {
        this.batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        intro.draw(batch, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if(intro.isFinished()) {
            gameFrame.setScreen(new MainMenu(gameService,
                audioService, boardService, pieceService));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        intro.dispose();
    }
}
