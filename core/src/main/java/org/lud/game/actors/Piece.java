package org.lud.game.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.lud.engine.enums.Turn;
import org.lud.game.entities.Board;
import org.lud.game.enums.TypeID;

public class Piece extends Actor {
    private TypeID typeID;
    private Piece other;
    private Turn color;
    private Texture sprite;
    private int preCol, preRow;
    private int col, row;

    private boolean hasMoved;
    private boolean isTwoStepsAhead;

    public Piece(TypeID typeID, Turn color, int col, int row) {
        this.typeID = typeID;
        this.color = color;
        this.col = col;
        this.row = row;
        this.preCol = col;
        this.preRow = row;
        setX(col * Board.getSQUARE());
        setY(row * Board.getSQUARE());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(sprite != null) {
            batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
        }
    }

    public TypeID getTypeID() { return typeID; }
    public void setTypeID(TypeID typeID) { this.typeID = typeID; }

    public Turn getTurn() { return color; }

    public Texture getSprite() { return sprite; }
    public void setSprite(Texture texture) { this.sprite = texture; }

    public int getCol() { return col; }
    public void setCol(int col) {
        this.col = col;
        setX(col * Board.getSQUARE());
    }

    public int getRow() { return row; }
    public void setRow(int row) {
        this.row = row;
        setY(row * Board.getSQUARE());
    }

    public int getPreCol() { return preCol; }
    public void setPreCol(int preCol) { this.preCol = preCol; }

    public int getPreRow() { return preRow; }
    public void setPreRow(int preRow) { this.preRow = preRow; }

    public boolean hasMoved() { return hasMoved; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }

    public Piece getOther() { return other; }
    public void setOther(Piece other) { this.other = other; }

    public boolean isTwoStepsAhead() { return isTwoStepsAhead; }
    public void setTwoStepsAhead(boolean twoStepsAhead) { isTwoStepsAhead = twoStepsAhead; }

    public int getPieceValue(Piece p) {
        return switch(p.getTypeID()) {
            case PAWN -> 10;
            case KNIGHT, BISHOP -> 30;
            case ROOK -> 50;
            case QUEEN -> 90;
            case KING -> 900;
        };
    }

    public Piece copy(Piece p) {
        Piece piece = new Piece(p.typeID, p.color, p.col, p.row);
        piece.setHasMoved(p.hasMoved());
        return piece;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) { return true; }
        if(!(o instanceof Piece p)) { return false; }
        return col == p.col && row == p.row && typeID == p.typeID && color == p.color;
    }
}
