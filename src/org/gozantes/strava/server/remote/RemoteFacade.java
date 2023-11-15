package org.gozantes.strava.server.remote;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.ServerParams;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionAssembler;
import org.gozantes.strava.server.data.dto.SessionDTO;
import org.gozantes.strava.server.services.AuthAppService;
import org.gozantes.strava.server.services.SessionAppService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    public List <ChallengeDTO> getActiveChallenges (String token) throws RemoteException {
        return null;
    }
}
