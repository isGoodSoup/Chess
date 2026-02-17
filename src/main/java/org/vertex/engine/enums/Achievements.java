package org.vertex.engine.enums;

public enum Achievements {
    FIRST_CAPTURE(1001L, "a01_first_capture", "First Capture", "Capture your first piece"),
    SECRET_TOGGLE(1002L, "a02_toggles", "One Rule To Rule 'Em All", "Find the (secret) toggle"),
    CHECKMATE(1003L, "a03_checkmate", "Checkmate!", "Win a game by checkmate"),
    CASTLING_MASTER(1004L, "a04_castling_master", "Oh My King", "Castle at least 10 times"),
    KING_PROMOTER(1005L, "a05_king_promoter", "King Promoter", "Promote the same pawn 4 times"),
    QUICK_WIN(1006L, "a06_quick_win", "Quick Win!", "Win a game in less than 5 moves"),
    CHECK_OVER(1007L, "a07_check_over", "It's Check And Over", "Check 4 times in the same game"),
    HEAVY_CROWN(1008L, "a08_heavy_crown", "Heavy Is The Crown", "Win 128 games"),
    ALL_PIECES(1009L, "a09_good_riddance", "And Good Riddance", "Clear all pieces from the board"),
    HARD_GAME(1010L, "a10_that_was_easy", "That Was Easy!", "Win a hard game"),
    UNTOUCHABLE(1011L, "a11_cant_touch_this", "Can't Touch This", "Win a game without getting checked"),
    GRANDMASTER(1012L, "axx_grandmaster", "Grandmaster", "Complete all achievements");

    private final long id;
    private final String file;
    private final String title;
    private final String description;

    Achievements(long id, String file, String title, String description) {
        this.id = id;
        this.file = file;
        this.title = title;
        this.description = description;
    }

    public long getId() { return id; }
    public String getFile() { return file; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}