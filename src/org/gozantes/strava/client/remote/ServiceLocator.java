package org.gozantes.strava.client.remote;

import org.gozantes.strava.server.ServerParams;
import org.gozantes.strava.server.remote.IRemoteFacade;

import java.rmi.Naming;

//This class implements Service Locator pattern
public class ServiceLocator {

    //Remote Facade reference
    private IRemoteFacade service;

    public IRemoteFacade getService () {
        return this.service;
    }

    @SuppressWarnings ({ "removal", "deprecation" })
    public void setService (ServerParams sp) {
        //Activate Security Manager. It is needed for RMI.
        if (System.getSecurityManager () == null) {
            System.setSecurityManager (new SecurityManager ());
        }
        System.out.println (sp.fullName ());
        //Get Remote Facade reference using RMIRegistry (IP + Port) and the service name.
        try {
            this.service = (IRemoteFacade) Naming.lookup (sp.fullName ());
        }
        catch (Exception ex) {
            System.err.println ("# Error locating remote facade: " + ex);
        }
    }
}