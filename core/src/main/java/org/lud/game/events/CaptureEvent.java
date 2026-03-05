package org.lud.game.events;

import org.lud.engine.interfaces.GameEvent;
import org.lud.game.actors.Piece;

public record CaptureEvent(Piece piece, Piece captured) implements GameEvent {}
