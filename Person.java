import java.io.*;
import java.util.Objects;

/**
 * Project 5 - Person Class
 *
 * This sets the framework students and teachers
 *
 * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
 *
 * @version Monday, May 2, 2022
 *
 */
public class Person {

    // The fields for the person object
    private String username;
    private String password;
    public  boolean teacher;

    /**
     * Constructor for the person object
     *
     * @param username the username of the person
     * @param password the person's password
     * @param teacher is the person a teacher
     */
    public Person(String username, String password, boolean teacher) {
        this.username = username;
        this.password = password;
        this.teacher = teacher;
    }

    /**
     * Default constructor for the person class
     */
    public Person() {
        this.username = null;
        this.password = null;
        this.teacher = false;
    }

    /**
     * Changes the username of the person
     *
     * @param username the new username of the person
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Changes the password of the person
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the username of the person
     *
     * @return the username instance variable
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the person
     *
     * @return the password instance variable
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks to see if two objects are equal
     *
     * @param o the object to check against this object
     * @return T/F if the object is this object
     */
    @Override
    public boolean equals(Object o) { //might delete
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return teacher == person.teacher && username.equals(person.username) && password.equals(person.password);
    }

    /**
     * The toString method for people
     *
     * @return a String representation of a person
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (teacher) {
            sb.append("Teacher:\n");
        } else {
            sb.append("Student:\n");
        }
        sb.append(username + "\n");
        sb.append(password + "\n");
        return sb.toString();
    }

    /**
     * Adds the person to the account file
     *
     * @throws IOException exception thrown if the person is added improperly
     */
    public void addToFile() throws IOException {
        BufferedWriter bfw = new BufferedWriter(new FileWriter("Accounts.txt", true));
        if (teacher) {
            bfw.write("Teacher:\n");
        } else {
            bfw.write("Student:\n");
        }
        bfw.write(username + "\n");
        bfw.write(password + "\n");
        bfw.close();
    }
}
