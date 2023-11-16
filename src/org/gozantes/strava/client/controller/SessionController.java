package org.gozantes.strava.client.controller;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;
import org.gozantes.strava.server.data.dto.SessionDTO;

import java.rmi.RemoteException;
import java.util.List;

public class SessionController {
    private ServiceLocator serviceLocator;

    public SessionController (ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public void createSession (String token, SessionData sessionData) {
        try {
            this.serviceLocator.getService ().createSession (token, sessionData);
        }
        catch (RemoteException e) {
            System.out.println ("Error al crear reto " + e);
        }
    }

    public List <SessionDTO> getSessions (String token) {
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

    public void setSessionState (String token, long session, SessionState state) {
        try {
            this.serviceLocator.getService ().setSessionState (token, session, state);
        }
        catch (Exception e) {
            System.out.println ("Error al establecer estado de la sesion " + e);
        }
    }
}