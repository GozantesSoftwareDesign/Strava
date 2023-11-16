package org.gozantes.strava.server.data.domain.challenge;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

public record ChallengeFilters(UserCredentials user, String title, Pair <Date, Date> lapse, Sport sport,
        Pair <BigDecimal, BigDecimal> distance, Pair <Duration, Duration> duration) {

    public ChallengeFilters (UserCredentials user, String title, Pair <Date, Date> lapse, Sport sport,
            Pair <BigDecimal, BigDecimal> distance, Pair <Duration, Duration> duration) {
        if (distance != null && duration != null)
            throw new RuntimeException ();

        this.user = user;
        this.title = title;
        this.lapse = lapse;
        this.sport = sport;
        this.distance = distance == null ? new Pair <BigDecimal, BigDecimal> (null, null) : distance;
        this.duration = duration == null ? new Pair <Duration, Duration> (null, null) : duration;
    }

    public ChallengeFilters (UserCredentials user, String title, Pair <Date, Date> lapse, Sport sport) {
        this (user, title, lapse, sport, null, null);
    }

    public ChallengeFilters (UserCredentials user, String title, Pair <Date, Date> lapse, Sport sport,
            Pair <?, ?> goal) {
        this (user, title, lapse, sport,
                goal == null || !(goal.x () instanceof BigDecimal) ? null : (Pair <BigDecimal, BigDecimal>) goal,
                goal == null || !(goal.x () instanceof Duration) ? null : (Pair <Duration, Duration>) goal);
    }

    public ChallengeFilters (UserCredentials user) {
        this (user, null, null, null, null);
    }

    public ChallengeFilters (UserCredentials user, String title) {
        this (user, title, null, null, null);
    }

    public ChallengeFilters (UserCredentials user, String title, Pair <Date, Date> lapse) {
        this (user, title, lapse, null, null);
    }
}
