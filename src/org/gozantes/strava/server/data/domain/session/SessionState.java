package org.gozantes.strava.server.data.domain.session;

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
