package org.gozantes.strava.client.controller;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.internals.types.Triplet;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionDTO;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ChallengeController {
    private ServiceLocator serviceLocator;

    public ChallengeController (ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public void createChallenge (String token) {
        try {
            this.serviceLocator.getService ().createChallenge (token);
        }
        catch (RemoteException e) {
            System.out.println ("Error al crear reto " + e);
        }
    }

    public List <ChallengeDTO> getActiveChallenges (String token) {
        try {
            return this.serviceLocator.getService ().getActiveChallenges (token);
        }
        catch (RemoteException e) {
            System.out.println ("Error en al obtener los retos activos " + e);
            return null;
        }
    }
    //public List<ChallengeDTO> getChallenges(String token){
    //try {
    //return this.serviceLocator.getService().getChallenges(token);
    //} catch (RemoteException e) {
    //System.out.println("Error en al obtener los retos "+e);
    //return null;
    //}
    //}

    public void acceptChallenge (String token, long challenge) {
        try {
            this.serviceLocator.getService ().acceptChallenge (token, challenge);
        }
        catch (RemoteException e) {
            System.out.println ("Error al aceptar el reto" + e);
        }
    }

    public Map <ChallengeDTO, Pair <Triplet <Object, Object, BigDecimal>, Map <Sport, List <SessionDTO>>>> getActiveChallengeStatus (
            String token) throws RemoteException {
        try {
            return this.serviceLocator.getService ().getActiveChallengeStatus (token);
        }
        catch (RemoteException e) {
            System.out.println ("Error al obtener el estado de los reos activos" + e);
            return null;
        }
    }
}