package za.ac.cput.serverapp.domain;

import java.io.Serializable;

/**
 *
 * @author Ryle
 */

//The Course domain class represents a course in the system
public class Course implements Serializable {

    private int courseId;
    private String code;
    private String title;

    //Constructor
    public Course(int courseId, String code, String title) {
        this.courseId = courseId;
        this.code = code;
        this.title = title;
    }
    //Getters
    public int getCourseId() {
        return courseId;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}

