package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionState;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public record SessionDTO(long id, SessionState state, SessionData data) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public SessionDTO (long id, SessionState state, SessionData data) {
        Objects.requireNonNull (id);

        this.id = id;
        this.state = state;
        this.data = data;
    }

    public long id () {
        return id;
    }

    public SessionState state () {
        return state;
    }

    public SessionData data () {
        return data;
    }
}
