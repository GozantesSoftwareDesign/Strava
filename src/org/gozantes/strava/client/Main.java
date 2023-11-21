package org.gozantes.strava.client;

import org.gozantes.strava.client.controller.AuthController;
import org.gozantes.strava.client.controller.MainController;
import org.gozantes.strava.client.gui.AuthWindow;
import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.security.Security;
import org.gozantes.strava.server.ServerParams;

import javax.swing.*;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main (String[] args) throws URISyntaxException, NoSuchAlgorithmException, UnknownHostException {
        ServerParams p = null;
        try {
            p = new ServerParams (args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null,
                    args.length > 2 ? args[2] : null);
        }

        catch (NumberFormatException | UnknownHostException e) {
            Logger.getLogger ().severe (e);
            throw e;
        }

        Security.init ();

        ServiceLocator sl = new ServiceLocator ();
        sl.setService (p);
        AuthController ac = new AuthController (sl);

        SwingUtilities.invokeLater (() -> new AuthWindow (ac, sl));

    }

}
