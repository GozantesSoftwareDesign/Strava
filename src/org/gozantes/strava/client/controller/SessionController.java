package org.gozantes.strava.client.controller;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.server.data.domain.session.Session;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.dto.SessionDTO;

public class SessionController {
	private ServiceLocator serviceLocator;
	public SessionController(ServiceLocator serviceLocator) {
		this.serviceLocator=serviceLocator;
	}
	
	public void createSession(String token, SessionData sessionData) {
		try {
			this.serviceLocator.getService().createSession(token,sessionData);
		} catch (RemoteException e) {
			System.out.println("Error al crear reto "+e);
		}
	}
	public Map<Long, SessionDTO> getSessions(String token){
		try {
			return this.serviceLocator.getService().getSessions(token);
		} catch (RemoteException e) {
			System.out.println("Error al sacar sesiones "+e);
			return null;
		}
	}
	public Map<Long, SessionDTO> searchSessions(SessionFilters filters){
		try {
			return this.serviceLocator.getService().searchSessions(filters);
		} catch (RemoteException e) {
			System.out.println("Error al buscar sesiones "+e);
			return null;
		}
	}
	public void setSessionState(String token, long session) {
		try {
			this.serviceLocator.getService().setSessionState(token, session);
		} catch (Exception e) {
			System.out.println("Error al establecer estado de la sesion "+e);
		}
	}
}