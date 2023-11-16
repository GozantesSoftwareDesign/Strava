package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

public record ChallengeDTO(String name, Pair <Date, Date> lapse, Sport sport, Object goal) {
    public ChallengeDTO (String name, Pair <Date, Date> lapse, Sport sport, Object goal) {
        Objects.requireNonNull (name);
        Objects.requireNonNull (lapse);
        Objects.requireNonNull (goal);

        if (name.isBlank ())
            throw new RuntimeException ("Challenge names cannot be blank.");

        if (lapse.x ().getTime () >= lapse.y ().getTime ())
            throw new RuntimeException ("Challenges must start before their deadline has already been met.");

        if (!(goal instanceof Duration || goal instanceof BigDecimal))
            throw new RuntimeException (
                    "Challenge goals must be either instances of Duration representing time goals or instances of "
                            + "BigDecimal representing distance goals in kilometers.");

        this.name = name;
        this.lapse = lapse;
        this.sport = sport;
        this.goal = goal;
    }

    public Pair <Date, Date> lapse () {
        return new Pair <Date, Date> (this.lapse);
    }

    public Date start () {
        return this.lapse.x ();
    }

    public Date end () {
        return this.lapse.y ();
    }
}
