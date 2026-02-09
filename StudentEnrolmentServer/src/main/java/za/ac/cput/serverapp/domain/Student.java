package za.ac.cput.serverapp.domain;

import java.io.Serializable;

/**
 *
 * @author Ryle 230333907
 * @author Damien Studnum here
 * @author David Studnum here
 */

//The Student Domain class represents a student entity in the system/ database
public class Student implements Serializable {

    private int userId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String password;
    private String role;//Damien: I added an exstra field here since I want the server to pass me the role

    //Conatructor
    public Student(int userId, String userEmail, String firstName, String lastName, String password, String role) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }
    //Getters
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

    public String getRole() {//Damien: added the get role method here 
        return role;
    }
}
