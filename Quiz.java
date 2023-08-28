import java.util.*;
import java.io.*;

/**
 * Project 5 - Quiz Class
 *
 * This sets the framework for Quizzes
 *
 * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
 *
 * @version Monday, May 2nd, 2022
 *
 */
public class Quiz implements Serializable {

    // The field of the quiz - title, time limit, questions
    private String quizTitle;
    private int timeLimit;
    private ArrayList<Question> questions;

    /**
     * The constructor for the quiz
     *
     * @param quizTitle the quiz title
     * @param timeLimit the time limit
     * @param questions the questions in the quiz
     */
    public Quiz(String quizTitle, int timeLimit, ArrayList<Question> questions) {
        this.quizTitle = quizTitle;
        this.timeLimit = timeLimit;
        this.questions = questions;
    }

    /**
     * gets the title of the quiz
     *
     * @return the quizTitle
     */
    public String getQuizTitle() {
        return quizTitle;
    }

    /**
     * Writes teh quiz to the quiz file specified
     *
     * @param filename the name of the quiz
     * @throws IOException exception for improper adding to quiz
     */
    public void writeQuiz(String filename) throws IOException {
        BufferedWriter bfw = new BufferedWriter(new FileWriter(filename, true));
        bfw.write(quizTitle + "\n");
        bfw.write("Time limit: " + timeLimit + " minutes\n");
        for (int i = 0; i < questions.size(); i++) {
            bfw.write(i + 1 + "");
            bfw.write("\n");
            bfw.write(questions.get(i).toString());
        }
        bfw.write("End of Quiz\n");
        bfw.close();
    }

    /**
     * Gets the name of the quiz
     *
     * @return the name of the quiz
     */
    public String getName() {
        return quizTitle;
    }

    /**
     * Gets teh questions of the quiz
     *
     * @return the quiz questions
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Gets the time limit for the quiz
     *
     * @return the time limit instance variable in minutes
     */
    public int getTimeLimit() {
        return timeLimit;
    }

}
