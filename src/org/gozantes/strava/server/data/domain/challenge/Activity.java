package org.gozantes.strava.server.data.domain.challenge;

import java.util.Objects;

import org.gozantes.strava.server.data.domain.Sport;

public class Activity {
	private Integer targetTimeInSec;
	private Double distance;
	private Sport sport;
	private StateActivity estado;
	
	public Activity(Integer targetTimeInSec, Double distance, Sport sport, StateActivity estado) {
		Objects.requireNonNull(sport);
		
		this.targetTimeInSec = targetTimeInSec;
		this.distance = distance;
		this.sport = sport;
		this.estado = estado;
	}
	public Activity(Integer targetTimeInSec, Sport sport, StateActivity estado) {
		this(targetTimeInSec, null , sport, estado);	}
	public Activity(Double distance, Sport sport, StateActivity estado) {
		this(null, distance, sport, estado);
	}
	public StateActivity getEstado() {
		return estado;
	}
	public void setEstado(StateActivity estado) {
		this.estado = estado;
	}
	public Integer getTargetTimeInSec() {
		return targetTimeInSec;
	}
	public Double getDistance() {
		return distance;
	}
	public Sport getSport() {
		return sport;
	}
	
}
