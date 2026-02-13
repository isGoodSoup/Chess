package org.chess.enums;

public enum Achievements {
    FIRST_MOVE("a01_first_move", "First Move", "Make your first move"),
    RULES_WHAT("a02_rules", "Rules? What's That?", "Discover the rule toggles"),
    CHECKMATE("a03_checkmate", "Checkmate!", "Win a game by checkmate"),
    CASTLING_MASTER("a04_castling_master", "Oh My King", "Castle at least 10 times"),
    PAWN_PROMOTER("a05_pawn_promoter", "Pawn Promoter", "Promote a pawn"),
    QUICK_WIN("a06_quick_win", "Quick Win!", "Win a game in less than 5 moves"),
    CHECK_OVER("a07_check_over", "Check And Over", "Check 30 times in the same game"),
    HEAVY_CROWN("a08_heavy_crown", "Heavy Is The Crown", "Win a 100 games"),
    GOOD_RIDDANCE("a09_good_riddance", "And Good Riddance", "Clear all pieces from the board"),
    THAT_WAS_EASY("a10_that_was_easy", "That Was Easy!", "Win a hard game"),
    UNTOUCHABLE("a11_untouchable", "The Untouchable", "Win a game without getting checked"),
    GRANDMASTER("a12_grandmaster", "Chess Grandmaster", "Complete all the achievements");

    private final String file;
    private final String title;
    private final String description;

    Achievements(String file, String title, String description) {
        this.file = file;
        this.title = title;
        this.description = description;
    }

    public String getFile() { return file; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}