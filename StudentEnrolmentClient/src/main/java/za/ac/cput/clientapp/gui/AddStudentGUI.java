package za.ac.cput.clientapp.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import za.ac.cput.clientapp.Client;

/**
 *
 * @author Ryle
 */
public class AddStudentGUI extends JFrame implements ActionListener {

    private JPanel studPnl;
    private JPanel mainPnl, headerPnl, formPnl, buttonPnl;
    private JLabel lblTitle, lblFirstName, lblLastName, lblEmail, lblPassword;
    private JTextField txtFirstName, txtLastName, txtEmail, txtPassword;
    private JButton btnSubmit;
    private Client client;

    public AddStudentGUI(Client client) {
        this.client = client;
        studPnl = new JPanel();

        mainPnl = new JPanel(new BorderLayout(10, 10));
        headerPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPnl = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        lblTitle = new JLabel("Fill in the form to add a new student");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        headerPnl.add(lblTitle);

        lblFirstName = new JLabel("First Name: ");
        lblLastName = new JLabel("Last Name: ");
        lblEmail = new JLabel("Email: ");
        lblPassword = new JLabel("Password: ");

        txtFirstName = new JTextField(17);
        txtLastName = new JTextField(17);
        txtEmail = new JTextField(17);
        txtPassword = new JTextField(17);

        btnSubmit = new JButton("Submit");

        //client = new Client();
    }

    public JPanel getPanel() {
        formPnl.add(lblFirstName);
        formPnl.add(txtFirstName);

        formPnl.add(lblLastName);
        formPnl.add(txtLastName);

        formPnl.add(lblEmail);
        formPnl.add(txtEmail);

        formPnl.add(lblPassword);
        formPnl.add(txtPassword);

        buttonPnl.add(btnSubmit);

        mainPnl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(formPnl, BorderLayout.CENTER);
        mainPnl.add(buttonPnl, BorderLayout.SOUTH);

        btnSubmit.addActionListener(this);

        studPnl.add(mainPnl);

        return studPnl;
    }

    public void setGUI() {
        formPnl.add(lblFirstName);
        formPnl.add(txtFirstName);

        formPnl.add(lblLastName);
        formPnl.add(txtLastName);

        formPnl.add(lblPassword);
        formPnl.add(txtPassword);

        buttonPnl.add(btnSubmit);
        

        mainPnl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(formPnl, BorderLayout.CENTER);
        mainPnl.add(buttonPnl, BorderLayout.SOUTH);

        btnSubmit.addActionListener(this);

        this.add(mainPnl);
        this.setTitle("Add Student");
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

//    public static void main(String[] args) {
//        Client client = new Client();
//        AddStudentGUI gui = new AddStudentGUI(client);
//        gui.setGUI();
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Client client = new Client();
        if (e.getSource() == btnSubmit) {
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String password = txtPassword.getText();
            String email = txtEmail.getText();

            client.sendStudentData(email, "student", firstName, lastName, password);

            if (firstName.equals("")
                    || lastName.equals("")
                    || email.equals("")
                    || password.equals("")) {
                JOptionPane.showMessageDialog(this, "Please fill in the details");
            } else {

                txtFirstName.setText("");
                txtLastName.setText("");
                txtEmail.setText("");
                txtPassword.setText("");

                JOptionPane.showMessageDialog(this, "Student added successfully");

            }

//        } else if (e.getSource() == btnCancel) {
//            //this should rather take admin back to previous gui
//            //change later
//            System.exit(0);
//        }
        }
    }
}
