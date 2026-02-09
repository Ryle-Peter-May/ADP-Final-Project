package za.ac.cput.clientapp.domain;

import java.io.Serializable;

/**
 * Group 64
 * class: 2F
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class Student implements Serializable {

    private int userId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String password;
    private String role;    //Role field for the server to pass the role

    public Student(int userId, String userEmail, String firstName, String lastName, String password, String role) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return userEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() { 
        return role;
    }
}
