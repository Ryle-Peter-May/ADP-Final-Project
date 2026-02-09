package za.ac.cput.clientapp.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import za.ac.cput.clientapp.Client;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class AdminPanel extends JFrame implements ActionListener {

    private JTabbedPane tabbedPane;
    private JLabel titleLbl, subtitleLbl;
    private JPanel headerPnl, titleBox;
    private JPanel btnLogoutPnl;
    private Client client;
    private JButton btnLogout;

    public AdminPanel(Client client) {
        this.client = client;
        
        headerPnl = new JPanel(new BorderLayout());
        titleBox = new JPanel(new GridLayout(2, 1));

        tabbedPane = new JTabbedPane();

        btnLogoutPnl = new JPanel();
        btnLogout = new JButton("Logout");

        titleLbl = new JLabel("Admin Dashboard");
        subtitleLbl = new JLabel("Manage Courses and Students");

        AddCourseGUI addCourse = new AddCourseGUI(client);
        AddStudentGUI addStudent = new AddStudentGUI(client);
        StudentsEnrolledGUI viewStudents = new StudentsEnrolledGUI(client);
        StudentsInCourseGUI studsInCourse = new StudentsInCourseGUI(client);
        StudentEnrolmentViewerGUI enrolViewer = new StudentEnrolmentViewerGUI(client);
        
        //Using TabbedPane to display the GUIs neccessary for Admin
        tabbedPane.addTab("Add Course", addCourse.getPanel());
        tabbedPane.addTab("Add Studennt", addStudent.getPanel());
        tabbedPane.addTab("View Students", viewStudents.getPanel());
        tabbedPane.addTab("Students in Course", studsInCourse.getPanel());
        tabbedPane.addTab("Student Enrollments", enrolViewer.getPanel());
    }

    public void setGUI() {
        btnLogoutPnl.add(btnLogout);
        headerPnl.setBackground(new Color(45, 62, 80));
        headerPnl.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 22));

        subtitleLbl.setForeground(Color.LIGHT_GRAY);
        subtitleLbl.setFont(new Font("Arial", Font.BOLD, 22));

        titleBox.setOpaque(false);
        titleBox.add(titleLbl);
        titleBox.add(subtitleLbl);

        headerPnl.add(titleBox, BorderLayout.WEST);
        headerPnl.add(btnLogout, BorderLayout.EAST);
        
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        btnLogout.addActionListener(this);
        this.getContentPane().setBackground(new Color(245,245,245));
        this.add(headerPnl, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
        //this.add(btnLogoutPnl, BorderLayout.SOUTH);
        this.setTitle("Admin Panel");
        
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }

    public static void main(String[] args) {
        Client client = new Client();
        AdminPanel gui = new AdminPanel(client);
        gui.setGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogout) {
            dispose();
            client.logout();
        }
    }

}
