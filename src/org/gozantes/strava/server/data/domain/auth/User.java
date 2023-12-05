package org.gozantes.strava.server.data.domain.auth;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Mergeable;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.session.Session;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class User implements Mergeable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List <Session> sessions = new ArrayList <Session> ();
    private final List <Challenge> challenges = new ArrayList <Challenge> ();
    private UserCredentials creds;
    private UserData data;

    public User (UserCredentials creds, UserData data) {
        super ();

        Objects.requireNonNull (creds);
        Objects.requireNonNull (data);

        this.creds = creds;
        this.data = data;
    }

    public User (User user) {
        this (user == null ? null : user.creds, user == null ? null : user.data);
    }

    public UserCredentials getCredentials () {
        return this.creds;
    }

    public String getId () {
        return this.creds.id ();
    }

    public CredType getType () {
        return this.creds.type ();
    }

    public UserData getData () {
        return this.data;
    }

    public Date getBirthDate () {
        return this.data.birth ();
    }

    public void setBirthDate (Date birth) {
        this.data = new UserData (this.data, birth);
    }

    public BigDecimal getWeight () {
        return this.data.weight ();
    }

    public void setWeight (BigDecimal weight) {
        this.data = new UserData (this.data, weight);
    }

    public Integer getHeight () {
        return this.data.height ();
    }

    public void setHeight (Integer height) {
        this.data = new UserData (this.data);
    }

    public Pair <Integer, Integer> getHeartRate () {
        return this.data.heartRate ();
    }

    public void setHeartRate (Pair <Integer, Integer> heartRate) {
        this.data = new UserData (this.data, heartRate);
    }

    public List <Session> getSessions () {
        return new ArrayList (this.sessions);
    }

    @Override
    public boolean equals (Object o) {
        return o instanceof User && this.getType ().equals (((User) o).getType ()) && this.getId ()
                .equals (((User) o).getId ());
    }

    @Override
    public String toString () {
        return "User [creds=" + creds + ", data=" + data + "]";
    }
}
