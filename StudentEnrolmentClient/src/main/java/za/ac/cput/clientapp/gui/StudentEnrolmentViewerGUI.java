package za.ac.cput.clientapp.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import za.ac.cput.clientapp.Client;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class StudentEnrolmentViewerGUI extends JFrame implements ActionListener, KeyListener {

    private JPanel mainPnl, topPanel;
    private JLabel searchLbl;
    private JTextField txtEmail;
    private JButton btnSearch;
    private JTable tblCourses;
    private DefaultTableModel model;
    private Client client;

    public StudentEnrolmentViewerGUI(Client client) {
        this.client = client;

        mainPnl = new JPanel(new BorderLayout());

        topPanel = new JPanel(new FlowLayout());
        searchLbl = new JLabel("Enter Student Email:");
        txtEmail = new JTextField(20);
        btnSearch = new JButton("Search");

        model = new DefaultTableModel(new Object[]{"Course Code", "Course Title"}, 0);
        tblCourses = new JTable(model);

        btnSearch.addActionListener(this);
        txtEmail.addKeyListener(this);
    }

    public JPanel getPanel() {
        topPanel.add(searchLbl);
        topPanel.add(txtEmail);
        topPanel.add(btnSearch);

        mainPnl.add(topPanel, BorderLayout.NORTH);
        mainPnl.add(new JScrollPane(tblCourses), BorderLayout.CENTER);

        return mainPnl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            if (txtEmail.getText().equals("")) {
                model.setRowCount(0);
                JOptionPane.showMessageDialog(this, "Please Fill In The Field");
            } else {
                String email = txtEmail.getText();
                List<String> courses = client.getEnrolledCourses(email);
                //checking if the student doesnt have courses
                if (courses.isEmpty()) {
                    model.setRowCount(0);
                    JOptionPane.showMessageDialog(this, "No Courses Found");
                    return;
                }
                model.setRowCount(0);
                for (String c : courses) {
                    String[] parts = c.split(" - ", 2);
                    if (parts.length == 2) {
                        model.addRow(new Object[]{parts[0], parts[1]});
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtEmail.getText().equals("")) {
                model.setRowCount(0);
                JOptionPane.showMessageDialog(this, "Please Fill In The Field");
            } else {
                String email = txtEmail.getText();
                List<String> courses = client.getEnrolledCourses(email);
                //checking if the student doesnt have courses
                if (courses.isEmpty()) {
                    model.setRowCount(0);
                    JOptionPane.showMessageDialog(this, "No Courses Found");
                    return;
                }

                model.setRowCount(0);
                for (String c : courses) {
                    String[] parts = c.split(" - ", 2);
                    if (parts.length == 2) {
                        model.addRow(new Object[]{parts[0], parts[1]});
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
