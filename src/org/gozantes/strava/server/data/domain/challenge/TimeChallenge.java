package org.gozantes.strava.server.data.domain.challenge;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

public final class TimeChallenge extends Challenge {
    private final Duration goal;

    public TimeChallenge (String name, Pair <Date, Date> lapse, Sport sport, UserCredentials parent,
            Duration goal) throws Exception {
        this (null, name, lapse, sport, parent, goal);
    }

    public TimeChallenge (Long id, String name, Pair <Date, Date> lapse, Sport sport, UserCredentials parent,
            Duration goal) throws Exception {
        super (id, name, lapse, sport, parent);

        Objects.requireNonNull (goal);

        if (goal.compareTo (Duration.ofMinutes (1)) < 0)
            throw new Exception ("Time goals must last longer than a minute.");

        this.goal = goal;
    }

    public Duration getGoal () {
        return this.goal;
    }
}
