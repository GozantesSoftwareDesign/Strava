package org.gozantes.strava.server.data.domain.challenge;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public final class DistanceChallenge extends Challenge implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final BigDecimal goal;

    public DistanceChallenge (String name, Pair <Date, Date> lapse, Sport sport, UserCredentials parent,
            BigDecimal goal) throws Exception {
        this (null, name, lapse, sport, parent, goal);
    }

    public DistanceChallenge (Long id, String name, Pair <Date, Date> lapse, Sport sport, UserCredentials parent,
            BigDecimal goal) throws Exception {
        super (id, name, lapse, sport, parent);

        Objects.requireNonNull (goal);

        if (goal.compareTo (BigDecimal.ZERO) <= 0)
            throw new Exception ("Distance goals should be positive numbers representing kilometers.");

        this.goal = goal;
    }

    public BigDecimal getGoal () {
        return this.goal;
    }

    @Override
    public String toString () {
        return "DistanceChallenge [goal=" + goal + ", name=" + name + ", lapse=" + lapse + ", sport=" + sport
                + ", parent=" + parent + ", participants=" + participants + ", id=" + id + "]";
    }
}
