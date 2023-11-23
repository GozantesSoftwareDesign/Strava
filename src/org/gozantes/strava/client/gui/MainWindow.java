package org.gozantes.strava.client.gui;

import java.io.Serial;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import org.gozantes.strava.client.controller.AuthController;
import org.gozantes.strava.client.controller.MainController;
import org.gozantes.strava.client.remote.ServiceLocator;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.challenge.Challenge;
import org.gozantes.strava.server.data.domain.challenge.ChallengeFilters;
import org.gozantes.strava.server.data.domain.challenge.DistanceChallenge;
import org.gozantes.strava.server.data.domain.challenge.TimeChallenge;
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

public class MainWindow extends JFrame {

	@Serial
    private static final long serialVersionUID = 1L;
	
	private JFrame frame = new JFrame ("Usuario");
	
	private JPanel pPrincipal = new JPanel (new BorderLayout (0, 1));
	private JPanel pHeader = new JPanel (new GridLayout (2, 1));
    private JPanel pNorte = new JPanel ();
    private JPanel pCentro = new JPanel (), pSur = new JPanel ();
    private JPanel pLogout = new JPanel (new FlowLayout (FlowLayout.LEFT));
    private JPanel pScrollPane = new JPanel (new GridLayout (0, 1));
    
    private JPanel pTitulo = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pDuracion = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pDistancia = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pFInicio = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pHInicio = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pFFinal = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pObjetivo = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pDeporte = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pUnidad = new JPanel (new FlowLayout (FlowLayout.CENTER));
    private JPanel pCrear = new JPanel (new FlowLayout (FlowLayout.CENTER));

    private JButton botonChallenge = new JButton ("Challenge");
    private JButton botonSession = new JButton ("Sesión");
    private JButton botonLogout = new JButton ("Logout");
    private JButton Crear = new JButton ("Crear");
    private JButton botonCrearSesion = new JButton ("Crear sesión");
    private JButton botonConsultarSesion = new JButton ("Consultar sesión");
    private JButton botonCrearReto = new JButton ("Crear reto");
    private JButton botonRetosActivos = new JButton ("Obtener retos activos");
    private JButton botonAceptarReto = new JButton ("Aceptar reto");
    private JButton botonComrpobarAceptados = new JButton ("Comprobar aceptados");
    
    private JLabel tituloLabel = new JLabel ("Titulo:");
    private JTextField tituloText = new JTextField (15);
    private JLabel deporteLabel = new JLabel ("Deporte:");
    private JComboBox <Sport> deporteBox = new JComboBox <> ();
    private JLabel distanciaLabel = new JLabel ("Distancia (km):");
    private JTextField distanciaTexto = new JTextField (15);
    private JLabel fInicioLabel = new JLabel ("Fecha de inicio:");
    private JTextField fInicioText = new JTextField (15);
    private JLabel hInicioLabel = new JLabel ("Hora de inicio:");
    private JTextField hInicioText= new JTextField (15);
    private JLabel DuracionLabel = new JLabel ("Duracion:");
    private JTextField DuracionText = new JTextField (15);
    private JLabel fFinalLabel = new JLabel ("Fecha Final:");
    private JTextField fFinalText = new JTextField (15);
    private JLabel unidadLabel = new JLabel ("Elija unidad");
    private JComboBox <String> kmorseg = new JComboBox <> ();
    private JLabel objetivoLabel = new JLabel ("Objetivo(km or s):");
    private JTextField objetivoText = new JTextField (15);
    
    private JComboBox <String> usuarioBox = new JComboBox <> ();
    
    private JScrollPane scrollPane = new JScrollPane ();

    private MainController mainController;
    private ServiceLocator serviceLocator;
    private ChallengeDTO cselected;
    private List <ChallengeDTO> activeChallenges;
    private List <SessionDTO> acceptedSessionThem;
    private List <SessionDTO> acceptedSession;
    
    private boolean estadoKmorseg;

