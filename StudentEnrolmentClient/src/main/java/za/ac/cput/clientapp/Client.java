package za.ac.cput.clientapp;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import za.ac.cput.clientapp.gui.AdminPanel;
import za.ac.cput.clientapp.gui.CourseEnrollmentGUI;
import za.ac.cput.clientapp.gui.LoginGUI;
import za.ac.cput.clientapp.domain.Course;
import za.ac.cput.clientapp.domain.Student;

/**
 * Group 64 class: 2F
 *
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class Client {

    public static ObjectInputStream in;
    public static ObjectOutputStream out;
    private Socket socket;

    public Client() {

        try {
            socket = new Socket("127.0.0.1", 6666);
            System.out.println("Connected to Server");
            getStreams();
        } catch (IOException e) {
            e.toString();
        }
    }

    public void getStreams() throws IOException {

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
    }

    //Sending enrollment requests (for students).
    public void sendEnrolmentRequest(String studentEmail, String courseCode, String courseTitle) {
        try {
          String msg = "ENROLL;" + studentEmail + ";" + courseCode + ";" + courseTitle;
             
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.toString();
        }
    }

    //Getting the email & password from the login gui
    public void login(String email, String password) {
        try {
            //Msg takes the email and password of the person trying to log in
            String msg = "LOGIN;" + email + ";" + password;
            //Sending the details to the server
            out.writeObject(msg);
            out.flush();

            //Get a response(THE ROLE OF PERSON) from the server after they have entered their login details
            Object response = in.readObject();

            if (response instanceof String respStr) {
                if (respStr.startsWith("LOGIN_FAILED")) {

                    JOptionPane.showMessageDialog(null, respStr.split(";")[1]);

                } else if (respStr.equalsIgnoreCase("admin")) {

                    new AdminPanel(this).setGUI();

                    //Dispose the gui window if login was succesful
                    if (LoginGUI.currentInstance != null) {
                        LoginGUI.currentInstance.dispose();
                        LoginGUI.currentInstance = null;
                    } else {
                        //This does the same as the first if, it only checks if the server sent some other type of string
                        System.out.println("Server: " + respStr);
                    }

                } else if (respStr.startsWith("student;")) {
                    if (LoginGUI.currentInstance != null) {
                        LoginGUI.currentInstance.dispose();
                        LoginGUI.currentInstance = null;
                    }

                    String[] parts = respStr.split(";");
                    String emailResp = parts[1];
                    String firstName = parts[2];
                    String lastName = parts[3];
                    String passwordResp = parts[4];

                    //Constructing client-side student manually
                    za.ac.cput.clientapp.domain.Student student
                            = new za.ac.cput.clientapp.domain.Student(0, emailResp, firstName, lastName, passwordResp, "student");

                    new CourseEnrollmentGUI(this, student).setGUI();
                } else {
                    System.out.println("Unexpected response: " + respStr);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    //Added a logout method instead of the buttons disposing their gui and calling the LoginGUI
    public void logout() {
        try {
            if (out != null) {
                out.writeObject("EXIT");
                out.flush();
            }

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Client disconnected from the server.");

            LoginGUI loginGUI = new LoginGUI(new Client());
            loginGUI.setGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Sending student data (for admins).
    public void sendStudentData(String email, String role, String firstName, String lastName, String password) {
        try {
            String msg = "ADD_STUDENT;" + email + ";" + role + ";" + firstName + ";" + lastName + ";" + password;
            out.writeObject(msg);
            out.flush();

            String response = (String) in.readObject();
            System.out.println("Server: " + response);

        } catch (IOException | ClassNotFoundException e) {
            e.toString();
        }

    }

    //Sending course data (for admins).
    public void sendCourseData(String courseCode, String courseTitle) {
        try {
            String msg = "ADD_COURSE;" + courseCode + ";" + courseTitle;
            out.writeObject(msg);
            out.flush();

            String response = (String) in.readObject();
            JOptionPane.showMessageDialog(null, response);

        } catch (IOException | ClassNotFoundException e) {
            e.toString();
        }
    }

    //Receiving and displaying available courses.
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try { 
            out.writeObject("LIST_COURSES");
            out.flush();

            Object response = in.readObject();
            System.out.println("Client received object: " + response.getClass().getName());

            if (response instanceof List<?> list) {
                for (Object o : list) {
                    if (o instanceof String[] arr && arr.length == 2) {
                        courses.add(new Course(0, arr[0], arr[1]));
                    }
                }
            } else {
                System.out.println("Unexpected repsonse: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.toString();
        }
        return courses;
    }

    // Sends a request to the server to retrieve all courses a specific student is enrolled in.
    public List<String> getEnrolledCourses(String studentEmail) {
        List<String> enrolled = new ArrayList<>();
        try {
            out.writeObject("STUDENT_COURSES;" + studentEmail);
            out.flush();

            Object response = in.readObject();

            if (response instanceof List<?>) {
                for (Object o : (List<?>) response) {
                    if (o instanceof String students) {
                        enrolled.add(students);
                    }
                }
            } else {
                System.out.println("Unexpected response: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return enrolled;
    }

    //Sends a request to the server to get all students enrolled in a specific course and returns the details
    public List<String[]> getStudentInCourse(String courseCode) {
        List<String[]> students = new ArrayList<>();
        try {
            out.writeObject("STUDENTS_IN_COURSE;" + courseCode);
            out.flush();

            Object response = in.readObject();
            if (response instanceof List<?> list) {
                for (Object o : list) {
                    if (o instanceof String[] arr && arr.length == 3) {
                        students.add(arr);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return students;
    }

    //Sending requests for all students.
    public List<Student> allStudentsRequest() {
        List<Student> student = new ArrayList<>();
        try {
            out.writeObject("LIST_STUDENTS");
            out.flush();

            Object response = in.readObject();

            if (response instanceof List<?>) {
                
                return (List<Student>) response;
                
            } else if (response instanceof String) {
                
                System.out.println("Server " + response);
                
            } else {
                System.out.println("Unexpected response: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.toString();
        }
        return null;
    }

    public static void main(String[] args) {

        Client client = new Client();
        LoginGUI loginGUI = new LoginGUI(client);
        loginGUI.setClient(client);
        loginGUI.setGUI();

    }
}
