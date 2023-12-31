package org.gozantes.strava.server.data.domain.session;

import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class Session implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UserCredentials parent;
    private final List <UserCredentials> participants = new ArrayList <> ();
    private long id;
    private SessionState state;

    private SessionData data;

    public Session (long id, UserCredentials parent, SessionData data) {
        this (id, parent, data, SessionState.IN_PROGRESS);
    }

    public Session (long id, UserCredentials parent, SessionData data, SessionState state) {
        super ();

        if (id < 0)
            throw new RuntimeException ("Session IDs cannot be negative");

        this.id = id;
        this.participants.add ((this.parent = Objects.requireNonNull (parent)));
        this.state = Objects.requireNonNull (state);
        this.data = Objects.requireNonNull (data);
    }

    public long getId () {
        return this.id;
    }

    public void setId (long id) {
        if (id < 0)
            return;

        this.id = id;
    }

    public UserCredentials getParent () {
        return this.parent;
    }

    public SessionState getState () {
        return this.state;
    }

    public void setState (SessionState state) {
        this.state = state;
    }

    public SessionData getData () {
        return this.data;
    }

    public void setData (SessionData data) {
        Objects.requireNonNull (data);

        this.data = data;
    }

    public String getTitle () {
        return this.data.title ();
    }

    public void setTitle (String title) {
        this.data = new SessionData (this.data, title);
    }

    public Sport getSport () {
        return this.data.sport ();
    }

    public void setSport (Sport sport) {
        this.data = new SessionData (this.data, sport);
    }

    public BigDecimal getDistance () {
        return this.data.distance ();
    }

    public void setDistance (BigDecimal distance) {
        this.data = new SessionData (this.data, distance);
    }

    public Date getStart () {
        return this.data.start ();
    }

    public void setStart (Date start) {
        this.data = new SessionData (this.data, start);
    }

    public Duration getDuration () {
        return this.data.duration ();
    }

    public void setDuration (Duration duration) {
        this.data = new SessionData (this.data, duration);
    }
}
