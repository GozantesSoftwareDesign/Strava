package org.gozantes.strava.client.controller;

import java.rmi.RemoteException;
import java.util.List;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.server.data.domain.challenge.*;
import org.gozantes.strava.server.data.dto.ChallengeDTO;

public class ChallengeController {
	private ServiceLocator serviceLocator;
	public ChallengeController(ServiceLocator serviceLocator) {
		this.serviceLocator=serviceLocator;
	}
	public void createChallenge(String token) {
		try {
			this.serviceLocator.getService().createChallenge(token);
		} catch (RemoteException e) {
			System.out.println("Error al crear reto "+e);
		}
	}
	public List<ChallengeDTO> getActiveChallenges(String token){
		try {
			return this.serviceLocator.getService().getActiveChallenges(token);
		} catch (RemoteException e) {
			System.out.println("Error en al obtener los retos activos "+e);
			return null;
		}
	}
	public List<ChallengeDTO> getChallenges(String token){
		try {
			return this.serviceLocator.getService().getChallenges(token);
		} catch (RemoteException e) {
			System.out.println("Error en al obtener los retos "+e);
			return null;
		}
	}
	
	public void acceptChallenges(String token) {
		try {
			this.serviceLocator.getService().acceptChallenge(token);
		} catch (RemoteException e) {
			System.out.println("Error al aceptar el reto"+e);
		}
	}
}