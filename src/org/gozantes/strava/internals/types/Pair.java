package org.gozantes.strava.internals.types;

import java.io.Serial;
import java.io.Serializable;

public record Pair <X, Y>(X x, Y y) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Pair (Pair <X, Y> pair) {
        this (pair.x, pair.y);
    }

    public boolean equals (Object o) {
        return o instanceof Pair && this.x.equals (((Pair <?, ?>) o).x) && this.y.equals (((Pair <?, ?>) o).y);
    }

    public X x () {
        return this.x;
    }

    public Y y () {
        return this.y;
    }

    @Override
    public String toString () {
        return String.format ("(%s: %s, %s: %s)", x.getClass ().getName (), x, y.getClass ().getName (), y);
    }
}
