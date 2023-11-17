package org.gozantes.strava.server;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.security.Security;
import org.gozantes.strava.server.remote.IRemoteFacade;
import org.gozantes.strava.server.remote.RemoteFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.registry.LocateRegistry;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main (String[] args)
            throws IOException, URISyntaxException, NoSuchAlgorithmException {
        Security.init ();

        try {
            ServerParams p = new ServerParams (args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null,
                    args.length > 2 ? args[2] : null);

            System.setProperty ("java.rmi.server.hostname", p.ip().getHostAddress ());
            LocateRegistry.createRegistry (p.port ());
            IRemoteFacade rf = new RemoteFacade (p);

            Logger.getLogger ().info ("Server " + ((RemoteFacade) rf).getName () + " started.");
        }
        catch (Exception e) {
            Logger.getLogger ().severe (e);
        }
    }
}