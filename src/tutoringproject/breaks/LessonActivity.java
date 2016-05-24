package tutoringproject.breaks;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by arsalan on 5/9/16.
 *
 * Filled out by aditi.
 * NOTE: This class makes the lesson flow in a very specific order.
 * Many things in the LessonActivity have been hard-coded for the purposes of our specific
 * study where the robot teaches a simple lesson on multiplication and parentheses in the context
 * of order of operations.
 */
public class LessonActivity extends Activity implements TCPClientOwner {
    // XML element variables
    private TextView content;
    private TextView exampleProblem;
    private TextView exampleStep1;
    private TextView exampleStep2;
    private TextView exampleStep3;
    private TextView exampleStep4;
    private TextView exampleStep5;
    private NoImeEditText AnswerText1;
    private NoImeEditText AnswerText2;
    private NoImeEditText AnswerText3;
    private Button AnswerButton1;
    private Button AnswerButton2;
    private Button AnswerButton3;
    private KeyboardView mKeyboardView;
    private Button continueLessonButton;
    private Button beginButton;

    private enum LessonView {
        MULTPLICATIONVIEW, PARENTHESESVIEW
    }
    private LessonView lessonView = LessonView.MULTPLICATIONVIEW;

    public final int MAX_NUM_DIGITS = 5;
    public final int EXAMPLE_ANSWER1 = 12;
    public final int EXAMPLE_ANSWER2 = 10;
    public final int EXAMPLE_ANSWER3 = 9;
    public final int EXAMPLE_ANSWER4 = 45;
    public final int EXAMPLE_ANSWER5 = 47;

    // Public variables ============================================================================

    // Speech strings
    //TODO: check speech with nao and adjust as necessary
    public String START_MSG =
        "Let's get started on our lesson.";
    public String MULT_STEP1_MSG =
        "When we have a problem with just addition and subtraction, we do it in order, from left to right. " +
        "But, when we have multiplication, addition, and subtraction in the problem, we have to do the multiplication first. " +
        "Let me show you what I mean! Let's look at the problem on the screen. " +
        "We can see there is multiplication here, so we have to do six times two first. " +
        "Enter your answer in the box on the screen.";
    public String MULT_STEP2_CORRECT_MSG =
        "That's correct! ";
    public String MULT_STEP2_INCORRECT_MSG =
        "Oops! The correct answer here is twelve. ";
    public String MULT_STEP3_MSG =
        "Now that we have the answer to that part, the problem looks like three plus twelve minus five. " +
        "You can just complete this part in order since there is just addition and subtraction in it! " +
        "Go ahead and enter your answer to this part in the box on the screen. ";
    public String MULT_STEP4_CORRECT_MSG =
            "Nice job! " +
            "Press the button on the bottom of the screen to continue to the next part of the lesson!";
    public String MULT_STEP4_INCORRECT_MSG =
            "The correct answer is ten. " +
            "Press the button on the bottom of the screen to continue to the next part of the lesson!";
    public String PAREN_INTRO_MSG =
            "Now we will learn about parentheses.";
    public String PAREN_STEP1_MSG =
            "The symbols you see on the screen are called parentheses. " +
            "When we have these in a math problem, we always do what's inside the parentheses first. " +
            "Let me show you what I mean! Let's look at this problem on the screen together. " +
            "We see parentheses in this problem, so we have to do three plus six first since that part is inside the parentheses. " +
            "Enter your answer to this part in the box on the screen.";
    public String PAREN_STEP2_CORRECT_MSG =
            "That's right!";
    public String PAREN_STEP2_INCORRECT_MSG =
            "The correct answer here is nine.";
    public String PAREN_STEP3_MSG =
            "Now that we did the parentheses, the problem looks like two plus nine times five. " +
            "We just learned in our last lesson that multiplication comes before addition so next we have to do nine times five. " +
            "Enter your answer to this part in the box on the screen.";
    public String PAREN_STEP4_CORRECT_MSG =
            "That's Correct!";
    public String PAREN_STEP4_INCORRECT_MSG =
            "Oops! The correct answer is forty-five.";
    public String PAREN_STEP5_MSG =
            "Now we have completed another step and the problem just looks like two plus forty-five. " +
            "This is just addition so we are ready to finish the whole problem. " +
            "Put your answer in the box on the screen.";
    public String PAREN_STEP6_CORRECT_MSG =
            "Good job! " +
            "Now let's try practicing what we just learned about multiplication and parentheses! " +
            "Press the button on the bottom of the screen to start!";
    public String PAREN_STEP6_INCORRECT_MSG =
            "The correct answer is forty-seven. " +
            "Now let's try practicing what we just learned about multiplication and parentheses! " +
            "Press the button on the bottom of the screen to start!";

