package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.lud.engine.data.Achievement;
import org.lud.engine.data.ButtonData;
import org.lud.engine.enums.Direction;
import org.lud.engine.enums.UIButton;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Localization;
import org.lud.engine.gui.Menu;
import org.lud.engine.input.InputContext;
import org.lud.engine.input.InputManager;
import org.lud.game.enums.Achievements;
import org.lud.game.service.*;

import java.util.ArrayList;
import java.util.List;

public class AchievementsMenu extends Menu {
    private static final float DURATION = 1f;
    private final GameService gameService;
    private final AudioService audioService;
    private final BoardService boardService;
    private final PieceService pieceService;
    private final AchievementService achievementService;
    private final List<ButtonData> data;
    private final List<Achievement<Achievements>> achievements;
    private Group group;
    private Texture baseButton;
    private Texture frame;

    public AchievementsMenu(GameService gameService, AudioService audioService,
                            BoardService boardService, PieceService pieceService,
                            AchievementService achievementService) {
        super();
        this.gameService = gameService;
        this.audioService = audioService;
        this.boardService = boardService;
        this.pieceService = pieceService;
        this.achievementService = achievementService;
        this.data = new ArrayList<>();
        this.achievements = achievementService.listOfAchievements();
        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");
        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, this::slideOut,
            () -> playFX(0)));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float startX = 25f;
        float y = 25f;

        group = new Group();

        for(ButtonData data : data) {
            Texture icon = getButton(data, false);
            Texture highlighted = getButton(data, true);

            Button b = new Button(startX, y, baseButton.getWidth(), baseButton.getHeight(),
                baseButton, icon, frame, highlighted, data.soundPath(), data.action());

            group.addActor(b);
            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }
    }

    @Override
    public void show() {
        super.show();
        InputContext menu = new InputContext("AchievementsMenu");
        menu.bindKey(Input.Keys.UP, () -> cursor(Direction.UP));
        menu.bindKey(Input.Keys.DOWN, () -> cursor(Direction.DOWN));
        menu.bindKey(Input.Keys.ENTER, this::activate);

        InputManager.get().addContext(menu);
        InputManager.get().setActiveContext(menu);

        getStage().addActor(group);
        group.addAction(Actions.moveTo(25f, 25f, DURATION, Interpolation.pow5Out));

        String headerText = Localization.lang.t("header.achievements").toUpperCase();
        GlyphLayout layout = new GlyphLayout(getLargeFont(), headerText);

        Label.LabelStyle style = new Label.LabelStyle(getLargeFont(), Colors.getForeground());
        Label headerLabel = new Label(headerText, style);

        headerLabel.setPosition((Gdx.graphics.getWidth() - layout.width)/2f,
            Gdx.graphics.getHeight() - 200f);
        getStage().addActor(headerLabel);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void playFX(int i) {
        audioService.playFX(i);
    }

    public void slideOut() {
        group.addAction(Actions.sequence(
            Actions.moveTo(0, -Gdx.graphics.getHeight(), DURATION, Interpolation.pow5Out),
            Actions.run(gameService::showMainMenu)
        ));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
