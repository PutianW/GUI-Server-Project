import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Project 5 - Server Class
 *
 * This sets up the threaded and concurrent implementation of the project
 *
 * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
 *
 * @version Monday, May 2, 2022
 *
 */
class Server {

    private static final Object GATEKEEPERONE = new Object(); // for "QuizSubmissions" text file
    private static final Object GATEKEEPERTWO = new Object(); // for "GradedQuiz" text file
    private static final Object GATEKEEPERTHREE = new Object(); // for "Quiz" text file
    private static final Object GATEKEEPERFOUR = new Object(); // for "Course" text file
    private static final Object GATEKEEPERFIVE = new Object(); // for "Randomization" text file
    private static final Object GATEKEEPERSIX = new Object(); // for "Accounts" text file

    public static void main(String[] args) {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            // running infinite loop for getting client request
            while (true) {

                // socket object to receive incoming client requests
                Socket client = server.accept();
                //outputToClient = new ObjectOutputStream(client.getOutputStream());
                //inputFromClient = new ObjectInputStream(client.getInputStream());
                // Displaying that new client is connected
                //System.out.println("New client connected!");
                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                // This thread will handle the client separately
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Project 5 - ClientHandler Class
     *
     * This allows communication with the client
     *
     * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
     *
     * @version Monday, May 2, 2022
     *
     */
    private static class ClientHandler extends Thread implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {

            ObjectOutputStream outputToClient = null;
            ObjectInputStream inputFromClient = null;

            try {
                outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException ioexc) {
                ioexc.printStackTrace();
            }

            PrintWriter pw = null;
            BufferedReader bfr = null;
            boolean isTeacher = false;
            ArrayList<Course> courses = makeCourseArray();
            ArrayList<String> courseNames = new ArrayList<>();
            Person currentUser = null;
            Course selectedCourse = null;

            try {
                boolean notLoggedIn = true;
                while (notLoggedIn) {
                    pw = new PrintWriter(clientSocket.getOutputStream());
                    bfr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    ArrayList<Person> accounts = accountList();

                    /*String teacher = bfr.readLine();
                    System.out.println("teacher received");

                    System.out.println(teacher);
                    if (teacher.equals("teacher")) {
                        isTeacher = true;
                    }
                    */

                    // Login window
                    String choice = bfr.readLine();
                    String username;
                    String password;
                    switch (choice) {
                        case "Log In":
                            pw.flush();
                            accounts = accountList();

                            //System.out.println("Log In"); //delete
                            username = bfr.readLine();
                            password = bfr.readLine();

                            //System.out.println(username); //Print statements for testing (I assume)
                            //System.out.println(password);

                            for (int i = 0; i < accounts.size(); i++) {
                                if (accounts.get(i).getUsername().equals(username)) {
                                    if (accounts.get(i).getPassword().equals(password)) {
                                        pw.println("Logged in successfully!");
                                        pw.flush();
                                        // check if student or teacher
                                        if (accounts.get(i) instanceof Teacher) {
                                            currentUser = new Teacher(accounts.get(i).getUsername(),
                                                    accounts.get(i).getUsername());
                                            pw.println("true");
                                            pw.flush();
                                            isTeacher = true;
                                        } else if (accounts.get(i) instanceof Student) {
                                            //Should always be true, added instanceof for clarity
                                            currentUser = new Student(accounts.get(i).getUsername(),
                                                    accounts.get(i).getUsername());
                                            pw.println("false");
                                            pw.flush();
                                            isTeacher = false;
                                        }
                                        notLoggedIn = false;
                                    } else {
                                        pw.println("Incorrect password.");
                                        pw.flush();
                                    }
                                    break;
                                }
                                if (i == accounts.size() - 1) {
                                    pw.println("Invalid Account");
                                    pw.flush();
                                }
                            }
                            break;
                        case "Create Account":
                            //System.out.println("Create Account");
                            username = bfr.readLine();
                            password = bfr.readLine();
                            isTeacher = bfr.readLine().equals("Teacher");

                            /*System.out.println(username); //Print statements for testing (I assume)
                            System.out.println(password);
                            System.out.println(isTeacher);*/

                            if (isTeacher) {
                                try {
                                    Teacher t = new Teacher(username, password);
                                    t.addToFile();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                try {
                                    Student s = new Student(username, password);
                                    s.addToFile();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        case "Edit Account":
                            //System.out.println("Edit Account"); //Why is this print statement here?
                            username = bfr.readLine();
                            password = bfr.readLine();

                            //System.out.println(username); //Print statements for testing (I assume)
                            //System.out.println(password);

                            for (int i = 0; i < accounts.size(); i++) {
                                if (accounts.get(i).getUsername().equals(username)) {
                                    if (accounts.get(i).getPassword().equals(password)) {
                                        pw.println("Logged in successfully!");
                                        pw.flush();
                                        //detectDuplcateUsernames somewhere
                                        // check if student or teacher
                                        if (accounts.get(i) instanceof Teacher) {
                                            currentUser = new Teacher(accounts.get(i).getUsername(),
                                                    accounts.get(i).getUsername());
                                        } else if (accounts.get(i) instanceof Student) {
                                            //Should always be true, added instanceof for clarity
                                            currentUser = new Student(accounts.get(i).getUsername(),
                                                    accounts.get(i).getUsername());
                                        }
                                        //above might not be necessary, delete maybe?
                                        accounts = accountList();
                                        String editChoice = bfr.readLine();
                                        switch (editChoice) {
                                            case "New Username":
                                                String newUsername = bfr.readLine();
                                                accounts.get(i).setUsername(newUsername);
                                                pw.println("Success!");
                                                pw.flush();
                                                writeAccountList(accounts);
                                                break;
                                            case "New Password":
                                                String newPassword = bfr.readLine();
                                                accounts.get(i).setPassword(newPassword);
                                                pw.println("Success!");
                                                pw.flush();
                                                writeAccountList(accounts);
                                                break;
                                            case "Change Account Type":
                                                if (accounts.get(i) instanceof Teacher) {
                                                    Student newStudent = new Student(accounts.get(i).getUsername(),
                                                            accounts.get(i).getPassword());
                                                    accounts.set(i, newStudent);
                                                } else if (accounts.get(i) instanceof Student) {
                                                    //should always be true
                                                    Teacher newTeacher = new Teacher(accounts.get(i).getUsername(),
                                                            accounts.get(i).getPassword());
                                                    accounts.set(i, newTeacher);
                                                }
                                                pw.println("Success!");
                                                pw.flush();
                                                writeAccountList(accounts);
                                                break;
                                        }
                                    } else {
                                        pw.println("Incorrect password.");
                                        pw.flush();
                                    }
                                    break;
                                }
                                if (i == accounts.size() - 1) {
                                    pw.println("Invalid Account");
                                    pw.flush();
                                }
                            }
                            break;

                        case "Delete Account":
                            accounts = accountList();

                            //System.out.println("Delete Account"); //for testing
                            username = bfr.readLine();
                            password = bfr.readLine();

                            for (int i = 0; i < accounts.size(); i++) {
                                if (accounts.get(i).getUsername().equals(username)) {
                                    if (accounts.get(i).getPassword().equals(password)) {
                                        pw.println("Logged in successfully!");
                                        pw.flush();
                                        // check if student or teacher
                                        if (accounts.get(i) instanceof Teacher) {
                                            currentUser = new Teacher(accounts.get(i).getUsername(),
                                                    accounts.get(i).getUsername());
                                        } else if (accounts.get(i) instanceof Student) {
                                            //Should always be true, added instanceof for clarity
                                            currentUser = new Student(accounts.get(i).getUsername(),
                                                    accounts.get(i).getUsername());
                                        }
                                        accounts.remove(i);
                                        writeAccountList(accounts);
                                    } else {
                                        pw.println("Incorrect password.");
                                        pw.flush();
                                    }
                                    break;
                                }
                                if (i == accounts.size() - 1) {
                                    pw.println("Invalid Account");
                                    pw.flush();
                                }
                            }
                            break;

                        case "Exit":
                            break;
                    }
                }
                //Write log in info to file

                //System.out.println("Waiting");

                for (int i = 0; i < courses.size(); i++) {
                    courseNames.add(courses.get(i).getCourseName());
                }

                outputToClient.writeObject(courseNames);

                // Determine whether the client is a Teacher or a Student
                boolean run = true;

                if (isTeacher) {

                    // Teacher Part
                    while (run) {
                        String choice = bfr.readLine();
                        switch (choice) {
                            case "Refresh":
                                courses = makeCourseArray();
                                courseNames = new ArrayList<>();
                                for (int i = 0; i < courses.size(); i++) {
                                    courseNames.add(courses.get(i).getCourseName());
                                }

                                //System.out.println(courseNames);

                                for (int i = 0; i < courses.size(); i++) {
                                    if (courses.get(i).getCourseName().equals(selectedCourse.getCourseName())) {
                                        selectedCourse = courses.get(i);
                                    }
                                }

                                outputToClient.writeObject(courseNames);
                                //System.out.println("refreshed");
                                break;
                            case "Create Course":
                                String courseName = bfr.readLine();
                                selectedCourse = new Course(courseName);
                                courses.add(selectedCourse);
                                writeCourse(courses);
                                break;
                            case "Select Course":
                                courseName = bfr.readLine();
                                for (int i = 0; i < courses.size(); i++) {
                                    if (courses.get(i).getCourseName().equals(courseName)) {
                                        selectedCourse = courses.get(i);
                                    }
                                }
                                break;
                            case "Create Quiz":
                                //System.out.println("Create Quiz");
                                String quizName = bfr.readLine();
                                int timeLimit = Integer.parseInt(bfr.readLine());

                                //System.out.println(quizName);
                                //System.out.println(timeLimit);

                                // Determine if the name of the quiz has already been used
                                boolean hasDuplicateName = false;
                                ArrayList<Quiz> quizList = makeQuizArray("Quiz.txt");
                                for (Quiz quiz : quizList) {
                                    if (quiz.getQuizTitle().equals(quizName)) {
                                        hasDuplicateName = true;
                                    }
                                }
                                pw.println(hasDuplicateName);
                                pw.flush();

                                if (!hasDuplicateName) {
                                    // Create question arraylist here
                                    ArrayList<Question> newQuestions = new ArrayList<>();

                                    while (true) {
                                        String action = bfr.readLine();
                                        if (action.equals("Add Question")) {

                                            String question = bfr.readLine();
                                            String answer_1 = bfr.readLine();
                                            String answer_2 = bfr.readLine();
                                            String answer_3 = bfr.readLine();
                                            String answer_4 = bfr.readLine();
                                            String[] answers = {answer_1, answer_2, answer_3, answer_4};

                                            // Add question to the questions arrayList
                                            newQuestions.add(new Question<>(question, answers));

                                        } else if (action.equals("Complete")) {
                                            //System.out.println("Complete");
                                            break;
                                        }

                                        // Create and write out quiz object here (Synchronized)

                                        Quiz newQuiz = new Quiz(quizName, timeLimit, newQuestions);
                                        quizList.add(newQuiz);

                                        // Write the quiz to Courses.txt
                                        selectedCourse.addQuiz(quizName);
                                        writeCourse(courses);

                                        // Write the quiz to randomization.txt
                                        synchronized (GATEKEEPERFIVE) {
                                            try {
                                                BufferedWriter fbfw =
                                                        new BufferedWriter(new FileWriter("Randomization.txt",
                                                                true));

                                                fbfw.write(quizName);
                                                fbfw.newLine();
                                                fbfw.write("false");
                                                fbfw.newLine();

                                                fbfw.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        // Write the quiz to Quiz.txt
                                        synchronized (GATEKEEPERTHREE) {
                                            try {
                                                BufferedWriter bfw = new BufferedWriter(new FileWriter(
                                                        "Quiz.txt", false));
                                                bfw.flush();
                                                for (int i = 0; i < quizList.size(); i++) {
                                                    quizList.get(i).writeQuiz("Quiz.txt");
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                break;
                            case "Import Quiz":

                                if (!bfr.readLine().equals("break")) {
                                    try {
                                        ArrayList<String> list = (ArrayList<String>) inputFromClient.readObject();
                                        int time;
                                        ArrayList<Question> questions = new ArrayList<>();
                                        String quizTitle = list.get(0);
                                        if (quizTitle.equals("")) {
                                            throw new Exception();
                                        }
                                        ArrayList<Quiz> quizzes = makeQuizArray("Quiz.txt");
                                        for (int i = 1; i < list.size(); i += 5) {
                                            String question = list.get(i);
                                            String[] answers = new String[4];
                                            answers[0] = list.get(i + 1);
                                            answers[1] = list.get(i + 2);
                                            answers[2] = list.get(i + 3);
                                            answers[3] = list.get(i + 4);
                                            questions.add(new Question(question, answers));
                                        }
                                        pw.println("no format issue");
                                        pw.flush();

                                        // Determine if the name of the quiz has already been used
                                        hasDuplicateName = false;
                                        for (Quiz quiz : quizzes) {
                                            if (quiz.getQuizTitle().equals(quizTitle)) {
                                                hasDuplicateName = true;
                                            }
                                        }

                                        if (hasDuplicateName) {
                                            pw.println("true");
                                            pw.flush();
                                        } else {
                                            pw.println("false");
                                            pw.flush();

                                            time = Integer.parseInt(bfr.readLine());

                                            Quiz newQuiz = new Quiz(quizTitle, time, questions);
                                            quizzes.add(newQuiz);

                                            // Write the quiz to Courses.txt
                                            selectedCourse.addQuiz(quizTitle);
                                            writeCourse(courses);

                                            // Write the quiz to randomization.txt
                                            synchronized (GATEKEEPERFIVE) {
                                                try {
                                                    BufferedWriter fbfw =
                                                            new BufferedWriter(new FileWriter(
                                                                    "Randomization.txt", true));

                                                    fbfw.write(quizTitle);
                                                    fbfw.newLine();
                                                    fbfw.write("false");
                                                    fbfw.newLine();

                                                    fbfw.close();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            // Write the quiz to Quiz.txt
                                            synchronized (GATEKEEPERTHREE) {
                                                try {
                                                    BufferedWriter bfw = new BufferedWriter(new FileWriter(
                                                            "Quiz.txt", false));
                                                    bfw.flush();
                                                    for (int i = 0; i < quizzes.size(); i++) {
                                                        quizzes.get(i).writeQuiz("Quiz.txt");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        }

                                    } catch (Exception e) {
                                        pw.println("format issue");
                                        pw.flush();
                                    }
                                }

                                break;
                            case "Edit Quiz":
                                //System.out.println("Edit received");//test

                                String filename = "Quiz.txt";
                                ArrayList<Quiz> quizzes = makeQuizArray(filename);

                                String QuizTitle = bfr.readLine();

                                //System.out.println("received name");//test

                                if (quizzes.size() == 0) {
                                    pw.println(0);
                                    pw.flush();//empty check
                                } else {
                                    pw.println(1);
                                    pw.flush();//ok
                                }
                                //System.out.println("Empty check sent");//test
                                ArrayList<String> titles = new ArrayList<>();
                                int num = 1;
                                for (Quiz quiz : quizzes) {
                                    titles.add(quiz.getQuizTitle());
                                    num++;
                                }
                                //System.out.println(titles);

                                int count = 0;

                                for (int i = 0; i < titles.size(); i++) {
                                    if (selectedCourse.getQuizzes().get(i).equals(QuizTitle)) {
                                        count++;//quiz check
                                        break;
                                    }
                                }

                                if (count > 0) {
                                    pw.println(2);
                                    pw.flush();
                                } else {
                                    pw.println(3);
                                    pw.flush();
                                }

                                int number = Integer.parseInt(bfr.readLine());

                                for (int i = 0; i < quizzes.size(); i++) {
                                    if (quizzes.get(i).getQuizTitle().equals(QuizTitle)) {
                                        if (number > quizzes.get(i).getQuestions().size()) {
                                            pw.println(0);
                                            pw.flush();
                                        } else {
                                            pw.println(1);
                                            pw.flush();
                                        }
                                    }
                                }

                                //System.out.println(QuizTitle);//test
                                //System.out.println(number);//test

                                String action = bfr.readLine();
                                //System.out.println(action);//test

                                if (action.equals("Edit Question")) {
                                    String questionTitle = bfr.readLine();
                                    String a1 = bfr.readLine();
                                    String a2 = bfr.readLine();
                                    String a3 = bfr.readLine();
                                    String a4 = bfr.readLine();
                                    String[] answers = new String[4];
                                    answers[0] = a1;
                                    answers[1] = a2;
                                    answers[2] = a3;
                                    answers[3] = a4;

                                    for (int i = 0; i < quizzes.size(); i++) { // editing the question
                                        if (quizzes.get(i).getQuizTitle().equals(QuizTitle)) {
                                            ArrayList<Question> questions = quizzes.get(i).getQuestions();
                                            questions.get(number - 1).setQuestion(questionTitle);
                                            questions.get(number - 1).setAnswers(answers);
                                        }
                                    }

                                    synchronized (GATEKEEPERTHREE) {
                                        try {
                                            BufferedWriter bfw = new BufferedWriter(new FileWriter(
                                                    filename, false));
                                            bfw.flush();
                                            for (int i = 0; i < quizzes.size(); i++) {
                                                quizzes.get(i).writeQuiz(filename);
                                                //rewriting edited quiz arraylist to file
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;

                                } else if (action.equals("Exit")) {
                                    //System.out.println("Exit");
                                }
                                break;

                            case "Delete Quiz":
                                //System.out.println("Delete Quiz");//test

                                filename = "Quiz.txt";
                                quizzes = makeQuizArray(filename, selectedCourse);
                                ArrayList<Quiz> actualQuizzes = makeQuizArray(filename);

                                titles = new ArrayList<>();
                                num = 1;
                                for (Quiz quiz : quizzes) {
                                    titles.add(quiz.getQuizTitle());
                                    num++;
                                }
                                outputToClient.writeObject(titles);

                                while (true) {

                                    action = bfr.readLine();

                                    if (action.equals("Delete Quiz")) {
                                        String deletedQuiz = bfr.readLine();
                                        int index = -1;
                                        for (int i = 0; i < actualQuizzes.size(); i++) {
                                            if (actualQuizzes.get(i).getQuizTitle().equals(deletedQuiz)) {
                                                actualQuizzes.remove(i);
                                            }
                                        }

                                        for (int i = 0; i < selectedCourse.getQuizzes().size(); i++) {
                                            if (selectedCourse.getQuizzes().get(i).equals(deletedQuiz)) {
                                                selectedCourse.getQuizzes().remove(i);
                                            }
                                        }

                                        writeCourse(courses);//updating course file

                                        for (Quiz q: actualQuizzes) {
                                            //System.out.println(q.getQuizTitle());
                                        }

                                        synchronized (GATEKEEPERTHREE) {
                                            try {
                                                BufferedWriter bfw = new BufferedWriter(new FileWriter(
                                                        filename, false));
                                                bfw.flush();
                                                for (int i = 0; i < actualQuizzes.size(); i++) {
                                                    actualQuizzes.get(i).writeQuiz(filename);
                                                    //rewriting edited quiz arraylist to file
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        break;
                                    } else if (action.equals("Exit")) {
                                        //System.out.println("Exit");
                                        break;
                                    } else if (action.equals("Refresh")) {
                                        courses = makeCourseArray();
                                        for (int i = 0; i < courses.size(); i++) {
                                            if (courses.get(i).getCourseName().equals(
                                                    selectedCourse.getCourseName())) {
                                                selectedCourse = courses.get(i);
                                            }
                                        }
                                        ArrayList<String> currentQuizzes = selectedCourse.getQuizzes();
                                        outputToClient.writeObject(currentQuizzes);
                                        break;
                                    }
                                }
                                break;

                            case "View Submissions":
                                //System.out.println("View Submissions");//test
                                boolean viewRun = true;
                                filename = "Quiz.txt";
                                quizzes = makeQuizArray(filename, selectedCourse);
                                titles = new ArrayList<>();
                                num = 1;
                                for (Quiz quiz : quizzes) {
                                    titles.add(quiz.getQuizTitle());
                                    num++;
                                }
                                //System.out.println(titles);//test
                                outputToClient.writeObject(titles);

                                quizName = bfr.readLine();
                                //System.out.println(quizName);//test

                                ArrayList<String> output = new ArrayList<>();
                                ArrayList<String> quizSubmissions = new ArrayList<>();

                                synchronized (GATEKEEPERONE) {
                                    try {
                                        BufferedReader nbfr = new BufferedReader(new FileReader(
                                                "QuizSubmissions.txt"));
                                        String line;
                                        while (true) {
                                            line = nbfr.readLine();
                                            if (line == null) {
                                                break;
                                            } else {
                                                quizSubmissions.add(line);
                                            }
                                        }

                                        for (int i = 0; i < quizSubmissions.size(); i += 8) {
                                            if (quizSubmissions.get(i).equals(quizName)) {
                                                if (quizSubmissions.get(i + 1) == null) {
                                                    break;
                                                }
                                                output.add(quizSubmissions.get(i + 1));
                                            }
                                        }
                                        nbfr.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                //System.out.println(output);//test
                                //System.out.println(quizSubmissions);//test
                                outputToClient.writeObject(output);
                                //System.out.println("Sent" + output);//test


                                while (viewRun) {

                                    action = bfr.readLine();
                                    ArrayList<String> viewable = new ArrayList<>();

                                    if (action.equals("View Submission")) {

                                        String studentName = bfr.readLine();
                                        int i = 0;
                                        int j = 0;

                                        do {
                                            if (quizSubmissions.get(i).equals(quizName)) {
                                                if (quizSubmissions.get(i + 1).equals(studentName)) {
                                                    i = i + 4;
                                                    do {
                                                        viewable.add("Question" + (j + 1) + ": " +
                                                                quizSubmissions.get(i));
                                                        i++;
                                                        j++;
                                                    } while (!quizSubmissions.get(i).equals("End of Quiz"));
                                                    outputToClient.writeObject(viewable);
                                                    //System.out.println("Sent" + viewable);//test
                                                    break;
                                                }
                                            }
                                            i++;
                                        } while (i < quizSubmissions.size());

                                    } else if (action.equals("Exit")) {

                                        viewRun = false;
                                        //System.out.println("Exit");
                                        break;

                                    } else if (action.equals("Grade Quiz")) {
                                        //System.out.println("grading");

                                        String studentName = bfr.readLine();
                                        //System.out.println("received name: " + studentName);//test
                                        String answerFile = bfr.readLine();
                                        //System.out.println("received file: " + answerFile);//test

                                        synchronized (GATEKEEPERTWO) {
                                            try {
                                                ArrayList<String> answers = new ArrayList<>();

                                                File a = new File(answerFile);
                                                FileReader ar = new FileReader(a);
                                                BufferedReader bar = new BufferedReader(ar);
                                                while (true) {
                                                    String line = bar.readLine();
                                                    if (line != null) {
                                                        answers.add(line);
                                                    } else {
                                                        break;
                                                    }
                                                }
                                                bar.close();
                                                //System.out.println(answers);

                                                //grading

                                                int totalPoints = 0;
                                                BufferedWriter writer = new BufferedWriter(new FileWriter(
                                                        "GradedQuizzes.txt", true));
                                                writer.newLine();
                                                writer.write(quizName);
                                                writer.newLine();
                                                writer.write(studentName);
                                                writer.newLine();
                                                int i = 0;
                                                int j = 0;
                                                do {
                                                    if (quizSubmissions.get(i).equals(quizName)) {
                                                        if (quizSubmissions.get(i + 1).equals(studentName)) {
                                                            i = i + 4;
                                                            do {
                                                                try {
                                                                    if (quizSubmissions.get(i).
                                                                            equalsIgnoreCase(answers.get(j))) {
                                                                        String correct = String.format(
                                                                                "Question %s: correct", j + 1);
                                                                        writer.write(correct);
                                                                        writer.newLine();
                                                                        writer.write("Points: 1");
                                                                        writer.newLine();
                                                                        totalPoints++;
                                                                    } else if (quizSubmissions.get(i).equals("N/G")) {

                                                                    } else {
                                                                        String incorrect = String.format(
                                                                                "Question%s : incorrect", j + 1);
                                                                        String correctAnswer = String.format(
                                                                                "Correct answer: %s", answers.get(j));
                                                                        writer.write(incorrect);
                                                                        writer.newLine();
                                                                        writer.write("Points: 0");
                                                                        writer.newLine();
                                                                        writer.write(correctAnswer);
                                                                        writer.newLine();
                                                                    }
                                                                    j++;
                                                                    i++;
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    //System.out.println("Error grading quiz!");
                                                                }
                                                            } while (!quizSubmissions.get(i).equals("End of Quiz"));
                                                            writer.write("Total points: " + totalPoints);
                                                            writer.newLine();
                                                            writer.write("End of Quiz");
                                                            writer.newLine();
                                                            writer.close();
                                                            pw.println(1);//send confirmation
                                                            pw.flush();
                                                            //System.out.println("Sent 1");
                                                            break;
                                                        }
                                                    }
                                                    i++;
                                                } while (i < quizSubmissions.size());
                                            } catch (Exception e) {
                                                pw.println(0);//send error
                                                pw.flush();
                                                //System.out.println("Sent 0");
                                            }
                                        }
                                    }
                                    //System.out.println("I'm here"); //test
                                }
                                break;
                            case "Randomize Quizzes":

                                ArrayList<String> currentQuizzes = null;

                                if (selectedCourse != null) {
                                    currentQuizzes = selectedCourse.getQuizzes();
                                }
                                outputToClient.writeObject(currentQuizzes);

                                boolean randRun = true;
                                while (randRun) {
                                    String randChoice = bfr.readLine();
                                    switch (randChoice) {
                                        case "Confirm":
                                            String selectedQuizName = bfr.readLine();
                                            synchronized (GATEKEEPERFIVE) {
                                                try {

                                                    ArrayList<String> list = readRandomizationFile();
                                                    BufferedWriter fbfw =
                                                            new BufferedWriter(new FileWriter(
                                                                    "Randomization.txt", false));

                                                    for (int i = 0; i < list.size(); i += 2) {
                                                        if (list.get(i).equals(selectedQuizName)) {
                                                            list.set(i + 1, "true");
                                                        }
                                                    }

                                                    for (String l : list) {
                                                        fbfw.write(l);
                                                        fbfw.newLine();
                                                    }

                                                    fbfw.close();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            randRun = false;
                                            break;
                                        case "Exit":
                                            //System.out.println("Exit");
                                            randRun = false;
                                            break;
                                        case "Refresh":
                                            //System.out.println("Refresh");
                                            courses = makeCourseArray();
                                            for (int i = 0; i < courses.size(); i++) {
                                                if (courses.get(i).getCourseName().
                                                        equals(selectedCourse.getCourseName())) {
                                                    selectedCourse = courses.get(i);
                                                }
                                            }
                                            currentQuizzes = selectedCourse.getQuizzes();
                                            outputToClient.writeObject(currentQuizzes);
                                            break;
                                    }
                                }

                                break;
                            case "Exit":
                                //System.out.println("Exit");
                                run = false;
                                break;
                        }
                    }

                } else {

                    // Student Part
                    Course currentCourse = null;
                    Quiz currentQuiz = null;
                    ArrayList<Quiz> courseQuizzes;
                    String currentCourseName;
                    ArrayList<ArrayList<Integer>> randomizedChoices = null;
                    ArrayList<Integer> randomizedQuestions = null;

                    boolean randomized = false;
                    while (run) {
                        String studentChoice = bfr.readLine();
                        ArrayList<Course> availableCourses = makeCourseArray();

                        switch (studentChoice) {
                            case "Select Course":
                                currentCourseName = bfr.readLine();
                                for (int i = 0; i < courses.size(); i++) {
                                    if (availableCourses.get(i).getCourseName().equals(currentCourseName)) {
                                        currentCourse = courses.get(i);
                                    }
                                }
                                break;

                            case "Take Quiz":
                                // Send the current courses quizzes to the client
                                outputToClient.writeObject(makeQuizArray("Quiz.txt", currentCourse));
                                outputToClient.flush();
                                break;

                            case "Exit":
                                return;

                            case "Select Quiz":
                                studentChoice = bfr.readLine();
                                courseQuizzes = makeQuizArray("Quiz.txt", currentCourse);
                                for (int i = 0; i < courseQuizzes.size(); i++) {
                                    if (courseQuizzes.get(i).getQuizTitle().equals(studentChoice)) {
                                        currentQuiz = courseQuizzes.get(i);
                                    }
                                }
                                outputToClient.writeObject(currentQuiz);
                                outputToClient.flush();
                                break;

                            case "Attempt Quiz":
                                ArrayList<String> randomizedQuizValues = readRandomizationFile();
                                for (int i = 0; i < randomizedQuizValues.size(); i += 2) {
                                    if (randomizedQuizValues.get(i).equals(currentQuiz.getQuizTitle())) {
                                        String randomReadString = randomizedQuizValues.get(i + 1);
                                        if (randomReadString.equals("true")) {
                                            randomized = true;
                                        } else {
                                            randomized = false;
                                        }
                                    }
                                }

                                randomizedQuestions = randomizeQuiz(currentQuiz.getQuestions().size(), randomized);
                                randomizedChoices = randomizeChoices(currentQuiz, randomized);

                                outputToClient.writeObject(randomizedQuestions);
                                outputToClient.flush();
                                outputToClient.writeObject(randomizedChoices);
                                outputToClient.flush();
                                break;

                            case "Submit Quiz":
                                ArrayList<String> quizResponses = null;
                                Date startDate = null;
                                Date endDate = null;
                                try {
                                    quizResponses = (ArrayList<String>) inputFromClient.readObject();
                                    startDate = (Date) inputFromClient.readObject();
                                    endDate = (Date) inputFromClient.readObject();
                                } catch (ClassNotFoundException | IOException exc) {
                                    exc.printStackTrace();
                                }

                                ArrayList<String> decodedResponses = decodeResponses(quizResponses,
                                        randomizedChoices, randomizedQuestions);

                                writeSubmission(startDate, endDate, decodedResponses,
                                        (Student) currentUser ,currentQuiz);
                                break;

                            case "View Graded":
                                String quizTitle = bfr.readLine();
                                String name = ((Student) currentUser).getUsername();

                                BufferedReader buff = new BufferedReader(new FileReader("GradedQuizzes.txt"));
                                ArrayList<String> gradedQuizArray = new ArrayList<>();
                                String lineRead;

                                do {
                                    do {
                                        lineRead = buff.readLine();
                                        if (lineRead != null){
                                            gradedQuizArray.add(lineRead);
                                        }
                                    } while (lineRead != null);
                                    lineRead = buff.readLine();
                                    if (lineRead == null) {
                                        break;
                                    } else {
                                        gradedQuizArray.add(lineRead);
                                    }
                                } while (true);

                                int index = 0;
                                ArrayList<String> titleSorted = new ArrayList<>();

                                do {
                                    if (gradedQuizArray.get(index).equals(quizTitle)) {
                                        do {
                                            titleSorted.add(gradedQuizArray.get(index));
                                            index++;
                                        } while (!gradedQuizArray.get(index).equals("End of Quiz"));
                                    } else {
                                        index++;
                                    }
                                } while (index < gradedQuizArray.size());

                                ArrayList<String> nameSorted = new ArrayList<>();
                                index = 0;

                                do {
                                    if (titleSorted.get(index).equals(name)) {
                                        do {
                                            nameSorted.add(titleSorted.get(index));
                                            index++;
                                        } while (index < titleSorted.size() &&
                                                !titleSorted.get(index).equals(quizTitle));
                                    } else {
                                        index++;
                                    }
                                } while (index < titleSorted.size());

                                outputToClient.writeObject(nameSorted);
                                outputToClient.writeObject(currentUser.getUsername());
                                break;

                            default:
                                break;

                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pw != null) {
                        pw.close();
                    }
                    if (bfr != null) {
                        bfr.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ArrayList<Person> accountList() throws IOException {
        synchronized (GATEKEEPERSIX) {
            ArrayList<Person> accounts = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader("Accounts.txt"));
            //Might have to change to your own file directory
            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.equals("Teacher:")) {
                    Teacher t = new Teacher();
                    line = bfr.readLine();
                    t.setUsername(line);
                    line = bfr.readLine();
                    t.setPassword(line);
                    accounts.add(t);
                } else if (line.equals("Student:")) {
                    Student s = new Student();
                    line = bfr.readLine();
                    s.setUsername(line);
                    line = bfr.readLine();
                    s.setPassword(line);
                    accounts.add(s);
                }
            }
            bfr.close();
            return accounts;
        }
    }

    public static void writeAccountList(ArrayList<Person> accountList) throws IOException {
        synchronized (GATEKEEPERSIX) {
            BufferedWriter bfw = new BufferedWriter(new FileWriter("Accounts.txt", false));
            //Make sure to change to your own directory when compiling locally
            for (Person p : accountList) {
                bfw.write(p.toString());
            }
            bfw.close();
        }
    }

    public static ArrayList<String> readRandomizationFile() {
        synchronized (GATEKEEPERFIVE) {
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("Randomization.txt"));
                ArrayList<String> list = new ArrayList<>();
                while (true) {
                    String line = bfr.readLine();
                    if (line == null) {
                        break;
                    } else {
                        list.add(line);
                    }
                }
                bfr.close();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static ArrayList<Course> makeCourseArray() {
        synchronized (GATEKEEPERFOUR) {
            BufferedReader br;
            ArrayList<String> entries = new ArrayList<>();
            ArrayList<Course> courses = new ArrayList<>();
            try {
                br = new BufferedReader(new FileReader("Courses.txt"));
                while (true) {
                    String line = br.readLine();
                    if (line != null) {
                        entries.add(line);
                    } else {
                        break;
                    }
                }

                if (entries.size() == 0) {
                    return null;
                }

                Course course;
                ArrayList<String> quizNameList = new ArrayList<>();
                String courseName = "";
                for (int i = 0; i < entries.size(); i++) {
                    if (entries.get(i).equals("End of Course")) {
                        if (quizNameList.size() == 0) {
                            course = new Course(courseName);
                        } else {
                            course = new Course(courseName);
                            for (int j = 0; j < quizNameList.size(); j++) {
                                course.addQuiz(quizNameList.get(j));
                            }
                        }
                        courseName = "";
                        quizNameList.clear();
                        courses.add(course);
                    } else if (i == 0 || entries.get(i - 1).equals("End of Course")) {
                        courseName = entries.get(i);
                    } else {
                        quizNameList.add(entries.get(i));
                    }
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                //System.out.println("No courses! Please make a course to continue.");
                return null;
            }
            if (courses.size() == 0) {
                //System.out.println("No courses! Please make a course to continue.");
                return null;
            }
            return courses;
        }
    }

    public static ArrayList<Quiz> makeQuizArray(String filename, Course course)  {
        synchronized (GATEKEEPERTHREE) {
            try {
                // reading the file containing the quiz
                File f = new File(filename);
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                ArrayList<String> list = new ArrayList<>();
                while (true) {
                    String line = bfr.readLine();
                    if (line != null) {
                        list.add(line);
                    } else {
                        break;
                    }
                }
                bfr.close();

                ArrayList<Quiz> quizArray = new ArrayList<>();
                int i = 0;
                // creating the quizzes
                do {
                    boolean isFirstQuestion = true;
                    ArrayList<Question> questionArray = new ArrayList<>();
                    if (list.size() == 0) {
                        return null;
                    }
                    String title = list.get(i);
                    String[] timeString = list.get(i + 1).split(" ");
                    int time = Integer.parseInt(timeString[2]);
                    do {
                        String[] answers = new String[4];
                        if (isFirstQuestion) {
                            answers[0] = list.get(i + 4).substring(3);
                            answers[1] = list.get(i + 5).substring(3);
                            answers[2] = list.get(i + 6).substring(3);
                            answers[3] = list.get(i + 7).substring(3);
                            Question ques = new Question(list.get(i + 3), answers);
                            questionArray.add(ques);
                            i = i + 8;
                            isFirstQuestion = false;
                        } else {
                            answers[0] = list.get(i + 2).substring(3);
                            answers[1] = list.get(i + 3).substring(3);
                            answers[2] = list.get(i + 4).substring(3);
                            answers[3] = list.get(i + 5).substring(3);
                            Question ques = new Question(list.get(i + 1), answers);
                            questionArray.add(ques);
                            i = i + 6;
                        }

                    } while (!list.get(i).equals("End of Quiz"));
                    i = i + 1;
                    Quiz quiz = new Quiz(title, time, questionArray);
                    quizArray.add(quiz);
                } while (i < list.size());


                //Filter quizzes for the specific course
                ArrayList<Quiz> output = new ArrayList<>();
                for (Quiz quiz : quizArray) {
                    for (String quizName : course.getQuizzes()) {
                        if (quiz.getQuizTitle().equals(quizName)) {
                            output.add(quiz);
                        }
                    }
                }

                return output;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void writeCourse(ArrayList<Course> courses) {
        synchronized (GATEKEEPERFOUR) {
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new FileWriter("Courses.txt", false));
                for (int i = 0; i < courses.size(); i++) {
                    bw.write(courses.get(i).toString());
                    bw.write("End of Course");
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<Quiz> makeQuizArray(String filename) {
        synchronized (GATEKEEPERTHREE) {
            try {
                // reading the file containing the quiz
                File f = new File(filename);
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                ArrayList<String> list = new ArrayList<>();
                while (true) {
                    String line = bfr.readLine();
                    if (line != null) {
                        list.add(line);
                    } else {
                        break;
                    }
                }
                bfr.close();

                ArrayList<Quiz> quizArray = new ArrayList<>();
                int i = 0;
                // creating the quizzes
                do {
                    boolean isFirstQuestion = true;
                    ArrayList<Question> questionArray = new ArrayList<>();
                    if (list.size() == 0) {
                        return null;
                    }
                    String title = list.get(i);
                    String[] timeString = list.get(i + 1).split(" ");
                    int time = Integer.parseInt(timeString[2]);
                    do {
                        String[] answers = new String[4];
                        if (isFirstQuestion) {
                            answers[0] = list.get(i + 4).substring(3);
                            answers[1] = list.get(i + 5).substring(3);
                            answers[2] = list.get(i + 6).substring(3);
                            answers[3] = list.get(i + 7).substring(3);
                            Question ques = new Question(list.get(i + 3), answers);
                            questionArray.add(ques);
                            i = i + 8;
                            isFirstQuestion = false;
                        } else {
                            answers[0] = list.get(i + 2).substring(3);
                            answers[1] = list.get(i + 3).substring(3);
                            answers[2] = list.get(i + 4).substring(3);
                            answers[3] = list.get(i + 5).substring(3);
                            Question ques = new Question(list.get(i + 1), answers);
                            questionArray.add(ques);
                            i = i + 6;
                        }

                    } while (!list.get(i).equals("End of Quiz"));
                    i = i + 1;
                    Quiz quiz = new Quiz(title, time, questionArray);
                    quizArray.add(quiz);
                } while (i < list.size());
                return quizArray;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Returns a randomized list of integers from 1 to the number of questions
     *
     * @param numQuestions the number of questions being randomized
     * @param randomized if the quiz is randomized
     * @return an arraylist of shuffled integer question indices
     */
    private static ArrayList<Integer> randomizeQuiz(int numQuestions, boolean randomized) {
        ArrayList<Integer> shuffler = new ArrayList<>(numQuestions);
        for (int i = 0; i < numQuestions; i++) {
            shuffler.add(i);
        }

        // randomize if randomized otherwise keep in order
        if (randomized) {
            Collections.shuffle(shuffler);
        }

        return shuffler;
    }

    /**
     * Decodes multiple choice responses to match the unscrambled test
     *
     * @param responses the encoded responses
     * @param shuffledChoices the array of shuffled integers corresponding to the responses
     * @return the decoded responses
     */
    private static ArrayList<String> decodeResponses(ArrayList<String> responses,
                                                     ArrayList<ArrayList<Integer>> shuffledChoices,
                                                     ArrayList<Integer> shuffledQuestions) {
        ArrayList<String> decodedResponses = new ArrayList<>(responses.size());
        int index;
        for (int i = 0; i < responses.size(); i++) {
            try {
                switch (responses.get(i).toUpperCase()) { // get the index for the correct response
                    case "A":
                        index = shuffledChoices.get(shuffledQuestions.indexOf(i)).get(0);
                        break;
                    case "B":
                        index = shuffledChoices.get(shuffledQuestions.indexOf(i)).get(1);
                        break;
                    case "C":
                        index = shuffledChoices.get(shuffledQuestions.indexOf(i)).get(2);
                        break;
                    case "D":
                        index = shuffledChoices.get(shuffledQuestions.indexOf(i)).get(3);
                        break;
                    default:
                        decodedResponses.add(responses.get(i));
                        continue;
                }
                switch (index) { // add response based on index
                    case 0:
                        decodedResponses.add("a");
                        break;
                    case 1:
                        decodedResponses.add("b");
                        break;
                    case 2:
                        decodedResponses.add("c");
                        break;
                    case 3:
                        decodedResponses.add("d");
                        break;
                }
            } catch (Exception e) { // probably indexOutOfBounds
                decodedResponses.add(responses.get(i));
                e.printStackTrace();
            }
        }
        return decodedResponses;
    }

    /**
     * Shuffles the choices in a quiz's questions
     *
     * @param quiz The quiz of randomized questions
     * @param randomized if the quiz is randomized
     * @return An arraylist of randomized integers
     */
    private static ArrayList<ArrayList<Integer>> randomizeChoices(Quiz quiz, boolean randomized) {
        int numQuestions = quiz.getQuestions().size();
        ArrayList<ArrayList<Integer>> shuffledChoices = new ArrayList<>(numQuestions);

        if (randomized) {
            for (int i = 0; i < numQuestions; i++) { // populate each entry in arraylist
                ArrayList<Integer> shuffledQuestion = new ArrayList<>(quiz.getQuestions().get(i).getAnswers().length);
                for (int j = 0; j < quiz.getQuestions().get(i).getAnswers().length; j++) {
                    shuffledQuestion.add(j);
                }
                Collections.shuffle(shuffledQuestion); // randomize order of responses
                shuffledChoices.add(shuffledQuestion); // add question by question
            }
        } else {
            for (int i = 0; i < numQuestions; i++) { // populate each entry in arraylist
                ArrayList<Integer> shuffledQuestion = new ArrayList<>(quiz.getQuestions().get(i).getAnswers().length);
                for (int j = 0; j < quiz.getQuestions().get(i).getAnswers().length; j++) {
                    shuffledQuestion.add(j);
                }
                shuffledChoices.add(shuffledQuestion); // add question by question
            }
        }
        return shuffledChoices;
    }

    /**
     * Writes the quiz to the quizSubmissions.txt file
     *
     * @param startDate the start time of the quiz
     * @param endDate the time the student submitted the quiz
     * @param responses the student's responses
     * @param s the student object
     * @param quiz the quiz object
     */
    private static void writeSubmission(Date startDate, Date endDate, ArrayList<String> responses,
                                        Student s, Quiz quiz) {
        /*
         * Write file format
         *
         * Quiz Title , i = 0
         * student username, i = 1
         * start timestamp, i = 2
         * end timestamp, i = 3
         * q1 response, i = 4
         * q2 response i = 5
         * N/G          // for questions not pulled from the q-bank
         * q4 response
         * .
         * .
         * .
         * End of Quiz
         */
        synchronized (GATEKEEPERONE) {
            BufferedWriter writer;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                writer = new BufferedWriter(new FileWriter("QuizSubmissions.txt", true));
                writer.write(quiz.getQuizTitle());
                writer.newLine();
                writer.write(s.getUsername());
                writer.newLine();
                writer.write(dateFormat.format(startDate));
                writer.newLine();
                writer.write(dateFormat.format(endDate));
                writer.newLine();
                for (int i = 0; i < quiz.getQuestions().size(); i++) {
                    // may need to change when randomization is added
                    writer.write(responses.get(i));
                    writer.newLine();
                }
                writer.write("End of Quiz");
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                //System.out.println("Submission record ERROR");
                e.printStackTrace();
            }
        }
    }

}
