package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.challenge.DistanceChallenge;
import org.gozantes.strava.server.data.domain.challenge.TimeChallenge;

import java.io.Serializable;
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

    public ChallengeDTO ChallengeToDTO (Challenge challenge) {
        return new ChallengeDTO (challenge.getName (), challenge.getLapse (),
                challenge.getSport (), (Serializable) (challenge.isTimed () ? ((TimeChallenge) challenge).getGoal () :
                ((DistanceChallenge) challenge).getGoal ()), challenge.getId ());
    }

    public List <ChallengeDTO> ChallengesToDTO (List <Challenge> challenges) {
        List <ChallengeDTO> dtos = new ArrayList <> ();

        for (Challenge challenge : challenges) {
            dtos.add (this.ChallengeToDTO (challenge));
        }

        return dtos;
    }
}
