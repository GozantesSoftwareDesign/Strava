package org.gozantes.strava.server.data.dto;

import java.util.ArrayList;
import java.util.List;

import org.gozantes.strava.server.data.domain.challenge.Challenge;

public class ChallengeAssembler {
	private static ChallengeAssembler instance;
	
	private ChallengeAssembler() { }
	
	public static ChallengeAssembler getInstance() {
		if (instance == null) {
			instance = new ChallengeAssembler();
		}
		return instance;
	}
	public ChallengeDTO ChallengeToDTOPrivate(Challenge challenge) {
		ChallengeDTO challengeDTO = new ChallengeDTO(challenge.getName(), challenge.getStartDate(), challenge.getEndDate(), challenge.getEstado(), challenge.getActivities());
		return challengeDTO;
	}
	public ChallengeDTO ChallengeToDTOPublic(Challenge challenge) {
		ChallengeDTO challengeDTO = new ChallengeDTO(challenge.getName(), null, null, null, challenge.getActivities());
		return challengeDTO;
	}
	public List<ChallengeDTO> ChallengesToDTOPrivate(List<Challenge> challenges) {
		List<ChallengeDTO> dtos = new ArrayList<>();
		
		for (Challenge challenge : challenges) {
			dtos.add(this.ChallengeToDTOPrivate(challenge));
		}
		
		return dtos;		
	}
	public List<ChallengeDTO> ChallengesToDTOPublic(List<Challenge> challenges) {
		List<ChallengeDTO> dtos = new ArrayList<>();
		
		for (Challenge challenge : challenges) {
			dtos.add(this.ChallengeToDTOPublic(challenge));
		}
		
		return dtos;		
	}
}