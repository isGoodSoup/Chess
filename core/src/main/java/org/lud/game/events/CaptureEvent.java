package org.lud.game.events;

import org.lud.game.actors.Piece;

public record CaptureEvent(Piece piece, Piece captured) implements GameEvent {}
