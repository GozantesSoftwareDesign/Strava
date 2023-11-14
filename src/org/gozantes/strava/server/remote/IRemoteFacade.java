package org.gozantes.strava.server.remote;

import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.session.Session;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public interface IRemoteFacade extends Remote {
    public String login(UserCredentials creds) throws RemoteException, NoSuchAlgorithmException, InvalidKeySpecException;

    public void logout(String token) throws RemoteException;

    public long createSession(String token, SessionData data) throws RemoteException;

    public Map<Long, Session> searchSessions(SessionFilters filters) throws RemoteException;

    public Map<Long, Session> getSessions(String token) throws RemoteException;

    public void setSessionState(String token, long session) throws RemoteException;

    public void createChallenge(String token) throws RemoteException;

    public void getActiveChallenges(String token) throws RemoteException;
}
