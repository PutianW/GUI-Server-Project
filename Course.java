import java.io.Serializable;
import java.util.*;

/**
 * Project 5 - Course Class
 *
 * This sets the framework for courses and allowing course by course access
 *
 * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
 *
 * @version Monday, May 2, 2022
 *
 */
public class Course implements Serializable {

    // Fields - name of course and quizzes within the course
    private String name;
    private ArrayList<String> quizNames;

    /**
     * Course constructor (name only)
     *
     * @param name the name of the course
     */
    public Course(String name) {
        this.name = name;
        this.quizNames = new ArrayList<>();
    }

    /**
     * Course Constructor
     *
     * @param name the name of the course
     * @param quizzes titles of the quizzes it contains
     */
    public Course(String name, ArrayList<String> quizzes) {
        this.name = name;
        this.quizNames = quizzes;
    }

    /**
     * Gets the quiz titles within the course
     *
     * @return the quiz title arrayList
     */
    public ArrayList<String> getQuizzes() {
        return this.quizNames;
    }

    /**
     * Gets the course name
     *
     * @return the course name
     */
    public String getCourseName() {
        return this.name;
    }

    /**
     * Adds a quiz title to the course
     *
     * @param quizName the title of the quiz to add
     */
    public void addQuiz(String quizName) {
        quizNames.add(quizName);
    }

    /**
     * Deletes a quiz title to the course
     *
     * @param quizName the title of the quiz to delete
     */
    public void deleteQuiz(String quizName) {
        ArrayList<String> qNames = new ArrayList<>();
        for (String quiz: quizNames) {
            if (!quizName.equals(quiz)) {
                qNames.add(quiz);
            }
        }
        this.quizNames = qNames;
    }

    /**
     * ToString method for the Course Object
     *
     * @return a string representation of a course
     */
    public String toString() {
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < quizNames.size(); i++) {
            names.append(quizNames.get(i)).append("\n");
        }
        return (name + "\n" + names);
    }
}
