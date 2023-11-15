package org.gozantes.strava.internals.types;

public record Pair <X, Y>(X x, Y y) {
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
