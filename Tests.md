Test 1: User log in

Steps:
User runs server.java
User runs client.java
User clicks "Log In" Button
User enters username into textbox
User enters password into textbox
User clicks the inner "Log in" button. 

Expected result: Application verifies the username and password, and opens student or teacher window depending on account type

Test Status: Passed. 

Test 2: User account creation

Steps:
User runs server.java
User runs client.java
User clicks "Create Account" button
User enters username into textbox
User enters password into textbox
User enters the same password into "confirm password" textbox
User checks the "Teacher" checkbox
User clicks the inner "Create Account" Button.
Application closes

Expected Result: Application adds a Teacher with the typed username and password into the accounts.txt file

Test Status: Passed.

Test 3: User account creation with user mistake verifying password

Steps:
User runs server.java
User runs client.java
User clicks "Create Account" button
User enters username into textbox
User enters password into textbox
User enters a different password into "confirm password" textbox
User clicks the inner "Create Account" Button.
User clicks "Ok" on error message JOptionPane
User enters password into textbox
user enters the same password into "confirm password" textbox
User clicks the inner "Create Account" Button.
Application closes

Expected Result: Application displays error JOptionPane on first attempt, 
then adds a Student with the typed username and password into the accounts.txt file on second attempt

Test Status: Passed.

Test 4: User edits account username

Steps:
User runs server.java
User runs client.java
User clicks "Edit Account" button
User enters username into textbox
User enters password into textbox
User clicks "Change Username" button
User enters new username into JOptionPane textbox
User clicks "OK"
Program displays "Username Changed Successfully" JOptionPane
User clicks "OK"
Application closes

Expected Result: Application changes username value for the entered username and password in accounts.txt

Test Status: Passed.

Test 5: User edits account password

Steps:
User runs server.java
User runs client.java
User clicks "Edit Account" button
User enters username into textbox
User enters password into textbox
User clicks "Change Password" button
User enters new password into JOptionPane textbox
User clicks "OK"
Program displays "Password Changed Successfully" JOptionPane
User clicks "OK"
Application closes

Expected Result: Application changes password value for the entered username and password in accounts.txt

Test Status: Passed.

Test 6: User edits account type

Steps:
User runs server.java
User runs client.java
User clicks "Edit Account" button
User enters username into textbox
User enters password into textbox
User clicks "Change Account Type" button
Program displays "Account Type Changed Successfully" JOptionPane
User clicks "OK"
Application closes

Expected Result: Application changes account type String ("Teacher:" to "Student:" or vice versa) for the entered username and password in accounts.txt

Test Status: Passed.

Test 7: User deletes account

Steps:
User runs server.java
User runs client.java
User clicks "Delete Account" button
User enters username into textbox
User enters password into textbox
User clicks innter "Delete Account" button
Program displays "Account Deleted Successfully" JOptionPane
User clicks "OK"
Application closes

Expected Result: Application removes entered account from accounts.txt

Test Status: Passed.

Test 8: Teacher selects course

Teacher runs server.java
Teacher runs client.java
Teacher logs in with the "Log In" button
Teacher selects course from dropdown 
Teacher clicks confirm

Expected Result: Course is selected for the session and teacher options are unlocked

Test Status: Passed.

Test 9: Teacher creates course

Teacher runs server.java
Teacher runs client.java
Teacher logs in with the "Log In" button
Teacher enters course name in the text field
Teacher clicks "Create Course"

Expected Result: New course is created and is selected for the session and teacher options are unlocked

Test Status: Passed.

Test 10: Teacher edits quiz

Teacher runs server.java
Teacher runs client.java
Teacher logs in with the "Log In" button
Teacher selects course from dropdown
Teacher clicks "Confirm"
Teacher clicks "Edit Quiz"
Teacher enters name of quiz to be edited and clicks ok
Teacher enters number of the question to be edited and clicks ok
Teacher enters new question and new answer choices in the text fields
Teacher clicks "Edit Question"

Expected Result: Specified question is edited and success message pops up

Test Status: Passed.

Test 11: Teacher deletes quiz

Teacher runs server.java
Teacher runs client.java
Teacher logs in with the "Log In" button
Teacher selects course from dropdown
Teacher clicks "Confirm"
Teacher clicks "Delete Quiz"
Teacher selects name of the quiz to be deleted from dropdown
Teacher clicks "Delete"

Expected Result: Selected quiz is deleted from the course and the Quiz.txt file

Test Status: Passed.

Test 12: Teacher views and grades a submission

Teacher runs server.java
Teacher runs client.java
Teacher logs in with the "Log In" button
Teacher selects course from dropdown
Teacher clicks "Confirm"
Teacher clicks "View and Grade Submissions"
Teacher selects quiz from dropdown
Teacher clicks "Confirm"
Teacher selects username of the student whose submission they would like to view from dropdown
Teacher clicks "View Submission"
Teacher enters pathname to a text file containing the correct answers in the text field
Teacher clicks "Grade Submission"

Expected Result: Text box appears with student submission after clicking view and submission is graded and recorded in
the GradedQuizzes.txt file

Test Status: Passed.

Test 13: Teacher tries to view submissions for a quiz with none available

