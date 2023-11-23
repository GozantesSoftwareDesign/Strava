package org.gozantes.strava.server.services;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.challenge.ChallengeFilters;
import org.gozantes.strava.server.data.domain.challenge.DistanceChallenge;
import org.gozantes.strava.server.data.domain.challenge.TimeChallenge;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public final class ChallengeAppService {
    private final static List <Challenge> challenges;
    private static ChallengeAppService instance;
    private static long counter;

    static {
        try {
            challenges = Arrays.asList (new DistanceChallenge (0L, "Mucho kilómetros", new Pair <> (
                            new Date (Calendar.getInstance ().getTime ().getTime () - Duration.ofDays (30).toMillis ()),
                            new Date (Calendar.getInstance ().getTime ().getTime () + Duration.ofDays (30).toMillis ())), null,
                            new UserCredentials (CredType.Google, "mikel.p@opendeusto.es"), BigDecimal.valueOf (500)),
                    new TimeChallenge (1L, "A madrugar", new Pair <> (
                            new Date (Calendar.getInstance ().getTime ().getTime () - Duration.ofDays (15).toMillis ()),
                            new Date (Calendar.getInstance ().getTime ().getTime () + Duration.ofDays (2).toMillis ())),
                            Sport.Running, new UserCredentials (CredType.Meta, "dnocito@opendeusto.es"),
                            Duration.ofHours (50)), new TimeChallenge (2L, "Hay que echarle horas",
                            new Pair <> (Calendar.getInstance ().getTime (), new Date (
                                    Calendar.getInstance ().getTime ().getTime () + Duration.ofDays (90).toMillis ())),
                            null, new UserCredentials (CredType.Google, "iker.arrien@opendeusto.es"),
                            Duration.ofHours (100)), new DistanceChallenge (3L, "Repartirle el Glovoo a ElMillor",
                            new Pair <> (new Date (
                                    Calendar.getInstance ().getTime ().getTime () - Duration.ofDays (365).toMillis ()),
                                    new Date (Calendar.getInstance ().getTime ().getTime () + Duration.ofDays (365)
                                            .toMillis ())), Sport.Cyclism,
                            new UserCredentials (CredType.Meta, "gonzalo.pena@opendeusto.es"),
                            BigDecimal.valueOf (2000))).stream().collect (Collectors.toCollection (ArrayList::new));
        }
        catch (Exception e) {
            throw new RuntimeException (e);
        }

        counter = challenges.size ();
    }

    private ChallengeAppService () {
        super ();
    }

    public static ChallengeAppService getInstance () {
        if (ChallengeAppService.instance == null)
            ChallengeAppService.instance = new ChallengeAppService ();

        return ChallengeAppService.instance;
    }

    public Challenge create (UserCredentials creds, Challenge data) throws RemoteException {
        if (data == null)
            throw new RemoteException ("The challenge data cannot be null");

        try {
            Challenge c = data.isTimed () ? new TimeChallenge (ChallengeAppService.counter++, data.getName (),
                    data.getLapse (),
                    data.getSport (), creds, ((TimeChallenge) data).getGoal ()) :
                    new DistanceChallenge (ChallengeAppService.counter++, data.getName (),
                    data.getLapse (),
                    data.getSport (), creds, ((DistanceChallenge) data).getGoal ());

            challenges.add (c);

            return c;
        }
        catch (Exception e) {
            throw new RemoteException (e.getMessage ());
        }
    }

    public List <Challenge> getChallenges (ChallengeFilters filters) {
        return filters == null
                ? new ArrayList <Challenge> (ChallengeAppService.challenges)
                : ChallengeAppService.challenges.stream ().filter (
                                (x) -> (filters.user () == null || (filters.user ().id ().equals (x.getParent ().id ())
                                        && filters.user ().type ().equals (x.getParent ().type ()))) && x.getName ()
                                        .toLowerCase ()
                                        .contains ((filters.title () == null ? "" : filters.title ()).toLowerCase ()) && (
                                        filters.sport () == null || x.getSport ().equals (filters.sport ())) && (
                                        filters.distance () == null || filters.distance ().x () == null || (!x.isTimed ()
                                                && filters.distance ().x ().compareTo (((DistanceChallenge) x).getGoal ())
                                                <= 0)) && (filters.distance () == null || filters.distance ().y () == null || (
                                        !x.isTimed () && filters.distance ().y ().compareTo (((DistanceChallenge) x).getGoal ())
                                                >= 0)) && (filters.duration () == null || filters.duration ().x () == null || (
                                        x.isTimed ()
                                                && filters.duration ().x ().compareTo (((TimeChallenge) x).getGoal ()) <= 0))
                                        && (filters.duration () == null || filters.duration ().y () == null || (x.isTimed ()
                                        && filters.duration ().y ().compareTo (((TimeChallenge) x).getGoal ()) >= 0)))
                        .toList ();
    }

    public void accept (UserCredentials creds, long challenge) throws RemoteException {
        final Optional <Challenge> c = ChallengeAppService.challenges.stream ()
                .filter ((x) -> x.getId ().equals (challenge)).findFirst ();

        if (c.isEmpty ())
            throw new RemoteException (String.format ("No challenges with ID %d could be found.", challenge));

        if (c.get ().getParticipants ().stream ()
                .anyMatch ((x) -> x.type ().equals (creds.type ()) && x.id ().equals (creds.id ())))
            throw new RemoteException (
                    String.format ("User %s (%s) has already accepted challenge %d", creds.id (), creds.type (),
                            challenge));

        c.get ().getParticipants ().add (creds);
    }
}
