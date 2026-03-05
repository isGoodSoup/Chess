package org.lud.game.events;

import org.lud.game.actors.Piece;

public record CastlingEvent(Piece king, Piece rook) implements GameEvent {}
