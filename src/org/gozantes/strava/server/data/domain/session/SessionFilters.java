package org.gozantes.strava.server.data.domain.session;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

public record SessionFilters(UserCredentials user, String title, Sport sport, Pair <BigDecimal, BigDecimal> distance,
        Pair <Duration, Duration> duration) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    public SessionFilters (UserCredentials user, String title, Sport sport, Pair <BigDecimal, BigDecimal> distance,
            Pair <Duration, Duration> duration) {
        this.user = user;
        this.title = title;
        this.sport = sport;
        this.distance = distance == null ? new Pair <BigDecimal, BigDecimal> (null, null) : distance;
        this.duration = duration == null ? new Pair <Duration, Duration> (null, null) : duration;
    }

    public SessionFilters (UserCredentials user) {
        this (user, null, null, null, null);
    }

    public SessionFilters (UserCredentials user, String title) {
        this (user, title, null, null, null);
    }

    public SessionFilters (UserCredentials user, String title, Sport sport) {
        this (user, title, sport, null, null);
    }

    public SessionFilters (UserCredentials user, String title, Sport sport, Pair <BigDecimal, BigDecimal> distance) {
        this (user, title, sport, distance, null);
    }
}
