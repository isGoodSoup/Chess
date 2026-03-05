package org.lud.game.events;

import org.lud.engine.interfaces.GameEvent;
import org.lud.game.actors.Piece;

public record CastlingEvent(Piece king, Piece rook) implements GameEvent {}
