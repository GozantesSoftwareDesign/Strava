package org.gozantes.strava.client;

import org.gozantes.strava.client.gui.AuthWindow;

import javax.swing.*;

public class Main {

    public static void main (String[] args) {
        //SwingUtilities.invokeLater(() -> new VentanaSecundaria(user));
        SwingUtilities.invokeLater (() -> new AuthWindow ());

    }

}
