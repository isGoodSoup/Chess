package org.lud.game.enums;

import org.lud.engine.gui.Localization;
import org.lud.engine.interfaces.Achieveable;

public enum Achievements implements Achieveable {
    FIRST_CAPTURE           (1001L, "a01_first_steps",            "achievement.first_steps.title",        "achievement.first_steps.desc"),
    SECRET_TOGGLE           (1002L, "a02_toggles",                "achievement.toggles.title",           "achievement.toggles.desc"),
    CHECKMATE               (1003L, "a03_checkmate",              "achievement.checkmate.title",         "achievement.checkmate.desc"),
    CASTLING_MASTER         (1004L, "a04_castling_master",        "achievement.castling_master.title",   "achievement.castling_master.desc"),
    KING_PROMOTER           (1005L, "a05_king_promoter",          "achievement.king_promoter.title",     "achievement.king_promoter.desc"),
    QUICK_WIN               (1006L, "a06_quick_win",              "achievement.quick_win.title",         "achievement.quick_win.desc"),
    CHECK_OVER              (1007L, "a07_check_over",             "achievement.check_over.title",        "achievement.check_over.desc"),
    HEAVY_CROWN             (1008L, "a08_heavy_crown",            "achievement.heavy_crown.title",       "achievement.heavy_crown.desc"),
    ALL_PIECES              (1009L, "a09_good_riddance",          "achievement.all_pieces.title",        "achievement.all_pieces.desc"),
    HARD_GAME               (1010L, "a10_that_was_easy",          "achievement.hard_game.title",         "achievement.hard_game.desc"),
    UNTOUCHABLE             (1011L, "a11_cant_touch_this",        "achievement.untouchable.title",       "achievement.untouchable.desc"),
    MASTER_OF_NONE          (1012L, "a12_master_of_none",         "achievement.master_of_none.title",    "achievement.master_of_none.desc");

    private final long id;
    private final String file;
    private final String titleKey;
    private final String descKey;

    Achievements(long id, String file, String titleKey, String descKey) {
        this.id = id;
        this.file = file;
        this.titleKey = titleKey;
        this.descKey = descKey;
    }

    public long getId() { return id; }
    public String getFile() { return file; }
    public String getTitle() { return Localization.lang.t(titleKey).toUpperCase(); }
    public String getDescription() { return Localization.lang.t(descKey); }
}
