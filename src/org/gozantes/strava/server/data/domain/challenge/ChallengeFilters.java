package org.gozantes.strava.server.data.domain.challenge;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public record ChallengeFilters(UserCredentials user, UserCredentials[] participants, String title,
        Pair <Date, Date> lapse, Sport sport, Pair <BigDecimal, BigDecimal> distance,
        Pair <Duration, Duration> duration) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public ChallengeFilters (UserCredentials user, UserCredentials[] participants, String title,
            Pair <Date, Date> lapse, Sport sport, Pair <BigDecimal, BigDecimal> distance,
            Pair <Duration, Duration> duration) {
        if (distance != null && duration != null)
            throw new RuntimeException ("Duration and distance are mutually exclusive.");

        this.user = user;
        this.participants = Arrays.asList (participants == null ? new UserCredentials[0] : participants).stream ()
                .filter (Objects::nonNull).toArray (UserCredentials[]::new);
        this.title = title;
        this.lapse = lapse;
        this.sport = sport;
        this.distance = distance == null ? new Pair <> (null, null) : distance;
        this.duration = duration == null ? new Pair <> (null, null) : duration;

        System.out.println (Arrays.toString (this.participants ()));
    }

    public ChallengeFilters (UserCredentials user) {
        this (user, (UserCredentials[]) null, null, null, null, null);
    }

    public ChallengeFilters (UserCredentials user, UserCredentials participant) {
        this (user, new UserCredentials[] { participant });
    }

    public ChallengeFilters (UserCredentials user, UserCredentials[] participants) {
        this (user, participants, null, null, null);
    }

    public ChallengeFilters (UserCredentials user, UserCredentials participant, String title, Pair <Date, Date> lapse,
            Sport sport) {
        this (user, new UserCredentials[] { participant }, title, lapse, sport);
    }

    public ChallengeFilters (UserCredentials user, UserCredentials[] participants, String title,
            Pair <Date, Date> lapse, Sport sport) {
        this (user, participants, title, lapse, sport, null, null);
    }

    public ChallengeFilters (UserCredentials user, UserCredentials participant, String title, Pair <Date, Date> lapse,
            Sport sport, Pair <?, ?> goal) {
        this (user, new UserCredentials[] { participant }, title, lapse, sport, goal);
    }

    public ChallengeFilters (UserCredentials user, UserCredentials[] participants, String title,
            Pair <Date, Date> lapse, Sport sport, Pair <?, ?> goal) {
        this (user, participants, title, lapse, sport,
                goal == null || !(goal.x () instanceof BigDecimal) ? null : (Pair <BigDecimal, BigDecimal>) goal,
                goal == null || !(goal.x () instanceof Duration) ? null : (Pair <Duration, Duration>) goal);
    }
}
