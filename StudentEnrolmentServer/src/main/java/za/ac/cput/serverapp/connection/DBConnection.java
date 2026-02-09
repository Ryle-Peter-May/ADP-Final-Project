package za.ac.cput.serverapp.connection;

import java.sql.*;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class DBConnection {

    public static Connection getConnection() throws SQLException {

        String database_url = "jdbc:derby://localhost:1527/StudentEnrolmentDB";
        String username = "administrator";
        String password = "admin";

        Connection connection = DriverManager.getConnection(database_url, username, password);
        return connection;
    }

    public static void DBCreator() {

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate(
                    "CREATE TABLE USERS (\n"
                    + "    user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n"
                    + "     email VARCHAR(50),\n"
                    + "    role VARCHAR(10) NOT NULL,\n"
                    + "    first_name VARCHAR(50),\n"
                    + "    last_name VARCHAR(50),\n"
                    + "    password VARCHAR(50) NOT NULL\n"
                    + ")"
            );

        } catch (SQLException e) {
             if(!e.getSQLState().equals("X0Y32")){ //Ignoring the Derby error: table already exists
                e.printStackTrace();
             }
        }

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate("CREATE TABLE COURSES (\n"
                    + "    course_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n"
                    + "    code VARCHAR(10) NOT NULL,\n"
                    + "    title VARCHAR(50) NOT NULL\n"
                    + ")");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) { // Ignoring the Derby error: table already exists
                e.printStackTrace();
            }
        }

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate("CREATE TABLE ENROLLMENTS (\n"
                    + "    enroll_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n"
                    + "    student_id INT REFERENCES USERS(user_id),\n"
                    + "    course_id INT REFERENCES COURSES(course_id)\n"
                    + ")");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) { // Ignoring the Derby error: table already exists
                e.printStackTrace();
            }
        }

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM USERS WHERE email = 'admin@mycput.ac.za'");
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                stmt.executeUpdate("INSERT INTO USERS (email, role, first_name, last_name, password) "
                        + "VALUES ('admin@mycput.ac.za', 'admin', 'Admin', 'User', 'admin123')");
                System.out.println("Default admin account created.");
            } else {
                System.out.println("Admin account already exists.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
