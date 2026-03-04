package org.lud.engine.data;

import org.lud.engine.interfaces.Achieveable;

public record Achievement<T extends Achieveable>(T id, boolean unlocked) {
    public Achievement(T id) {
        this(id, false);
    }

    public Achievement<T> unlock() {
        return new Achievement<>(this.id, true);
    }
}
