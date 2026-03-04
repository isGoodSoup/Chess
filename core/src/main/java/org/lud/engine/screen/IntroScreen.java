package org.lud.engine.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lud.engine.core.Intro;
import org.lud.engine.interfaces.ScreenTransition;

public class IntroScreen implements Screen {

    private final ScreenTransition transition;
    private final Intro intro;
    private SpriteBatch batch;

    public IntroScreen(ScreenTransition transition) {
        this.transition = transition;
        this.intro = new Intro();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
    }

    @Override @SuppressWarnings("StatementWithEmptyBody")
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        intro.draw(batch, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if(intro.isFinished() && transition != null) {
            Screen next = transition.nextScreen();
            if(next != null) {}
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        intro.dispose();
    }
}
