package org.gozantes.strava.server;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.remote.IRemoteFacade;
import org.gozantes.strava.server.remote.RemoteFacade;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {
    @SuppressWarnings({"removal", "deprecation"})
    public static void main(String[] args) throws RemoteException {
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            ServerParams p = new ServerParams(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null, args.length > 2 ? args[2] : null);

            LocateRegistry.createRegistry(p.port());
            IRemoteFacade rf = new RemoteFacade(p);

            Logger.getLogger().info("Server " + ((RemoteFacade) rf).getName() + " started.");
        } catch (Exception e) {
            Logger.getLogger().severe(e);
        }
    }
}