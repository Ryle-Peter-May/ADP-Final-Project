package za.ac.cput.clientapp.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.clientapp.Client;
import java.util.List;
import javax.swing.BorderFactory;
import za.ac.cput.clientapp.domain.Course;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class AddCourseGUI extends JFrame implements ActionListener {

    private Client client;
    private JPanel mainPnl;
    private JPanel panelNorth, panelSouth, panelWest, panelEast;
    private JLabel courseCode, courseTitle;
    private JTextField txtCourseCode, txtCourseTitle;
    private JButton btnSave;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane sp;

    public AddCourseGUI(Client client) {// here to handle the communications with the server
        //Refers to the instance variable at the top
        this.client = client;
        mainPnl = new JPanel(new BorderLayout());
        panelNorth = new JPanel();
        panelSouth = new JPanel();
        panelWest = new JPanel();
        panelEast = new JPanel();

        courseCode = new JLabel("Course Code");
        courseTitle = new JLabel("Course Title");

        txtCourseCode = new JTextField(20);
        txtCourseTitle = new JTextField(20);
        txtCourseCode.setPreferredSize(new Dimension(300, 25));
        txtCourseTitle.setPreferredSize(new Dimension(300, 25));

        btnSave = new JButton("Add");

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        Object[] columnNames = {"Course Code", "Course Description"};
        tableModel.setColumnIdentifiers(columnNames);

        sp = new JScrollPane(table);

    }

    public JPanel getPanel() {

        JPanel codePnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        codePnl.add(txtCourseCode);

        JPanel titlePnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        titlePnl.add(txtCourseTitle);

        panelSouth.add(btnSave);
        panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        panelWest.setLayout(new GridLayout(4, 1, 10, 10));
        panelWest.add(courseCode);
        panelWest.add(codePnl);
        panelWest.add(courseTitle);
        panelWest.add(titlePnl);

        //adding padding like mr naidoo suggested
        panelWest.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel westPnlHolder = new JPanel(new BorderLayout());
        westPnlHolder.add(panelWest, BorderLayout.NORTH);
        westPnlHolder.setPreferredSize(new Dimension(300, 150));

        panelEast.setLayout(new BorderLayout());
        sp.setPreferredSize(new Dimension(450, 250));
        panelEast.add(sp, BorderLayout.CENTER);
        panelEast.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        btnSave.addActionListener(this);

        mainPnl.add(westPnlHolder, BorderLayout.WEST);
        mainPnl.add(panelSouth, BorderLayout.SOUTH);
        mainPnl.add(panelEast, BorderLayout.CENTER);

        loadCoursesToTable();
        return mainPnl;
    }

    private void loadCoursesToTable() {
        List<Course> courses = client.getAllCourses();

        tableModel.setRowCount(0);

        if (courses == null || courses.isEmpty()) {
            System.out.println("No Courses found or server not responding");
        } else {
            for (Course c : courses) {
                tableModel.addRow(new Object[]{c.getCode(), c.getTitle()});
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            String courseCode = txtCourseCode.getText();
            String courseTitle = txtCourseTitle.getText();

            if (courseCode.equals("") || courseTitle.equals("")) {
                JOptionPane.showMessageDialog(this, "Please Fill In All Details");
            } else {

                client.sendCourseData(courseCode, courseTitle);
                tableModel.addRow(new Object[]{courseCode, courseTitle});

                txtCourseCode.setText("");
                txtCourseTitle.setText("");
            }
        }
    }

}
