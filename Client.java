import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.*;

/**
 * Project 5 - Client Class
 *
 * This manages the GUI and the student/teacher side of the functionality
 *
 * @author Ethan Weiss, Putian Wang, Jerry Mann, Adree Das
 *
 * @version Monday, May 2, 2022
 *
 */
public class Client extends JComponent implements Runnable {

    private static BufferedReader bfr = null;
    private static PrintWriter pw = null;
    private static Boolean isTeacher = null;
    private static ObjectOutputStream outputToServer;
    private static ObjectInputStream inputFromServer;

    //MainMenu mainMenu;

    JFrame frame;
    JPanel panel;

    JButton logInButton;
    JButton createAccountButton;
    JButton editAccountButton;
    JButton deleteAccountButton;

    ArrayList<Quiz> courseQuizzes;
    Quiz currentQuiz;
    String currentQuizTitle;
    int currentQuizNumQ;
    int currentQuizTimeLimit;
    ArrayList<Integer> randomizedQuestions = null;
    ArrayList<ArrayList<Integer>> randomizedChoices = null;
    ArrayList<String> studentResponses;
    String selectedChoice = "a";
    int currentQuestionNum;
    Date startDate = null;
    Date endDate = null;
    long startTime;
    long currentTime;

    public void run() {
        frame = new JFrame("Welcome to Java Academy!");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        logInButton = new JButton("Log in");
        logInButton.setVisible(true);

        createAccountButton = new JButton("Create account");
        createAccountButton.setVisible(true);

        editAccountButton = new JButton("Edit account");
        editAccountButton.setVisible(true);

        deleteAccountButton = new JButton("Delete account");
        //deleteAccountButton.addActionListener(actionListener);
        deleteAccountButton.setVisible(true);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        panel.add(logInButton);
        panel.add(createAccountButton);
        panel.add(editAccountButton);
        panel.add(deleteAccountButton);

        logInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //log in
                panel.setEnabled(false);
                logInButton.setEnabled(false);
                createAccountButton.setEnabled(false);
                editAccountButton.setEnabled(false);
                deleteAccountButton.setEnabled(false);

                pw.println("Log In");
                pw.flush();

                Panel logInPanel = new Panel();
                SpringLayout layout = new SpringLayout();
                logInPanel.setLayout(layout);
                frame.add(logInPanel);

                JLabel usernameLabel = new JLabel("Username: ");
                JLabel passwordLabel = new JLabel("Password: ");
                JTextField usernameField = new JTextField(20);
                JTextField passwordField = new JTextField(20);
                JButton logIn = new JButton("Log In");


                logInPanel.add(usernameLabel);
                logInPanel.add(passwordLabel);
                logInPanel.add(usernameField);
                logInPanel.add(passwordField);
                logInPanel.add(logIn);

                layout.putConstraint(SpringLayout.WEST, usernameLabel, 40, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, usernameLabel, 60, SpringLayout.NORTH, logInPanel);
                // Set the username textField
                layout.putConstraint(SpringLayout.WEST, usernameField, 40, SpringLayout.EAST, usernameLabel);
                layout.putConstraint(SpringLayout.NORTH, usernameField, 60, SpringLayout.NORTH, logInPanel);

                layout.putConstraint(SpringLayout.WEST, passwordLabel, 40, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, passwordLabel, 80, SpringLayout.NORTH, usernameLabel);
                // Set password textField
                layout.putConstraint(SpringLayout.WEST, passwordField, 40, SpringLayout.EAST, passwordLabel);
                layout.putConstraint(SpringLayout.NORTH, passwordField, 80, SpringLayout.NORTH, usernameField);

                layout.putConstraint(SpringLayout.WEST, logIn, 180, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, logIn, 80, SpringLayout.NORTH, passwordLabel);

                SwingUtilities.updateComponentTreeUI(frame);

                logIn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        pw.flush(); // might delete
                        //Get texts from text field, attempt log in
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        // Determine if there has any empty input
                        if (username.equals("") || password.equals("")) {

                            // Error message for empty inputs
                            JOptionPane.showMessageDialog(null, "Inputs cannot be empty!",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            // Set textFields to empty
                            usernameField.setText("");
                            passwordField.setText("");

                        } else {

                            // Send question info to server
                            pw.println(username);
                            pw.println(password);
                            pw.flush();

                            String logInResult = null;
                            try {
                                logInResult = bfr.readLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            //System.out.println("Log in result: " + logInResult);
                            switch (logInResult) {
                                case "Logged in successfully!":
                                    //set logged in to true
                                    JOptionPane.showMessageDialog(null,
                                            "Logged in successfully!", "Java Academy",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    try {
                                        if (bfr.readLine().equals("true")) {
                                            isTeacher = true;
                                        } else {
                                            isTeacher = false;
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    //System.out.println("test2");
                                    frame.dispose();
                                    //System.out.println("test3");
                                    run2();
                                    //System.out.println("test4");
                                    break;
                                case "Incorrect password.":
                                    JOptionPane.showMessageDialog(null, "Incorrect password!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    frame.dispose();
                                    break;
                                case "Invalid Account":
                                    JOptionPane.showMessageDialog(null, "Invalid Account",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    frame.dispose();
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Something went wrong",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    frame.dispose();
                                    break;
                            }
                            SwingUtilities.updateComponentTreeUI(frame);

                        }
                    }
                });
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setEnabled(false);
                logInButton.setEnabled(false);
                createAccountButton.setEnabled(false);
                editAccountButton.setEnabled(false);
                deleteAccountButton.setEnabled(false);

                pw.println("Create Account");
                pw.flush();

                Panel createAccountPanel = new Panel();
                SpringLayout layout = new SpringLayout();
                createAccountPanel.setLayout(layout);
                frame.add(createAccountPanel);

                JLabel usernameLabel = new JLabel("Username: ");
                JLabel passwordLabel = new JLabel("Password: ");
                JLabel confirmPasswordLabel = new JLabel("Confirm Password: ");
                JTextField usernameField = new JTextField(20);
                JTextField passwordField = new JTextField(20);
                JTextField confirmPasswordField = new JTextField(20);
                JButton createAccount = new JButton("Create Account");
                JCheckBox teacherOrStudentCheckBox = new JCheckBox("Teacher");


                createAccountPanel.add(usernameLabel);
                createAccountPanel.add(passwordLabel);
                createAccountPanel.add(confirmPasswordLabel);
                createAccountPanel.add(usernameField);
                createAccountPanel.add(passwordField);
                createAccountPanel.add(confirmPasswordField);
                createAccountPanel.add(createAccount);
                createAccountPanel.add(teacherOrStudentCheckBox);

                layout.putConstraint(SpringLayout.WEST, usernameLabel, 40, SpringLayout.WEST, createAccountPanel);
                layout.putConstraint(SpringLayout.NORTH, usernameLabel,
                        60, SpringLayout.NORTH, createAccountPanel);
                // Set the username textField
                layout.putConstraint(SpringLayout.WEST, usernameField, 56, SpringLayout.EAST, usernameLabel);
                layout.putConstraint(SpringLayout.NORTH, usernameField, 60,
                        SpringLayout.NORTH, createAccountPanel);

                layout.putConstraint(SpringLayout.WEST, passwordLabel, 40, SpringLayout.WEST, createAccountPanel);
                layout.putConstraint(SpringLayout.NORTH, passwordLabel, 80, SpringLayout.NORTH, usernameLabel);
                // Set password textField
                layout.putConstraint(SpringLayout.WEST, passwordField, 56, SpringLayout.EAST, passwordLabel);
                layout.putConstraint(SpringLayout.NORTH, passwordField, 80, SpringLayout.NORTH, usernameField);

                layout.putConstraint(SpringLayout.WEST, confirmPasswordLabel,
                        40, SpringLayout.WEST, createAccountPanel);
                layout.putConstraint(SpringLayout.NORTH, confirmPasswordLabel,
                        80, SpringLayout.NORTH, passwordLabel);
                // Set password textField
                layout.putConstraint(SpringLayout.WEST, confirmPasswordField,
                        32, SpringLayout.EAST, confirmPasswordLabel);
                layout.putConstraint(SpringLayout.NORTH, confirmPasswordField,
                        80, SpringLayout.NORTH, passwordField);

                layout.putConstraint(SpringLayout.WEST, teacherOrStudentCheckBox,
                        50, SpringLayout.WEST, createAccountPanel);
                layout.putConstraint(SpringLayout.NORTH, teacherOrStudentCheckBox,
                        75, SpringLayout.NORTH, confirmPasswordLabel);

                layout.putConstraint(SpringLayout.WEST, createAccount,
                        180, SpringLayout.WEST, createAccountPanel);
                layout.putConstraint(SpringLayout.NORTH, createAccount,
                        80, SpringLayout.NORTH, confirmPasswordLabel);

                SwingUtilities.updateComponentTreeUI(frame);

                createAccount.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get texts from text field, attempt log in
                        String username = usernameField.getText();
                        //DETECT DUPLICATE USERNAMES!!!! (DO THIS IN THE SERVER)
                        String password = passwordField.getText();
                        String confirmPassword = confirmPasswordField.getText();
                        boolean teacher = teacherOrStudentCheckBox.isSelected();
                        // Determine if there has any empty input
                        if (username.equals("") || password.equals("") || confirmPassword.equals("")) {

                            // Error message for empty inputs
                            JOptionPane.showMessageDialog(null, "Inputs cannot be empty!",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            // Set textFields to empty
                            usernameField.setText("");
                            passwordField.setText("");
                            confirmPasswordField.setText("");

                        } else if (!(password.equals(confirmPassword))) {
                            JOptionPane.showMessageDialog(null, "Passwords must match!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            passwordField.setText("");
                            confirmPasswordField.setText("");
                        } else {
                            // Send question info to server

                            pw.println(username);
                            pw.println(password);
                            if (teacher) {
                                pw.println("Teacher");
                            } else {
                                pw.println("Student");
                            }
                            pw.flush();

                            String createAccountResult = "Success";
                            /*try {
                                createAccountResult = bfr.readLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }*/
                            //Add above with DetectDuplicateUsernames, should be only instance of server-side failure

                            //System.out.println("Create Account Result: " + createAccountResult);
                            switch (createAccountResult) {
                                case "Success":
                                    JOptionPane.showMessageDialog(null,
                                            "Account created successfully!", "Java Academy",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    frame.dispose();
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Something went wrong",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                            }
                            SwingUtilities.updateComponentTreeUI(frame);
                        }
                    }
                });
            }
        });

        editAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setEnabled(false);
                logInButton.setEnabled(false);
                createAccountButton.setEnabled(false);
                editAccountButton.setEnabled(false);
                deleteAccountButton.setEnabled(false);

                pw.println("Edit Account");
                pw.flush();

                Panel logInPanel = new Panel();
                SpringLayout layout = new SpringLayout();
                logInPanel.setLayout(layout);
                frame.add(logInPanel);

                JLabel usernameLabel = new JLabel("Username: ");
                JLabel passwordLabel = new JLabel("Password: ");
                JTextField usernameField = new JTextField(20);
                JTextField passwordField = new JTextField(20);
                JButton changeUsername = new JButton("Change Username");
                JButton changePassword = new JButton("Change Password");
                JButton changeAccountType = new JButton("Change account type");

                logInPanel.add(usernameLabel);
                logInPanel.add(passwordLabel);
                logInPanel.add(usernameField);
                logInPanel.add(passwordField);
                logInPanel.add(changeUsername);
                logInPanel.add(changePassword);
                logInPanel.add(changeAccountType);

                layout.putConstraint(SpringLayout.WEST, usernameLabel, 40, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, usernameLabel, 60, SpringLayout.NORTH, logInPanel);
                // Set the username textField
                layout.putConstraint(SpringLayout.WEST, usernameField, 40, SpringLayout.EAST, usernameLabel);
                layout.putConstraint(SpringLayout.NORTH, usernameField, 60, SpringLayout.NORTH, logInPanel);

                layout.putConstraint(SpringLayout.WEST, passwordLabel, 40, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, passwordLabel, 80, SpringLayout.NORTH, usernameLabel);
                // Set password textField
                layout.putConstraint(SpringLayout.WEST, passwordField, 40, SpringLayout.EAST, passwordLabel);
                layout.putConstraint(SpringLayout.NORTH, passwordField, 80, SpringLayout.NORTH, usernameField);

                layout.putConstraint(SpringLayout.WEST, changeUsername, 20, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, changeUsername, 80, SpringLayout.NORTH, passwordLabel);

                layout.putConstraint(SpringLayout.WEST, changePassword, 160, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, changePassword, 80, SpringLayout.NORTH, passwordLabel);

                layout.putConstraint(SpringLayout.WEST, changeAccountType, 300, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, changeAccountType,
                        80, SpringLayout.NORTH, passwordLabel);

                SwingUtilities.updateComponentTreeUI(frame);

                final String[] newUsername = new String[1];
                final String[] newPassword = new String[1];
                boolean newAccountType;

                changeUsername.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get texts from text field, attempt log in
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        // Determine if there has any empty input
                        if (username.equals("") || password.equals("")) {

                            // Error message for empty inputs
                            JOptionPane.showMessageDialog(null, "Inputs cannot be empty!",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            // Set textFields to empty
                            usernameField.setText("");
                            passwordField.setText("");

                        } else {

                            // Send question info to server
                            pw.println(username);
                            pw.println(password);
                            pw.flush();

                            String editAccountResult = null;
                            try {
                                editAccountResult = bfr.readLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            //System.out.println("Edit account result: " + editAccountResult);
                            switch (editAccountResult) {
                                case "Logged in successfully!":
                                    //set logged in to true
                                    newUsername[0] = JOptionPane.showInputDialog(null,
                                            "Enter new username: ",
                                            "Edit Account", JOptionPane.QUESTION_MESSAGE);
                                    pw.println("New Username");
                                    pw.println(newUsername[0]);
                                    pw.flush();
                                    try {
                                        if (bfr.readLine().equals("Success!")) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Username changed!",
                                                    "Edit Account", JOptionPane.INFORMATION_MESSAGE);
                                            frame.dispose();
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    break;
                                case "Incorrect password.":
                                    JOptionPane.showMessageDialog(null, "Incorrect password!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                case "Invalid Account":
                                    JOptionPane.showMessageDialog(null, "Invalid Account",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Something went wrong",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                            }
                            SwingUtilities.updateComponentTreeUI(frame);

                        }
                    }
                });
                changePassword.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get texts from text field, attempt log in
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        // Determine if there has any empty input
                        if (username.equals("") || password.equals("")) {

                            // Error message for empty inputs
                            JOptionPane.showMessageDialog(null, "Inputs cannot be empty!",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            // Set textFields to empty
                            usernameField.setText("");
                            passwordField.setText("");

                        } else {

                            // Send question info to server
                            pw.println(username);
                            pw.println(password);
                            pw.flush();

                            String editAccountResult = null;
                            try {
                                editAccountResult = bfr.readLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            //System.out.println("Edit account result: " + editAccountResult);
                            switch (editAccountResult) {
                                case "Logged in successfully!":
                                    //set logged in to true
                                    newPassword[0] = JOptionPane.showInputDialog(null,
                                            "Enter new password: ",
                                            "Edit Account", JOptionPane.QUESTION_MESSAGE);
                                    pw.println("New Password");
                                    pw.println(newPassword[0]);
                                    pw.flush();
                                    try {
                                        if (bfr.readLine().equals("Success!")) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Password changed!",
                                                    "Edit Account", JOptionPane.INFORMATION_MESSAGE);
                                            frame.dispose();
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    break;
                                case "Incorrect password.":
                                    JOptionPane.showMessageDialog(null, "Incorrect password!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                case "Invalid Account":
                                    JOptionPane.showMessageDialog(null, "Invalid Account",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Something went wrong",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                            }
                            SwingUtilities.updateComponentTreeUI(frame);

                        }
                    }
                });
                changeAccountType.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        // Determine if there has any empty input
                        if (username.equals("") || password.equals("")) {

                            // Error message for empty inputs
                            JOptionPane.showMessageDialog(null, "Inputs cannot be empty!",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            // Set textFields to empty
                            usernameField.setText("");
                            passwordField.setText("");

                        } else {

                            // Send question info to server
                            pw.println(username);
                            pw.println(password);
                            pw.flush();

                            String editAccountResult = null;
                            try {
                                editAccountResult = bfr.readLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            //System.out.println("Edit account result: " + editAccountResult);
                            switch (editAccountResult) {
                                case "Logged in successfully!":
                                    //set logged in to true?
                                    pw.println("Change Account Type");
                                    pw.flush();
                                    try {
                                        if (bfr.readLine().equals("Success!")) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Account type changed",
                                                    "Edit Account", JOptionPane.INFORMATION_MESSAGE);
                                            frame.dispose();
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    break;
                                case "Incorrect password.":
                                    JOptionPane.showMessageDialog(null, "Incorrect password!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                case "Invalid Account":
                                    JOptionPane.showMessageDialog(null, "Invalid Account",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Something went wrong",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                            }
                            SwingUtilities.updateComponentTreeUI(frame);

                        }
                    }
                });
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //log in
                panel.setEnabled(false);
                logInButton.setEnabled(false);
                createAccountButton.setEnabled(false);
                editAccountButton.setEnabled(false);
                deleteAccountButton.setEnabled(false);

                pw.println("Delete Account");
                pw.flush();

                Panel logInPanel = new Panel();
                SpringLayout layout = new SpringLayout();
                logInPanel.setLayout(layout);
                frame.add(logInPanel);

                JLabel usernameLabel = new JLabel("Username: ");
                JLabel passwordLabel = new JLabel("Password: ");
                JTextField usernameField = new JTextField(20);
                JTextField passwordField = new JTextField(20);
                JButton deleteAccount = new JButton("Delete Account");

                logInPanel.add(usernameLabel);
                logInPanel.add(passwordLabel);
                logInPanel.add(usernameField);
                logInPanel.add(passwordField);
                logInPanel.add(deleteAccount);

                layout.putConstraint(SpringLayout.WEST, usernameLabel, 40, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, usernameLabel, 60, SpringLayout.NORTH, logInPanel);
                // Set the username textField
                layout.putConstraint(SpringLayout.WEST, usernameField, 40, SpringLayout.EAST, usernameLabel);
                layout.putConstraint(SpringLayout.NORTH, usernameField, 60, SpringLayout.NORTH, logInPanel);

                layout.putConstraint(SpringLayout.WEST, passwordLabel, 40, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, passwordLabel, 80, SpringLayout.NORTH, usernameLabel);
                // Set password textField
                layout.putConstraint(SpringLayout.WEST, passwordField, 40, SpringLayout.EAST, passwordLabel);
                layout.putConstraint(SpringLayout.NORTH, passwordField, 80, SpringLayout.NORTH, usernameField);

                layout.putConstraint(SpringLayout.WEST, deleteAccount, 180, SpringLayout.WEST, logInPanel);
                layout.putConstraint(SpringLayout.NORTH, deleteAccount, 80, SpringLayout.NORTH, passwordLabel);

                SwingUtilities.updateComponentTreeUI(frame);

                deleteAccount.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get texts from text field, attempt log in
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        // Determine if there has any empty input
                        if (username.equals("") || password.equals("")) {

                            // Error message for empty inputs
                            JOptionPane.showMessageDialog(null, "Inputs cannot be empty!",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            // Set textFields to empty
                            usernameField.setText("");
                            passwordField.setText("");

                        } else {

                            // Send question info to server
                            pw.println(username);
                            pw.println(password);
                            pw.flush();

                            String logInResult = null;
                            try {
                                logInResult = bfr.readLine();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            //System.out.println("Log in result: " + logInResult);
                            switch (logInResult) {
                                case "Logged in successfully!":
                                    //set logged in to true
                                    JOptionPane.showMessageDialog(null,
                                            "Account deleted successfully!",
                                            "Delete Account", JOptionPane.INFORMATION_MESSAGE);
                                    frame.dispose();
                                    break;
                                case "Incorrect password.":
                                    JOptionPane.showMessageDialog(null, "Incorrect password!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                case "Invalid Account":
                                    JOptionPane.showMessageDialog(null, "Invalid Account",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Something went wrong",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                            }
                            SwingUtilities.updateComponentTreeUI(frame);

                        }
                    }
                });
            }
        });
        content.add(panel, BorderLayout.WEST);
    }


    public void run2() {
        if (isTeacher) {
            // Teacher Part

            // Top Panel
            JFrame teacherFrame = new JFrame("Teacher");
            teacherFrame.setSize(600, 400);
            // Disable the "Close" button
            teacherFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            teacherFrame.setVisible(true);

            Panel coursePanel = new Panel();
            teacherFrame.add(coursePanel, BorderLayout.NORTH);
            JButton createCourse = new JButton("Create Course");
            JButton confirmCourse = new JButton("Confirm");
            Icon icon = new ImageIcon("icon.png");
            JButton refresh = new JButton("Refresh", icon);
            refresh.setVerticalTextPosition(SwingConstants.BOTTOM);
            refresh.setHorizontalTextPosition(SwingConstants.CENTER);
            refresh.setSize(20, 20);
            JLabel selectCourse = new JLabel("Select Course:");
            JComboBox<String> courseField = new JComboBox<>();
            JTextField createCourseField = new JTextField(10);
            createCourse.setBackground(Color.orange);
            createCourse.setOpaque(true);
            createCourse.setBorderPainted(false);
            coursePanel.add(createCourse);
            coursePanel.add(createCourseField);
            coursePanel.add(selectCourse);
            coursePanel.add(courseField);
            coursePanel.add(confirmCourse);
            coursePanel.add(refresh);

            // Left Panel
            Panel menu = new Panel();
            menu.setLayout(new GridLayout(7, 1));
            teacherFrame.add(menu, BorderLayout.WEST);
            JButton createQuiz = new JButton("Create Quiz");
            JButton importQuiz = new JButton("Import Quiz");
            JButton editQuiz = new JButton("Edit Quiz");
            JButton deleteQuiz = new JButton("Delete Quiz");
            JButton gradeQuiz = new JButton("Grade Quiz");
            JButton viewAndGradeSubmission = new JButton("View and Grade Submissions");
            JButton randomization = new JButton("Randomize Quizzes");
            JButton exit = new JButton("Exit");
            menu.add(createQuiz);
            menu.add(importQuiz);
            menu.add(editQuiz);
            menu.add(deleteQuiz);
            menu.add(viewAndGradeSubmission);
            menu.add(randomization);
            menu.add(exit);
            // Disabled the menu until a course is selected, except for the "Exit" button
            createQuiz.setEnabled(false);
            importQuiz.setEnabled(false);
            editQuiz.setEnabled(false);
            deleteQuiz.setEnabled(false);
            viewAndGradeSubmission.setEnabled(false);
            randomization.setEnabled(false);
            refresh.setEnabled(false);


            // Notification says "select a course to continue"
            TextField notification = new TextField("Please select a course first!");
            Font font = new Font("SansSerif", Font.BOLD, 30);
            notification.setFont(font);
            notification.setEditable(false);
            teacherFrame.add(notification, BorderLayout.CENTER);



            ArrayList<String> courses = new ArrayList<>();
            try {
                courses = (ArrayList<String>) inputFromServer.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (courses.size() == 0) {
                courseField.addItem("No courses available");
            } else {
                for (int i = 0; i < courses.size(); i++) {
                    courseField.addItem(courses.get(i));
                }
            }

            SwingUtilities.updateComponentTreeUI(teacherFrame);

            // Confirm Course
            confirmCourse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    createQuiz.setEnabled(true);
                    importQuiz.setEnabled(true);
                    editQuiz.setEnabled(true);
                    deleteQuiz.setEnabled(true);
                    viewAndGradeSubmission.setEnabled(true);
                    randomization.setEnabled(true);
                    refresh.setEnabled(true);
                    teacherFrame.remove(notification);
                    pw.println("Select Course");
                    pw.flush();
                    pw.println(courseField.getSelectedItem());
                    pw.flush();
                    SwingUtilities.updateComponentTreeUI(teacherFrame);
                }
            });

            // Create Course
            createCourse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pw.println("Create Course");
                    pw.flush();
                    String name = createCourseField.getText();
                    pw.println(name);
                    pw.flush();
                    SwingUtilities.updateComponentTreeUI(teacherFrame);
                }
            });

            //Refresh Course
            refresh.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pw.println("Refresh");
                    pw.flush();

                    try {
                        ArrayList<String> courseNames = (ArrayList<String>) inputFromServer.readObject();
                        if (courseNames.size() == 0) {
                            courseField.removeAllItems();
                            courseField.addItem("No courses available");
                        } else {
                            courseField.removeAllItems();
                            for (String quizName : courseNames) {
                                courseField.addItem(quizName);
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }
            });


            // Create Quiz Functionality
            createQuiz.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    // Disable top panel and left panel
                    coursePanel.setEnabled(false);
                    menu.setEnabled(false);

                    // Send message to server
                    pw.println("Create Quiz");
                    pw.flush();

                    // Prompt for the quiz name
                    String quizName;
                    do {
                        quizName = JOptionPane.showInputDialog(null, "Enter the quiz title: ",
                                "Create Quiz", JOptionPane.QUESTION_MESSAGE);
                        if ((quizName == null || quizName.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Quiz title cannot be empty!",
                                    "Create Quiz", JOptionPane.ERROR_MESSAGE);
                        } else {
                            break;
                        }
                    } while (true);

                    // Prompt for the time limit of the quiz
                    int timeLimit;
                    do {
                        try {
                            timeLimit = Integer.parseInt(JOptionPane.showInputDialog(null,
                                    "Enter the time limit in minutes: ",
                                    "Create Quiz", JOptionPane.QUESTION_MESSAGE));
                            if (timeLimit <= 0) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException exception) {
                            JOptionPane.showMessageDialog(null,
                                    "Please enter a valid time limit!",
                                    "Create Quiz", JOptionPane.ERROR_MESSAGE);
                        }
                    } while (true);

                    // Send quizName and timeLimit to sever
                    pw.println(quizName);
                    pw.println(timeLimit);
                    pw.flush();

                    boolean hasDuplicateName = false;
                    try {
                        if (bfr.readLine().equals("true")) {
                            hasDuplicateName = true;
                            JOptionPane.showMessageDialog(null,
                                    "The title of the quiz has already been used!\nFailed!");
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    if (hasDuplicateName) {
                        // Enable the top panel and left panel
                        coursePanel.setEnabled(true);
                        menu.setEnabled(true);
                    } else {
                        // Create the panel for "Create Quiz" and add it to the main frame
                        // Use SpringLayout for this panel
                        Panel teacherPanelMain = new Panel();
                        SpringLayout layout = new SpringLayout();
                        teacherPanelMain.setLayout(layout);
                        teacherFrame.add(teacherPanelMain);

                        // Create components for this panel
                        JLabel question = new JLabel("Question: ");
                        JLabel answer1 = new JLabel("a) ");
                        JLabel answer2 = new JLabel("b) ");
                        JLabel answer3 = new JLabel("c) ");
                        JLabel answer4 = new JLabel("d) ");
                        JTextField q = new JTextField(25);
                        JTextField a1 = new JTextField(25);
                        JTextField a2 = new JTextField(25);
                        JTextField a3 = new JTextField(25);
                        JTextField a4 = new JTextField(25);
                        JButton addQuestion = new JButton("Add Question");
                        JButton complete = new JButton("Complete");

                        // Set "Complete" button to invisible until the user add the first question
                        complete.setVisible(false);

                        // Add components to the panel
                        JLabel[] labels = {question, answer1, answer2, answer3, answer4};
                        JTextField[] textFields = {q, a1, a2, a3, a4};
                        for (JLabel label : labels) {
                            teacherPanelMain.add(label);
                        }
                        for (JTextField textField : textFields) {
                            teacherPanelMain.add(textField);
                        }
                        teacherPanelMain.add(addQuestion);
                        teacherPanelMain.add(complete);

                        // Set the layout
                        for (int i = 0; i < labels.length; i++) {
                            if (i == 0) {
                                // Set the top label
                                // Ex: "Set the west edge of label[i] to
                                // be 40 pixels away from the west edge of the panel"
                                layout.putConstraint(SpringLayout.WEST, labels[i], 40,
                                        SpringLayout.WEST, teacherPanelMain);
                                layout.putConstraint(SpringLayout.NORTH, labels[i],
                                        40, SpringLayout.NORTH, teacherPanelMain);
                                // Set the top textField
                                layout.putConstraint(SpringLayout.WEST, textFields[i],
                                        40, SpringLayout.EAST, labels[i]);
                                layout.putConstraint(SpringLayout.NORTH, textFields[i],
                                        40, SpringLayout.NORTH, teacherPanelMain);
                            } else {
                                // Set label
                                layout.putConstraint(SpringLayout.WEST, labels[i], 83, SpringLayout.WEST,
                                        teacherPanelMain);
                                layout.putConstraint(SpringLayout.NORTH, labels[i],
                                        40, SpringLayout.NORTH, labels[i - 1]);
                                // Set textField
                                layout.putConstraint(SpringLayout.WEST, textFields[i],
                                        40, SpringLayout.EAST, labels[i]);
                                layout.putConstraint(SpringLayout.NORTH, textFields[i],
                                        40, SpringLayout.NORTH, textFields[i - 1]);
                            }
                        }

                        // Set the layout for buttons
                        layout.putConstraint(SpringLayout.WEST, addQuestion, 140, SpringLayout.WEST, teacherPanelMain);
                        layout.putConstraint(SpringLayout.NORTH, addQuestion,
                                60, SpringLayout.NORTH, labels[labels.length - 1]);
                        layout.putConstraint(SpringLayout.WEST, complete, 100, SpringLayout.EAST, addQuestion);
                        layout.putConstraint(SpringLayout.NORTH, complete,
                                60, SpringLayout.NORTH, labels[labels.length - 1]);

                        // Refresh the frame
                        SwingUtilities.updateComponentTreeUI(teacherFrame);

                        // Set functionalities for buttons
                        addQuestion.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                // Get texts from each textFiled
                                String question = q.getText();
                                String answer1 = a1.getText();
                                String answer2 = a2.getText();
                                String answer3 = a3.getText();
                                String answer4 = a4.getText();

                                // Determine if there has any empty input
                                if (question.equals("") || answer1.equals("") || answer2.equals("")
                                        || answer3.equals("") || answer4.equals("")) {

                                    // Error message for empty inputs
                                    JOptionPane.showMessageDialog(null,
                                            "Inputs cannot be empty!", "Error",
                                            JOptionPane.ERROR_MESSAGE);

                                    // Set textFields to empty
                                    q.setText("");
                                    a1.setText("");
                                    a2.setText("");
                                    a3.setText("");
                                    a4.setText("");

                                } else {

                                    // Send action message to server, so that
                                    // server can start processing "Add Question".
                                    pw.println("Add Question");
                                    pw.flush();

                                    // Send question info to server
                                    pw.println(question);
                                    pw.println(answer1);
                                    pw.println(answer2);
                                    pw.println(answer3);
                                    pw.println(answer4);
                                    pw.flush();

                                    // Set textFields to empty
                                    q.setText("");
                                    a1.setText("");
                                    a2.setText("");
                                    a3.setText("");
                                    a4.setText("");

                                    // Set "Exit" button to visible after the first question is added
                                    complete.setVisible(true);
                                }

                            }
                        });

                        // "Complete" button
                        complete.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                pw.println("Complete");
                                pw.flush();
                                coursePanel.setEnabled(true);
                                menu.setEnabled(true);
                                teacherFrame.remove(teacherPanelMain);
                            }
                        });
                    }
                }
            });


            // Import Quiz functionality
            importQuiz.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    // Disable top panel and left panel
                    coursePanel.setEnabled(false);
                    menu.setEnabled(false);

                    // Send message to server
                    pw.println("Import Quiz");
                    pw.flush();

                    // Create new panel for this functionality
                    Panel importPanel = new Panel();
                    SpringLayout layout = new SpringLayout();
                    importPanel.setLayout(layout);
                    teacherFrame.add(importPanel);

                    // Create components
                    JLabel enterFile = new JLabel("Enter the path of the quiz file:");
                    JTextField filePath = new JTextField(10);
                    JButton confirm = new JButton("Confirm");

                    // Add components to panel
                    importPanel.add(enterFile);
                    importPanel.add(filePath);
                    importPanel.add(confirm);

                    // Set layout
                    layout.putConstraint(SpringLayout.WEST, enterFile, 40, SpringLayout.WEST, importPanel);
                    layout.putConstraint(SpringLayout.NORTH, enterFile, 40, SpringLayout.NORTH, importPanel);
                    layout.putConstraint(SpringLayout.WEST, filePath, 40, SpringLayout.EAST, enterFile);
                    layout.putConstraint(SpringLayout.NORTH, filePath, 40, SpringLayout.NORTH, importPanel);
                    layout.putConstraint(SpringLayout.WEST, confirm, 150, SpringLayout.WEST, importPanel);
                    layout.putConstraint(SpringLayout.NORTH, confirm, 40, SpringLayout.SOUTH, enterFile);

                    // Refresh the frame
                    SwingUtilities.updateComponentTreeUI(teacherFrame);

                    // "Confirm" button
                    confirm.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            String file = filePath.getText();
                            if (file.equals("")) {
                                JOptionPane.showMessageDialog(null, "Input cannot be empty!");
                            } else {
                                ArrayList<String> list = new ArrayList<>();
                                boolean run = true;
                                try {
                                    BufferedReader fbr = new BufferedReader(new FileReader(file));
                                    String line;
                                    while (true) {
                                        line = fbr.readLine();
                                        if (line == null) {
                                            break;
                                        } else {
                                            list.add(line);
                                        }
                                    }
                                    fbr.close();
                                    pw.println("not break");
                                    pw.flush();
                                } catch (Exception exception) {
                                    JOptionPane.showMessageDialog(null, "Invalid file path!");
                                    pw.println("break");
                                    pw.flush();
                                    run = false;
                                    coursePanel.setEnabled(true);
                                    menu.setEnabled(true);
                                    teacherFrame.remove(importPanel);
                                }

                                if (run) {
                                    try {
                                        outputToServer.writeObject(list);
                                        if (bfr.readLine().equals("format issue")) {
                                            JOptionPane.showMessageDialog(null,
                                                    "The format of the quiz file is unavailable!\nFailed!");
                                            coursePanel.setEnabled(true);
                                            menu.setEnabled(true);
                                            teacherFrame.remove(importPanel);
                                        } else {
                                            // Determine if it has duplicate quiz title
                                            if (bfr.readLine().equals("true")) {
                                                JOptionPane.showMessageDialog(null,
                                                        "The title of the quiz has already been used!\nFailed!"
                                                );
                                                coursePanel.setEnabled(true);
                                                menu.setEnabled(true);
                                                teacherFrame.remove(importPanel);
                                            } else {
                                                // Prompt for the time limit of the quiz
                                                int timeLimit;
                                                do {
                                                    try {
                                                        timeLimit = Integer.parseInt(JOptionPane.showInputDialog(
                                                                null,
                                                                "Enter the time limit in minutes: ",
                                                                "Create Quiz", JOptionPane.QUESTION_MESSAGE));
                                                        if (timeLimit <= 0) {
                                                            throw new NumberFormatException();
                                                        }
                                                        break;
                                                    } catch (NumberFormatException exception) {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Please enter a valid time limit!",
                                                                "Create Quiz", JOptionPane.ERROR_MESSAGE);
                                                    }
                                                } while (true);

                                                pw.println(timeLimit);
                                                pw.flush();

                                                coursePanel.setEnabled(true);
                                                menu.setEnabled(true);
                                                teacherFrame.remove(importPanel);
                                            }
                                        }
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }

                            }
                        }
                    });
                }
            });

            //edit quiz functionality
            editQuiz.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("Editing");//test
                    coursePanel.setEnabled(false);
                    menu.setEnabled(false);

                    pw.println("Edit Quiz");
                    pw.flush();
                    //System.out.println("Sent switch");//test

                    // Prompt for the quiz name
                    String quizName;
                    do {
                        quizName = JOptionPane.showInputDialog(null,
                                "Enter the quiz title to be edited: ",
                                "Edit Quiz", JOptionPane.QUESTION_MESSAGE);
                        if ((quizName == null || quizName.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Quiz title cannot be empty!",
                                    "Edit Quiz", JOptionPane.ERROR_MESSAGE);
                        } else {
                            break;
                        }
                    } while (true);

                    // Prompt for the time limit of the quiz
                    int questionNumber;
                    do {
                        try {
                            questionNumber = Integer.parseInt(JOptionPane.showInputDialog(null,
                                    "Enter the question number to be edited ",
                                    "Edit Quiz", JOptionPane.QUESTION_MESSAGE));
                            if (questionNumber <= 0) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException exception) {
                            JOptionPane.showMessageDialog(null,
                                    "Please enter a valid question number!",
                                    "Edit Quiz", JOptionPane.ERROR_MESSAGE);
                        }
                    } while (true);

                    // Send quizName and timeLimit to sever
                    pw.println(quizName);
                    pw.flush();

                    //System.out.println("Sent name");//test
                    int emptyCheck = 100;

                    try {
                        emptyCheck = Integer.parseInt(bfr.readLine());
                        //System.out.println("received empty check");//test
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (emptyCheck == 0) {
                        JOptionPane.showMessageDialog(null,
                                "There are no quizzes available in this course",
                                "Edit Quiz", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int quizCheck = 100;
                    try {
                        quizCheck = Integer.parseInt(bfr.readLine());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    //System.out.println("Quiz check is: " + quizCheck);//test
                    if (quizCheck == 3) {
                        JOptionPane.showMessageDialog(null,
                                "There are no quizzes with this title in this course",
                                "Edit Quiz", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    pw.println(questionNumber);
                    pw.flush();

                    int numberCheck = 100;
                    try {
                        numberCheck = Integer.parseInt(bfr.readLine());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    //System.out.println("number check is" + numberCheck);
                    if (numberCheck == 0) {
                        JOptionPane.showMessageDialog(null,
                                "Entered number exceeds available questions",
                                "Edit Quiz", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    //System.out.println(quizName + "sent"); //test
                    //System.out.println(questionNumber + "sent"); //test

                    Panel springPanel = new Panel();
                    SpringLayout layout = new SpringLayout();
                    springPanel.setLayout(layout);
                    teacherFrame.add(springPanel);

                    JLabel question = new JLabel("New Question: ");
                    JLabel answer1 = new JLabel("a) ");
                    JLabel answer2 = new JLabel("b) ");
                    JLabel answer3 = new JLabel("c) ");
                    JLabel answer4 = new JLabel("d) ");
                    JTextField q = new JTextField(25);
                    JTextField a1 = new JTextField(25);
                    JTextField a2 = new JTextField(25);
                    JTextField a3 = new JTextField(25);
                    JTextField a4 = new JTextField(25);
                    JButton editQuestion = new JButton("Edit Question");
                    JButton exit = new JButton("Exit");


                    JLabel[] labels = {question, answer1, answer2, answer3, answer4};
                    JTextField[] textFields = {q, a1, a2, a3, a4};
                    for (JLabel label : labels) {
                        springPanel.add(label);
                    }
                    for (JTextField textField : textFields) {
                        springPanel.add(textField);
                    }
                    springPanel.add(editQuestion);
                    springPanel.add(exit);

                    // Set the layout
                    for (int i = 0; i < labels.length; i++) {
                        if (i == 0) {
                            // Set the top label
                            // Ex: "Set the west edge of label[i] to be 40
                            // pixels away from the west edge of the panel"
                            layout.putConstraint(SpringLayout.WEST, labels[i],
                                    40, SpringLayout.WEST, springPanel);
                            layout.putConstraint(SpringLayout.NORTH, labels[i],
                                    40, SpringLayout.NORTH, springPanel);
                            // Set the top textField
                            layout.putConstraint(SpringLayout.WEST, textFields[i],
                                    40, SpringLayout.EAST, labels[i]);
                            layout.putConstraint(SpringLayout.NORTH, textFields[i],
                                    40, SpringLayout.NORTH, springPanel);
                        } else {
                            // Set label
                            layout.putConstraint(SpringLayout.WEST, labels[i], 83,
                                    SpringLayout.WEST, springPanel);
                            layout.putConstraint(SpringLayout.NORTH, labels[i],
                                    40, SpringLayout.NORTH, labels[i - 1]);
                            // Set textField
                            layout.putConstraint(SpringLayout.WEST, textFields[i],
                                    40, SpringLayout.EAST, labels[i]);
                            layout.putConstraint(SpringLayout.NORTH, textFields[i],
                                    40, SpringLayout.NORTH, textFields[i - 1]);
                        }
                    }

                    // Set the layout for buttons
                    layout.putConstraint(SpringLayout.WEST, editQuestion, 140, SpringLayout.WEST, springPanel);
                    layout.putConstraint(SpringLayout.NORTH, editQuestion,
                            60, SpringLayout.NORTH, labels[labels.length - 1]);
                    layout.putConstraint(SpringLayout.WEST, exit, 100, SpringLayout.EAST, editQuestion);
                    layout.putConstraint(SpringLayout.NORTH, exit,
                            60, SpringLayout.NORTH, labels[labels.length - 1]);

                    // Refresh the frame
                    SwingUtilities.updateComponentTreeUI(teacherFrame);

                    // Set functionalities for buttons
                    editQuestion.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            // Get texts from each textFiled
                            String newQuestion = q.getText();
                            String answer1 = a1.getText();
                            String answer2 = a2.getText();
                            String answer3 = a3.getText();
                            String answer4 = a4.getText();

                            // Determine if there has any empty input
                            if (newQuestion.equals("") || answer1.equals("") || answer2.equals("")
                                    || answer3.equals("") || answer4.equals("")) {

                                // Error message for empty inputs
                                JOptionPane.showMessageDialog(null, "Inputs cannot be empty!",
                                        "Error", JOptionPane.ERROR_MESSAGE);

                                // Set textFields to empty
                                q.setText("");
                                a1.setText("");
                                a2.setText("");
                                a3.setText("");
                                a4.setText("");

                            } else {

                                // Send action message to server, so that server can start processing "Add Question".
                                pw.println("Edit Question");
                                pw.flush();
                                //System.out.println("Sent action"); //test

                                // Send question info to server
                                pw.println(newQuestion);
                                pw.println(answer1);
                                pw.println(answer2);
                                pw.println(answer3);
                                pw.println(answer4);
                                pw.flush();
                                //System.out.println("Sent new");//test

                                // Set textFields to empty
                                q.setText("");
                                a1.setText("");
                                a2.setText("");
                                a3.setText("");
                                a4.setText("");

                                JOptionPane.showMessageDialog(null, "Edited successfully",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            }

                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(springPanel);

                        }
                    });

                    // "Exit" button on the "Create Quiz" panel
                    exit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Exit");
                            pw.flush();
                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(springPanel);
                        }
                    });

                }
            });

            //delete quiz functionality
            deleteQuiz.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("Deleting");//test
                    coursePanel.setEnabled(false);
                    menu.setEnabled(false);

                    pw.println("Delete Quiz");
                    pw.flush();
                    //System.out.println("Sent switch");//test

                    Panel panelDelete = new Panel();
                    SpringLayout layout = new SpringLayout();
                    panelDelete.setLayout(layout);
                    teacherFrame.add(panelDelete);

                    JLabel title = new JLabel("Select quiz to be deleted:");
                    JComboBox quizzes = new JComboBox();
                    JButton delete = new JButton("Delete");
                    JButton exit = new JButton("Exit");
                    JButton refreshQuiz = new JButton("Refresh", icon);

                    ArrayList<String> titles = null;
                    try {
                        titles = (ArrayList<String>) inputFromServer.readObject();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    for (int i = 0; i < titles.size(); i++) {
                        quizzes.addItem(titles.get(i));
                    }
                    //System.out.println("Titles received");

                    panelDelete.add(title);
                    panelDelete.add(delete);
                    panelDelete.add(quizzes);
                    panelDelete.add(exit);
                    panelDelete.add(refreshQuiz);

                    layout.putConstraint(SpringLayout.WEST, title, 20, SpringLayout.WEST, panelDelete);
                    layout.putConstraint(SpringLayout.NORTH, title, 40, SpringLayout.NORTH, panelDelete);

                    layout.putConstraint(SpringLayout.WEST, quizzes, 200, SpringLayout.WEST, panelDelete);
                    layout.putConstraint(SpringLayout.NORTH, quizzes, 40, SpringLayout.NORTH, panelDelete);

                    layout.putConstraint(SpringLayout.WEST, delete, 100, SpringLayout.WEST, panelDelete);
                    layout.putConstraint(SpringLayout.NORTH, delete, 150, SpringLayout.NORTH, panelDelete);

                    layout.putConstraint(SpringLayout.WEST, exit, 200, SpringLayout.WEST, panelDelete);
                    layout.putConstraint(SpringLayout.NORTH, exit, 150, SpringLayout.NORTH, panelDelete);

                    layout.putConstraint(SpringLayout.WEST, refreshQuiz, 300, SpringLayout.WEST, panelDelete);
                    layout.putConstraint(SpringLayout.NORTH, refreshQuiz, 150, SpringLayout.NORTH, panelDelete);

                    SwingUtilities.updateComponentTreeUI(teacherFrame);

                    //functionality to button
                    delete.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Delete Quiz");
                            pw.flush();
                            //System.out.println("Sent action");

                            String deleteQuiz = (String) quizzes.getSelectedItem();

                            pw.println(deleteQuiz);
                            pw.flush();
                            //System.out.println(deleteQuiz);//test
                            SwingUtilities.updateComponentTreeUI(teacherFrame);

                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(panelDelete);
                        }
                    });

                    exit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Exit");
                            pw.flush();
                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(panelDelete);
                        }
                    });

                    refreshQuiz.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Refresh");
                            pw.flush();
                            try {
                                ArrayList<String> currentQuizzes = (ArrayList<String>) inputFromServer.readObject();
                                if (currentQuizzes.size() == 0) {
                                    quizzes.removeAllItems();
                                    quizzes.addItem("(No available quiz)");
                                    delete.setEnabled(false);
                                } else {
                                    quizzes.removeAllItems();
                                    delete.setEnabled(true);
                                    for (String quizName : currentQuizzes) {
                                        quizzes.addItem(quizName);
                                    }
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(panelDelete);
                        }
                    });

                }
            });

            //grade quiz functionality
            /*gradeQuiz.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    coursePanel.setEnabled(false);
                    menu.setEnabled(false);

                    pw.println("Grade Quiz");
                    pw.flush();

                    Panel panel = new Panel();
                    SpringLayout layout = new SpringLayout();
                    panel.setLayout(layout);
                    frame.add(panel);

                    JLabel quizTitle = new JLabel("Choose the quiz you would like to grade:");
                    JComboBox<String> quizzes = new JComboBox<>();
                    ArrayList<String> titles = null;
                    try {
                        titles = (ArrayList<String>) inputFromServer.readObject();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    for (int i = 0; i < titles.size(); i++) {
                        quizzes.addItem(titles.get(i));
                    }
                    System.out.println("Titles received");

                    //SwingUtilities.updateComponentTreeUI(frame);

                    JLabel studentName = new JLabel("Choose the username of the student you would like to grade:");
                    JComboBox<String> submissions = new JComboBox<>();


                    JLabel answerFile = new JLabel("Enter the name of the file with the correct answers:");
                    JTextField pathname = new JTextField(20);
                    JButton exit = new JButton("Exit");
                    JButton grade = new JButton("Grade");
                    JButton confirmQuiz = new JButton("Confirm");

                    studentName.setVisible(false);
                    submissions.setVisible(false);
                    answerFile.setVisible(false);
                    pathname.setVisible(false);
                    grade.setVisible(false);
                    pathname.setVisible(false);
                    exit.setVisible(false);

                    panel.add(quizTitle);
                    panel.add(quizzes);
                    panel.add(studentName);
                    panel.add(submissions);
                    panel.add(answerFile);
                    panel.add(pathname);
                    panel.add(exit);
                    panel.add(grade);
                    panel.add(confirmQuiz);

                    layout.putConstraint(SpringLayout.WEST, quizTitle, 20, SpringLayout.WEST, panel);
                    layout.putConstraint(SpringLayout.NORTH, quizTitle, 30, SpringLayout.NORTH, panel);

                    layout.putConstraint(SpringLayout.WEST, quizzes, 5, SpringLayout.EAST, quizTitle);
                    layout.putConstraint(SpringLayout.NORTH, quizzes, 30, SpringLayout.NORTH, panel);

                    layout.putConstraint(SpringLayout.WEST, confirmQuiz, 5, SpringLayout.EAST, quizzes);
                    layout.putConstraint(SpringLayout.NORTH, confirmQuiz, 30, SpringLayout.NORTH, panel);

                    layout.putConstraint(SpringLayout.WEST, studentName, 20, SpringLayout.WEST, panel);
                    layout.putConstraint(SpringLayout.NORTH, studentName, 30, SpringLayout.SOUTH, quizTitle);

                    layout.putConstraint(SpringLayout.WEST, submissions, 5, SpringLayout.EAST, studentName);
                    layout.putConstraint(SpringLayout.NORTH, submissions, 30, SpringLayout.SOUTH, quizTitle);

                    layout.putConstraint(SpringLayout.WEST, answerFile, 20, SpringLayout.WEST, panel);
                    layout.putConstraint(SpringLayout.NORTH, answerFile, 30, SpringLayout.SOUTH, studentName);

                    layout.putConstraint(SpringLayout.WEST, pathname, 5, SpringLayout.EAST, answerFile);
                    layout.putConstraint(SpringLayout.NORTH, pathname, 30, SpringLayout.SOUTH, studentName);

                    layout.putConstraint(SpringLayout.WEST, grade, 100, SpringLayout.WEST, panel);
                    layout.putConstraint(SpringLayout.NORTH, grade, 30, SpringLayout.SOUTH, answerFile);

                    layout.putConstraint(SpringLayout.WEST, exit, 50, SpringLayout.EAST, grade);
                    layout.putConstraint(SpringLayout.NORTH, exit, 30, SpringLayout.SOUTH, answerFile);

                    SwingUtilities.updateComponentTreeUI(frame);


                    //functionality to buttons
                    confirmQuiz.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String q = (String) quizzes.getSelectedItem();
                            pw.println(q);
                            pw.flush();

                            studentName.setVisible(true);
                            submissions.setVisible(true);
                            answerFile.setVisible(true);
                            pathname.setVisible(true);
                            grade.setVisible(true);
                            pathname.setVisible(true);

                            ArrayList<String> students = null;
                            try {
                                students = (ArrayList<String>) inputFromServer.readObject();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }

                            if (students.size() == 0) {
                                grade.setVisible(false);
                                answerFile.setVisible(false);
                                pathname.setVisible(false);
                                exit.setVisible(true);
                                submissions.addItem("No submissions available");
                            } else {
                                for (int i = 0; i < students.size(); i++) {
                                    submissions.addItem(students.get(i));
                                }
                                exit.setVisible(true);
                            }
                        }
                    });

                    grade.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String filename = pathname.getText();
                            if (filename == null || filename.equals("")) {
                                JOptionPane.showMessageDialog(null, "Filename cannot be empty!",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                pw.println("Grade Quiz");
                                pw.flush();
                                System.out.println("Sent action");//test
                                pw.println((String) submissions.getSelectedItem());
                                pw.flush();
                                System.out.println((String) submissions.getSelectedItem());//test
                                pw.println(filename);
                                pw.flush();
                                System.out.println(filename);
                                exit.setVisible(true);
                                int errorCheck = 100;
                                try {
                                    errorCheck = Integer.parseInt(bfr.readLine());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                if (errorCheck == 1) {
                                    JOptionPane.showMessageDialog(null, "Edited successfully",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                                } else if (errorCheck == 0) {
                                    JOptionPane.showMessageDialog(null, "Process Failed",
                                            "Failure", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                        }
                    });

                    exit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Exit");
                            pw.flush();
                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            frame.remove(panel);
                        }
                    });
                }
            });
            */

            viewAndGradeSubmission.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    coursePanel.setEnabled(false);
                    menu.setEnabled(false);

                    pw.println("View Submissions");
                    pw.flush();

                    Panel viewAndGradePanel = new Panel();
                    SpringLayout layout = new SpringLayout();
                    viewAndGradePanel.setLayout(layout);
                    teacherFrame.add(viewAndGradePanel);

                    JLabel quizTitle = new JLabel("Choose the quiz you would like to view:");
                    JComboBox<String> quizzes = new JComboBox<>();
                    ArrayList<String> titles = null;
                    try {
                        titles = (ArrayList<String>) inputFromServer.readObject();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    for (int i = 0; i < titles.size(); i++) {
                        quizzes.addItem(titles.get(i));
                    }
                    //System.out.println("Titles received");

                    JLabel studentName = new JLabel(
                            "Choose the username of the student whose submission you would like to view:");
                    JComboBox<String> submissions = new JComboBox<>();
                    JButton exit = new JButton("Exit");
                    JButton view = new JButton("View Submission");
                    JButton grade = new JButton("Grade Submission");
                    JLabel answerFile = new JLabel("Enter the name of the file with the correct answers:");
                    JTextField pathname = new JTextField(20);
                    JButton confirmQuiz = new JButton("Confirm");
                    JTextPane paper = new JTextPane();
                    StyledDocument doc = paper.getStyledDocument();
                    JPanel submissionPanel = new JPanel();

                    studentName.setVisible(false);
                    submissions.setVisible(false);
                    exit.setVisible(false);
                    view.setVisible(false);


                    viewAndGradePanel.add(quizTitle);
                    viewAndGradePanel.add(quizzes);
                    viewAndGradePanel.add(studentName);
                    viewAndGradePanel.add(submissions);
                    viewAndGradePanel.add(exit);
                    viewAndGradePanel.add(view);
                    viewAndGradePanel.add(confirmQuiz);


                    layout.putConstraint(SpringLayout.WEST, quizTitle, 20, SpringLayout.WEST,
                            viewAndGradePanel);
                    layout.putConstraint(SpringLayout.NORTH, quizTitle, 30, SpringLayout.NORTH,
                            viewAndGradePanel);

                    layout.putConstraint(SpringLayout.WEST, quizzes, 5, SpringLayout.EAST, quizTitle);
                    layout.putConstraint(SpringLayout.NORTH, quizzes, 30, SpringLayout.NORTH,
                            viewAndGradePanel);

                    layout.putConstraint(SpringLayout.WEST, confirmQuiz, 5, SpringLayout.EAST, quizzes);
                    layout.putConstraint(SpringLayout.NORTH, confirmQuiz, 30, SpringLayout.NORTH,
                            viewAndGradePanel);

                    layout.putConstraint(SpringLayout.WEST, studentName, 20, SpringLayout.WEST,
                            viewAndGradePanel);
                    layout.putConstraint(SpringLayout.NORTH, studentName, 30, SpringLayout.SOUTH, quizTitle);

                    layout.putConstraint(SpringLayout.WEST, submissions, 5, SpringLayout.EAST, studentName);
                    layout.putConstraint(SpringLayout.NORTH, submissions, 30, SpringLayout.SOUTH, quizTitle);

                    layout.putConstraint(SpringLayout.WEST, view, 100, SpringLayout.WEST, viewAndGradePanel);
                    layout.putConstraint(SpringLayout.NORTH, view, 30, SpringLayout.SOUTH, studentName);

                    layout.putConstraint(SpringLayout.WEST, exit, 50, SpringLayout.EAST, view);
                    layout.putConstraint(SpringLayout.NORTH, exit, 30, SpringLayout.SOUTH, studentName);

                    SwingUtilities.updateComponentTreeUI(teacherFrame);

                    confirmQuiz.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String q = (String) quizzes.getSelectedItem();
                            pw.println(q);
                            pw.flush();
                            studentName.setVisible(true);
                            submissions.setVisible(true);

                            ArrayList<String> students = null;
                            try {
                                students = (ArrayList<String>) inputFromServer.readObject();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }

                            if (students.size() == 0) {
                                view.setVisible(false);
                                exit.setVisible(true);
                                submissions.addItem("No submissions available");
                            } else {
                                for (int i = 0; i < students.size(); i++) {
                                    submissions.addItem(students.get(i));
                                    view.setVisible(true);
                                    exit.setVisible(true);
                                }
                            }
                        }
                    });

                    grade.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            String filename = pathname.getText();
                            if (filename == null || filename.equals("")) {
                                JOptionPane.showMessageDialog(null, "Filename cannot be empty!",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            } else {

                                pw.println("Grade Quiz");
                                pw.flush();
                                //System.out.println("Sent action");//test

                                pw.println(submissions.getSelectedItem());
                                pw.flush();

                                pw.println(filename);
                                pw.flush();
                                //System.out.println(filename);

                                exit.setVisible(true);

                                String errorCheck = null;
                                try {
                                    errorCheck = bfr.readLine();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                //System.out.println(errorCheck);

                                int errorInt = Integer.parseInt(errorCheck);

                                if (errorInt == 1) {
                                    JOptionPane.showMessageDialog(null, "Graded successfully",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                                } else if (errorInt == 0) {
                                    JOptionPane.showMessageDialog(null, "Process Failed",
                                            "Failure", JOptionPane.ERROR_MESSAGE);
                                }

                            }

                        }
                    });


                    view.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            pw.println("View Submission");

                            pw.println((String) submissions.getSelectedItem());
                            pw.flush();
                            //System.out.println((String) submissions.getSelectedItem());//test
                            ArrayList<String> viewable = new ArrayList<>();
                            try {
                                viewable = (ArrayList<String>) inputFromServer.readObject();
                                //System.out.println(viewable);//test
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }
                            for (int i = 0; i < viewable.size(); i++) {
                                try {
                                    doc.insertString(0, viewable.get(i) + "\n", null);
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }

                            teacherFrame.remove(viewAndGradePanel);
                            teacherFrame.add(submissionPanel);

                            submissionPanel.add(paper);
                            submissionPanel.add(exit);
                            submissionPanel.add(answerFile);
                            submissionPanel.add(pathname);
                            submissionPanel.add(grade);

                            SpringLayout subLayout = new SpringLayout();
                            submissionPanel.setLayout(subLayout);

                            subLayout.putConstraint(SpringLayout.WEST, exit,
                                    250, SpringLayout.WEST, submissionPanel);
                            subLayout.putConstraint(
                                    SpringLayout.NORTH, exit, 225, SpringLayout.NORTH, submissionPanel);

                            subLayout.putConstraint(SpringLayout.WEST, paper,
                                    250, SpringLayout.WEST, submissionPanel);

                            subLayout.putConstraint(SpringLayout.WEST, answerFile,
                                    1, SpringLayout.WEST, submissionPanel);
                            subLayout.putConstraint(SpringLayout.NORTH, answerFile,
                                    100, SpringLayout.NORTH, submissionPanel);

                            subLayout.putConstraint(SpringLayout.WEST, pathname,
                                    1, SpringLayout.EAST, answerFile);
                            subLayout.putConstraint(SpringLayout.NORTH, pathname,
                                    100, SpringLayout.NORTH, submissionPanel);

                            subLayout.putConstraint(SpringLayout.WEST, grade,
                                    215, SpringLayout.WEST, submissionPanel);
                            subLayout.putConstraint(SpringLayout.NORTH, grade,
                                    175, SpringLayout.NORTH, submissionPanel);

                            SwingUtilities.updateComponentTreeUI(teacherFrame);
                        }
                    });

                    exit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Exit");
                            pw.flush();
                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(viewAndGradePanel);
                            teacherFrame.remove(submissionPanel);
                            SwingUtilities.updateComponentTreeUI(teacherFrame);
                        }
                    });
                }
            });


            // Randomization button on the left panel
            randomization.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Disable top panel and left panel
                    coursePanel.setEnabled(false);
                    menu.setEnabled(false);

                    // Send message to server
                    pw.println("Randomize Quizzes");
                    pw.flush();

                    // Create the subPanel
                    Panel subPanel = new Panel();
                    SpringLayout layout = new SpringLayout();
                    subPanel.setLayout(layout);
                    teacherFrame.add(subPanel);

                    // Create components for this subPanel
                    JLabel selectStatement = new JLabel("Select the quiz to be randomized: ");
                    JComboBox<String> selectQuiz = new JComboBox<>();
                    JButton confirm = new JButton("Confirm");
                    JButton exit = new JButton("Exit");
                    Icon icon = new ImageIcon("icon.png");
                    JButton refresh = new JButton("Refresh", icon);

                    // Add components to the panel
                    subPanel.add(selectStatement);
                    subPanel.add(selectQuiz);
                    subPanel.add(confirm);
                    subPanel.add(exit);
                    subPanel.add(refresh);

                    // Set layout for selectStatement
                    layout.putConstraint(SpringLayout.WEST, selectStatement, 40, SpringLayout.WEST, subPanel);
                    layout.putConstraint(SpringLayout.NORTH, selectStatement, 40, SpringLayout.NORTH, subPanel);
                    // Set layout for selectQuiz
                    layout.putConstraint(SpringLayout.WEST, selectQuiz, 40, SpringLayout.EAST, selectStatement);
                    layout.putConstraint(SpringLayout.NORTH, selectQuiz, 40, SpringLayout.NORTH, subPanel);
                    // Set layout for confirm
                    layout.putConstraint(SpringLayout.WEST, confirm, 50, SpringLayout.WEST, subPanel);
                    layout.putConstraint(SpringLayout.NORTH, confirm, 150, SpringLayout.SOUTH, selectStatement);
                    // Set layout for exit
                    layout.putConstraint(SpringLayout.WEST, exit, 30, SpringLayout.EAST, confirm);
                    layout.putConstraint(SpringLayout.NORTH, exit, 150, SpringLayout.SOUTH, selectStatement);
                    // Set layout for inner refresh button
                    layout.putConstraint(SpringLayout.WEST, refresh, 30, SpringLayout.EAST, exit);
                    layout.putConstraint(SpringLayout.NORTH, refresh, 150, SpringLayout.SOUTH, selectStatement);

                    try {
                        ArrayList<String> currentQuizzes = (ArrayList<String>) inputFromServer.readObject();
                        if (currentQuizzes.size() == 0) {
                            selectQuiz.addItem("(No available quiz)");
                            confirm.setEnabled(false);
                        } else {
                            for (String quizName : currentQuizzes) {
                                selectQuiz.addItem(quizName);
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }


                    // Refresh the frame
                    SwingUtilities.updateComponentTreeUI(teacherFrame);


                    // Confirm button
                    confirm.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Confirm");
                            pw.flush();
                            pw.println(selectQuiz.getSelectedItem());
                            pw.flush();
                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(subPanel);
                            JOptionPane.showMessageDialog(null,
                                    "The selected quiz is now randomized!");
                        }
                    });

                    // Exit button
                    exit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Exit");
                            pw.flush();
                            coursePanel.setEnabled(true);
                            menu.setEnabled(true);
                            teacherFrame.remove(subPanel);
                        }
                    });

                    // Inner Refresh button
                    refresh.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pw.println("Refresh");
                            pw.flush();
                            try {
                                ArrayList<String> currentQuizzes = (ArrayList<String>) inputFromServer.readObject();
                                if (currentQuizzes.size() == 0) {
                                    selectQuiz.removeAllItems();
                                    selectQuiz.addItem("(No available quiz)");
                                    confirm.setEnabled(false);
                                } else {
                                    selectQuiz.removeAllItems();
                                    confirm.setEnabled(true);
                                    for (String quizName : currentQuizzes) {
                                        selectQuiz.addItem(quizName);
                                    }
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    });


                }
            });


            // Exit button on the left panel
            exit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pw.println("Exit");
                    pw.flush();
                    teacherFrame.dispose();
                }
            });

        } else {

            // Student Part
            JFrame studentFramePanel = new JFrame("Student");

            //System.out.println("Student student");//test

            studentFramePanel.setSize(800, 600);
            JPanel coursePanel = new JPanel();
            studentFramePanel.add(coursePanel, BorderLayout.NORTH);
            JButton confirmCourse = new JButton("Confirm");
            Icon icon = new ImageIcon("icon.png");
            JButton refresh = new JButton("Refresh", icon);
            refresh.setVerticalTextPosition(SwingConstants.BOTTOM);
            refresh.setHorizontalTextPosition(SwingConstants.CENTER);
            refresh.setSize(20, 20);
            JLabel selectCourse = new JLabel("Select Course:");
            JComboBox<String> courseField = new JComboBox<>();
            coursePanel.add(selectCourse);
            coursePanel.add(courseField);
            coursePanel.add(confirmCourse);
            coursePanel.add(refresh);

            // Left Panel
            JPanel menu = new JPanel();
            menu.setLayout(new GridLayout(4, 1));
            JButton takeAQuiz = new JButton("Take A Quiz");
            JButton viewGradedQuizzes = new JButton("View Graded Quizzes");
            JButton exit = new JButton("Exit");
            menu.add(takeAQuiz);
            menu.add(viewGradedQuizzes);
            menu.add(exit);
            takeAQuiz.setEnabled(false);
            viewGradedQuizzes.setEnabled(false);
            studentFramePanel.add(menu, BorderLayout.WEST);

            // Disable the "Close" button
            studentFramePanel.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            studentFramePanel.setVisible(true);

            // Get the courses immediately from server
            ArrayList<String> courses = new ArrayList<>();

            // Read the course from the server
            try {
                courses = (ArrayList<String>) inputFromServer.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Populate the Course Field
            if (courses.size() == 0) {
                courseField.addItem("No courses available");
            } else {
                for (int i = 0; i < courses.size(); i++) {
                    courseField.addItem(courses.get(i));
                }
            }

            // New panel for takeAQuiz and viewGradedQuizzes functionality
            JPanel functionalPanel = new JPanel();
            JLabel selectQuizText = new JLabel();
            JComboBox<String> selectQuizBox = new JComboBox<>();
            JButton selectQuizButton = new JButton("Confirm Quiz");

            // Add components to functionalPanel
            functionalPanel.setLayout(new BorderLayout());

            // Create panel for text and box
            JPanel selectQuizPanel = new JPanel();
            selectQuizPanel.add(selectQuizText);
            selectQuizPanel.add(selectQuizBox);
            selectQuizPanel.add(selectQuizButton);
            functionalPanel.add(selectQuizPanel, BorderLayout.NORTH);
            studentFramePanel.add(functionalPanel, BorderLayout.CENTER);
            functionalPanel.setVisible(false);

            // Populate selectQuiz label
            selectQuizText.setText("Select Quiz:");

            // Quiz Details Panel
            JPanel quizDetailsPanel = new JPanel(new BorderLayout());
            JPanel quizTextDetails = new JPanel();
            quizTextDetails.setLayout(new BoxLayout(quizTextDetails, BoxLayout.Y_AXIS));
            quizDetailsPanel.add(quizTextDetails, BorderLayout.CENTER);
            JLabel quizTitleDetail = new JLabel();
            JLabel quizNumberQuestions = new JLabel();
            JLabel quizTimeLimit = new JLabel();
            quizTextDetails.add(quizTitleDetail);
            quizTextDetails.add(quizNumberQuestions);
            quizTextDetails.add(quizTimeLimit);
            JButton confirmTakeQuiz = new JButton("Take Quiz");
            quizDetailsPanel.add(confirmTakeQuiz, BorderLayout.SOUTH);
            functionalPanel.add(quizDetailsPanel, BorderLayout.CENTER);
            quizDetailsPanel.setVisible(true);
            confirmTakeQuiz.setVisible(false);

            // Currently Taking Quiz GUI Panels
            JPanel quizPanel = new JPanel(new BorderLayout());
            JLabel quizQuestionInfo = new JLabel(); // Goes on NORTH of quizPanel
            JPanel responsesPanel = new JPanel(); // Goes on CENTER of quizPanel
            JPanel southQuizPanel = new JPanel(); // goes on SOUTH of quizPanel
            JPanel questionPanel = new JPanel(); // goes on EAST of quizPanel
            JPanel limitBoxSize = new JPanel(new BorderLayout()); // goes on questionPanel
            responsesPanel.setLayout(new BoxLayout(responsesPanel, BoxLayout.Y_AXIS));
            southQuizPanel.setLayout(new BoxLayout(southQuizPanel, BoxLayout.X_AXIS));
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
            JComboBox<String> questionBox = new JComboBox<>(); // goes on questionPanel
            JButton jumpToQuestion = new JButton("Jump to Question"); // goes on questionPanel
            JButton getTime = new JButton("Get Remaining Time"); // goes on southQuizPanel
            JCheckBox responseA = new JCheckBox(); // goes on responses panel
            JCheckBox responseB = new JCheckBox(); // goes on responses panel
            JCheckBox responseC = new JCheckBox(); // goes on responses panel
            JCheckBox responseD = new JCheckBox(); // goes on responses panel
            JButton saveQuestion = new JButton("Save Response"); // goes on southQuizPanel
            JButton submitQuiz = new JButton("Submit Quiz"); // goes on southQuizPanel
            JButton importAnswerFile = new JButton("Import Answer File"); // goes on southQuizPanel
            questionPanel.add(jumpToQuestion);
            limitBoxSize.add(questionBox, BorderLayout.NORTH);
            questionPanel.add(limitBoxSize);
            responsesPanel.add(responseA);
            responsesPanel.add(responseB);
            responsesPanel.add(responseC);
            responsesPanel.add(responseD);
            southQuizPanel.add(getTime);
            southQuizPanel.add(saveQuestion);
            southQuizPanel.add(importAnswerFile);
            southQuizPanel.add(submitQuiz);
            quizPanel.add(quizQuestionInfo, BorderLayout.NORTH);
            quizPanel.add(responsesPanel, BorderLayout.CENTER);
            quizPanel.add(southQuizPanel, BorderLayout.SOUTH);
            quizPanel.add(questionPanel, BorderLayout.EAST);
            quizPanel.setVisible(false);

            // View Graded Quiz additions for GUI
            JPanel gradedQuizPanel = new JPanel(new BorderLayout()); // GQP
            JLabel attemptLabel = new JLabel();
            gradedQuizPanel.add(attemptLabel, BorderLayout.NORTH);
            JPanel attemptDetails = new JPanel();
            attemptDetails.setLayout(new BoxLayout(attemptDetails, BoxLayout.X_AXIS));
            JLabel quizTitleLabel = new JLabel();
            attemptDetails.add(quizTitleLabel);
            gradedQuizPanel.add(attemptDetails, BorderLayout.NORTH);
            JPanel questionResults = new JPanel();
            questionResults.setLayout(new BoxLayout(questionResults, BoxLayout.Y_AXIS));
            JScrollPane questionResultsScroller = new JScrollPane(questionResults);
            gradedQuizPanel.add(questionResultsScroller, BorderLayout.CENTER);
            JButton viewQuizSelectButton = new JButton("Confirm Quiz");
            gradedQuizPanel.setVisible(false);
            questionResultsScroller.setVisible(false);

            SwingUtilities.updateComponentTreeUI(studentFramePanel);

            refresh.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    currentQuizTitle = (String) selectQuizBox.getSelectedItem();
                    pw.println("View Graded");
                    pw.flush();
                    pw.println(currentQuizTitle);
                    pw.flush();

                    ArrayList<String> gradedDetailString = null;
                    String currentUserName = null;
                    int counter = 1;

                    try {
                        gradedDetailString = (ArrayList<String>) inputFromServer.readObject();
                        currentUserName = (String) inputFromServer.readObject();
                    } catch (ClassNotFoundException | IOException exc) {
                        exc.printStackTrace();
                    }

                    questionResults.removeAll();

                    for (int i = 0; i < gradedDetailString.size(); i++) {
                        if (gradedDetailString.get(i).equals(currentUserName)) {
                            if (counter > 1) {
                                JLabel labelSpace = new JLabel(" ");
                                questionResults.add(labelSpace);
                            }
                            JLabel labelAttempt = new JLabel("Attempt " + counter);
                            questionResults.add(labelAttempt);
                            counter += 1;
                        }
                        JLabel labelName = new JLabel(gradedDetailString.get(i));
                        questionResults.add(labelName);
                    }

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                    // Sends desired operation to server
                    pw.println("Take Quiz");
                    pw.flush();

                    // read the available quizzes from the server
                    try {
                        courseQuizzes = (ArrayList<Quiz>) inputFromServer.readObject();
                    } catch (ClassNotFoundException cnf) {
                        cnf.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    // reset quiz box
                    selectQuizBox.removeAllItems();

                    // check if there are ANY quizzes available
                    if (courseQuizzes == null || courseQuizzes.size() == 0)
                        selectQuizBox.addItem("No Quizzes Available");
                    else {
                        // populate the quiz box with the available quizzes
                        for (int i = 0; i < courseQuizzes.size(); i++) {
                            selectQuizBox.addItem(courseQuizzes.get(i).getQuizTitle());
                        }
                    }

                    // Write quiz action to server
                    pw.println("Select Quiz");
                    pw.flush();

                    // Write the desired quiz to server
                    pw.println(selectQuizBox.getSelectedItem());
                    pw.flush();

                    // Read the desired quiz object from the server
                    try {
                        currentQuiz = (Quiz) inputFromServer.readObject();
                    } catch (ClassNotFoundException | IOException exc) {
                        exc.printStackTrace();
                    }

                    currentQuizTitle = currentQuiz.getQuizTitle();
                    currentQuizTimeLimit = currentQuiz.getTimeLimit();
                    currentQuizNumQ = currentQuiz.getQuestions().size();

                    quizTitleDetail.setText(currentQuizTitle);
                    quizNumberQuestions.setText(String.format("Questions: %d", currentQuizNumQ));
                    quizTimeLimit.setText(String.format("Time Limit: %d minutes", currentQuizTimeLimit));

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                }
            });


            viewQuizSelectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    currentQuizTitle = (String) selectQuizBox.getSelectedItem();
                    pw.println("View Graded");
                    pw.flush();
                    pw.println(currentQuizTitle);
                    pw.flush();

                    ArrayList<String> gradedDetailString = null;
                    String currentUserName = null;
                    int counter = 1;

                    try {
                        gradedDetailString = (ArrayList<String>) inputFromServer.readObject();
                        currentUserName = (String) inputFromServer.readObject();
                    } catch (ClassNotFoundException | IOException exc) {
                        exc.printStackTrace();
                    }

                    questionResults.removeAll();

                    for (int i = 0; i < gradedDetailString.size(); i++) {
                        if (gradedDetailString.get(i).equals(currentUserName)) {
                            if (counter > 1) {
                                JLabel labelSpace = new JLabel(" ");
                                questionResults.add(labelSpace);
                            }
                            JLabel labelAttempt = new JLabel("Attempt " + counter);
                            questionResults.add(labelAttempt);
                            counter += 1;
                        }
                        JLabel labelName = new JLabel(gradedDetailString.get(i));
                        questionResults.add(labelName);
                    }

                    questionResults.setVisible(true);
                    questionResultsScroller.setVisible(true);
                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                }
            });

            viewGradedQuizzes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // Sends desired operation to server
                    pw.println("Take Quiz");
                    pw.flush();

                    // read the available quizzes from the server
                    try {
                        courseQuizzes = (ArrayList<Quiz>) inputFromServer.readObject();
                    } catch (ClassNotFoundException cnf) {
                        cnf.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    // reset quiz box
                    selectQuizBox.removeAllItems();

                    // check if there are ANY quizzes available
                    if (courseQuizzes == null || courseQuizzes.size() == 0)
                        selectQuizBox.addItem("No Quizzes Available");
                    else {
                        // populate the quiz box with the available quizzes
                        for (int i = 0; i < courseQuizzes.size(); i++) {
                            selectQuizBox.addItem(courseQuizzes.get(i).getQuizTitle());
                        }
                    }

                    // set the quiz panel to be visible
                    functionalPanel.setVisible(true);

                    selectQuizPanel.remove(selectQuizButton);
                    selectQuizPanel.add(viewQuizSelectButton);
                    functionalPanel.remove(quizPanel);
                    functionalPanel.add(gradedQuizPanel, BorderLayout.CENTER);
                    gradedQuizPanel.setVisible(true);

                    quizDetailsPanel.setVisible(false);

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                }
            });

            confirmCourse.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.println("Select Course");
                    pw.flush();
                    pw.println(courseField.getSelectedItem());
                    pw.flush();
                    takeAQuiz.setEnabled(true);
                    viewGradedQuizzes.setEnabled(true);
                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                    // Update Quiz Box
                    pw.println("Take Quiz");
                    pw.flush();

                    // read the available quizzes from the server
                    try {
                        courseQuizzes = (ArrayList<Quiz>) inputFromServer.readObject();
                    } catch (ClassNotFoundException cnf) {
                        cnf.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    // reset quiz box
                    selectQuizBox.removeAllItems();

                    // check if there are ANY quizzes available
                    if (courseQuizzes == null || courseQuizzes.size() == 0)
                        selectQuizBox.addItem("No Quizzes Available");
                    else {
                        // populate the quiz box with the available quizzes
                        for (int i = 0; i < courseQuizzes.size(); i++) {
                            selectQuizBox.addItem(courseQuizzes.get(i).getQuizTitle());
                        }
                    }

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                }
            });

            getTime.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    long currentTimeInMethod = System.currentTimeMillis();
                    long minutesElapsed = (currentTimeInMethod - startTime) / 60000;
                    long timeLeft = currentQuizTimeLimit - minutesElapsed;

                    JOptionPane.showMessageDialog(null,
                            String.format("Time Remaining: %d minutes", timeLeft),
                            "Check Time", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            takeAQuiz.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // Sends desired operation to server
                    pw.println("Take Quiz");
                    pw.flush();

                    // read the available quizzes from the server
                    try {
                        courseQuizzes = (ArrayList<Quiz>) inputFromServer.readObject();
                    } catch (ClassNotFoundException cnf) {
                        cnf.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    // reset quiz box
                    selectQuizBox.removeAllItems();

                    // check if there are ANY quizzes available
                    if (courseQuizzes == null || courseQuizzes.size() == 0)
                        selectQuizBox.addItem("No Quizzes Available");
                    else {
                        // populate the quiz box with the available quizzes
                        for (int i = 0; i < courseQuizzes.size(); i++) {
                            selectQuizBox.addItem(courseQuizzes.get(i).getQuizTitle());
                        }
                    }

                    gradedQuizPanel.setVisible(false);
                    selectQuizPanel.add(selectQuizButton);
                    selectQuizPanel.remove(viewQuizSelectButton);
                    functionalPanel.remove(gradedQuizPanel);
                    functionalPanel.add(quizPanel, BorderLayout.CENTER);
                    gradedQuizPanel.setVisible(false);
                    questionResultsScroller.setVisible(false);

                    // set the quiz panel to be visible
                    functionalPanel.setVisible(true);
                    SwingUtilities.updateComponentTreeUI(studentFramePanel);
                }
            });

            selectQuizButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Write quiz action to server
                    pw.println("Select Quiz");
                    pw.flush();

                    // Write the desired quiz to server
                    pw.println(selectQuizBox.getSelectedItem());
                    pw.flush();

                    // Read the desired quiz object from the server
                    try {
                        currentQuiz = (Quiz) inputFromServer.readObject();
                    } catch (ClassNotFoundException | IOException exc) {
                        exc.printStackTrace();
                    }

                    currentQuizTitle = currentQuiz.getQuizTitle();
                    currentQuizTimeLimit = currentQuiz.getTimeLimit();
                    currentQuizNumQ = currentQuiz.getQuestions().size();

                    quizTitleDetail.setText(currentQuizTitle);
                    quizNumberQuestions.setText(String.format("Questions: %d", currentQuizNumQ));
                    quizTimeLimit.setText(String.format("Time Limit: %d minutes", currentQuizTimeLimit));

                    quizTitleDetail.setVisible(true);
                    quizDetailsPanel.setVisible(true);
                    quizTextDetails.setVisible(true);

                    confirmTakeQuiz.setVisible(true);

                    quizDetailsPanel.setVisible(true);
                    functionalPanel.add(quizDetailsPanel, BorderLayout.CENTER);

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);
                }
            });

            confirmTakeQuiz.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    pw.println("Attempt Quiz");
                    pw.flush();

                    takeAQuiz.setEnabled(false);
                    viewGradedQuizzes.setEnabled(false);
                    exit.setEnabled(false);
                    confirmCourse.setEnabled(false);
                    selectQuizButton.setEnabled(false);
                    refresh.setEnabled(false);
                    selectQuizBox.setEnabled(false);
                    courseField.setEnabled(false);
                    functionalPanel.remove(quizDetailsPanel);

                    startDate = new Date();
                    startTime = System.currentTimeMillis();

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                    try {
                        // the randomized question numbers
                        randomizedQuestions = (ArrayList<Integer>) inputFromServer.readObject();

                        // the randomized response choices
                        randomizedChoices = (ArrayList<ArrayList<Integer>>) inputFromServer.readObject();

                    } catch (ClassNotFoundException | IOException exc) {
                        exc.printStackTrace();
                    }

                    quizQuestionInfo.setText(String.format("1.) %s\n", currentQuiz.getQuestions().get(
                            randomizedQuestions.get(0)).getQuestion()));
                    responseA.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(0)).getAnswers()[
                            randomizedChoices.get(0).get(0)]);
                    responseB.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(0)).getAnswers()[
                            randomizedChoices.get(0).get(1)]);
                    responseC.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(0)).getAnswers()[
                            randomizedChoices.get(0).get(2)]);
                    responseD.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(0)).getAnswers()[
                            randomizedChoices.get(0).get(3)]);

                    for (int i = 0; i < currentQuiz.getQuestions().size(); i++) {
                        String generateQuestionJump = String.format("%d", i + 1);
                        questionBox.addItem(generateQuestionJump);
                    }

                    currentQuestionNum = 0;

                    functionalPanel.add(quizPanel);
                    quizPanel.setVisible(true);

                    studentResponses = new ArrayList<>(currentQuizNumQ);
                    for (int i = 0; i < currentQuizNumQ; i++) {
                        studentResponses.add("a");
                    }

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                }
            });

            responseA.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    responseB.setSelected(false);
                    responseC.setSelected(false);
                    responseD.setSelected(false);
                    selectedChoice = "a";

                }
            });

            responseB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    responseA.setSelected(false);
                    responseC.setSelected(false);
                    responseD.setSelected(false);
                    selectedChoice = "b";

                }
            });

            responseC.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    responseA.setSelected(false);
                    responseB.setSelected(false);
                    responseD.setSelected(false);
                    selectedChoice = "c";

                }
            });

            responseD.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    responseA.setSelected(false);
                    responseB.setSelected(false);
                    responseC.setSelected(false);
                    selectedChoice = "d";

                }
            });

            saveQuestion.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    studentResponses.set(currentQuestionNum, selectedChoice);
                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                }
            });

            jumpToQuestion.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    studentResponses.set(currentQuestionNum, selectedChoice);
                    currentQuestionNum = Integer.parseInt((String) questionBox.getSelectedItem()) - 1;
                    selectedChoice = studentResponses.get(currentQuestionNum);

                    switch (selectedChoice) {
                        case "a":
                            responseA.setSelected(true);
                            responseB.setSelected(false);
                            responseC.setSelected(false);
                            responseD.setSelected(false);
                            break;
                        case "b":
                            responseA.setSelected(false);
                            responseB.setSelected(true);
                            responseC.setSelected(false);
                            responseD.setSelected(false);
                            break;
                        case "c":
                            responseA.setSelected(false);
                            responseB.setSelected(false);
                            responseC.setSelected(true);
                            responseD.setSelected(false);
                            break;
                        case "d":
                            responseA.setSelected(false);
                            responseB.setSelected(false);
                            responseC.setSelected(false);
                            responseD.setSelected(true);
                            break;
                    }

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                    //Update question Info
                    quizQuestionInfo.setText(String.format("%d.) %s\n",
                            currentQuestionNum + 1, currentQuiz.getQuestions().get(randomizedQuestions.get(
                                    currentQuestionNum)).getQuestion()));
                    responseA.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(
                            currentQuestionNum)).getAnswers()[randomizedChoices.get(currentQuestionNum).get(0)]);
                    responseB.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(
                            currentQuestionNum)).getAnswers()[randomizedChoices.get(currentQuestionNum).get(1)]);
                    responseC.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(
                            currentQuestionNum)).getAnswers()[randomizedChoices.get(currentQuestionNum).get(2)]);
                    responseD.setText(currentQuiz.getQuestions().get(randomizedQuestions.get(
                            currentQuestionNum)).getAnswers()[randomizedChoices.get(currentQuestionNum).get(3)]);

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);
                }
            });

            importAnswerFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String importFileName = JOptionPane.showInputDialog(
                            null, "Enter the Name of the Answer File");

                    if (importFileName == null) {
                        return;
                    }

                    BufferedReader fileReader;
                    String readAnswer;
                    try {
                        fileReader = new BufferedReader(new FileReader(importFileName));
                        readAnswer = fileReader.readLine();
                    } catch (FileNotFoundException fnfe) {
                        JOptionPane.showMessageDialog(null, "No such file found.",
                                "File Not Found", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(null, "Invalid File Format",
                                "Invalid File", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (readAnswer == null) {
                        JOptionPane.showMessageDialog(null, "Invalid File Format",
                                "Invalid File", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    readAnswer = readAnswer.toLowerCase();
                    switch (readAnswer) {
                        case "a":
                            responseA.setSelected(true);
                            responseB.setSelected(false);
                            responseC.setSelected(false);
                            responseD.setSelected(false);
                            selectedChoice = "a";
                            studentResponses.set(currentQuestionNum, "a");
                            JOptionPane.showMessageDialog(null, "File Read Successfully!",
                                    "Upload Successful", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case "b":
                            responseA.setSelected(false);
                            responseB.setSelected(true);
                            responseC.setSelected(false);
                            responseD.setSelected(false);
                            selectedChoice = "b";
                            studentResponses.set(currentQuestionNum, "b");
                            JOptionPane.showMessageDialog(null, "File Read Successfully!",
                                    "Upload Successful", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case "c":
                            responseA.setSelected(false);
                            responseB.setSelected(false);
                            responseC.setSelected(true);
                            responseD.setSelected(false);
                            selectedChoice = "c";
                            studentResponses.set(currentQuestionNum, "c");
                            JOptionPane.showMessageDialog(null, "File Read Successfully!",
                                    "Upload Successful", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case "d":
                            responseA.setSelected(false);
                            responseB.setSelected(false);
                            responseC.setSelected(false);
                            responseD.setSelected(true);
                            selectedChoice = "d";
                            studentResponses.set(currentQuestionNum, "d");
                            JOptionPane.showMessageDialog(null, "File Read Successfully!",
                                    "Upload Successful", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid File Format",
                                    "Invalid File", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                }
            });

            submitQuiz.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] options = {"Yes", "No"};
                    int selectedOption = JOptionPane.showOptionDialog(null,
                            "Are you sure you want to submit the quiz?", "Submission Confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (selectedOption == JOptionPane.NO_OPTION) {
                        return;
                    }

                    endDate = new Date();

                    pw.println("Submit Quiz");
                    pw.flush();

                    try {
                        outputToServer.writeObject(studentResponses);
                        outputToServer.flush();
                        outputToServer.writeObject(startDate);
                        outputToServer.flush();
                        outputToServer.writeObject(endDate);
                        outputToServer.flush();
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }

                    exit.setEnabled(true);
                    takeAQuiz.setEnabled(true);
                    viewGradedQuizzes.setEnabled(true);
                    confirmCourse.setEnabled(true);
                    selectQuizButton.setEnabled(true);
                    refresh.setEnabled(true);
                    selectQuizBox.setEnabled(true);
                    courseField.setEnabled(true);
                    quizPanel.setVisible(false);
                    functionalPanel.remove(quizPanel);

                    courseQuizzes = null;
                    currentQuiz = null;
                    currentQuizTitle = null;
                    randomizedQuestions = null;
                    randomizedChoices = null;
                    studentResponses = null;
                    startDate = null;
                    endDate = null;

                    SwingUtilities.updateComponentTreeUI(studentFramePanel);

                }
            });

            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.println("Exit");
                    pw.flush();
                    coursePanel.setEnabled(true);
                    menu.setEnabled(true);
                    studentFramePanel.dispose();
                }
            });
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket socket = new Socket("localhost", 1234);
        bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream());
        outputToServer = new ObjectOutputStream(socket.getOutputStream());
        inputFromServer = new ObjectInputStream(socket.getInputStream());

        // Login window here


        // Set "isTeacher" to true for testing purpose
        isTeacher = true;

        // Send isTeacher to server
        //pw.println("teacher");
        //pw.flush();
        //System.out.println("Sent");
        // Connected Message
        JOptionPane.showMessageDialog(null, "Connected!", "Login",
                JOptionPane.INFORMATION_MESSAGE);

        // Run the GUI on EDT
        SwingUtilities.invokeLater(new Client());
    }
}
