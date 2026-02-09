package za.ac.cput.serverapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import za.ac.cput.serverapp.connection.DBConnection;
import za.ac.cput.serverapp.domain.Student;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */

// Methods to add and retrieve student records from the Users table
public class UserDAO {

    //This method returns a student object if login credentials are correct
    public Student login(String email, String password) {
        String sql = "SELECT * FROM USERS WHERE EMAIL = ? AND PASSWORD = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {          
                //creates student object with the students details
                return new Student(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;//returning null if there is no record found in the database with matching details
    }
    //The below method adds a new student record to the USERS table
    public void addStudent(Student student) {
        String sql = "INSERT INTO USERS ( email, role, first_name, last_name, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Student details are added/ bind to a prepared statement and executes a insertion operation
            ps.setString(1, student.getEmail());
            ps.setString(2, student.getRole());
            ps.setString(3, student.getFirstName());
            ps.setString(4, student.getLastName());
            ps.setString(5, student.getPassword());
            ps.executeUpdate();
            //return true;
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    //Retrieves all students from the USERS table

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE role='student'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            //Creates a student object for each row in the resultsset
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password"),
                        rs.getString("role")//Damien: Note that this is the bad boy that I am looking for (admin or student)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    //This method retrieves a user's ID using their email address
    //Email is the user's email and the User 

    public int getUserIdByEmail(String Email) {
        String sql = "SELECT user_id FROM USERS WHERE email=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; //The use of Return -1 is to indicate that the no user was found/ operation was not successful
    }
}
