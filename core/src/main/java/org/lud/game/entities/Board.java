package org.lud.game.entities;

public class Board {
    private static final int SIZE = 8;
    private static final int PIXEL_SIZE = 512;
    private static final int SQUARE = SIZE * 8;

    public Board() {}

    public static int getSQUARE() {
        return SQUARE;
    }
    public static int getSIZE() {
        return SIZE;
    }
    public static int getPIXEL_SIZE() { return PIXEL_SIZE; }
    public static float getTileSize() { return PIXEL_SIZE/(float)SIZE; }

    public String getSquareNameAt(int col, int row) {
        int rankIndex = SIZE - 1 - row;
        char fileChar = (char) ('a' + col);
        return "" + fileChar + (rankIndex + 1);
    }
}
