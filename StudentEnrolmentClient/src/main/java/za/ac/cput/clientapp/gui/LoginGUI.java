package za.ac.cput.clientapp.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import za.ac.cput.clientapp.Client;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class LoginGUI extends JFrame implements ActionListener, KeyListener {

    public static LoginGUI currentInstance;//Single variable to only point at the current open gui

    private JPanel logoPnl, headerPnl, mainHeader, formPnl, inputPnl, buttonPnl, mainPnl;
    private JLabel logoLbl, loginLbl, emailLbl, passLbl;
    private JTextField emailTxtFld, passTxtFld;
    private JButton loginBtn;
    private Client client;

    public LoginGUI(Client client) {
        currentInstance = this;
        this.client = client;
        //Setting up panels to house the logo, input fields and button
        logoPnl = new JPanel();
        headerPnl = new JPanel();
        mainHeader = new JPanel();
        formPnl = new JPanel();
        inputPnl = new JPanel();
        buttonPnl = new JPanel();
        mainPnl = new JPanel();
        loginLbl = new JLabel("Login");
        emailLbl = new JLabel("Email: ");
        passLbl = new JLabel("Password: ");

        try {
            BufferedImage logo = ImageIO.read(new File("cput_logo.jpeg"));
            logoLbl = new JLabel(new ImageIcon(logo));
        } catch (IOException ex) {
            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        loginLbl.setFont(new Font("Calibri", Font.PLAIN, 30));
        emailTxtFld = new JTextField(20);
        passTxtFld = new JTextField(20);
        loginBtn = new JButton("Login");

    }

    public void setGUI() {
        logoPnl.add(logoLbl);
        headerPnl.add(loginLbl);

        mainHeader.setLayout(new BorderLayout());
        mainHeader.add(logoPnl, BorderLayout.NORTH);
        mainHeader.add(headerPnl, BorderLayout.CENTER);

        formPnl.setLayout(new GridLayout(4, 2));
        formPnl.add(emailLbl);
        formPnl.add(emailTxtFld);
        formPnl.add(passLbl);
        formPnl.add(passTxtFld);

        inputPnl.add(formPnl);
        buttonPnl.add(loginBtn);

        mainPnl.setLayout(new BorderLayout());
        mainPnl.add(mainHeader, BorderLayout.NORTH);
        mainPnl.add(inputPnl, BorderLayout.CENTER);
        mainPnl.add(buttonPnl, BorderLayout.SOUTH);

        loginBtn.addActionListener(this);

        passTxtFld.addKeyListener(this);
        this.setLayout(new BorderLayout());
        this.add(mainPnl);

        this.setTitle("Login");
        this.setVisible(true);
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    //Created a set client method to be called whenever a client logs in
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            //Placing input inside string variables
            String email = emailTxtFld.getText();
            String password = passTxtFld.getText();

            if (email.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(this, "Please Fill In Details");
            }

            //passing the email and password into the login method inside the client class
            client.login(email, password);

            //making both input fields empty after
            emailTxtFld.setText("");
            passTxtFld.setText("");

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            //Placing input inside string variables
            String email = emailTxtFld.getText();
            String password = passTxtFld.getText();

            if (email.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(this, "Please Fill In Details");
            } else {
                //passing the email and password into the login method inside the client class
                client.login(email, password);

                //making both input fields empty after
                emailTxtFld.setText("");
                passTxtFld.setText("");
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