    public String NOTHING_STRING = "nothing";



    // Tablet text strings
    public String CLICK_BEGIN_BUTTON_TEXT =
        "Click the button below to begin the tutoring session.";
    public String PAREN_CONTENT =
        "These symbols are called parentheses: ( ) \n When we have these in a math problem, we always do what's inside the parentheses first! Let's look at this problem together:";
    public String PAREN_EXAMPLE_PROBLEM =
        "2 + (3 + 6) x 5";
    public String PAREN_STEP1_TEXT =
        "So, first we do: (3 + 6) = ";
    public String PAREN_STEP2_TEXT =
        "Now, the problem looks like this: 2 + 9 x 5. From what we learned in our last lesson, we have to do the multiplication next.";
    public String PAREN_STEP3_TEXT =
        "So, next we do: 9 x 5 = ";


    // Constructor =================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        beginButton = (Button)   findViewById(R.id.beginButton);
        content     = (TextView) findViewById(R.id.content2);
        exampleProblem = (TextView) findViewById(R.id.example_problem1);
        exampleStep1 = (TextView) findViewById(R.id.example1_step1);
        AnswerText1 = (NoImeEditText) findViewById(R.id.answer1);
        AnswerButton1 = (Button) findViewById(R.id.AnswerButton);

        exampleStep2 = (TextView) findViewById(R.id.example1_step2);
        exampleStep3 = (TextView) findViewById(R.id.example1_step3);
        AnswerText2 = (NoImeEditText) findViewById(R.id.answer2);
        AnswerButton2 = (Button) findViewById(R.id.AnswerButton2);

        exampleStep4 = (TextView) findViewById(R.id.example1_step4);
        exampleStep5 = (TextView) findViewById(R.id.example1_step5);
        AnswerText3 = (NoImeEditText) findViewById(R.id.answer3);
        AnswerButton3 = (Button) findViewById(R.id.AnswerButton3);
        continueLessonButton = (Button) findViewById(R.id.continueLessonButton);

        Keyboard mKeyboard= new Keyboard(getApplicationContext(), R.xml.numbers_keyboard);

        // Lookup the KeyboardView
        mKeyboardView= (KeyboardView)findViewById(R.id.keyboardview);
        // Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);
        // Do not show the preview balloons
        mKeyboardView.setPreviewEnabled(false);

        mKeyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                //Here check the primaryCode to see which key is pressed
                //based on the android:codes property
                EditText target = AnswerText1;
                if (AnswerText2.hasFocus()) target = AnswerText2;
                if (AnswerText3.hasFocus()) target = AnswerText3;

