package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.challenge.Challenge;

import java.util.ArrayList;
import java.util.List;

public class ChallengeAssembler {
    private static ChallengeAssembler instance;

    private ChallengeAssembler () {
    }

    public static ChallengeAssembler getInstance () {
        if (instance == null) {
            instance = new ChallengeAssembler ();
        }
        return instance;
    }

    public ChallengeDTO ChallengeToDTOPrivate (Challenge challenge) {
        ChallengeDTO challengeDTO = new ChallengeDTO (challenge.getName (), challenge.getStart (),
                challenge.getEnd(), challenge.getSport());
        return challengeDTO;
    }

    public ChallengeDTO ChallengeToDTOPublic (Challenge challenge) {
        ChallengeDTO challengeDTO = new ChallengeDTO (challenge.getName (), null, null,
                challenge.getSport());
        return challengeDTO;
    }

    public List <ChallengeDTO> ChallengesToDTOPrivate (List <Challenge> challenges) {
        List <ChallengeDTO> dtos = new ArrayList <> ();

        for (Challenge challenge : challenges) {
            dtos.add (this.ChallengeToDTOPrivate (challenge));
        }

        return dtos;
    }

    public List <ChallengeDTO> ChallengesToDTOPublic (List <Challenge> challenges) {
        List <ChallengeDTO> dtos = new ArrayList <> ();

        for (Challenge challenge : challenges) {
            dtos.add (this.ChallengeToDTOPublic (challenge));
        }

        return dtos;
    }
}
