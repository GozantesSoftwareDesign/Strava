package gui;

import internals.swing.ImageDisplayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JFrame frame = new JFrame("STRAVA");
    ;
    private JPanel panelPrincipal = new JPanel(new GridLayout(4, 1));
    private JPanel panelUser = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelPassword = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelName = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelBirthDate = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelWeight = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelHeight = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelMaximunHeartRate = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelRestingHeartRate = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelBotonesLogin = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelBotonesSignUp = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JLabel userLabel = new JLabel("Email");
    private JTextField userText = new JTextField(20);
    private JLabel passwordLabel = new JLabel("Password");
    private JPasswordField passwordText = new JPasswordField(20);
    private JLabel nameLabel = new JLabel("Name");
    private JTextField nameText = new JTextField(20);
    private JLabel birthDateLabel = new JLabel("BirthDate");
    private JTextField birthDateText = new JTextField(20);
    private JLabel weightLabel = new JLabel("Weight(Optional)");
    private JTextField weightText = new JTextField(20);
    private JLabel heightLabel = new JLabel("Height(Optional)");
    private JTextField heightText = new JTextField(20);
    private JLabel maximunHeartRateLabel = new JLabel("MaximunHeartRate(Optional)");
    private JTextField maximunHeartRateText = new JTextField(20);
    private JLabel restingHeartRateLabel = new JLabel("RestingHeartRate(Optional)");
    private JTextField restingHeartRateText = new JTextField(20);
    private JButton login = new JButton("Login");
    private JButton loginFacebook = new JButton("Login with Facebook");
    private JButton loginGoogle = new JButton("Login with Google");
    private JButton signUp = new JButton("Sign up");
    private JButton signUpFacebook = new JButton("Sign up with Facebook");
    private JButton signUpGoogle = new JButton("Sign up with Google");
    private ImageDisplayer iconoGoogle;
    private ImageDisplayer iconoFacebook;

    public VentanaPrincipal() {
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        try {
            iconoGoogle = new ImageDisplayer(ImageIO.read(new File("img/Google.png")), 25, 25);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            iconoFacebook = new ImageDisplayer(ImageIO.read(new File("img/Facebook.png")), 25, 25);
        } catch (IOException e) {
            e.printStackTrace();
        }

        inicializarVentana();

        paintVentana(0);

        signUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                paintVentana(1);
            }

        });
        signUpGoogle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
        signUpFacebook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
        login.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                paintVentana(0);
            }

        });
        loginGoogle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
        loginFacebook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
    }

    // estado de la ventana
    // 0 = ventana de Login
    // 1 = venatna de SignUp
    void paintVentana(int estado) {

        ArrayList<JPanel> arrayDePaneles = new ArrayList<JPanel>();
        JPanel panelScroll = new JPanel(new GridLayout(arrayDePaneles.size(), 1, 0, 10));
        JScrollPane scrollPane = new JScrollPane();

        panelPrincipal.removeAll();
        panelBotonesLogin.removeAll();
        panelBotonesSignUp.removeAll();
        scrollPane.getViewport().removeAll();

        panelPrincipal.setLayout(null);
        panelPrincipal.setBounds(50, 0, 500, 500);
        scrollPane.setBounds(50, 25, 450, 200);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        switch (estado) {
            case 0:

                panelBotonesLogin.add(loginFacebook);
                panelBotonesLogin.add(iconoFacebook);
                panelBotonesLogin.add(loginGoogle);
                panelBotonesLogin.add(iconoGoogle);
                panelBotonesSignUp.add(signUp);

                panelUser.setBounds(panelPrincipal.getX(), panelPrincipal.getY() + 100, panelPrincipal.getWidth(), 50);
                panelPassword.setBounds(panelPrincipal.getX(), panelUser.getY() + panelUser.getHeight(), panelPrincipal.getWidth(), 50);
                panelBotonesLogin.setBounds(panelPrincipal.getX(), panelPassword.getY() + panelPassword.getHeight() * 2, panelPrincipal.getWidth(), 50);
                panelBotonesSignUp.setBounds(panelPrincipal.getX(), panelBotonesLogin.getY() + panelBotonesLogin.getHeight(), panelPrincipal.getWidth(), 50);

                panelPrincipal.add(panelUser);
                panelPrincipal.add(panelPassword);
                panelPrincipal.add(panelBotonesLogin);
                panelPrincipal.add(panelBotonesSignUp);

                panelPrincipal.revalidate();
                panelPrincipal.repaint();
                scrollPane.getViewport().revalidate();
                scrollPane.getViewport().repaint();

                frame.add(panelPrincipal);
                break;
            case 1:

                panelBotonesLogin.add(signUpFacebook);
                panelBotonesLogin.add(iconoFacebook);
                panelBotonesLogin.add(signUpGoogle);
                panelBotonesLogin.add(iconoGoogle);
                panelBotonesSignUp.add(login);

                arrayDePaneles.add(panelUser);
                arrayDePaneles.add(panelPassword);
                arrayDePaneles.add(panelName);
                arrayDePaneles.add(panelBirthDate);
                arrayDePaneles.add(panelWeight);
                arrayDePaneles.add(panelHeight);
                arrayDePaneles.add(panelMaximunHeartRate);
                arrayDePaneles.add(panelRestingHeartRate);

                generateScrollPane(scrollPane, arrayDePaneles, panelScroll);
                panelBotonesLogin.setBounds(panelPrincipal.getX(), 250, panelPrincipal.getWidth(), 50);
                panelBotonesSignUp.setBounds(panelPrincipal.getX(), 300, panelPrincipal.getWidth(), 50);

                panelPrincipal.add(scrollPane);
                panelPrincipal.add(panelBotonesLogin);
                panelPrincipal.add(panelBotonesSignUp);

                panelPrincipal.revalidate();
                panelPrincipal.repaint();

                frame.add(panelPrincipal);
                break;
        }
    }

    void inicializarVentana() {
        userLabel.setPreferredSize(new Dimension(75, 25));
        userText.setPreferredSize(new Dimension(50, 25));
        passwordLabel.setPreferredSize(new Dimension(75, 25));
        passwordText.setPreferredSize(new Dimension(50, 25));
        nameLabel.setPreferredSize(new Dimension(75, 25));
        nameText.setPreferredSize(new Dimension(50, 25));
        birthDateLabel.setPreferredSize(new Dimension(75, 25));
        birthDateText.setPreferredSize(new Dimension(50, 25));
        weightLabel.setPreferredSize(new Dimension(175, 25));
        weightText.setPreferredSize(new Dimension(50, 25));
        heightLabel.setPreferredSize(new Dimension(175, 25));
        heightText.setPreferredSize(new Dimension(50, 25));
        maximunHeartRateLabel.setPreferredSize(new Dimension(175, 25));
        maximunHeartRateText.setPreferredSize(new Dimension(50, 25));
        restingHeartRateLabel.setPreferredSize(new Dimension(175, 25));
        restingHeartRateText.setPreferredSize(new Dimension(50, 25));

        login.setPreferredSize(new Dimension(150, 25));
        loginFacebook.setPreferredSize(new Dimension(150, 25));
        loginGoogle.setPreferredSize(new Dimension(150, 25));
        signUp.setPreferredSize(new Dimension(150, 25));
        signUpFacebook.setPreferredSize(new Dimension(170, 25));
        signUpGoogle.setPreferredSize(new Dimension(150, 25));

        panelUser.add(userLabel);
        panelUser.add(userText);
        panelName.add(nameLabel);
        panelName.add(nameText);
        panelBirthDate.add(birthDateLabel);
        panelBirthDate.add(birthDateText);
        panelWeight.add(weightLabel);
        panelWeight.add(weightText);
        panelHeight.add(heightLabel);
        panelHeight.add(heightText);
        panelMaximunHeartRate.add(maximunHeartRateLabel);
        panelMaximunHeartRate.add(maximunHeartRateText);
        panelRestingHeartRate.add(restingHeartRateLabel);
        panelRestingHeartRate.add(restingHeartRateText);
        panelPassword.add(passwordLabel);
        panelPassword.add(passwordText);
    }

    JScrollPane generateScrollPane(JScrollPane scroll, ArrayList<JPanel> arrayDePaneles, JPanel panel) {
        scroll.setViewportView(panel);
        arrayDePaneles.get(0).setBounds(scroll.getX(), scroll.getY(), scroll.getWidth() - 50, 50);
        ;
        panel.add(arrayDePaneles.get(0));
        for (int i = 1; i < arrayDePaneles.size(); i++) {
            arrayDePaneles.get(i).setBounds(scroll.getX(), arrayDePaneles.get(i - 1).getY() + arrayDePaneles.get(i - 1).getHeight(), scroll.getWidth(), 100);
            panel.add(arrayDePaneles.get(i));
        }

        return scroll;
    }
}
