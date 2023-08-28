import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Project 5 - Student Class
 *
 * This sets the framework students and teachers
 *
 * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
 *
 * @version Monday, May 2nd, 2022
 *
 */
public class Student extends Person {

    /**
     * Constructs a student
     *
     * @param username the student's system username
     * @param password the student's password
     */
    public Student(String username, String password) {
        super(username, password, false);
    }

    /**
     * Default constructor
     */
    public Student() {
        super(null, null, false);
    }

    @Override
    /*
     * Checks if a given object is this object
     */
    public boolean equals(Object o) { //might delete
        if (o instanceof Student) {
            return super.equals(o);
        } else {
            return false;
        }
    }

    @Override
    public void addToFile() throws IOException {
        super.addToFile();
    }
}
