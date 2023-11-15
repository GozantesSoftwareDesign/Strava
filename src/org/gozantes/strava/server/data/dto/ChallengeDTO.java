package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.challenge.activity.Activity;
import org.gozantes.strava.server.data.domain.challenge.ChallengeState;

import java.util.Date;
import java.util.Objects;

public record ChallengeDTO(String name, Date startDate, Date endDate, ChallengeState estado, Activity[] activities) {
    public ChallengeDTO (String name, Date startDate, Date endDate, ChallengeState estado, Activity[] activities) {
        Objects.requireNonNull (name);
        Objects.requireNonNull (activities);

        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estado = estado;
        this.activities = activities;

    }

    public Date startDate () {
        return startDate;
    }

    public Date endDate () {
        return endDate;
    }

    public ChallengeState estado () {
        return estado;
    }

    public String name () {
        return name;
    }

    public Activity[] activities () {
        return activities;
    }
}
