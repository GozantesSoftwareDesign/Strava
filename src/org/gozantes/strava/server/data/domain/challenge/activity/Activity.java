package org.gozantes.strava.server.data.domain.challenge.activity;

import org.gozantes.strava.server.data.domain.Sport;

import java.util.Objects;

public abstract class Activity {
    protected final Sport sport;
    protected final ActivityState state;

    protected Activity (Sport sport, ActivityState state) {
        super ();

        Objects.requireNonNull (sport);
        Objects.requireNonNull (state);

        this.sport = sport;
        this.state = state;
    }

    public Sport getSport () {
        return this.sport;
    }

    public ActivityState getState () {
        return this.state;
    }

    public boolean isTimed () {
        return this instanceof DistanceActivity;
    }
}