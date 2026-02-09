package za.ac.cput.clientapp.domain;

import java.io.Serializable;

/**
 * Group 64
 * class: 2F
 * @author David Daniel Sepkitt 240046935
 * @author Damien Nolan Swarts 222868791
 * @author Ryle Peter May 230333907
 */
public class Course implements Serializable {

    private int courseId;
    private String code;
    private String title;

    public Course(int courseId, String code, String title) {
        this.courseId = courseId;
        this.code = code;
        this.title = title;
    }

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

