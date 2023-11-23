package org.gozantes.strava.server.remote;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.internals.types.Triplet;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.challenge.ChallengeFilters;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

public interface IRemoteFacade extends Remote {
    public String login (UserCredentials creds)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeySpecException;

    public String signup (UserCredentials creds, UserData data) throws RemoteException;

    public void logout (String token) throws RemoteException;

    public boolean createSession (String token, SessionData data) throws RemoteException;

    public List <SessionDTO> searchSessions (SessionFilters filters) throws RemoteException;

    public List <SessionDTO> getSessions (String token) throws RemoteException;

    public void setSessionState (String token, long session, SessionState state) throws RemoteException;

    public boolean createChallenge (String token, Challenge data) throws RemoteException;

    public List <ChallengeDTO> searchChallenges (ChallengeFilters filters) throws RemoteException;

    public List <ChallengeDTO> getActiveChallenges (String token) throws RemoteException;

    public void acceptChallenge (String token, long challenge) throws RemoteException;

    public Map <ChallengeDTO, Pair <Triplet <Serializable, Serializable, BigDecimal>, Map <Sport, List <SessionDTO>>>> getActiveChallengeStatus (
            String token) throws RemoteException;
}
