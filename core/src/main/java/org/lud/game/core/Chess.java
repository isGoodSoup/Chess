package org.lud.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.core.GameFrame;
import org.lud.engine.service.AchievementPersistence;
import org.lud.game.screens.MainMenu;
import org.lud.game.service.*;
import org.lud.engine.service.EventBus;
import org.lud.engine.enums.Theme;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Localization;
import org.lud.engine.screen.IntroScreen;
import org.lud.engine.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class Chess extends GameFrame {
    private static final Logger log = LoggerFactory.getLogger(Chess.class);
    private ServiceFactory service;
    private OrthographicCamera camera;

    @Override
    public void create() {
        log.info("SoS (Start of Session)");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        camera.update();

        service = new ServiceFactory();
        EventBus eventBus = new EventBus();

        service.register(EventBus.class, eventBus);
        service.register(AudioService.class, new AudioService());
        service.register(BoardService.class, new BoardService(service, camera));
        service.register(PieceService.class, new PieceService(service));
        service.register(AchievementService.class,
            new AchievementService(eventBus, service, new AchievementPersistence()));
        service.register(GameService.class, new GameService(this, service));

        service.get(AudioService.class).playMusic();
        service.get(AudioService.class).setMusicVolume(-0.4f);

        Localization.lang.setLocale(Locale.forLanguageTag("en"));
        Colors.setTheme(Theme.LEGACY);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);

        setScreen(new IntroScreen(() -> new MainMenu(
            service.get(GameService.class), service.get(AudioService.class),
            service.get(BoardService.class), service.get(PieceService.class))));
    }

    @Override
    public void render() {
        camera.update();
        super.render();
    }
}
