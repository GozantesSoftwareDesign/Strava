package org.gozantes.strava.server.remote;

import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface IRemoteFacade extends Remote {
    public String login (UserCredentials creds)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeySpecException;

    public String signup(UserCredentials creds, UserData data) throws RemoteException;

    public void logout (String token) throws RemoteException;

    public boolean createSession (String token, SessionData data) throws RemoteException;

    public List <SessionDTO> searchSessions (SessionFilters filters) throws RemoteException;

    public List <SessionDTO> getSessions (String token) throws RemoteException;

    public void setSessionState (String token, long session, SessionState state) throws RemoteException;

    public void createChallenge (String token) throws RemoteException;

    public List<ChallengeDTO> getActiveChallenges (String token) throws RemoteException;

    public void acceptChallenge (String token, long challenge) throws RemoteException;
}
