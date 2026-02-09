package za.ac.cput.clientapp.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.clientapp.Client;
import za.ac.cput.clientapp.domain.Course;
import za.ac.cput.clientapp.domain.Student;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
//GUI for Students to see what Courses they are in and to enroll in Courses
public class CourseEnrollmentGUI extends JFrame implements ActionListener {

    private JPanel mainPnl, btnPanel, enrolledPanel, centerPanel;
    private JTable tblCourses;
    private DefaultTableModel tableModel;
    private JLabel lblWelcome;
    private JButton btnEnroll, btnLogout;
    private DefaultListModel<String> enrolledListModel;
    private JScrollPane sp, enrolledScroll;
    private Student student;
    private Course course;
    private Client client;

     
    public CourseEnrollmentGUI(Client client, Student student) {
        //refers to the instance variables at the top
        this.client = client;
        this.student = student; 

        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        lblWelcome = new JLabel("Welcome " + student.getFirstName());
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);

        //Used for checkbox column
        String[] columnNames = {"Select", "Course Code", "Course Title"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int index) {
                if (index == 0) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

        };

        tblCourses = new JTable(tableModel);
        sp = new JScrollPane(tblCourses);

        btnEnroll = new JButton("Enroll");
        btnLogout = new JButton("Logout");

        btnPanel = new JPanel();
        btnPanel.add(btnEnroll);
        btnPanel.add(btnLogout);

        //Enroll course list
        enrolledListModel = new DefaultListModel<>();
        JList<String> enrolledList = new JList<>(enrolledListModel);
        enrolledScroll = new JScrollPane(enrolledList);
        enrolledScroll.setPreferredSize(new Dimension(250, 100));

        enrolledPanel = new JPanel(new BorderLayout());
        enrolledPanel.setBorder(BorderFactory.createTitledBorder("My Enrolled Courses"));

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(sp, BorderLayout.CENTER);
        centerPanel.add(enrolledPanel, BorderLayout.SOUTH);
        
        loadCoursesFromServer();
    }

    public void setGUI() {
        enrolledPanel.add(enrolledScroll, BorderLayout.CENTER);
        mainPnl.add(lblWelcome, BorderLayout.NORTH);
        mainPnl.add(centerPanel, BorderLayout.CENTER);
        mainPnl.add(btnPanel, BorderLayout.SOUTH);

        btnEnroll.addActionListener(this);
        btnLogout.addActionListener(this);
        this.setTitle("Course Enroll - "+ student.getFirstName());
        this.add(mainPnl);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEnroll) {
            List<Integer> rowsToRemove = new ArrayList<>();
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Boolean selected = (Boolean) tableModel.getValueAt(i, 0);
                if (selected != null && selected) {
                    String courseCode = (String) tableModel.getValueAt(i, 1);
                    String courseTitle = (String) tableModel.getValueAt(i, 2);
                    client.sendEnrolmentRequest(student.getEmail(), courseCode,courseTitle);
                    
                    enrolledListModel.addElement(courseCode+" - "+ courseTitle);
                    rowsToRemove.add(i);
                }
            }
            //remove enrolled courses from the table
            for(int i = rowsToRemove.size()-1;i>=0;i--){
                tableModel.removeRow(rowsToRemove.get(i));
            }
            
            JOptionPane.showMessageDialog(this, "Enrolment request sent");
        } else if (e.getSource() == btnLogout) {
            dispose();
            client.logout();
        }
    }

    private void loadCoursesFromServer() {
        List<Course> courses = client.getAllCourses();
        List<String> enrolledCourses = client.getEnrolledCourses(student.getEmail());
        if (courses != null) {
            for (Course c : courses) {
                boolean alreadyEnrolled = false;
                
                for(String ec: enrolledCourses){
                    if(ec.startsWith(c.getCode()+" - "+c.getTitle() )){
                        alreadyEnrolled = true;
                        break;//Here to ensure it stops checking
                    }
                }
                if(!alreadyEnrolled){
                tableModel.addRow(new Object[]{false, c.getCode(), c.getTitle()});
                }else{
                        enrolledListModel.addElement(c.getCode()+ " - "+ c.getTitle());
                }
                
            }
        } else {
            JOptionPane.showMessageDialog(this, "No courses found or failed to load");
        }
    }

}
