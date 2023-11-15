package org.gozantes.strava.client;

import javax.swing.*;

import org.gozantes.strava.client.gui.AuthWindow;

public class Main {

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new VentanaSecundaria(user));
        SwingUtilities.invokeLater(() -> new AuthWindow());

    }

}
