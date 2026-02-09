package za.ac.cput.serverapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import za.ac.cput.serverapp.connection.DBConnection;
import za.ac.cput.serverapp.dao.CourseDAO;
import za.ac.cput.serverapp.dao.EnrollmentDAO;
import za.ac.cput.serverapp.dao.UserDAO;
import za.ac.cput.serverapp.domain.Course;
import za.ac.cput.serverapp.domain.Student;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
//Server side class which is responsible for listening to client connections
//It also listens and processes incoming requests related to students, courses and enrollments*/
public class ServerApp {

    private ServerSocket listener;
    private Socket client;

    //This constructor initializes the server socket on port 6666
    public ServerApp() {
        try {
            listener = new ServerSocket(6666, 1);
        } catch (Exception e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    public void listen() {
        try {

            System.out.println("Server is listening on port " + listener.getLocalPort());//Displaying the port that the client need to connect to
            //Creates a infinate loop waiting for a new client to connect
            //done to acomodate the loop function when students log in and admin log in
            while (true) {

                client = listener.accept();
                System.out.println("Client connected");

                processClient();
            }
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }

    //Processes/Handles communication between the server and the client
    public void processClient() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            String msg;
            while (true) {
                msg = (String) in.readObject();

                if (msg.equalsIgnoreCase("exit")) {
                    System.out.println("Client has exited.");
                    break;
                }

                System.out.println("Client request: " + msg);

                Object response = handleRequest(msg, out);

                if (response != null) {
                    out.writeObject(response);
                    out.flush();
                }
            }
            //Closes connection
            out.close();
            in.close();
            client.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Object handleRequest(String msg, ObjectOutputStream out) {
        String[] parts = msg.split(";");
        String command = parts[0].toUpperCase();

        UserDAO userDAO = new UserDAO();
        CourseDAO courseDAO = new CourseDAO();
        EnrollmentDAO enrollDAO = new EnrollmentDAO();

        try {
            switch (command) {

                case "ADD_STUDENT":
                    if (parts.length == 6) {
                        Student student = new Student(0, parts[1], parts[3], parts[4], parts[5], parts[2]);
                        userDAO.addStudent(student);
                        return "Student added successfully.";
                    } else {
                        return "Invalid ADD_STUDENT format. Use: ADD_STUDENT;email;firstName;lastName;password";
                    }

                case "ADD_COURSE":
                    if (parts.length == 3) {
                        Course course = new Course(0, parts[1], parts[2]);
                        courseDAO.addCourse(course);
                        return "Course added successfully.";
                    } else {
                        return "Invalid ADD_COURSE format. Use: ADD_COURSE;courseCode;courseTitle";
                    }

                case "LOGIN":
                    if (parts.length == 3) {
                        String email = parts[1];
                        String password = parts[2];

                        Student loggedInUser = userDAO.login(email, password);

                        if (loggedInUser != null) {
                            if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                                return "admin";
                            } else if (loggedInUser.getRole().equalsIgnoreCase("student")) {
                                return "student;" + loggedInUser.getEmail() + ";"
                                        + loggedInUser.getFirstName() + ";"
                                        + loggedInUser.getLastName() + ";"
                                        + loggedInUser.getPassword();
                            }
                        } else {
                            return "LOGIN_FAILED;Invalid email or password.";
                        }

                    } else {
                        return "Invalid Login format. Use: LOGIN;email;password";
                    }

                case "ENROLL":
                    if (parts.length == 3) {
                        String email = parts[1];
                        String courseCode = parts[2];

                        if (enrollDAO.enrollStudent(email, courseCode)) {
                            return "Enrollment successful.";
                        } else {
                            return "Enrollment failed.";
                        }
                    } else {
                        return "Invalid ENROLL format. Use: ENROLL;studentEmail;courseCode";
                    }

                case "LIST_COURSES":
                    ArrayList<Course> courses = courseDAO.getAllCourses();
                    //Debug check
                    System.out.println("Server: Courses found: " + courses.size());

                    ArrayList<String[]> courseData = new ArrayList<>();
                    for (Course c : courses) {
                        courseData.add(new String[]{c.getCode(), c.getTitle()});
                    }
                    return courseData;

                case "LIST_STUDENTS":
                    ArrayList<Student> students = userDAO.getAllStudents();
                    System.out.println("Server: Students found: " + students.size());

                    ArrayList<String[]> studentData = new ArrayList<>();
                    for (Student s : students) {
                        studentData.add(new String[]{String.valueOf(s.getUserId()),
                            s.getFirstName(),
                            s.getLastName(),
                            s.getEmail()
                        });
                    }
                    return studentData;

                case "STUDENT_COURSES":
                    if (parts.length == 2) {
                        String email = parts[1];
                        ArrayList<String> enrolledCourses = enrollDAO.getStudentCourses(email);
                        return enrolledCourses;
                    } else {
                        return "Invalid STUDENT_COURSES format. Use: STUDENT_COURSES;email";
                    }

                case "STUDENTS_IN_COURSE":
                    if (parts.length == 2) {
                        String courseCode = parts[1];
                        ArrayList<String> enrolledStudents = enrollDAO.getStudentsInCourse(courseCode);

                        ArrayList<String[]> studentsInfo = new ArrayList<>();
                        for (String student : enrolledStudents) {

                            String namePart = student.substring(0, student.indexOf("(")).trim();
                            String emailPart = student.substring(student.indexOf("(") + 1, student.indexOf(")"));
                            String[] names = namePart.split(" ", 2);
                            studentsInfo.add(new String[]{
                                names[0], names.length > 1 ? names[1] : "", emailPart
                            });
                        }
                        return studentsInfo;
                    } else {
                        return "Invalid STUDENTS_IN_COURSE format. Use: STUDENTS_IN_COURSE;courseCode";
                    }
                default:
                    return "Unknown command: " + command;
            }
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        DBConnection.DBCreator();
        ServerApp server = new ServerApp();
        server.listen();
    }
}
