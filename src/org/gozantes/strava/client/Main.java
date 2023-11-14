package main;

import domain.User;
import gui.VentanaSecundaria;

import javax.swing.*;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        User user = new User("daniel.nocito@opendeusto.es", "Daniel", new Date(2003, 7, 4), 84.00, 1.87, null, null);
        SwingUtilities.invokeLater(() -> new VentanaSecundaria(user));
        //SwingUtilities.invokeLater(() -> new VentanaPrincipal());

    }

}
