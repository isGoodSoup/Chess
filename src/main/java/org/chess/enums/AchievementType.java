package org.chess.enums;

public enum AchievementType {
    FIRST_MOVE("First Move", "Make your first move"),
    RULES_WHAT("Rules? What's That?", "Discover the rule toggles"),
    CHECKMATE("Checkmate!", "Win a game by checkmate"),
    CASTLING_MASTER("Oh My King", "Castle at least 10 times as a last resource"),
    PAWN_PROMOTER("Pawn Promoter", "Promote a pawn"),
    QUICK_WIN("Quick Win!", "Win a game in less than 10 moves"),
    CHECK_OVER("Check And Over", "Check the King at least 30 times in the same game"),
    HEAVY_CROWN("Heavy Is The Crown", "Win a 100 games"),
    GOOD_RIDDANCE("And Good Riddance", "Clear all pieces from the board until only the King is left"),
    THAT_WAS_EASY("That Was Easy!", "Win a hard game and survive to tell the tale"),
    UNTOUCHABLE("The Untouchable", "Win a game without ever getting checked"),
    GRANDMASTER("Chess Grandmaster", "Complete all the achievements");

    private final String title;
    private final String description;

    AchievementType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
}

