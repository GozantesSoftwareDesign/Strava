package org.gozantes.strava.client.controller;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.internals.types.Triplet;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.challenge.ChallengeFilters;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainController {
    private ServiceLocator serviceLocator;
    private String token;

    public MainController (ServiceLocator serviceLocator, String token) {
        super ();

        this.serviceLocator = serviceLocator;
        this.token = token;
    }

    public boolean createSession (SessionData sessionData) {
        try {
            this.serviceLocator.getService ().createSession (token, sessionData);
            return true;
        }
        catch (RemoteException | NoSuchAlgorithmException | URISyntaxException e) {
            System.out.println ("Error al crear reto " + e);
            return false;
        }
    }

    public List <SessionDTO> getSessions () {
        try {
            return this.serviceLocator.getService ().getSessions (token);
        }
        catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
            System.out.println ("Error al sacar sesiones " + e);
            return null;
        }
    }

    public List <SessionDTO> searchSessions (SessionFilters filters) {
        try {
            return this.serviceLocator.getService ().searchSessions (filters);
        }
        catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
            System.out.println ("Error al buscar sesiones " + e);
            return null;
        }
    }

    public void setSessionState (long session, SessionState state) {
        try {
            this.serviceLocator.getService ().setSessionState (token, session, state);
        }
        catch (Exception e) {
            System.out.println ("Error al establecer estado de la sesion " + e);
        }
    }

    public boolean createChallenge (Challenge challenge)
            throws URISyntaxException, NoSuchAlgorithmException, RemoteException {
            return this.serviceLocator.getService ().createChallenge (token, challenge);
    }

    public List <ChallengeDTO> getActiveChallenges () {
        try {
            return this.serviceLocator.getService ().getActiveChallenges (token);
        }
        catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
            System.out.println ("Error en al obtener los retos activos " + e);
            return null;
        }
    }
    public List<ChallengeDTO> searchChallenges(ChallengeFilters challengeFilters){
		try {
			return this.serviceLocator.getService().searchChallenges(challengeFilters);
		} catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
			Logger.getLogger ().severe("Error en al obtener los retos: " + e.getMessage());
			return Collections.EMPTY_LIST;
		}
    	
    }
    
    public void acceptChallenge (long challenge) {
        try {
            this.serviceLocator.getService ().acceptChallenge (token, challenge);
        }
        catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
            System.out.println ("Error al aceptar el reto" + e);
        }
    }

    public Map <ChallengeDTO, Pair <Triplet <Serializable, Serializable, BigDecimal>, Map <Sport, List <SessionDTO>>>> getActiveChallengeStatus (
    ) throws RemoteException {
        try {
            return this.serviceLocator.getService ().getActiveChallengeStatus (token);
        }
        catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
            System.out.println ("Error al obtener el estado de los reos activos" + e);
            return null;
        }
    }

    public void logout () {
        try {
            this.serviceLocator.getService ().logout (this.token);
            token = null;
        }
        catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
            Logger.getLogger ().severe (String.format ("Could not log out the user: %s", e.getMessage ()));
        }
    }

}
