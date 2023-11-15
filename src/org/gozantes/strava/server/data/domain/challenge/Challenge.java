package org.gozantes.strava.server.data.domain.challenge;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

import java.util.Date;
import java.util.Objects;

public abstract class Challenge {
    protected Long id;
    protected final String name;
    protected final Pair <Date, Date> lapse;
    protected final Sport sport;

    protected final UserCredentials parent;

    protected Challenge (String name, Pair <Date, Date> lapse, Sport sport, UserCredentials parent) throws Exception {
        this (null, name, lapse, sport, parent);
    }

    protected Challenge (Long id, String name, Pair <Date, Date> lapse, Sport sport, UserCredentials parent)
            throws Exception {
        super ();

        Objects.requireNonNull (name);
        Objects.requireNonNull (lapse);
        Objects.requireNonNull (parent);

        if (name.isBlank ())
            throw new Exception ("Challenge names cannot be blank.");

        if (lapse.x ().getTime () >= lapse.y ().getTime ())
            throw new Exception ("Challenges must start before their deadline has already been met.");

        this.id = id;
        this.name = name;
        this.lapse = lapse;
        this.sport = sport;
        this.parent = parent;
    }

    public Long getId () {
        return this.id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getName () {
        return this.name;
    }

    public Pair <Date, Date> getLapse () {
        return this.lapse;
    }

    public Date getStart () {
        return this.lapse.x ();
    }

    public Date getEnd () {
        return this.lapse.y ();
    }

    public Sport getSport () {
        return this.sport;
    }

    public UserCredentials getParent () {
        return this.parent;
    }

    public final boolean isTimed () {
        return this instanceof TimeChallenge;
    }
}