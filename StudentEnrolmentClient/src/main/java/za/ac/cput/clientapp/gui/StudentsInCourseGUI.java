package za.ac.cput.clientapp.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.clientapp.Client;
import za.ac.cput.clientapp.domain.Course;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class StudentsInCourseGUI extends JFrame implements ActionListener {

    private JComboBox<String> cmbCourses;
    private JPanel topPanel, mainPnl;
    private JTable tblStudents;
    private DefaultTableModel model;
    private JButton btnLoad;
    private Client client;

    public StudentsInCourseGUI(Client client) {
        this.client = client;
        mainPnl = new JPanel(new BorderLayout());

        topPanel = new JPanel();
        cmbCourses = new JComboBox<>();
        btnLoad = new JButton("Load Students");

        model = new DefaultTableModel(new Object[]{"First Name", "Last Name", "Email"}, 0);
        tblStudents = new JTable(model);

        btnLoad.addActionListener(this);

        List<Course> courses = client.getAllCourses();
        for (Course c : courses) {
            cmbCourses.addItem(c.getCode());
        }

    }

    public JPanel getPanel() {
        topPanel.add(cmbCourses);
        topPanel.add(btnLoad);

        mainPnl.add(topPanel, BorderLayout.NORTH);
        mainPnl.add(new JScrollPane(tblStudents), BorderLayout.CENTER);

        return mainPnl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLoad) {
            String courseCode = (String) cmbCourses.getSelectedItem();
            List<String[]> students = client.getStudentInCourse(courseCode);

            model.setRowCount(0);
            for (String[] s : students) {
                model.addRow(s);
            }
        }
    }

}
