package org.gozantes.strava.server.data.domain.session;

public class Session {
    private long id;
    private SessionState state;
    private SessionData sessionData;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id < 0)
            return;

        this.id = id;
    }

    public SessionState getState() {
        return this.state;
    }

    public void setState(SessionState state) {
        this.state = state;
    }
}
