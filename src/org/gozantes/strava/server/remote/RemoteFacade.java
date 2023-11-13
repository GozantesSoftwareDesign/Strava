package org.gozantes.strava.server.remote;

import org.gozantes.strava.internals.hash.SHA1Hasher;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.ServerParams;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.session.Session;
import org.gozantes.strava.server.data.domain.session.SessionFilters;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public final class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {
    public final Map<String, User> state = new HashMap<String, User>();
    private ServerParams params;

    public RemoteFacade(ServerParams params) throws RemoteException, MalformedURLException {
        super();

        Naming.rebind((this.params = params).fullName(), this);
    }

    public String getName() {
        return this.params.fullName();
    }

    @Override
    public synchronized String login(UserCredentials creds) throws RemoteException {
        final User user = null;

        if (user == null)
            Logger.getLogger().warning(new RemoteException("Unsuccessful login for " + creds.id() + "(" + creds.type().toString() + " account)."));


        if (this.state.values().contains(user)) {
            Logger.getLogger().info(new RemoteException(creds.id() + "(" + creds.type().toString() + " account)" + " is already logged in."));
        }

        String token = SHA1Hasher.hash(user, System.currentTimeMillis());
        state.put(token, user);

        Logger.getLogger().info("User " + creds.id() + "(" + creds.type().toString() + " account) sucessfully logged in.");

        return token;
    }

    @Override
    public synchronized void logout(String token) throws RemoteException {
        if (this.state.containsKey(token)) {
            this.state.remove(token);

            Logger.getLogger().info("Logout successful for token " + token + ".");
        } else
            Logger.getLogger().warning(new RemoteException("Token " + token + " belongs to no logged users."));
    }

    @Override
    public Map<Long, Session> searchSessions(SessionFilters filters) throws RemoteException {
        return null;
    }

    @Override
    public Map<Long, Session> getSessions(String token) throws RemoteException {
        return null;
    }

    @Override
    public void setSessionState(String token, long session) throws RemoteException {

    }

    @Override
    public void createChallenge(String token) {

    }

    @Override
    public void getActiveChallenges(String token) {

    }
}
