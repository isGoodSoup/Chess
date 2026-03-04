package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.lud.engine.enums.Lang;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Localization;
import org.lud.engine.gui.Menu;
import org.lud.game.actors.Logo;
import org.lud.engine.data.ButtonData;
import org.lud.engine.enums.UIButton;
import org.lud.game.service.AudioService;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;
import org.lud.game.service.PieceService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("ALL")
public class MainMenu extends Menu {
    private static final float DURATION = 1f;
    private Map<Button, Supplier<String>> tooltips;
    private final GameService gameService;
    private final AudioService audioService;
    private final BoardService boardService;
    private final PieceService pieceService;
    private List<ButtonData> data;
    private Texture logo;

    private Group menuGroup;
    private Texture baseButton;
    private Texture altButton;
    private Texture frame;

    private float fadeAlpha = 1f;
    private final float FADE_SPEED = 0.5f;
    private boolean isPlayButton;

    public MainMenu(GameService gameService, AudioService audioService,
                    BoardService boardService, PieceService pieceService) {
        super();
        this.audioService = audioService;
        this.boardService = boardService;
        this.pieceService = pieceService;
        this.tooltips = new LinkedHashMap<>();
        this.gameService = gameService;
        this.data = new ArrayList<>();
        loadSprites();
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.logo = new Texture("logo_chess.png");
        this.altButton = new Texture(defaultPath + "button.png");
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");

        data.add(new ButtonData(UIButton.PLAY, () -> slideOut(0), () -> playFX(0)));
        data.add(new ButtonData(UIButton.SETTINGS, () -> slideOut(1), () -> playFX(0)));
        data.add(new ButtonData(UIButton.ACHIEVEMENTS, () -> slideOut(2), () -> playFX(0)));
        data.add(new ButtonData(UIButton.LANG, Lang::nextLang, () -> playFX(0)));
        data.add(new ButtonData(UIButton.EXIT, () -> fadeOut(3), () -> playFX(0)));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float totalWidth = 0;

        for(ButtonData data : data) {
            totalWidth += baseButton.getWidth();
        }
        totalWidth += spacing * (data.size() - 1);

        float startX = (Gdx.graphics.getWidth() - totalWidth)/2f;
        float y = 200f;

        for(ButtonData data : data) {
            Texture icon = getButton(data, false);
            Texture highlighted = getButton(data, true);

            boolean isPlayButton = data.type().getSuffix().equals("play");

            Button b = new Button(
                startX, y - baseButton.getHeight()/2f,
                baseButton.getWidth(), baseButton.getHeight(),
                isPlayButton ? altButton : baseButton,
                icon, frame, highlighted,
                data.soundPath(), data.action()
            );

            tooltips.put(b, () -> Localization.lang.t("tooltip." + data.type().getSuffix()));
            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }

        menuGroup = new Group();
        menuGroup.addActor(new Logo(logo, Gdx.graphics.getWidth()/2f - logo.getWidth()/2,
            Gdx.graphics.getHeight()/2f));

        for(Button b : getButtons()) {
            menuGroup.addActor(b);
        }
    }

    @Override
    public void show() {
        super.show();
        menuGroup.setPosition(0, Gdx.graphics.getHeight());
        getStage().addActor(menuGroup);
        menuGroup.addAction(Actions.moveTo(0, 0, DURATION, Interpolation.pow5Out));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        globalInput();
        checkInput();

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean hovering = false;

        for(Button b : getButtons()) {
            if(b.isHovered()) {
                Supplier<String> supplier = tooltips.get(b);
                getTooltip().setText(supplier.get());
                hovering = true;
                break;
            }
        }

        getTooltip().update(delta, hovering, mouseX, mouseY);
        getTooltip().render(getBatch());
    }

    @Override
    public void checkInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            activate();
        }
    }

    @Override
    public void loadKeys() {}

    @Override
    public void playFX(int i) {
        audioService.playFX(i);
    }

    public void slideOut(int i) {
        menuGroup.addAction(Actions.sequence(
            Actions.moveTo(0, -Gdx.graphics.getHeight(), DURATION, Interpolation.pow5Out),
            Actions.run(() -> gameService.getActiveMenu(i))
        ));
    }

    public void fadeOut(int i) {
        menuGroup.addAction(Actions.sequence(
            Actions.fadeOut(DURATION, Interpolation.pow5Out),
            Actions.run(() -> gameService.getActiveMenu(i))
        ));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
