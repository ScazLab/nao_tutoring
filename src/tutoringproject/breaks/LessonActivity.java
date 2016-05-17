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
 */
public class LessonActivity extends Activity implements TCPClientOwner {
    // XML element variables
    private Button beginButton;
    private TextView content;
    private TextView exampleProblem;
    private TextView exampleStep1;
    private TextView exampleStep2;
    private TextView exampleStep3;
    private NoImeEditText AnswerText1;
    private NoImeEditText AnswerText2;
    private Button AnswerButton1;
    private Button AnswerButton2;
    private KeyboardView mKeyboardView;

    private enum LessonView {
        MULTPLICATIONVIEW, PARENTHESESVIEW
    }
    private LessonView lessonView = LessonView.MULTPLICATIONVIEW;

    public final int MAX_NUM_DIGITS = 5;

    // Public variables ============================================================================

    // Speech strings
    //TODO: fix START_MSG string to be correct intro for lesson activity
    public String START_MSG =
        "Before we start answering questions, let's review how order of operations works.";

    public String MULT_STEP1_MSG =
        "Let me show you what I mean! Let's look at the problem three plus six times two minus five";

    public String MULT_STEP2_CORRECT_MSG =
            "";
    public String MULT_STEP2_INCORRECT_MSG =
            "";
    public String MULT_STEP3_MSG =
            "";
    // Tablet text strings
    public String CLICK_BEGIN_BUTTON_TEXT =
        "Click the button below to begin the tutoring session.";

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
        mKeyboardView.setVisibility(View.INVISIBLE);
        beginButton.setVisibility(View.INVISIBLE);


        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }

        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("LESSON-START;-1;-1;" + START_MSG);
        }
    }

    // Button handlers =============================================================================

    public void exampleSubmit(View view) {
        int correctAnswer = 0;
        String msgType = "";
        String robotSpeechToSend = "";
        String enteredStr1;
        if (view == AnswerButton1){
            enteredStr1 = AnswerText1.getText().toString();
            if (!enteredStr1.equals("")) { //only process if something is in the answertext field
                int entered1 = Integer.parseInt(enteredStr1);
                if (lessonView == LessonView.MULTPLICATIONVIEW) {
                    correctAnswer = 12;
                    msgType = "LESSON-PART2";
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = MULT_STEP2_CORRECT_MSG;
                    } else {
                        robotSpeechToSend = MULT_STEP2_INCORRECT_MSG;
                    }

                } else if (lessonView == LessonView.PARENTHESESVIEW) {
                    correctAnswer = 9;
                    msgType = "LESSON-PART6"; //check that this is the right number
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = "";
                    } else {
                        robotSpeechToSend = "";
                    }
                }
            }
        }
        else if (view == AnswerButton2){
            enteredStr1 = AnswerText2.getText().toString();
            if (!enteredStr1.equals("")) { //only process if something is in the answertext field
                int entered1 = Integer.parseInt(enteredStr1);
                if (lessonView == LessonView.MULTPLICATIONVIEW) {
                    correctAnswer = 10;
                    msgType = "";
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = "";
                    } else {
                        robotSpeechToSend = "";
                    }

                } else if (lessonView == LessonView.PARENTHESESVIEW) {
                    correctAnswer = 45;
                    msgType = ""; //check that this is the right number
                    if (entered1 == correctAnswer){
                        robotSpeechToSend = "";
                    } else {
                        robotSpeechToSend = "";
                    }
                }
            }
        }
        else { //AnswerButton3

        }

        //depending on which button was pressed and where in the lesson we are, send msg to robot
        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage(msgType +";-1;-1;" + robotSpeechToSend);
        }
    }


    public void nextLesson(View view) {
        lessonView = LessonView.PARENTHESESVIEW;

        //setText appropriately for the second lesson - parentheses

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
                TCPClient.singleton.sendMessage("LESSON-PART1;-1;-1;" + MULT_STEP1_MSG);
            }
        }
        else if(msg.equals("LESSON-PART1")) {
            exampleStep1.setVisibility(View.VISIBLE);
            AnswerText1.setVisibility(View.VISIBLE);
            AnswerText1.setEnabled(true);
            AnswerButton1.setVisibility(View.VISIBLE);
            AnswerButton1.setEnabled(true);
        }
        else if(msg.equals("LESSON-PART2")){
            AnswerText1.setEnabled(false);
            AnswerButton1.setEnabled(false);
            exampleStep2.setVisibility(View.VISIBLE);
            //send message to get nao speech and then move to next part of lesson
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage("LESSON-PART3;-1;-1;" + MULT_STEP3_MSG);
            }
        }
        else if(msg.equals("LESSON-PART3")){
            exampleStep3.setVisibility(View.VISIBLE);
            AnswerText2.setVisibility(View.VISIBLE);
            AnswerText2.setEnabled(true);
            AnswerButton2.setVisibility(View.VISIBLE);
            AnswerButton2.setEnabled(true);
        }
        else if(msg.equals("LESSON-PART4")){

        }
        else if (msg.equals("LESSON-END")) {
            enableButtons();
        }
    }

    // Button disabling and enabling methods =======================================================

    public void disableButtons() {
        beginButton.setEnabled(false);
    }

    public void enableButtons() {
        beginButton.setEnabled(true);
    }
}
