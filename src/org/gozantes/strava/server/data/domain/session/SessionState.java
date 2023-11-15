package org.gozantes.strava.server.data.domain.session;

import org.w3c.dom.ranges.RangeException;

import java.util.Arrays;

public enum SessionState {
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
