package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.lud.engine.enums.Theme;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Colors;
import org.lud.engine.interfaces.Service;
import org.lud.engine.service.ServiceFactory;
import org.lud.game.entities.Board;
import org.lud.game.actors.Piece;
import org.lud.game.enums.TypeID;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class PieceService implements Service {
    private final Map<String, Texture> sprites;
    private final ServiceFactory service;
    private final List<Piece> pieces;

    public PieceService(ServiceFactory service) {
        this.sprites = new LinkedHashMap<>();
        this.pieces = new ArrayList<>();
        this.service = service;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPieces() {
        pieces.clear();
        for (int col = 0; col < 8; col++) {
            addPiece(new Piece(TypeID.PAWN, Turn.DARK, col, 6));
            addPiece(new Piece(TypeID.PAWN, Turn.LIGHT, col, 1));
        }

        addPiece(new Piece(TypeID.ROOK, Turn.DARK, 0, 7));
        addPiece(new Piece(TypeID.ROOK, Turn.DARK, 7, 7));
        addPiece(new Piece(TypeID.ROOK, Turn.LIGHT, 0, 0));
        addPiece(new Piece(TypeID.ROOK, Turn.LIGHT, 7, 0));

        addPiece(new Piece(TypeID.KNIGHT, Turn.DARK, 1, 7));
        addPiece(new Piece(TypeID.KNIGHT, Turn.DARK, 6, 7));
        addPiece(new Piece(TypeID.KNIGHT, Turn.LIGHT, 1, 0));
        addPiece(new Piece(TypeID.KNIGHT, Turn.LIGHT, 6, 0));

        addPiece(new Piece(TypeID.BISHOP, Turn.DARK, 2, 7));
        addPiece(new Piece(TypeID.BISHOP, Turn.DARK, 5, 7));
        addPiece(new Piece(TypeID.BISHOP, Turn.LIGHT, 2, 0));
        addPiece(new Piece(TypeID.BISHOP, Turn.LIGHT, 5, 0));

        addPiece(new Piece(TypeID.QUEEN, Turn.DARK, 3, 7));
        addPiece(new Piece(TypeID.QUEEN, Turn.LIGHT, 3, 0));

        addPiece(new Piece(TypeID.KING, Turn.DARK, 4, 7));
        addPiece(new Piece(TypeID.KING, Turn.LIGHT, 4, 0));
    }

    public Texture getSprite(Piece p) {
        String name = p.getTypeID().name().toLowerCase();
        Theme theme = Colors.getTheme();
        String key = name + "_" + theme.getColor(p.getTurn());
        if(sprites.containsKey(key)) { return sprites.get(key); }
        String path = "pieces/" + name + "/" + name + "_" + theme.getColor(p.getTurn()) + ".png";
        Texture t = new Texture(Gdx.files.internal(path));
        sprites.put(key, t);
        return t;
    }

    public void addPiece(Piece p) {
        pieces.add(p);
        getSprite(p);
    }

    public void replacePiece(Piece pold, Piece pnew) {
        int index = pieces.indexOf(pold);
        if (index != -1) {
            pieces.set(index, pnew);
        }

        removePiece(pold);
        addPiece(pnew);
        getSprite(pnew);
    }

    public void removePiece(Piece p) {
        pieces.remove(p);
        if(p.getParent() != null) { p.remove(); }
    }

    public Piece getKing(Turn turn) {
        Piece king = null;
        for(Piece piece : pieces) {
            if(piece.getTypeID() == TypeID.KING && piece.getTurn() == turn.getTurn()) {
                king = piece;
                return king;
            }
        }
        return king;
    }

    public static float[] toPixels(int col, int row) {
        float square = Board.getSQUARE();
        return new float[]{col * square, row * square};
    }

    public void clearBoard() {
        for(Piece p : new ArrayList<>(pieces)) {
            if(p.getParent() != null) {
                p.remove();
            }
        }
        pieces.clear();
    }
}
