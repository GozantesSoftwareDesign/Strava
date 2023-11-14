package org.gozantes.strava.server.data.dto;

import java.util.Date;
import java.util.Objects;

import org.gozantes.strava.server.data.domain.challenge.Activity;
import org.gozantes.strava.server.data.domain.challenge.StateChallenge;

public record ChallengeDTO(String name, Date startDate,Date endDate, StateChallenge estado, Activity[] activities) {
	public ChallengeDTO(String name, Date startDate,Date endDate, StateChallenge estado, Activity[] activities) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(activities);
		
		
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.estado = estado;
		this.activities = activities;
		
	}
	public Date startDate() {
		return startDate;
	}
	public Date endDate() {
		return endDate;
	}
	public StateChallenge estado() {
		return estado;
	}
	public String name() {
		return name;
	}

	public Activity[] activities() {
		return activities;
	}
}
