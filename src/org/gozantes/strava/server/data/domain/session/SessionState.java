package org.gozantes.strava.server.data.domain.session;

import java.io.Serializable;

public enum SessionState implements Serializable {
    CANCELLED ("Cancelled"),
    IN_PROGRESS ("In progress"),
    COMPLETED ("Completed");

    public final String value;

    private SessionState (String value) {
        this.value = value;
    }

    @Override
    public String toString () {
        return this.value;
    }
}
