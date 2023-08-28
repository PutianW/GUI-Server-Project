import java.io.*;
import java.util.*;
import java.util.Random;
import java.util.Collections;

/**
 * Project 5 - Teacher Class
 *
 * This sets the framework for teachers
 *
 * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
 *
 * @version Monday, May 2nd, 2022
 *
 */
public class Teacher extends Person {

    /**
     * Constructs a teacher
     * @param username the teacher's system username
     * @param password the teacher's system password
     */

    public Teacher(String username, String password) {
        super(username, password, true);
    }

    /**
     * Default constructor for a teacher
     */
    public Teacher() {
        super(null, null, true);
    }

    /**
     * Checks to see if two objects are equal
     *
     * @param o the object to check against this object
     * @return T/F if the objects are the same
     */
    @Override
    public boolean equals(Object o) { //might delete
        if (o instanceof Teacher) {
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
