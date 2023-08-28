import java.io.Serializable;

/**
 * Project 5 - Question Class
 *
 * Allows for individual questions in quizzes
 *
 * @author Ethan Weiss
 * @author Putian Wang
 * @author Jerry Mann
 * @author Adree Das
 *
 * @version 2022-05-02
 * @param <Answers> answer parameter
 */
public class Question<Answers> implements Serializable {

    // Question and potential answers
    private String question;
    private String[] answers;

    /**
     * Constructor for the question object
     *
     * @param question The question prompt
     * @param answers the potential responses
     */
    public Question(String question, String[] answers) {
        this.question = question;
        this.answers = answers;
    }

    /**
     * Returns the question
     *
     * @return get the question object
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the question prompt
     *
     * @param question the prompt to be answered
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * sets the answers prompts to the question
     *
     * @param answers the answers for the question
     */
    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    /**
     * Gets the answers for the question
     *
     * @return an array of the answers
     */
    public String[] getAnswers() {
        return answers;
    }

    /**
     * A toString representation of the question
     *
     * @return a string of the question
     */
    public String toString() {
        return question + "\na) " + answers[0] + "\nb) " + answers[1] + "\nc) "
                + answers[2] + "\nd) " + answers[3] + "\n";
    }
}
