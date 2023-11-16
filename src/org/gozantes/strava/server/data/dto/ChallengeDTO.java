package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.Sport;

import java.util.Date;
import java.util.Objects;

public record ChallengeDTO(String name, Date startDate, Date endDate, Sport sport) {
    public ChallengeDTO (String name, Date startDate, Date endDate, Sport sport) {
        Objects.requireNonNull (name);
        Objects.requireNonNull (sport);

        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;       
        this.sport = sport;

    }

    public Date getStartDate () {
        return startDate;
    }

    public Date getEndDate () {
        return endDate;
    }

    
    public String getName () {
        return name;
    }

    public Sport getSport () {
        return sport;
    }

	public boolean isTimed() {
		Date fechaActual = new Date();
		if(fechaActual.before(endDate)&&fechaActual.after(startDate)) {
			return true;
		}else {
			return false;
		}
	}
}
