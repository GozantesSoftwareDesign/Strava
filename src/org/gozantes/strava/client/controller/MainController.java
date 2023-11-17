package org.gozantes.strava.client.controller;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.internals.types.Triplet;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionDTO;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class MainController {
    private ServiceLocator serviceLocator;
    private String token;

    public MainController (ServiceLocator serviceLocator, String token) {
        this.serviceLocator = serviceLocator;
        this.token = token;
    }

    public void createSession (SessionData sessionData) {
        try {
            this.serviceLocator.getService ().createSession (token, sessionData);
        }
        catch (RemoteException e) {
            System.out.println ("Error al crear reto " + e);
        }
    }

    public String getToken () {
        return token;
    }

    public List <SessionDTO> getSessions () {
        try {
            return this.serviceLocator.getService ().getSessions (token);
        }
        catch (RemoteException e) {
            System.out.println ("Error al sacar sesiones " + e);
            return null;
        }
    }

    public List <SessionDTO> searchSessions (SessionFilters filters) {
        try {
            return this.serviceLocator.getService ().searchSessions (filters);
        }
        catch (RemoteException e) {
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

    public void createChallenge (Challenge challenge) {
        try {
            this.serviceLocator.getService ().createChallenge (token, challenge);
        }
        catch (RemoteException e) {
            System.out.println ("Error al crear reto " + e);
        }
    }

    public List <ChallengeDTO> getActiveChallenges () {
        try {
            return this.serviceLocator.getService ().getActiveChallenges (token);
        }
        catch (RemoteException e) {
            System.out.println ("Error en al obtener los retos activos " + e);
            return null;
        }
    }
    //public List<ChallengeDTO> getChallenges(){
    //try {
    //return this.serviceLocator.getService().getChallenges(token);
    //} catch (RemoteException e) {
    //System.out.println("Error en al obtener los retos "+e);
    //return null;
    //}
    //}

    public void acceptChallenge (long challenge) {
        try {
            this.serviceLocator.getService ().acceptChallenge (token, challenge);
        }
        catch (RemoteException e) {
            System.out.println ("Error al aceptar el reto" + e);
        }
    }

    public Map <ChallengeDTO, Pair <Triplet <Object, Object, BigDecimal>, Map <Sport, List <SessionDTO>>>> getActiveChallengeStatus (
    ) throws RemoteException {
        try {
            return this.serviceLocator.getService ().getActiveChallengeStatus (token);
        }
        catch (RemoteException e) {
            System.out.println ("Error al obtener el estado de los reos activos" + e);
            return null;
        }
    }

    public void logout () {
        try {
            this.serviceLocator.getService ().logout (this.token);
            token = null;
        }
        catch (RemoteException e) {
            // TODO Auto-generated catch block
            Logger.getLogger ().severe (String.format ("Could not log out the user: %s", e.getMessage ()));
        }
    }

}
