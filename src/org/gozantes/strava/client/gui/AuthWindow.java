package org.gozantes.strava.client.gui;

import org.gozantes.strava.client.controller.AuthController;
import org.gozantes.strava.client.controller.MainController;
import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.swing.ImageDisplayer;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.internals.swing.TextPrompt;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AuthWindow extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;
    private AuthController authController;
    private ServiceLocator serviceLocator;
    
    private JFrame frame = new JFrame ("STRAVA");
    private JPanel panelPrincipal = new JPanel (new GridLayout (4, 1));
    private JPanel panelUser = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelPassword = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelName = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelBirthDate = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelWeight = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelHeight = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelMaximunHeartRate = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelRestingHeartRate = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelBotonesLogin = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel panelBotonesSignUp = new JPanel (new FlowLayout (FlowLayout.CENTER));

    private SpinnerNumberModel weightSpinnerM = new SpinnerNumberModel (0, 0, 300, 1);
    private SpinnerNumberModel heightSpinnerM = new SpinnerNumberModel (0, 0, 250, 1);
    private SpinnerNumberModel maximunHeartRateSpinnerM = new SpinnerNumberModel (0, 0, 300, 1);
    private SpinnerNumberModel restingHeartRateSpinnerM = new SpinnerNumberModel (0, 0, 300, 1);

    private DateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy");

    private JLabel userLabel = new JLabel ("Email");
    private JTextField userText = new JTextField (20);
    private JLabel passwordLabel = new JLabel ("Password");
    private JPasswordField passwordText = new JPasswordField (20);
    private JLabel nameLabel = new JLabel ("Name");
    private JTextField nameText = new JTextField (20);
    private JLabel birthDateLabel = new JLabel ("BirthDate");
    private JTextField birthDateText = new JTextField ();
    private JLabel weightLabel = new JLabel ("Weight(Optional)");
    private JSpinner weightSpinner = new JSpinner (weightSpinnerM);
    private JLabel heightLabel = new JLabel ("Height(Optional)");
    private JSpinner heightSpinner = new JSpinner (heightSpinnerM);
    private JLabel maximunHeartRateLabel = new JLabel ("MaximunHeartRate(Optional)");
    private JSpinner maximunHeartRateSpinner = new JSpinner (maximunHeartRateSpinnerM);
    private JLabel restingHeartRateLabel = new JLabel ("RestingHeartRate(Optional)");
    private JSpinner restingHeartRateSpinner = new JSpinner (restingHeartRateSpinnerM);

    @SuppressWarnings("unused")
	private TextPrompt placeholder = new TextPrompt ("dd/MM/yyyy", birthDateText);

    private JButton login = new JButton ("Login");
    private JButton loginMeta = new JButton ("Login with Meta");
    private JButton loginGoogle = new JButton ("Login with Google");
    private JButton signUp = new JButton ("Sign up");
    private JButton signUpMeta = new JButton ("Sign up with Meta");
    private JButton signUpGoogle = new JButton ("Sign up with Google");

    private ImageDisplayer iconoGoogle;
    private ImageDisplayer iconoMeta;

    public AuthWindow (AuthController authController, ServiceLocator serviceLocator) {
    	super();
    	this.authController = authController;
        this.serviceLocator = serviceLocator;
        frame.setSize (700, 500);
        frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible (true);
        frame.setLocationRelativeTo (null);
        frame.setLayout (null);

        try {
            iconoGoogle = new ImageDisplayer (ImageIO.read (
                    Objects.requireNonNull (this.getClass ().getClassLoader ().getResource ("Google.png"))), 25, 25);
        }
        catch (IOException e) {
            e.printStackTrace ();
        }
        try {
            iconoMeta = new ImageDisplayer (ImageIO.read (
                    Objects.requireNonNull (this.getClass ().getClassLoader ().getResource ("Meta.png"))), 25, 25);
        }
        catch (IOException e) {
            e.printStackTrace ();
        }

        inicializarVentana ();

        paintVentana (0);

        signUp.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                paintVentana (1);
            }

        });
        signUpGoogle.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                signUp (CredType.Google);
            }

        });
        signUpMeta.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                signUp (CredType.Meta);
            }

        });
        login.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                paintVentana (0);
            }

        });
        loginGoogle.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                login (CredType.Google);
                frame.dispose();
            }

        });
        loginMeta.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                login (CredType.Meta);
                frame.dispose();
            }

        });
    }

    // estado de la ventana
    // 0 = ventana de Login
    // 1 = venatna de SignUp
    void paintVentana (int estado) {

        ArrayList <JPanel> arrayDePaneles = new ArrayList <JPanel> ();
        JPanel panelScroll = new JPanel (new GridLayout (arrayDePaneles.size (), 1, 0, 10));
        JScrollPane scrollPane = new JScrollPane ();

        panelPrincipal.removeAll ();
        panelBotonesLogin.removeAll ();
        panelBotonesSignUp.removeAll ();
        scrollPane.getViewport ().removeAll ();

        panelPrincipal.setLayout (null);
        panelPrincipal.setBounds (50, 0, 500, 500);
        scrollPane.setBounds (50, 25, 450, 200);
        scrollPane.getVerticalScrollBar ().setUnitIncrement (16);

        switch (estado) {
            case 0:

                panelBotonesLogin.add (loginMeta);
                panelBotonesLogin.add (iconoMeta);
                panelBotonesLogin.add (loginGoogle);
                panelBotonesLogin.add (iconoGoogle);
                panelBotonesSignUp.add (signUp);

                panelUser.setBounds (panelPrincipal.getX (), panelPrincipal.getY () + 100, panelPrincipal.getWidth (),
                        50);
                panelPassword.setBounds (panelPrincipal.getX (), panelUser.getY () + panelUser.getHeight (),
                        panelPrincipal.getWidth (), 50);
                panelBotonesLogin.setBounds (panelPrincipal.getX (),
                        panelPassword.getY () + panelPassword.getHeight () * 2, panelPrincipal.getWidth (), 50);
                panelBotonesSignUp.setBounds (panelPrincipal.getX (),
                        panelBotonesLogin.getY () + panelBotonesLogin.getHeight (), panelPrincipal.getWidth (), 50);

                panelPrincipal.add (panelUser);
                panelPrincipal.add (panelPassword);
                panelPrincipal.add (panelBotonesLogin);
                panelPrincipal.add (panelBotonesSignUp);

                panelPrincipal.revalidate ();
                panelPrincipal.repaint ();
                scrollPane.getViewport ().revalidate ();
                scrollPane.getViewport ().repaint ();

                frame.add (panelPrincipal);
                break;
            case 1:

                panelBotonesLogin.add (signUpMeta);
                panelBotonesLogin.add (iconoMeta);
                panelBotonesLogin.add (signUpGoogle);
                panelBotonesLogin.add (iconoGoogle);
                panelBotonesSignUp.add (login);

                arrayDePaneles.add (panelUser);
                arrayDePaneles.add (panelPassword);
                arrayDePaneles.add (panelName);
                arrayDePaneles.add (panelBirthDate);
                arrayDePaneles.add (panelWeight);
                arrayDePaneles.add (panelHeight);
                arrayDePaneles.add (panelMaximunHeartRate);
                arrayDePaneles.add (panelRestingHeartRate);

                generateScrollPane (scrollPane, arrayDePaneles, panelScroll);
                panelBotonesLogin.setBounds (panelPrincipal.getX (), 250, panelPrincipal.getWidth (), 50);
                panelBotonesSignUp.setBounds (panelPrincipal.getX (), 300, panelPrincipal.getWidth (), 50);

                panelPrincipal.add (scrollPane);
                panelPrincipal.add (panelBotonesLogin);
                panelPrincipal.add (panelBotonesSignUp);

                panelPrincipal.revalidate ();
                panelPrincipal.repaint ();

                frame.add (panelPrincipal);
                break;
        }
    }

    void inicializarVentana () {

        userLabel.setPreferredSize (new Dimension (75, 25));
        userText.setPreferredSize (new Dimension (50, 25));
        passwordLabel.setPreferredSize (new Dimension (75, 25));
        passwordText.setPreferredSize (new Dimension (50, 25));
        nameLabel.setPreferredSize (new Dimension (75, 25));
        nameText.setPreferredSize (new Dimension (50, 25));
        birthDateLabel.setPreferredSize (new Dimension (75, 25));
        birthDateText.setPreferredSize (new Dimension (100, 25));
        weightLabel.setPreferredSize (new Dimension (175, 25));
        weightSpinner.setPreferredSize (new Dimension (50, 25));
        heightLabel.setPreferredSize (new Dimension (175, 25));
        heightSpinner.setPreferredSize (new Dimension (50, 25));
        maximunHeartRateLabel.setPreferredSize (new Dimension (175, 25));
        maximunHeartRateSpinner.setPreferredSize (new Dimension (50, 25));
        restingHeartRateLabel.setPreferredSize (new Dimension (175, 25));
        restingHeartRateSpinner.setPreferredSize (new Dimension (50, 25));

        login.setPreferredSize (new Dimension (150, 25));
        loginMeta.setPreferredSize (new Dimension (150, 25));
        loginGoogle.setPreferredSize (new Dimension (150, 25));
        signUp.setPreferredSize (new Dimension (150, 25));
        signUpMeta.setPreferredSize (new Dimension (170, 25));
        signUpGoogle.setPreferredSize (new Dimension (150, 25));

        panelUser.add (userLabel);
        panelUser.add (userText);
        panelPassword.add (passwordLabel);
        panelPassword.add (passwordText);
        panelName.add (nameLabel);
        panelName.add (nameText);
        panelBirthDate.add (birthDateLabel);
        panelBirthDate.add (birthDateText);
        panelWeight.add (weightLabel);
        panelWeight.add (weightSpinner);
        panelHeight.add (heightLabel);
        panelHeight.add (heightSpinner);
        panelMaximunHeartRate.add (maximunHeartRateLabel);
        panelMaximunHeartRate.add (maximunHeartRateSpinner);
        panelRestingHeartRate.add (restingHeartRateLabel);
        panelRestingHeartRate.add (restingHeartRateSpinner);
    }

    void signUp (CredType type) {
        Pair <Integer, Integer> HeartRate = new Pair <> ((Integer) maximunHeartRateSpinner.getValue (),
                (Integer) restingHeartRateSpinner.getValue ());
        UserData data = null;
        BigDecimal w;
        Integer h;
        if ((Integer) weightSpinner.getValue () == 0) {
            w = null;
        }
        else {

            w = BigDecimal.valueOf (Long.valueOf((Integer)weightSpinner.getValue()));
        }
        if ((Integer) heightSpinner.getValue () == 0) {
            h = null;
        }
        else {
            h = (Integer) heightSpinner.getValue ();
        }
        System.out.println (birthDateText.getText ());
        try {
            Date birth = formatter.parse (birthDateText.getText ());

            data = new UserData (userText.getText (), birth, w, h, HeartRate);
        }
        catch (ParseException e) {
            Logger.getLogger ().severe ("Birthdate cant be parse: " + e.getMessage ());
        }
        UserCredentials creds = new UserCredentials (type, userText.getText (),
                new String (passwordText.getPassword ()));
        boolean res = this.authController.signUp (creds, data);
        Logger.getLogger ().info ("sign up result: " + res);
    }

    void login (CredType type) {
        UserCredentials creds = new UserCredentials (type, userText.getText (),
                new String (passwordText.getPassword ()));
        boolean res = this.authController.login (creds);
        Logger.getLogger ().info ("login result: " + res);
        if (res) {
        	MainController mc = new MainController(serviceLocator, authController.getToken());
        	SwingUtilities.invokeLater (() -> new MainWindow (mc, serviceLocator));
        }
    }

    JScrollPane generateScrollPane (JScrollPane scroll, ArrayList <JPanel> arrayDePaneles, JPanel panel) {
        scroll.setViewportView (panel);
        arrayDePaneles.get (0).setBounds (scroll.getX (), scroll.getY (), scroll.getWidth () - 50, 50);
        ;
        panel.add (arrayDePaneles.get (0));
        for (int i = 1; i < arrayDePaneles.size (); i++) {
            arrayDePaneles.get (i).setBounds (scroll.getX (),
                    arrayDePaneles.get (i - 1).getY () + arrayDePaneles.get (i - 1).getHeight (), scroll.getWidth (),
                    100);
            panel.add (arrayDePaneles.get (i));
        }

        return scroll;
    }
}