                if (primaryCode >= 0 && primaryCode <= 9) {
                    if (target.getText().toString().length() < MAX_NUM_DIGITS)
                        target.setText(target.getText().toString() + primaryCode + "");
                } else if (primaryCode == -1) {
                    if (target.getText().toString().length() > 0) {
                        String old_string = target.getText().toString();
                        int string_length = old_string.length();

                        String new_string = old_string.substring(0, string_length - 1);

                        target.setText(new_string);
                    }
                }
            }

            @Override
            public void onPress(int arg0) {
            }

            @Override
            public void onRelease(int primaryCode) {
            }

            @Override
            public void onText(CharSequence text) {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeRight() {
            }

            @Override
            public void swipeUp() {
            }
        });

        //set elements to be invisible before lesson starts
        content.setVisibility(View.INVISIBLE);
        exampleProblem.setVisibility(View.INVISIBLE);
        exampleStep1.setVisibility(View.INVISIBLE);
        AnswerText1.setVisibility(View.INVISIBLE);
        AnswerButton1.setVisibility(View.INVISIBLE);
        exampleStep2.setVisibility(View.INVISIBLE);
        exampleStep3.setVisibility(View.INVISIBLE);
        AnswerText2.setVisibility(View.INVISIBLE);
        AnswerButton2.setVisibility(View.INVISIBLE);
        exampleStep4.setVisibility(View.INVISIBLE);
        exampleStep5.setVisibility(View.INVISIBLE);
        AnswerText3.setVisibility(View.INVISIBLE);
        AnswerButton3.setVisibility(View.INVISIBLE);
        mKeyboardView.setVisibility(View.INVISIBLE);
        continueLessonButton.setVisibility(View.INVISIBLE);
        beginButton.setVisibility(View.INVISIBLE);



        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }

        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("LESSON-START;-1;-1;" + START_MSG + ";" + NOTHING_STRING);
        }
    }

    // Button handlers =============================================================================

    public void exampleSubmit(View view) {
        int correctAnswer = 0;
        String msgType = "";
        String robotSpeechToSend = "";
        String enteredStr1 = "";
        if (view == AnswerButton1) {
            enteredStr1 = AnswerText1.getText().toString();
            if (!enteredStr1.equals("")) { //only process if something is in the answertext field
                int entered1 = Integer.parseInt(enteredStr1);
                if (lessonView == LessonView.MULTPLICATIONVIEW) {
                    correctAnswer = EXAMPLE_ANSWER1;
                    msgType = "LESSON-PART2";
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = MULT_STEP2_CORRECT_MSG;
                    } else {
                        robotSpeechToSend = MULT_STEP2_INCORRECT_MSG;
                    }

                } else if (lessonView == LessonView.PARENTHESESVIEW) {
                    correctAnswer = EXAMPLE_ANSWER3;
                    msgType = "LESSON-PART7"; //check that this is the right number
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = PAREN_STEP2_CORRECT_MSG;
                    } else {
                        robotSpeechToSend = PAREN_STEP2_INCORRECT_MSG;
                    }
                }
            }
        }
        else if (view == AnswerButton2) {
            enteredStr1 = AnswerText2.getText().toString();
            if (!enteredStr1.equals("")) { //only process if something is in the answertext field
                int entered1 = Integer.parseInt(enteredStr1);
                if (lessonView == LessonView.MULTPLICATIONVIEW) {
                    correctAnswer = EXAMPLE_ANSWER2;
                    msgType = "LESSON-PART4";
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = MULT_STEP4_CORRECT_MSG;
                    } else {
                        robotSpeechToSend = MULT_STEP4_INCORRECT_MSG;
                    }

                } else if (lessonView == LessonView.PARENTHESESVIEW) {
                    correctAnswer = EXAMPLE_ANSWER4;
                    msgType = "LESSON-PART9"; //check that this is the right number
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = PAREN_STEP4_CORRECT_MSG;
                    } else {
                        robotSpeechToSend = PAREN_STEP4_INCORRECT_MSG;
                    }
                }
            }
        }
        else if (view == AnswerButton3) { //AnswerButton3
            enteredStr1 = AnswerText3.getText().toString();
            if (!enteredStr1.equals("")) { //only process if something is in the answertext field
                int entered1 = Integer.parseInt(enteredStr1);
                correctAnswer = EXAMPLE_ANSWER5;
                msgType = "LESSON-END";
                if (entered1 == correctAnswer){
                    robotSpeechToSend = PAREN_STEP6_CORRECT_MSG;
                } else {
                    robotSpeechToSend = PAREN_STEP6_INCORRECT_MSG;
                }
            }
        }

        if (!enteredStr1.equals("")) {
            //depending on which button was pressed and where in the lesson we are, send msg to robot
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage(msgType + ";-1;-1;" + robotSpeechToSend + ";" + enteredStr1);
            }
        }
    }


    public void nextLesson(View view) {
        lessonView = LessonView.PARENTHESESVIEW;

        //make almost everything invisible again on lesson screen and mention that now we will learn something else
        content.setVisibility(View.INVISIBLE);
        exampleProblem.setVisibility(View.INVISIBLE);
        exampleStep1.setVisibility(View.INVISIBLE);
        exampleStep2.setVisibility(View.INVISIBLE);
        exampleStep3.setVisibility(View.INVISIBLE);
        AnswerText1.setVisibility(View.INVISIBLE);
        AnswerButton1.setVisibility(View.INVISIBLE);
        AnswerText2.setVisibility(View.INVISIBLE);
        AnswerButton2.setVisibility(View.INVISIBLE);
        mKeyboardView.setVisibility(View.INVISIBLE);
        continueLessonButton.setVisibility(View.INVISIBLE);

        String msgType = "LESSON-PART5";
        String robotSpeechToSend = PAREN_INTRO_MSG;
        //send message
        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage(msgType + ";-1;-1;" + robotSpeechToSend + ";" + NOTHING_STRING);
        }
    }

    public void beginButtonPressed(View view) {
        finish();
    }

    // Incoming message handler ====================================================================

    public void messageReceived(String msg) {
        System.out.println("[ LessonActivity ] Received the following message: " + msg);
        if (msg.equals("LESSON-START")) {
            //enableButtons();
            //content.setText(CLICK_BEGIN_BUTTON_TEXT);
            content.setVisibility(View.VISIBLE);
            exampleProblem.setVisibility(View.VISIBLE);
            //send message to get nao speech and then move to next part of lesson
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage("LESSON-PART1;-1;-1;" + MULT_STEP1_MSG + ";" + NOTHING_STRING);
            }
        }
        else if (msg.equals("LESSON-PART1")) {
            exampleStep1.setVisibility(View.VISIBLE);
            AnswerText1.setVisibility(View.VISIBLE);
            AnswerText1.setEnabled(true);
            AnswerButton1.setVisibility(View.VISIBLE);
            AnswerButton1.setEnabled(true);
            mKeyboardView.setVisibility(View.VISIBLE);
            mKeyboardView.setEnabled(true);
        }
        else if (msg.equals("LESSON-PART2")){
            AnswerText1.setText(""+EXAMPLE_ANSWER1);
            AnswerText1.setEnabled(false);
            AnswerButton1.setEnabled(false);
            exampleStep2.setVisibility(View.VISIBLE);
            //send message to get nao speech and then move to next part of lesson
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage("LESSON-PART3;-1;-1;" + MULT_STEP3_MSG + ";" + NOTHING_STRING);
            }
        }
        else if (msg.equals("LESSON-PART3")){
            exampleStep3.setVisibility(View.VISIBLE);
            AnswerText2.setVisibility(View.VISIBLE);
            AnswerText2.setEnabled(true);
            AnswerText2.requestFocus();
            AnswerButton2.setVisibility(View.VISIBLE);
            AnswerButton2.setEnabled(true);
        }
        else if (msg.equals("LESSON-PART4")){
            AnswerText2.setText(""+EXAMPLE_ANSWER2);
            AnswerText2.setEnabled(false);
            AnswerButton2.setEnabled(false);
            continueLessonButton.setVisibility(View.VISIBLE);
            continueLessonButton.setEnabled(true);

        }
        else if (msg.equals("LESSON-PART5")){
            content.setText(PAREN_CONTENT);
            content.setVisibility(View.VISIBLE);
            exampleProblem.setText(PAREN_EXAMPLE_PROBLEM);
            exampleProblem.setVisibility(View.VISIBLE);
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage("LESSON-PART6;-1;-1;" + PAREN_STEP1_MSG + ";" + NOTHING_STRING);
            }
        }
        else if (msg.equals("LESSON-PART6")){
            exampleStep1.setText(PAREN_STEP1_TEXT);
            exampleStep1.setVisibility(View.VISIBLE);
            AnswerText1.setVisibility(View.VISIBLE);
            AnswerText1.setEnabled(true);
            AnswerText1.setText("");
            AnswerText1.requestFocus();
            AnswerButton1.setVisibility(View.VISIBLE);
            AnswerButton1.setEnabled(true);
            mKeyboardView.setVisibility(View.VISIBLE);
            mKeyboardView.setEnabled(true);
        }
        else if (msg.equals("LESSON-PART7")){
            AnswerText1.setText(""+EXAMPLE_ANSWER3);
            AnswerText1.setEnabled(false);
            AnswerButton1.setEnabled(false);
            exampleStep2.setText(PAREN_STEP2_TEXT);
            exampleStep2.setVisibility(View.VISIBLE);
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage("LESSON-PART8;-1;-1;" + PAREN_STEP3_MSG + ";" + NOTHING_STRING);
            }

        }
        else if (msg.equals("LESSON-PART8")){
            exampleStep3.setText(PAREN_STEP3_TEXT);
            exampleStep3.setVisibility(View.VISIBLE);
            AnswerText2.setVisibility(View.VISIBLE);
            AnswerText2.setEnabled(true);
            AnswerText2.setText("");
            AnswerText2.requestFocus();
            AnswerButton2.setVisibility(View.VISIBLE);
            AnswerButton2.setEnabled(true);
        }
        else if (msg.equals("LESSON-PART9")){
            AnswerText2.setText(""+EXAMPLE_ANSWER4);
            AnswerText2.setEnabled(false);
            AnswerButton2.setEnabled(false);
            exampleStep4.setVisibility(View.VISIBLE);
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage("LESSON-PART10;-1;-1;" + PAREN_STEP5_MSG + ";" + NOTHING_STRING);
            }
        }
        else if (msg.equals("LESSON-PART10")){
            exampleStep5.setVisibility(View.VISIBLE);
            AnswerText3.setVisibility(View.VISIBLE);
            AnswerText3.setEnabled(true);
            AnswerText3.requestFocus();
            AnswerButton3.setVisibility(View.VISIBLE);
            AnswerButton3.setEnabled(true);
        }
        else if (msg.equals("LESSON-END")) {
            AnswerText3.setText(""+EXAMPLE_ANSWER5);
            AnswerText3.setEnabled(false);
            AnswerButton3.setEnabled(false);
            beginButton.setVisibility(View.VISIBLE);
            enableButtons();
        }
    }

    // Button disabling and enabling methods =======================================================

    public void disableButtons() {
        AnswerButton1.setEnabled(false);
        AnswerButton2.setEnabled(false);
        AnswerButton3.setEnabled(false);
        continueLessonButton.setEnabled(false);
        beginButton.setEnabled(false);
    }

    public void enableButtons() {
        beginButton.setEnabled(true);
    }
}
