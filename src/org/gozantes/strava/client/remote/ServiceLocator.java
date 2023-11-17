package org.gozantes.strava.client.remote;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.security.Security;
import org.gozantes.strava.server.ServerParams;
import org.gozantes.strava.server.remote.IRemoteFacade;

import java.net.URISyntaxException;
import java.rmi.Naming;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

//This class implements Service Locator pattern
public class ServiceLocator {

    //Remote Facade reference
    private IRemoteFacade service;
    private ServerParams sp;

    public IRemoteFacade getService () throws URISyntaxException, NoSuchAlgorithmException {
        this.setService (this.sp);

        return this.service;
    }

    @SuppressWarnings ({ "removal", "deprecation" })
    public void setService (ServerParams sp) {
        this.sp = Objects.requireNonNull (sp);

        try {
            this.service = (IRemoteFacade) Naming.lookup (this.sp.fullName ());

            Logger.getLogger ().info ("Service set to " + this.service + " (server: " + this.sp.fullName () + ").");
        }
        catch (Exception ex) {
            Logger.getLogger().severe ("Error locating remote facade: " + ex.getMessage ());
        }
    }
}