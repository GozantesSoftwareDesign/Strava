package org.gozantes.strava.internals.types;

import java.io.Serial;
import java.io.Serializable;

public record Triplet <X, Y, Z>(X x, Y y, Z z) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Triplet (Triplet <X, Y, Z> triplet) {
        this (triplet.x, triplet.y, triplet.z);
    }

    public boolean equals (Object o) {
        return o instanceof Triplet && this.x.equals (((Triplet <?, ?, ?>) o).x) && this.y.equals (
                ((Triplet <?, ?, ?>) o).y) && this.z.equals (((Triplet <?, ?, ?>) o).z);
    }

    public X x () {
        return this.x;
    }

    public Y y () {
        return this.y;
    }

    @Override
    public String toString () {
        return String.format ("(%s: %s, %s: %s, %s: %s)", x.getClass ().getName (), x, y.getClass ().getName (), y,
                z.getClass ().getName (), z);
    }
}
