package org.gozantes.strava.server.data.dto;

import java.util.Objects;

import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionState;

public record SessionDTO(long id, SessionState state, SessionData data) {
	public SessionDTO(long id, SessionState state, SessionData data) {
		Objects.requireNonNull(id);
		
		this.id = id;
		this.state = state;
		this.data = data;
	}

	public long id() {
		return id;
	}

	public SessionState state() {
		return state;
	}

	public SessionData data() {
		return data;
	}
}
