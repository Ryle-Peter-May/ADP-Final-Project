package za.ac.cput.serverapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import za.ac.cput.serverapp.connection.DBConnection;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */

// This DAO class is responsible for managing students enrollment in courses 
public class EnrollmentDAO {
//Below allows a student to enroll in a course using their email and coursecode
//email is the Student's email address and courseCode is the code of the course
    public boolean enrollStudent(String email, String courseCode) {
        String sql = "INSERT INTO ENROLLMENTS (student_id, course_id) "
                + "VALUES ((SELECT user_id FROM USERS WHERE email = ?), "
                + "(SELECT course_id FROM COURSES WHERE code = ?))";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, courseCode);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//The below method retrieves all the courses that a student is enrolled in
    public ArrayList<String> getStudentCourses(String email) {
        ArrayList<String> courses = new ArrayList<>();
        String sql = "SELECT C.code, C.title "
                + "FROM ENROLLMENTS E "
                + "JOIN COURSES C ON E.course_id = C.course_id "
                + "JOIN USERS U ON E.student_id = U.user_id "
                + "WHERE U.email = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
// Adds each course to the list of enrolled courses/ subjects
            while (rs.next()) {
                courses.add(rs.getString("code") + " - " + rs.getString("title"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
//Retrieves all the students who are enrolled in a course.
    public ArrayList<String> getStudentsInCourse(String courseCode) {
        ArrayList<String> students = new ArrayList<>();
        String sql = "SELECT U.first_name, U.last_name, U.email "
                + "FROM ENROLLMENTS E "
                + "JOIN USERS U ON E.student_id = U.user_id "
                + "JOIN COURSES C ON E.course_id = C.course_id "
                + "WHERE C.code = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();
//Add student details (their first name, last name and email) to the list
            while (rs.next()) {
                students.add(rs.getString("first_name") + " "
                        + rs.getString("last_name")
                        + " (" + rs.getString("email") + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}