Teacher runs server.java
Teacher runs client.java
Teacher logs in with the "Log In" button
Teacher selects course from dropdown
Teacher clicks "Confirm"
Teacher clicks "View and Grade Submissions"
Teacher selects quiz from dropdown
Teacher clicks "Confirm"

Expected Result: Submissions dropdown displays message "No submissions available"

Test Status: Passed.


Test 14: Teacher – Refresh Button

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
Another user adds a new course on another computer;
User clicks “Refresh” button.

Expected Result: The dropdown box for course selection shows the newly added course.

Test Status: Passed.


Test 15: Teacher – Create Quiz – Invalid Inputs

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User clicks “Create Quiz” button;
User enters nothing for the quiz title;
User enters nothing for the time limit;
User enters a String for the time limit;
User enters a number smaller than 1 for the time limit.

Expected Result: The program prompts out error message pane for invalid inputs.

Test Status: Passed.


Test 16: Teacher – Create Quiz – Add Questions

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User clicks “Create Quiz” button;
User enters a proper String for the quiz title;
User enters a proper number for the time limit;
User enters nothing for the question;
User clicks “Add Question”.

Expected Result: The program prompts out error message pane for empty input.

Test Status: Passed.


Test 17: Teacher – Create Quiz – Add Questions and Create Quiz

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User clicks “Create Quiz” button;
User enters a proper String for the quiz title;
User enters a proper number for the time limit;
User enters proper Strings for the question and choices;
User clicks “Add Question”;
User clicks “Complete”.

Expected Result: A new quiz is created and added to the current course.

Test Status: Passed.


Test 18: Teacher – Import Quiz – Invalid Inputs

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User clicks “Import Quiz” button;
User enters nothing for the file path;
User enters an invalid file path;
User enters a correct file path to a text file with inappropriate formatting;
User clicks “Confirm”.

Expected Result: The program prompts out error message pane for invalid / empty input.

Test Status: Passed.


Test 19: Teacher – Import Quiz

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User clicks “Import Quiz” button;
User enters a correct file path to a text file with correct formatting;
User clicks “Confirm”.

Expected Result: A new quiz is imported and added to the current course.

Test Status: Passed.


Test 19: Teacher – Randomization – Refresh button

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User clicks “Randomize Quizzes” button;
Another user adds / deletes a quiz from the current course on a different computer.
User clicks “Refresh” button.

Expected Result: The quiz selection dropdown box lists a newly updated quiz list for the current course.

Test Status: Passed.


Test 20: Teacher – Randomization – No quiz

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User selects a course with no quiz in it.
User clicks “Randomize Quizzes” button.

Expected Result: The quiz selection dropdown box shows a “No Quiz Available” message.

Test Status: Passed.


Test 21: Teacher – Randomization

Steps: 
User runs Server.java;
User runs Client.java;
User logs in as a teacher;
User clicks “Randomize Quizzes” button;
User selects a quiz from the dropdown box;
User clicks “Confirm” button.

Expected Result: The quiz is noted to be randomized in “Randomization.txt”. When a student takes this quiz, the questions and answers are randomized.

Test Status: Passed.


Test 22: Student - Take Quiz

Steps:
User runs Server.java;
User runs Client.java;
User logs in as a student using credentials USR: Ethan, PSW: 123456;
User selects the course dropdown box and selects "Course1";
User clicks "Take A Quiz" button;
User selects the Select Quiz dropdown box and selects "TestQuiz01";
User clicks the "Confirm Quiz";
User clicks the "Take Quiz" button at the bottom of the window;
User selects "purple" checkbox;
User selects "dark purple checkbox";
User clicks "Get Remaining Time" button;
User clicks "OK" button in the JOptionPane;
User clicks the "Save Response" Button;
User selects the dropdown box below the "Jump to Question" button;
User selects "2" in the dropdown menu;
User clicks the "Jump to Question" Button;
User clicks the "Import Answer File" button;
User types "QuestionAnswerImport.txt" into the text box;
User selects the "OK" button on the JOptionPane;
User clicks the "Submit Quiz" button;
User clicks the "No" button on the confirmation JOptionPane;
User clicks the "Submit Quiz" button;
User clicks the "Yes" button on the confirmation JOptionPane;
User clicks the "Exit" button;

Expected Result: The student takes a quiz. In the quiz, the remaining time should start at 150 minutes. 
When importing the answer, the last response box is selected. 
When submitting the quiz, the quiz attempt is added to the end of the QuizSubmissions.txt file.

Test Status: Passed.


Test 23: Student - View Graded Quizzes

Steps:
User runs Server.java;
User runs Client.java;
User logs in as a student using credentials USR: Ethan, PSW: 123456;
User selects the course dropdown box and selects "Course1";
User selects the "View Graded Quizzes" button;
User selects "TestQuiz01" in the select quiz dropdown box;
User clicks the "Confirm Quiz" button;
User clicks the "Exit" button;

Expected Result: A text field with the student's graded TestQuiz01 attempts appears.
Then the window is closed.

Test Status: Passed.


Test 24: Student – Refresh Button

Steps:
User runs Server.java;
User runs Client.java;
User logs in as a student;
Another user adds a new course on another computer;
User clicks “Refresh” button.

Expected Result: The dropdown box for course selection shows the newly added course.

Test Status: Passed.