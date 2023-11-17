package org.gozantes.strava.server.remote;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.internals.types.Triplet;
import org.gozantes.strava.server.ServerParams;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.challenge.ChallengeFilters;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;
import org.gozantes.strava.server.data.dto.ChallengeAssembler;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionAssembler;
import org.gozantes.strava.server.data.dto.SessionDTO;
import org.gozantes.strava.server.services.AuthAppService;
import org.gozantes.strava.server.services.ChallengeAppService;
import org.gozantes.strava.server.services.SessionAppService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {
    public final Map <String, User> state = new HashMap <String, User> ();
    final private ServerParams params;

    public RemoteFacade (ServerParams params) throws RemoteException, MalformedURLException {
        super ();

        Naming.rebind ((this.params = params).fullName (), this);
    }

    public String getName () {
        return this.params.fullName ();
    }

    @Override
    public synchronized String login (UserCredentials creds)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Pair <String, User> user = AuthAppService.getInstance ().login (creds);

        if (user == null) {
            Logger.getLogger ().warning (new RemoteException (
                    "Unsuccessful login for " + creds.id () + "(" + creds.type ().toString () + " account)."));

            return null;
        }

        if (this.state.values ().stream ().anyMatch ((u) -> u.equals (user.y ()))) {
            Logger.getLogger ().info (new RemoteException (
                    creds.id () + "(" + creds.type ().toString () + " account)" + " is already logged in."));
        }

        state.put (user.x (), user.y ());

        Logger.getLogger ()
                .info ("User " + creds.id () + "(" + creds.type ().toString () + " account) sucessfully logged in.");

        return user.x ();
    }

    public synchronized String signup (UserCredentials creds, UserData data) {
        Pair <String, User> user = null;

        try {
            user = AuthAppService.getInstance ().signup (creds, data);
        }

        catch (Exception e) {
            Logger.getLogger ().severe (String.format ("Could not create the user object: %s", e.getMessage ()), e);
        }

        assert user != null;
        state.put (user.x (), user.y ());

        return user.x ();
    }

    @Override
    public synchronized void logout (String token) throws RemoteException {
        if (this.state.containsKey (token)) {
            this.state.remove (token);

            Logger.getLogger ().info ("Logout successful for token " + token + ".");
        }

        else
            Logger.getLogger ().warning (new RemoteException ("Token " + token + " belongs to no logged users."));
    }

    @Override
    public boolean createSession (String token, SessionData data) throws RemoteException {
        if (!this.state.containsKey (token))
            Logger.getLogger ()
                    .severe (new RemoteException ("The user trying to create the session is not logged in."));

        SessionAppService.getInstance ().create (this.state.get (token).getCredentials (), data);

        return true;
    }

    @Override
    public List <SessionDTO> searchSessions (SessionFilters filters) throws RemoteException {
        if (filters != null)
            filters = new SessionFilters (null, filters.title (), filters.sport (), filters.distance (),
                    filters.duration ());

        return SessionAssembler.getInstance ().SessionsToDTO (SessionAppService.getInstance ().getSessions (filters));
    }

    @Override
    public List <SessionDTO> getSessions (String token) throws RemoteException {
        if (!this.state.containsKey (token))
            Logger.getLogger ()
                    .severe (new RemoteException ("The user trying to get their personal sessions is not logged in."));

        return SessionAssembler.getInstance ().SessionsToDTO (SessionAppService.getInstance ()
                .getSessions (new SessionFilters (this.state.get (token).getCredentials ())));
    }

    @Override
    public void setSessionState (String token, long session, SessionState state) throws RemoteException {
        if (!this.state.containsKey (token))
            Logger.getLogger ()
                    .severe (new RemoteException ("The user trying to get their personal sessions is not logged in."));

        try {
            SessionAppService.getInstance ().setState (this.state.get (token).getCredentials (), session, state);
        }
        catch (Exception e) {
            Logger.getLogger ().severe (e);
        }
    }

    @Override
    public List <ChallengeDTO> searchChallenges (ChallengeFilters filters) throws RemoteException {
        if (filters != null)
            filters = new ChallengeFilters (null, filters.title (), filters.lapse (), filters.sport (),
                    filters.distance (), filters.duration ());

        List <ChallengeDTO> c = null;

        try {
            c = ChallengeAssembler.getInstance ()
                    .ChallengesToDTO (ChallengeAppService.getInstance ().getChallenges (filters));
        }
        catch (Exception e) {
            Logger.getLogger ().severe (e);
        }

        return c;
    }

    @Override
    public List <ChallengeDTO> getActiveChallenges (String token) throws RemoteException {
        if (!this.state.containsKey (token))
            Logger.getLogger ().severe (new RemoteException (
                    "The user trying to get their active challenges is not " + "currently logged in."));

        List <ChallengeDTO> c = null;
        try {
            c = ChallengeAssembler.getInstance ().ChallengesToDTO (ChallengeAppService.getInstance ()
                    .getChallenges (new ChallengeFilters (this.state.get (token).getCredentials ())));
        }
        catch (Exception e) {
            Logger.getLogger ().severe (e);
        }

        return c;
    }

    @Override
    public boolean createChallenge (String token, Challenge data) throws RemoteException {
        if (!this.state.containsKey (token))
            Logger.getLogger ()
                    .severe (new RemoteException ("The user trying to create the challenge is not logged in."));

        try {
            ChallengeAppService.getInstance ().create (this.state.get (token).getCredentials (), data);
        }
        catch (Exception e) {
            Logger.getLogger ().severe ("Could not create challenge: " + e.getMessage ());

            return false;
        }

        return true;
    }

    @Override
    public void acceptChallenge (String token, long challenge) throws RemoteException {
        if (!this.state.containsKey (token))
            Logger.getLogger ()
                    .severe (new RemoteException ("The user trying to create the challenge is not logged in."));

        ChallengeAppService.getInstance ().accept (this.state.get (token).getCredentials (), challenge);
    }

    /*
     * The layout of the return type consists of a map:
     * - (Key) A challenge express as a Data Transfer Object
     * - (Value) A pair of:
     *   - A triplet containing:
     *     - An Object that might be:
     *       - An instance of Duration representing the time completed by the user
     *       - An instance of BigDecimal representing the amount of kilometers the user has run
     *     - An object that might be:
     *       - An instance of Duration representing the total challenge time
     *       - An instance of BigDecimal
     *     - The completion rate (0 <= r <= 1, either t / tT or d / dT) as BigDecimal
     *   - The sessions, mapped by sport
     * */
    @Override
    public Map <ChallengeDTO, Pair <Triplet <Object, Object, BigDecimal>, Map <Sport, List <SessionDTO>>>> getActiveChallengeStatus (
            String token) throws RemoteException {
        Map <ChallengeDTO, List <SessionDTO>> sessions;

        {
            List <SessionDTO> sessionTotal = this.getSessions (token);

            sessions = Map.ofEntries ((Map.Entry[]) this.getActiveChallenges (token).stream ().map ((c) -> Map.entry (c,
                    sessionTotal.stream ()
                            .filter ((s) -> c.sport () == null || c.sport ().equals (s.data ().sport ())))).toArray ());
        }

        Map <ChallengeDTO, Pair <Triplet <Object, Object, BigDecimal>, Map <Sport, List <SessionDTO>>>> map =
                new HashMap <> ();

        ChallengeDTO k;
        List <SessionDTO> v;
        Object[] ret;
        for (Map.Entry <ChallengeDTO, List <SessionDTO>> x : sessions.entrySet ().stream ().map ((x) -> x).toList ()) {
            k = x.getKey ();
            v = x.getValue ();

            Map <Sport, List <SessionDTO>> m = Map.of (Sport.Cyclism, new ArrayList <SessionDTO> (), Sport.Running,
                    new ArrayList <SessionDTO> ());

            ret = new Object[3];

            if (k.goal () instanceof Duration) {
                Duration t[] = new Duration[] { Duration.ofMinutes (0), Duration.ofMinutes (0) };

                v.forEach ((y) -> {
                    m.get (y.data ().sport ()).add (y);

                    if (y.state ().equals (SessionState.COMPLETED))
                        t[0] = t[0].plus (y.data ().duration ());

                    t[1] = t[1].plus (y.data ().duration ());
                });

                ret[2] = (BigDecimal.valueOf ((Long) (ret[0] = (Long) t[0].toMinutes ()))).divide (
                        BigDecimal.valueOf ((Long) (ret[1] = (Long) t[1].toMinutes ())), RoundingMode.HALF_EVEN);
            }

            else {
                BigDecimal d[] = new BigDecimal[] { BigDecimal.valueOf (0), BigDecimal.valueOf (0) };

                v.forEach ((y) -> {
                    m.get (y.data ().sport ()).add (y);

                    if (y.state ().equals (SessionState.COMPLETED))
                        d[0] = d[0].add (y.data ().distance ());

                    d[1] = d[1].add (y.data ().distance ());
                });

                ret[2] = ((BigDecimal) (ret[0] = d[0])).divide ((BigDecimal) (ret[1] = d[1]));
            }

            map.put (k, new Pair <> (new Triplet <> (ret[0], ret[1], ((BigDecimal) ret[2]).setScale (2)), m));
        }

        return map;
    }
}