package org.gozantes.strava.client;

import org.gozantes.strava.client.gui.AuthWindow;
import org.gozantes.strava.internals.security.Security;

import javax.swing.*;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main (String[] args) throws URISyntaxException, NoSuchAlgorithmException {
        Security.init ();

        //SwingUtilities.invokeLater(() -> new VentanaSecundaria(user));
        SwingUtilities.invokeLater (() -> new AuthWindow ());

    }

}
