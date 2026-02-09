package za.ac.cput.serverapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import za.ac.cput.serverapp.connection.DBConnection;
import za.ac.cput.serverapp.domain.Course;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class CourseDAO {
//Adds a new course/subject and adds/records it into the Courses table
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO COURSES (code, title) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //Method below inserts course details into the prepared statement
            ps.setString(1, course.getCode());
            ps.setString(2, course.getTitle());
            //Method below executes the insertion operation
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    // This is responsible for retrieving all courses/ subjects that are stored in the Courses table
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM COURSES";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            // Iterates Loops through the results and builds a Course object for each record
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("code"),
                        rs.getString("title")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return courses;
    }
}

