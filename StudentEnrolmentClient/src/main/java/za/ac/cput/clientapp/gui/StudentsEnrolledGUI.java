package za.ac.cput.clientapp.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.clientapp.Client;
import za.ac.cput.clientapp.domain.Student;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class StudentsEnrolledGUI extends JFrame implements ActionListener {

    private JPanel studEnrollPnl, btnPnl;
    private JTable studEnrolledTbl;
    private DefaultTableModel tableModel;
    private JScrollPane sp, enrolledScroll;
    private JButton refreshBtn;
    private Client client;

    public StudentsEnrolledGUI(Client client) {
        this.client = client;
        studEnrollPnl = new JPanel(new BorderLayout());
        btnPnl = new JPanel();
        tableModel = new DefaultTableModel();

        Object[] columnNames = {"ID", "First Name", "Last Name", "Email"};
        tableModel.setColumnIdentifiers(columnNames);
        studEnrolledTbl = new JTable(tableModel);
        sp = new JScrollPane(studEnrolledTbl);

        refreshBtn = new JButton("Refresh");

    }

    public JPanel getPanel() {
        refreshBtn.addActionListener(this);
        btnPnl.add(refreshBtn);
        studEnrollPnl.add(sp, BorderLayout.CENTER);
        studEnrollPnl.add(btnPnl, BorderLayout.SOUTH);

        loadEnrolledStudentsToTable();
        return studEnrollPnl;

    }

    private void loadEnrolledStudentsToTable() {

        tableModel.setRowCount(0);

        try {
            Client.out.writeObject("LIST_STUDENTS"); // had to make the out and in public
            Client.out.flush();

            Object response = Client.in.readObject();

            if (response instanceof List<?>) {
                List<?> students = (List<?>) response;

                for (Object o : students) {
                    if (o instanceof String[] arr && arr.length == 4) {
                        tableModel.addRow(new Object[]{arr[0], arr[1], arr[2], arr[3]});
                    }

                }

            } else {
                JOptionPane.showMessageDialog(this, "Unexpected response from server");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.toString();
        }

    }

    public static void main(String[] args) {
        //StudentsEnrolledGUI gui = new StudentsEnrolledGUI();
        //gui.setGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshBtn) {
            loadEnrolledStudentsToTable();
        }
    }

}
