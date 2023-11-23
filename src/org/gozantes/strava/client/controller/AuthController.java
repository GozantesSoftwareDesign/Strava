package org.gozantes.strava.client.controller;

import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AuthController {
    private ServiceLocator serviceLocator;

    private String token = null;

    public AuthController (ServiceLocator serviceLocator) throws URISyntaxException, NoSuchAlgorithmException {
        super ();

        this.serviceLocator = serviceLocator;
    }

    public Boolean login (UserCredentials cred) {
        try {
            this.token = this.serviceLocator.getService ().login (cred);
            return true;
        }
        catch (RemoteException | NoSuchAlgorithmException | InvalidKeySpecException | URISyntaxException e) {
            Logger.getLogger ().severe (String.format ("Could not log in the user: %s", e.getMessage ()));
            token = null;
            return false;
        }
    }

    public boolean signUp (UserCredentials creds, UserData data) {
        try {
            this.token = serviceLocator.getService ().signup (creds, data);

            return this.token != null;
        }
        catch (RemoteException | URISyntaxException | NoSuchAlgorithmException e) {
            token = null;
            Logger.getLogger ().severe (String.format ("Could not sign in the user: %s", e.getMessage ()));
            return false;
        }
    }
    public String getToken() {
    	return this.token;
    }
}
