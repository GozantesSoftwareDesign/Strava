package org.gozantes.strava.server.data.domain.challenge;

import java.util.Arrays;
import java.util.Date;

import org.gozantes.strava.server.data.domain.Sport;

public class Challenge {
	private String name;
	private Date startDate,endDate;
	private StateChallenge estado;
	private Activity[] activities;
	
	public Challenge(String name, Date startDate, Date endDate, StateChallenge estado, Activity[] activities) {
		super();
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.estado = estado;
		this.activities = activities;
	}
	public Challenge(String name, Activity[] activities) {
		this(name, null, null, null, activities);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public StateChallenge getEstado() {
		return estado;
	}

	public void setEstado(StateChallenge estado) {
		this.estado = estado;
	}

	public Activity[] getActivities() {
		return activities;
	}
	
}