    public MainWindow (MainController mainController, ServiceLocator serviceLocator) {
        super ();
        this.mainController = mainController;
        this.serviceLocator = serviceLocator;
        this.activeChallenges = this.mainController.getActiveChallenges();
        this.acceptedSessionThem = this.mainController.searchSessions(new SessionFilters(null));
        this.acceptedSession = this.mainController.getSessions();
        
        frame.setSize (1000, 800);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        inicializarVentana();

        botonChallenge.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                paintVentana (0);
            }
        });

        botonSession.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                paintVentana (1);
            }
        });
        botonLogout.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                try {
                	AuthController ac= new AuthController(serviceLocator);
                	SwingUtilities.invokeLater (() -> new AuthWindow (ac, serviceLocator));
                }
                catch (URISyntaxException ex) {
                    throw new RuntimeException (ex);
                }
                catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException (ex);
                }
                mainController.logout();
                frame.dispose ();
            }
        });
        botonConsultarSesion.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                consultarSession ();
            }
        });

        botonCrearSesion.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                paintVentana (1);
            }
        });
        botonCrearReto.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                ventanaCreateChallenge ();

            }
        });
        botonRetosActivos.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                ventanaGetChallenges ();
            }
        });
        botonAceptarReto.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
            	
                mainController.acceptChallenge(cselected.id());
            }
        });
        botonComrpobarAceptados.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                ventanaAcceptedChallenges ();
            }
        });
        
        kmorseg.addItemListener (new ItemListener () {       	

            @Override
            public void itemStateChanged (ItemEvent e) {
                if (e.getStateChange () == ItemEvent.SELECTED) {
                    estadoKmorseg = true;
                }else{
                	estadoKmorseg = false;
                }
                
            }
        });

        frame.add(pPrincipal);
    }
    
    void paintVentana (int estado) {
    	
        switch (estado) {
            case 0:
                ventanaCreateChallenge ();

                break;
            case 1:
            	pCentro.removeAll ();
                pSur.removeAll ();
                pScrollPane.removeAll ();
                
                ArrayList <JPanel> arrayDePaneles = new ArrayList <JPanel> ();
                
                arrayDePaneles.add (pTitulo);
                arrayDePaneles.add (pDuracion);
                arrayDePaneles.add (pDistancia);
                arrayDePaneles.add (pFInicio);
                arrayDePaneles.add (pHInicio);
                arrayDePaneles.add (pDeporte);
                arrayDePaneles.add (pCrear);
                
                this.generateScrollPane(scrollPane, arrayDePaneles, pScrollPane);
                
                pSur.add (botonCrearSesion);
                pSur.add (botonConsultarSesion);
                
                pCentro.add(pScrollPane);
                
                pScrollPane.revalidate ();
                pScrollPane.repaint ();
                pCentro.revalidate ();
                pCentro.repaint ();
                pSur.revalidate ();
                pSur.repaint ();
                
                Crear.addActionListener (new ActionListener () {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                    	String st=tituloText.getText();
                    	Sport sp=(Sport) deporteBox.getSelectedItem();
                    	BigDecimal bd= new BigDecimal(distanciaTexto.getText());
						Duration dr= Duration.ofMinutes(Integer.parseInt(DuracionText.getText()));
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
						Date di = null;
						try {
							di = sdf.parse(fInicioText.getText());
							} catch (ParseException e1) {
								e1.printStackTrace();
								}
						SessionData sessionData=new SessionData(st,sp, bd, di, dr);
                    	mainController.createSession(sessionData);
                        paintVentana (1);
                    }
                });
                break;
                
        }
    }

    public void ventanaCreateChallenge () {
        pCentro.removeAll ();
        pSur.removeAll ();
        pScrollPane.removeAll ();
        
        ArrayList <JPanel> arrayDePaneles = new ArrayList <JPanel> ();
        
        arrayDePaneles.add (pTitulo);
        arrayDePaneles.add (pFInicio);
        arrayDePaneles.add (pFFinal);
        arrayDePaneles.add (pObjetivo);
        arrayDePaneles.add (pUnidad);
        arrayDePaneles.add (pDeporte);
        arrayDePaneles.add (pCrear);
        
        this.generateScrollPane(scrollPane, arrayDePaneles, pScrollPane);
        
        pSur.add (botonCrearReto);
        pSur.add (botonRetosActivos);
        pSur.add (botonComrpobarAceptados);
        
        pCentro.add(pScrollPane);
        
        pScrollPane.revalidate ();
        pScrollPane.repaint ();
        pCentro.revalidate ();
        pCentro.repaint ();
        pSur.revalidate ();
        pSur.repaint ();
        
        Crear.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) { 
            	String ti=tituloText.getText();
            	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            	Date di = null;
				try {
					di = sdf.parse(fInicioText.getText());
				} catch (ParseException e1) {					
					e1.printStackTrace();
				}
            	Date df=null;
				try {
					df = sdf.parse(fFinalText.getText());
				} catch (ParseException e1) {					
					e1.printStackTrace();
				}
            	Pair<Date,Date> lapse=new Pair<Date, Date>(di, df);
            	Sport sp=(Sport) deporteBox.getSelectedItem();            	
            	Challenge ch = null;
            	
            	if (estadoKmorseg) {
            		if(kmorseg.getSelectedObjects().equals(kmorseg.getItemAt(0))) {
            			BigDecimal bd=new BigDecimal(distanciaTexto.getText());
            			try {
							ch= new DistanceChallenge(ti, lapse, sp, null, bd);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
            			mainController.createChallenge(ch);
            		}
            		else if(kmorseg.getSelectedObjects().equals(kmorseg.getItemAt(1))){
            			Duration dr = Duration.ofMinutes(Integer.parseInt(DuracionText.getText()));
            			try {
							ch=new TimeChallenge(ti,lapse,sp,null,dr);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
            		}
            	}else{
            		Logger.getLogger().info("No esta seleccionado la unidad de challenge");
            	}
                paintVentana (0);
            }
        });
    }
    
    public void ventanaGetChallenges () {
    	pCentro.removeAll ();
        
        List <ChallengeDTO> activeChallenges = mainController.searchChallenges(null);       
        DefaultListModel<ChallengeDTO>model=new DefaultListModel<ChallengeDTO>();
        for (ChallengeDTO challengeDTO : activeChallenges) {
			model.addElement(challengeDTO);
		}
        JList <ChallengeDTO> lista = new JList <> (model);
        scrollPane = new JScrollPane (lista);

        lista.addListSelectionListener (new ListSelectionListener () {
            @Override
            public void valueChanged (ListSelectionEvent e) {
                if (!e.getValueIsAdjusting ()) {
                    ChallengeDTO sChallenge = lista.getSelectedValue ();
                    if (sChallenge != null) {
                        cselected = sChallenge;
                    }
                }

            }
        });

        pCentro.add (scrollPane);
        pCentro.revalidate ();
        pCentro.repaint ();
    }

    public void ventanaAcceptedChallenges () {

        pCentro.removeAll ();
        DefaultListModel <ChallengeDTO> lmd = new DefaultListModel <> ();
        for (ChallengeDTO c : activeChallenges) {
            lmd.addElement (c);
        }
        JList <ChallengeDTO> lista = new JList <> (lmd);

        scrollPane = new JScrollPane (lista);

        pCentro.add (scrollPane);
        pCentro.revalidate ();
        pCentro.repaint ();
    }

    public void consultarSession() {
    	pCentro.removeAll();

        DefaultListModel <SessionDTO> dlm2 = new DefaultListModel <SessionDTO> ();
        if(acceptedSession != null) {
        	for (SessionDTO s : acceptedSession) {
                dlm2.addElement(s);
            }
        } else {
        	Logger.getLogger().info("no tienes sesiones personales");
        }

        JList <SessionDTO> lista = new JList <> (dlm2);
        scrollPane = new JScrollPane (lista);
        pCentro.add (usuarioBox);
        pCentro.add (scrollPane);

        usuarioBox.addItemListener (new ItemListener () {

            @Override
            public void itemStateChanged (ItemEvent e) {
                if (e.getStateChange () == ItemEvent.SELECTED) {
                    if (usuarioBox.getSelectedItem().equals (usuarioBox.getItemAt(0))) {
                    	consultarSession();
                    } else if (usuarioBox.getSelectedItem().equals (usuarioBox.getItemAt(1))) {
                        theirSession (dlm2);
                    }
                }
            }
        });
        pCentro.revalidate ();
        pCentro.repaint ();
    }

    public void theirSession (DefaultListModel<SessionDTO> dlm2) {
        pCentro.removeAll ();
        dlm2.removeAllElements();
        if(acceptedSessionThem != null) {
        	for (SessionDTO s : acceptedSessionThem) {
                dlm2.addElement (s);
            }
        } else {
        	Logger.getLogger().info("no hay sesiones publicadas");
        }
        JList<SessionDTO> lista = new JList <> (dlm2);
        pCentro.add (usuarioBox);
        scrollPane = new JScrollPane (lista);
        pCentro.add (scrollPane);
        pCentro.revalidate ();
        pCentro.repaint ();
    }
    private void inicializarVentana() {
    	pNorte.setBackground (new Color (255, 255, 255));
        pCentro.setBackground (new Color (255, 255, 255));
        pSur.setBackground (new Color (255, 255, 255));
        pLogout.setBackground (new Color (255, 255, 255));
        
        botonSession.setFont (new Font ("Tahoma", Font.BOLD, 10));
        botonChallenge.setFont (new Font ("Tahoma", Font.BOLD, 10));
        
        botonLogout.setPreferredSize (new Dimension (100, 20));
        botonChallenge.setPreferredSize (new Dimension (150, 25));
        botonSession.setPreferredSize (new Dimension (150, 25));
        botonConsultarSesion.setPreferredSize (new Dimension (130, 20));
        botonCrearSesion.setPreferredSize (new Dimension (110, 20));
        botonCrearReto.setPreferredSize (new Dimension (100, 20));
        botonRetosActivos.setPreferredSize (new Dimension (160, 20));
        botonAceptarReto.setPreferredSize (new Dimension (110, 20));
        botonComrpobarAceptados.setPreferredSize (new Dimension (170, 20));

        deporteBox.addItem (Sport.Cyclism);
        deporteBox.addItem (Sport.Running);
        kmorseg.addItem ("Distancia(km)");
        kmorseg.addItem ("Tiempo(sec)");
        usuarioBox.addItem ("Tu Usuario");
        usuarioBox.addItem ("Otros usuarios");
        
        
        //Header Panels
        pLogout.add (botonLogout);
        pNorte.add (botonChallenge);
        pNorte.add (botonSession);
        pHeader.add (pLogout);
        pHeader.add (pNorte);
        //Central Panels
        pTitulo.add (tituloLabel);
        pTitulo.add (tituloText);
        pDuracion.add (DuracionLabel);
        pDuracion.add (DuracionText);
        pDistancia.add (distanciaLabel);
        pDistancia.add (distanciaTexto);
        pFInicio.add (fInicioLabel);
        pFInicio.add (fInicioText);
        pHInicio.add (hInicioLabel);
        pHInicio.add (hInicioText);
        pFFinal.add (fFinalLabel);
        pFFinal.add (fFinalText);
        pObjetivo.add (objetivoLabel);
        pObjetivo.add (objetivoText);
        pUnidad.add (unidadLabel);
        pUnidad.add (kmorseg);
        pDeporte.add (deporteLabel);
        pDeporte.add (deporteBox);
        pCrear.add (Crear);
        //Sesion scroll Pane
        //Challenge scroll Pane
        
        pPrincipal.add(pHeader, BorderLayout.NORTH);
        pPrincipal.add(pSur, BorderLayout.SOUTH);
        pPrincipal.add(pCentro, BorderLayout.CENTER);
        
        
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