package org.gozantes.strava.client.controller;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AuthController {
    private ServiceLocator serviceLocator;

    private String token = null;

    public AuthController (ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public Boolean login (UserCredentials cred) {
        try {
            this.token = this.serviceLocator.getService ().login (cred);
            return true;
        }
        catch (RemoteException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getLogger ().severe (String.format ("Could not log in the user", e.getMessage ()));
            token = null;
            return false;
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

    public Boolean signUp (UserCredentials creds, UserData data) {
        try {
            this.token = serviceLocator.getService ().signup (creds, data);
            return true;
        }
        catch (RemoteException e) {
            token = null;
            Logger.getLogger ().severe (String.format ("Could not sign in the user: %s", e.getMessage()));
            return false;
        }
    }
}
