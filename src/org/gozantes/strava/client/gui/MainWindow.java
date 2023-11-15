package org.gozantes.strava.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.session.Session;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame{
    private JPanel pArriba = new JPanel(new GridLayout(2,1));
    private JPanel pTodo= new JPanel();
    private JPanel pNorte= new JPanel();
    private JPanel pCentro2 = new JPanel(), pSur= new JPanel();
    private JPanel pLogout= new JPanel(new FlowLayout(FlowLayout.LEFT));

    private JButton botonChallenge = new JButton("Challenge");
    private JButton botonSession = new JButton("Sesi�n");
    private JButton logout = new JButton("Logout");
    private JScrollPane jsp = new JScrollPane();

    private Challenge cselected;
    private List<Challenge> acceptedChallenges = new ArrayList<Challenge>();
    private List<Session> acceptedSessionThem = new ArrayList<Session>();
    private List<Session> acceptedSession = new ArrayList<Session>();

    public MainWindow(User user){
        super();
        setBounds(300, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pTodo=new JPanel(new BorderLayout(0, 1));
        pNorte.setBackground(new Color(255, 255, 255));
        pCentro2.setBackground(new Color(255, 255, 255));
        pSur.setBackground(new Color(255, 255, 255));
        pTodo.add(pArriba, BorderLayout.NORTH);

        pNorte.add(botonChallenge);
        botonChallenge.setFont(new Font("Tahoma", Font.BOLD, 10));

        logout.setPreferredSize(new Dimension(100, 20));
        pLogout.setBackground(new Color(255, 255, 255));
        pLogout.add(logout);
        pArriba.add(pLogout);
        pArriba.add(pNorte);

        pNorte.add(botonSession);
        botonSession.setFont(new Font("Tahoma", Font.BOLD, 10));
        pTodo.add(pCentro2, BorderLayout.CENTER);
        pTodo.add(pSur, BorderLayout.SOUTH);

        botonChallenge.setPreferredSize(new Dimension(150, 25));
        botonSession.setPreferredSize(new Dimension(150, 25));

        botonChallenge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintVentana(0);
            }
        });

        botonSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintVentana(1);
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AuthWindow();
                dispose();
            }
        });

        getContentPane().add(pTodo);
        setVisible(true);
    }

    void paintVentana(int estado) {

        pCentro2.removeAll();
        pSur.removeAll();

        switch(estado) {
            case 0:
                ventanaCreateChallenge();

                break;
            case 1:
                pCentro2.removeAll();
                pSur.removeAll();
                JPanel jp = new JPanel(new GridLayout(7,1));
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

                JButton boton1=new JButton("Crear");

                JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JPanel jp7 = new JPanel(new FlowLayout(FlowLayout.CENTER));

                jp1.add(etiqueta1);jp1.add(campoTexto1);
                jp2.add(etiqueta6);jp2.add(campoTexto6);
                jp3.add(etiqueta3);jp3.add(campoTexto3);
                jp4.add(etiqueta4);jp4.add(campoTexto4);
                jp5.add(etiqueta5);jp5.add(campoTexto5);
                jp6.add(etiqueta2);jp6.add(campoTexto2);
                jp7.add(boton1);

                jp.add(jp1);jp.add(jp2);jp.add(jp3);jp.add(jp4);jp.add(jp5);jp.add(jp6);jp.add(jp7);

                JButton boton3 = new JButton("Crear sesi�n");
                boton3.setPreferredSize(new Dimension(110, 20));
                pSur.add(boton3);
                JButton boton4 = new JButton("Consultar sesi�n");
                boton4.setPreferredSize(new Dimension(130, 20));
                pSur.add(boton4);
                boton4.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        consultarSession(jp6, jsp, boton3, boton4);
                    }
                });

                boton3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paintVentana(1);
                    }
                });
                boton1.addActionListener(new ActionListener() {
                    Sport sp=(Sport)campoTexto2.getSelectedItem();
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //acceptedSession.add(new Session(campoTexto1.getText(),sp,Double.parseDouble(campoTexto5.getText()),new Date(campoTexto6.getText()),new Time(Long.parseLong(campoTexto4.getText())),Integer.parseInt(etiqueta3.getText())));
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
        JPanel jpan = new JPanel(new GridLayout(0,1));
        JScrollPane jsp2 = new JScrollPane();
        jsp2.setViewportView(jpan);
        JLabel abel1 = new JLabel("T�tulo:");
        JTextField abelTexto1 = new JTextField(15);
        JLabel abel2 = new JLabel("Fecha Inicio:");
        JTextField abelTexto2 = new JTextField(15);
        JLabel abel3 = new JLabel("Fecha Final:");
        JTextField abelTexto3 = new JTextField(15);
        JLabel jlkm=new JLabel("Elija unidad");
        JComboBox<String>kmorseg=new JComboBox<>();
        kmorseg.addItem("Distancia(km)");
        kmorseg.addItem("Tiempo(sec)");
        JLabel abel4 = new JLabel("Objetivo(km or s):");
        JTextField abelTexto4 = new JTextField(15);
        JLabel abel5 = new JLabel("Deporte:");
        JComboBox<Sport> abelTexto5 = new JComboBox<>();
        abelTexto5.addItem(Sport.Cyclism);
        abelTexto5.addItem(Sport.Running);
        JButton bunda=new JButton("Crear");
        bunda.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sport[] sports=new Sport[2];
                sports[1]=(Sport) abelTexto5.getSelectedItem();
                if(kmorseg.getSelectedItem().equals("Distancia(km)")) {
                    //Challenge ch=new Challenge(abelTexto1.getText(), new Date(abelTexto2.getText()), new Date(abelTexto3.getText()), Integer.parseInt(abelTexto4.getText()),sports );
                }
                if(kmorseg.getSelectedItem().equals("Tiempo(sec)")) {
                    //Challenge ch=new Challenge(abelTexto1.getText(), new Date(abelTexto2.getText()), new Date(abelTexto3.getText()), Double.parseDouble(abelTexto4.getText()),sports );
                }
            }
        });

        JPanel jpan1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpan6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jkm = new JPanel(new FlowLayout(FlowLayout.CENTER));

        jpan1.add(abel1);jpan1.add(abelTexto1);
        jpan2.add(abel2);jpan2.add(abelTexto2);
        jpan3.add(abel3);jpan3.add(abelTexto3);
        jpan4.add(abel4);jpan4.add(abelTexto4);
        jpan5.add(abel5);jpan5.add(abelTexto5);
        jpan6.add(bunda);jkm.add(jlkm);jkm.add(kmorseg);

        jpan.add(jpan1);jpan.add(jpan2);
        jpan.add(jpan3);;jpan.add(jpan4);jpan.add(jkm);
        jpan.add(jpan5);jpan.add(jpan6);

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
                ventanaGetChallenges(jpan,jsp2,boton2,boton5,boton6,boton7);
            }
        });
        boton6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptedChallenges.add(cselected);
            }
        });
        boton7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaAcceptedChallenges(jpan,jsp2,boton2,boton5,boton6,boton7);
            }
        });
        pCentro2.add(jsp2);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }
    public void ventanaGetChallenges(JPanel jpan,JScrollPane jsp2,JButton boton2,JButton boton5,JButton boton6,JButton boton7) {

        List<Challenge> activeChallenges = new ArrayList<Challenge>();
        pCentro2.removeAll();
        pSur.removeAll();

        JList<Challenge>lista=new JList<>();
        jsp2=new JScrollPane(lista);

        lista.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    Challenge sChallenge = lista.getSelectedValue();
                    if(sChallenge!=null) {
                        cselected=sChallenge;
                    }
                }

            }
        });

        pSur.add(boton2);pSur.add(boton5);
        pSur.add(boton6);pSur.add(boton7);
        pCentro2.add(jsp2);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }

    public void ventanaAcceptedChallenges(JPanel jpan,JScrollPane jsp2,JButton boton2,JButton boton5,JButton boton6,JButton boton7) {

        pCentro2.removeAll();
        pSur.removeAll();
        DefaultListModel<Challenge> lmd=new DefaultListModel<>();
        for (Challenge c : acceptedChallenges) {
            lmd.addElement(c);
        }
        JList<Challenge>lista=new JList<>(lmd);

        jsp2=new JScrollPane(lista);

        pSur.add(boton2);pSur.add(boton5);
        pSur.add(boton6);pSur.add(boton7);
        pCentro2.add(jsp2);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }

    public void consultarSession(JPanel jp,JScrollPane jsp,JButton boton3,JButton boton4) {

        List<Session> Sessiones = new ArrayList<>();

        pCentro2.removeAll();
        pSur.removeAll();

        DefaultListModel<Session>dlm2=new DefaultListModel<Session>();
        for (Session s : acceptedSession) {
            dlm2.addElement(s);
        }


        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Tu Usuario");
        comboBox.addItem("Otros usuarios");



        JList<Session>lista=new JList<>(dlm2);
        jsp=new JScrollPane(lista);
        pCentro2.add(comboBox);
        pCentro2.add(jsp);

        comboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedOption = (String) comboBox.getSelectedItem();
                    if(selectedOption.equals("Tu Usuario")) {
                        mySession(dlm2,comboBox);

                    }
                    if(selectedOption.equals("Otros usuarios")) {
                        theirSession(dlm2,comboBox);

                    }
                }

            }
        });
        pSur.add(boton3);pSur.add(boton4);
        pCentro2.revalidate();
        pCentro2.repaint();
        pSur.revalidate();
        pSur.repaint();
    }
    public void mySession(DefaultListModel<Session> dlm2,JComboBox<String> comboBox) {
        pCentro2.removeAll();
        dlm2.clear();
        for (Session s : acceptedSession) {
            dlm2.addElement(s);
        }
        JList<Session>lista=new JList<>(dlm2);
        pCentro2.add(comboBox);
        jsp=new JScrollPane(lista);
        pCentro2.add(jsp);
        pCentro2.revalidate();
        pCentro2.repaint();
    }
    public void theirSession(DefaultListModel<Session> dlm2,JComboBox<String> comboBox) {
        pCentro2.removeAll();
        dlm2.clear();
        for (Session s : acceptedSessionThem) {
            dlm2.addElement(s);
        }
        JList<Session>lista=new JList<>(dlm2);
        pCentro2.add(comboBox);
        jsp=new JScrollPane(lista);
        pCentro2.add(jsp);
        pCentro2.revalidate();
        pCentro2.repaint();
    }
}