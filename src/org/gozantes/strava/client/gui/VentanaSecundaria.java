package gui;

import domain.Challenge;
import domain.Sesion;
import domain.Sport;
import domain.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VentanaSecundaria extends JFrame {

    private JPanel pTodo = new JPanel();
    private JPanel pNorte = new JPanel();
    private JPanel pCentro2 = new JPanel(), pSur = new JPanel();
    private JPanel pLogout = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JButton botonChallenge = new JButton("Challenge");
    private JButton botonSesion = new JButton("Sesi�n");

    public VentanaSecundaria(User user) {

        super();
        setBounds(300, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pTodo = new JPanel(new BorderLayout(0, 1));
        pNorte.setBackground(new Color(255, 255, 255));
        pCentro2.setBackground(new Color(255, 255, 255));
        pSur.setBackground(new Color(255, 255, 255));

        JPanel pArriba = new JPanel(new GridLayout(2, 1));
        pTodo.add(pArriba, BorderLayout.NORTH);

        pNorte.add(botonChallenge);
        botonChallenge.setFont(new Font("Tahoma", Font.BOLD, 10));

        JButton logout = new JButton("Logout");
        logout.setPreferredSize(new Dimension(100, 20));
        pLogout.setBackground(new Color(255, 255, 255));
        pLogout.add(logout);
        pArriba.add(pLogout);
        pArriba.add(pNorte);

        pNorte.add(botonSesion);
        botonSesion.setFont(new Font("Tahoma", Font.BOLD, 10));
        pTodo.add(pCentro2, BorderLayout.CENTER);
        pTodo.add(pSur, BorderLayout.SOUTH);

        botonChallenge.setPreferredSize(new Dimension(150, 25));
        botonSesion.setPreferredSize(new Dimension(150, 25));

        botonChallenge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintVentana(0);
            }
        });

        botonSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintVentana(1);
            }
        });

        getContentPane().add(pTodo);
        setVisible(true);
    }

    void paintVentana(int estado) {

        pCentro2.removeAll();
        pSur.removeAll();

        switch (estado) {
            case 0:
                ventanaCreateChallenge();

                break;
            case 1:
                pCentro2.removeAll();
                pSur.removeAll();
                JPanel jp = new JPanel(new GridLayout(7, 1));
                JScrollPane jsp = new JScrollPane();
                jsp.setViewportView(jp);

                JLabel etiqueta1 = new JLabel("T�tulo:");
                JTextField campoTexto1 = new JTextField(15);

                JLabel etiqueta2 = new JLabel("Deporte:");
                JComboBox<Sport> campoTexto2 = new JComboBox<>();
                campoTexto2.addItem(Sport.Cyclism);
                campoTexto2.addItem(Sport.Running);

                JLabel etiqueta3 = new JLabel("Distancia (km):");
                JTextField campoTexto3 = new JTextField(15);

                JLabel etiqueta4 = new JLabel("Fecha de inicio:");
                JTextField campoTexto4 = new JTextField(15);

                JLabel etiqueta5 = new JLabel("Hora de inicio:");
                JTextField campoTexto5 = new JTextField(15);

                JLabel etiqueta6 = new JLabel("Duraci�n:");
                JTextField campoTexto6 = new JTextField(15);

                JButton boton1 = new JButton("Crear");

                JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp7 = new JPanel(new FlowLayout(FlowLayout.CENTER));

                jp1.add(etiqueta1);
                jp1.add(campoTexto1);
                jp2.add(etiqueta6);
                jp2.add(campoTexto6);
                jp3.add(etiqueta3);
                jp3.add(campoTexto3);
                jp4.add(etiqueta4);
                jp4.add(campoTexto4);
                jp5.add(etiqueta5);
                jp5.add(campoTexto5);
                jp6.add(etiqueta2);
                jp6.add(campoTexto2);
                jp7.add(boton1);

                jp.add(jp1);
                jp.add(jp2);
                jp.add(jp3);
                jp.add(jp4);
                jp.add(jp5);
                jp.add(jp6);
                jp.add(jp7);

                JButton boton3 = new JButton("Crear sesi�n");
                boton3.setPreferredSize(new Dimension(110, 20));
                pSur.add(boton3);
                JButton boton4 = new JButton("Consultar sesi�n");
                boton4.setPreferredSize(new Dimension(130, 20));
                pSur.add(boton4);
                boton4.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        consultarSesion(jp6, jsp, boton3, boton4);
                    }
                });

                boton3.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paintVentana(1);
                    }
                });

                pCentro2.add(jsp);
                pCentro2.revalidate();
                pCentro2.repaint();
                pSur.revalidate();
                pSur.repaint();
                break;
        }
    }

    public void ventanaCreateChallenge() {
        pCentro2.removeAll();
        pSur.removeAll();
        JPanel jpan = new JPanel(new GridLayout(0, 1));
        JScrollPane jsp2 = new JScrollPane();
        jsp2.setViewportView(jpan);
        JLabel abel1 = new JLabel("T�tulo:");
        JTextField abelTexto1 = new JTextField(15);
        JLabel abel2 = new JLabel("Fecha Inicio:");
        JTextField abelTexto2 = new JTextField(15);
        JLabel abel3 = new JLabel("Fecha Final:");
        JTextField abelTexto3 = new JTextField(15);
        JLabel abel4 = new JLabel("Objetivo(km or s):");
        JTextField abelTexto4 = new JTextField(15);
        JLabel abel5 = new JLabel("Deporte:");
        JComboBox<Sport> abelTexto5 = new JComboBox<>();
        abelTexto5.addItem(Sport.Cyclism);
        abelTexto5.addItem(Sport.Running);
        JButton bunda = new JButton("Crear");
        bunda.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        JPanel jpan1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan6 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        jpan1.add(abel1);
        jpan1.add(abelTexto1);
        jpan2.add(abel2);
        jpan2.add(abelTexto2);
        jpan3.add(abel3);
        jpan3.add(abelTexto3);
        jpan4.add(abel4);
        jpan4.add(abelTexto4);
        jpan5.add(abel5);
        jpan5.add(abelTexto5);
        jpan6.add(bunda);

        jpan.add(jpan1);
        jpan.add(jpan2);
        jpan.add(jpan3);
        jpan.add(jpan4);
        jpan.add(jpan5);
        jpan.add(jpan6);

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

        boton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaCreateChallenge();

            }
        });
        boton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaGetChallenges(jpan, jsp2, boton2, boton5, boton6, boton7);
            }
        });
        boton6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaAcceptChallenge(jpan6, jsp2, boton2, boton5, boton6, boton7);
            }
        });
        boton7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaAcceptedChallenges(jpan, jsp2, boton2, boton5, boton6, boton7);
            }
        });
        pCentro2.add(jsp2);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }

    public void ventanaGetChallenges(JPanel jpan, JScrollPane jsp2, JButton boton2, JButton boton5, JButton boton6, JButton boton7) {

        List<Challenge> activeChallenges = new ArrayList<Challenge>();

        pCentro2.removeAll();
        pSur.removeAll();

        JList<Challenge> lista = new JList<>();
        jsp2 = new JScrollPane(lista);

        pSur.add(boton2);
        pSur.add(boton5);
        pSur.add(boton6);
        pSur.add(boton7);
        pCentro2.add(jsp2);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }

    public void ventanaAcceptChallenge(JPanel jpan, JScrollPane jsp2, JButton boton2, JButton boton5, JButton boton6, JButton boton7) {

        List<Challenge> challenges = new ArrayList<Challenge>();

        pCentro2.removeAll();
        pSur.removeAll();

        JList<Challenge> lista = new JList<>();
        jsp2 = new JScrollPane(lista);

        JButton boton9 = new JButton("Aceptar");
        pCentro2.add(boton9);

        pSur.add(boton2);
        pSur.add(boton5);
        pSur.add(boton6);
        pSur.add(boton7);
        pCentro2.add(jsp2);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }

    public void ventanaAcceptedChallenges(JPanel jpan, JScrollPane jsp2, JButton boton2, JButton boton5, JButton boton6, JButton boton7) {

        List<Challenge> acceptedChallenges = new ArrayList<Challenge>();

        pCentro2.removeAll();
        pSur.removeAll();

        JList<Challenge> lista = new JList<>();
        jsp2 = new JScrollPane(lista);

        pSur.add(boton2);
        pSur.add(boton5);
        pSur.add(boton6);
        pSur.add(boton7);
        pCentro2.add(jsp2);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }

    public void consultarSesion(JPanel jp, JScrollPane jsp, JButton boton3, JButton boton4) {

        List<Sesion> sesiones = new ArrayList<>();

        pCentro2.removeAll();
        pSur.removeAll();

        JList<Challenge> lista = new JList<>();
        jsp = new JScrollPane(lista);


        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Tu Usuario");
        comboBox.addItem("Otros usuarios");

        pCentro2.add(comboBox);

        pSur.add(boton3);
        pSur.add(boton4);
        pCentro2.add(jsp);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }
}