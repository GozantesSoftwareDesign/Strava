package org.gozantes.strava.client.gui;

import javax.swing.JFrame;


//Import statements...
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import org.gozantes.strava.client.controller.AuthController;
import org.gozantes.strava.client.controller.MainController;
import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.challenge.DistanceChallenge;
import org.gozantes.strava.server.data.domain.challenge.TimeChallenge;
import org.gozantes.strava.server.data.domain.session.Session;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.dto.ChallengeDTO;
import org.gozantes.strava.server.data.dto.SessionDTO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainWindow2 extends JFrame {

 // Instance variables...
 private JFrame frame = new JFrame("Usuario");
 private JPanel pPrincipal = new JPanel(new BorderLayout(0, 1));
 private JPanel pHeader = new JPanel(new GridLayout(2, 1));
 private JPanel pNorte = new JPanel();
 private JPanel pCentro = new JPanel(), pSur = new JPanel();
 private JPanel pLogout = new JPanel(new FlowLayout(FlowLayout.LEFT));
 private JPanel pScrollPane = new JPanel(new GridLayout(7, 1));
 private JButton botonChallenge = new JButton("Challenge");
 private JButton botonSession = new JButton("Sesión");
 private JButton botonLogout = new JButton("Logout");
 private JButton Crear = new JButton("Crear");
 private JButton botonCrearSesion = new JButton("Crear sesión");
 private JButton botonConsultarSesion = new JButton("Consultar sesión");
 private JLabel tituloLabel = new JLabel("Titulo:");
 private JTextField tituloText = new JTextField(15);
 private JLabel deporteLabel = new JLabel("Deporte:");
 private JComboBox<Sport> deporteBox = new JComboBox<>();
 private JLabel distanciaLabel = new JLabel("Distancia (km):");
 private JTextField distanciaTexto = new JTextField(15);
 private JLabel fInicioLabel = new JLabel("Fecha de inicio:");
 private JTextField fInicioText = new JTextField(15);
 private JLabel hInicioLabel = new JLabel("Hora de inicio:");
 private JTextField hInicioText = new JTextField(15);
 private JLabel DuracionLabel = new JLabel("Duracion:");
 private JTextField DuracionText = new JTextField(15);
 private JScrollPane ScrollPane = new JScrollPane();
 private JComboBox<Sport> abelTexto5 = new JComboBox<>();
 private JComboBox<String> kmorseg = new JComboBox<>();
 private MainController mainController;
 private ServiceLocator serviceLocator;
 private ChallengeDTO cselected;
 private List<ChallengeDTO> activeChallenges;
 private List<SessionDTO> acceptedSessionThem;
 private List<SessionDTO> acceptedSession;

 public MainWindow2(MainController mainController, ServiceLocator serviceLocator) {
     super();
     this.mainController = mainController;
     this.serviceLocator = serviceLocator;
     this.activeChallenges = this.mainController.getActiveChallenges();
     this.acceptedSessionThem = this.mainController.searchSessions(new SessionFilters(null));
     this.acceptedSession = this.mainController.getSessions();

     frame.setSize(1000, 800);
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setVisible(true);
     frame.setLocationRelativeTo(null);

     initializeWindow();
     setListeners();
     
     frame.add(pPrincipal);
 }

 private void initializeWindow() {
     // Initialization logic...
     initializeHeaderPanels();
     initializeSessionPanels();
 }

 private void setListeners() {
     // Listener setup...
     botonChallenge.addActionListener(e -> paintWindow(0));
     botonSession.addActionListener(e -> paintWindow(1));
     botonLogout.addActionListener(e -> {
         try {
             AuthController ac = new AuthController(serviceLocator);
             SwingUtilities.invokeLater(() -> new AuthWindow(ac, serviceLocator));
         } catch (URISyntaxException | NoSuchAlgorithmException ex) {
             throw new RuntimeException(ex);
         }
         mainController.logout();
         frame.dispose();
     });
 }

 private void paintWindow(int state) {
     // Painting logic...
     pCentro.removeAll();
     pSur.removeAll();
     switch (state) {
         case 0:
             createChallengeWindow();
             break;
         case 1:
             pCentro.removeAll();
             pSur.removeAll();
             pSur.add(botonCrearSesion);
             pSur.add(botonConsultarSesion);
             //botonConsultarSesion.addActionListener(e -> consultarSession());
             botonCrearSesion.addActionListener(e -> paintWindow(1));
             Crear.addActionListener(e -> {
                 // Session creation logic...
                 paintWindow(1);
             });
             pCentro.add(ScrollPane);
             pCentro.revalidate();
             pCentro.repaint();
             pSur.revalidate();
             pSur.repaint();
             break;
     }
 }

 private void createChallengeWindow() {
     // Challenge creation logic...
     pCentro.removeAll();
     pSur.removeAll();
     JPanel jpan = new JPanel(new GridLayout(0, 1));
     JScrollPane jsp2 = new JScrollPane();
     jsp2.setViewportView(jpan);
     JLabel abel1 = new JLabel("Título:");
     JTextField abelTexto1 = new JTextField(15);
     // ... (similar for other components)
     JButton bunda = new JButton("Crear");
     bunda.addActionListener(e -> {
         // Challenge creation logic...
     });

     JPanel jpan1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
     // ... (similar for other panels)
     JPanel jkm = new JPanel(new FlowLayout(FlowLayout.CENTER));

     jpan.add(jpan1);
     //jpan.add(jpan2);
     //jpan.add(jpan3);
     // ... (similar for other panels)
     jpan.add(jkm);

     JButton boton2 = new JButton("Crear reto");
     boton2.setPreferredSize(new Dimension(100, 20));
     pSur.add(boton2);
     JButton boton5 = new JButton("Obtener retos activos");
     boton5.setPreferredSize(new Dimension(160, 20));
     pSur.add(boton5);
     JButton boton6 = new JButton("Aceptar reto");
     boton6.setPreferredSize(new Dimension(110, 20));
     pSur.add(boton6);
     JButton boton7 = new JButton("Comprobar aceptados");
     boton7.setPreferredSize(new Dimension(170, 20));
     pSur.add(boton7);
     jsp2.addMouseListener(null);

     boton2.addActionListener(e -> paintWindow(0));
     boton5.addActionListener(e -> getSessionChallenges(jpan, jsp2, boton2, boton5, boton6, boton7));
     boton6.addActionListener(e -> acceptChallenge());
     boton7.addActionListener(e -> acceptedChallengesWindow(jpan, jsp2, boton2, boton5, boton6, boton7));
     pCentro.add(jsp2);
     pCentro.revalidate();
     pCentro.repaint();
     pSur.revalidate();
     pSur.repaint();
 }

 private void getSessionChallenges(JPanel panel, JScrollPane scrollPane, JButton... buttons) {
     // Get session challenges logic...
     List<ChallengeDTO> activeChallenges = mainController.getActiveChallenges();
     pCentro.removeAll();
     pSur.removeAll();

     JList<ChallengeDTO> lista = new JList<>();
     scrollPane = new JScrollPane(lista);

     lista.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
             ChallengeDTO sChallenge = lista.getSelectedValue();
             if (sChallenge != null) {
                 cselected = sChallenge;
             }
         }
     });

     pSur.add(buttons[0]);
     pSur.add(buttons[1]);
     pSur.add(buttons[2]);
     pSur.add(buttons[3]);
     pCentro.add(scrollPane);
     pCentro.revalidate();
     pCentro.repaint();
     pSur.revalidate();
     pSur.repaint();
 }

 private void acceptChallenge() {
     // Accept challenge logic...
     mainController.acceptChallenge(cselected.id());
 }

 private void acceptedChallengesWindow(JPanel panel, JScrollPane scrollPane, JButton... buttons) {
     // Accepted challenges window logic...
     pCentro.removeAll();
     pSur.removeAll();
     DefaultListModel<ChallengeDTO> lmd = new DefaultListModel<>();
     for (ChallengeDTO c : activeChallenges) {
         lmd.addElement(c);
     }
     JList<ChallengeDTO> lista = new JList<>(lmd);

     scrollPane = new JScrollPane(lista);

     pSur.add(buttons[0]);
     pSur.add(buttons[1]);
     pSur.add(buttons[2]);
     pSur.add(buttons[3]);
     pCentro.add(scrollPane);
     pCentro.revalidate();
     pCentro.repaint();
     pSur.revalidate();
     pSur.repaint();
 }

 private void consultSession() {
     // Session consultation logic...
     pCentro.removeAll();
     pSur.removeAll();

     DefaultListModel<SessionDTO> dlm2 = new DefaultListModel<>();
     for (SessionDTO s : acceptedSession) {
         dlm2.addElement(s);
     }

     JComboBox<String> comboBox = new JComboBox<>();
     comboBox.addItem("Tu Usuario");
     comboBox.addItem("Otros usuarios");

     JList<SessionDTO> lista = new JList<>(dlm2);
     ScrollPane = new JScrollPane(lista);
     pCentro.add(comboBox);
     pCentro.add(ScrollPane);

     comboBox.addItemListener(e -> {
         if (e.getStateChange() == ItemEvent.SELECTED) {
             String selectedOption = (String) comboBox.getSelectedItem();
             if (selectedOption.equals("Tu Usuario")) {
                 mySession(dlm2, comboBox);
             }
             if (selectedOption.equals("Otros usuarios")) {
                 theirSession(dlm2, comboBox);
             }
         }
     });
     pSur.add(botonCrearSesion);
     pSur.add(botonConsultarSesion);
     pCentro.revalidate();
     pCentro.repaint();
     pSur.revalidate();
     pSur.repaint();
 }

 private void mySession(DefaultListModel<SessionDTO> dlm2, JComboBox<String> comboBox) {
     // My session logic...
     pCentro.removeAll();
     dlm2.clear();
     for (SessionDTO s : acceptedSession) {
         dlm2.addElement(s);
     }
     JList<SessionDTO> lista = new JList<>(dlm2);
     pCentro.add(comboBox);
     ScrollPane = new JScrollPane(lista);
     pCentro.add(ScrollPane);
     pCentro.revalidate();
     pCentro.repaint();
 }

 private void theirSession(DefaultListModel<SessionDTO> dlm2, JComboBox<String> comboBox) {
     // Their session logic...
     pCentro.removeAll();
     dlm2.clear();
     for (SessionDTO s : acceptedSessionThem) {
         dlm2.addElement(s);
     }
     JList<SessionDTO> lista = new JList<>(dlm2);
     pCentro.add(comboBox);
     ScrollPane = new JScrollPane(lista);
     pCentro.add(ScrollPane);
     pCentro.revalidate();
     pCentro.repaint();
 }

 private void initializeHeaderPanels() {
     // Header panel initialization...
     pNorte.setBackground(new Color(255, 255, 255));
     pCentro.setBackground(new Color(255, 255, 255));
     pSur.setBackground(new Color(255, 255, 255));
     pLogout.setBackground(new Color(255, 255, 255));

     botonSession.setFont(new Font("Tahoma", Font.BOLD, 10));
     botonChallenge.setFont(new Font("Tahoma", Font.BOLD, 10));

     // ... (similar for other components)
 }

 private void initializeSessionPanels() {
     // Session panel initialization...
     // ... (similar for other components)
 }
}

