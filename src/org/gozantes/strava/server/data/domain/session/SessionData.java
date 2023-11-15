package org.gozantes.strava.server.data.domain.session;

import org.gozantes.strava.server.data.domain.Sport;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public record SessionData(String title, Sport sport, BigDecimal distance, Date start, Duration duration) {
    public SessionData (String title, Sport sport, BigDecimal distance, Date start, Duration duration) {
        Objects.requireNonNull (title);
        Objects.requireNonNull (sport);
        Objects.requireNonNull (distance);
        Objects.requireNonNull (start);
        Objects.requireNonNull (duration);

        if (title.length () < 4)
            throw new RuntimeException ("Session titles must be at least 4 characters long.");

        if (distance.compareTo (BigDecimal.ZERO) <= 0)
            throw new RuntimeException ("Distances must positive BigDecimals representing kilometers.");

        if (start.getTime () >= System.currentTimeMillis () + TimeUnit.MINUTES.toMillis (1))
            throw new RuntimeException ("Sessions cannot have their start date one minute or more in the future");

        if (duration.isNegative () || duration.isZero ())
            throw new RuntimeException ("Sessions cannot last zero minutes or less.");

        if (duration.toMinutes () > TimeUnit.HOURS.toMinutes (5))
            throw new RuntimeException ("Sessions cannot be more than five hours long.");

        this.title = title;
        this.sport = sport;
        this.distance = distance;
        this.start = start;
        this.duration = duration;
    }

    public SessionData (SessionData data) {
        this (data == null ? null : data.title, data == null ? null : data.sport, data == null ? null : data.distance,
                data == null ? null : data.start, data == null ? null : data.duration);
    }

    public SessionData (SessionData data, String title) {
        this (title, data == null ? null : data.sport, data == null ? null : data.distance,
                data == null ? null : data.start, data == null ? null : data.duration);
    }

    public SessionData (SessionData data, Sport sport) {
        this (data == null ? null : data.title, sport, data == null ? null : data.distance,
                data == null ? null : data.start, data == null ? null : data.duration);
    }

    public SessionData (SessionData data, BigDecimal distance) {
        this (data == null ? null : data.title, data == null ? null : data.sport, distance,
                data == null ? null : data.start, data == null ? null : data.duration);
    }

    public SessionData (SessionData data, Date start) {
        this (data == null ? null : data.title, data == null ? null : data.sport, data == null ? null : data.distance,
                start, data == null ? null : data.duration);
    }

    public SessionData (SessionData data, Duration duration) {
        this (data == null ? null : data.title, data == null ? null : data.sport, data == null ? null : data.distance,
                data == null ? null : data.start, duration);
    }
}
