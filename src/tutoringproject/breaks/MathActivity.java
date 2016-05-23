package tutoringproject.breaks;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;



public class MathActivity extends Activity implements TCPClientOwner {

    private final String SUBMIT_STRING = "Submit";
    private final String CORRECT_STRING = "Correct!";
    private final String TOO_MANY_INCORRECT_PREFIX = "The correct answer is";
    private final String TOO_MANY_INCORRECT_POSTFIX = "Let's try the next problem!";
    private final String NEXT_QUESTION_STRING = "Next Question";
    private final String REQUEST_HINT_STRING1 = "Request Hint 1";
    private final String REQUEST_HINT_STRING2 = "Request Hint 2";
    private final String REQUEST_HINT_STRING3 = "Request Hint 3";
    private final String REPEAT_HINT_STRING1 = "Repeat Hint 1";
    private final String REPEAT_HINT_STRING2 = "Repeat Hint 2";
    private final String REPEAT_HINT_STRING3 = "Repeat Hint 3";
    private final String INCORRECT_POSTFIX = /* Answer */ " is incorrect!";
    private final String REMAINING_POSTFIX = " attempts remaining.";
    private final String REMAINING_POSTFIX_ONE = " attempt remaining.";
    private final String TITLE_PREFIX = "Question " /* number */;
    private final String INVALID_STRING_FRACTION = "Type in an answer into both boxes before submitting!";
    private final String INVALID_STRING_VALUE = "Type in an answer before submitting!";
    private final String AUTO_HINT_VALUE = "Let me give you a hint!";
    private final String DENIED_HINT_VALUE = "Try making an attempt before requesting more help!";
    private final String QUESTION_INTRO_PREFIX = "This is a question for you about ";
    private final String QUESTION_INTRO_POSTFIX = ". Here it is!";


    private TextView fractionLine;
    private NoImeEditText AnswerText1;
    private NoImeEditText AnswerText2;
    private TextView RightWrongLabel;
    private AutoResizeTextView CurrentQuestion;
    private Button HintButton1;
    private Button HintButton2;
    private Button HintButton3;
    private Button SubmitButton;
    private TextView TitleLabel;
    private TextView AskRobotLabel;
    private KeyboardView mKeyboardView;
    private int sessionNum = -1;
    private int expGroup = 0;  //1: fixed, 2: reward, 3: frustration
    private int startQuestionNum = 1;

    private Questions questions;
    private Questions level1_questions;
    private Questions level2_questions;
    private Questions level3_questions;

    public final int MAX_NUM_DIGITS = 6;

    public final int MAX_HINTS = 3;
    public int hintsRemaining = MAX_HINTS;

    public final int MAX_ATTEMPTS = 1;
    public int attemptsRemaining = MAX_ATTEMPTS;

    public final int NUM_HINTS_TO_LIMIT = 3;
    public int numConsecHintsRequested = 0;
    public final int NUM_INCORRECT_TO_AUTO_HINT = 2;
    public int numIncorrectWithoutHint = 0;

    //States
    private enum QState {
        INIT, INVALID, DISPLAYCORRECT, DISPLAYINCORRECT
    }
    private QState questionState = QState.INIT;

    private enum HState {
        QUESTIONVIEW, HINTVIEW
    }
    private HState hintState = HState.QUESTIONVIEW;
    private int MAXHINTS = 3;

    private int currentQuestionIndex = -1;
    private int numberCorrect = 0;
    private int numberWrong = 0;
    private int numberHints = 0;
    private boolean autoHint = false;

    // New attributes for break study. Setting <takeBreak> to true will cause a break to be
    // triggered the next time NextQuestion() is called.
    private boolean takeBreak = false;
    private int numberBreaksGiven = 0;
    private boolean firstTimeCallingOnResume = true;

    //temporary variable (should be read in through question format)
    private int max_time_per_question = 10000;  // This initial value doesn't matter. A new value is loaded in for every question.
    private Timer timer;
    private TimerTask timerTask;
    private TimeWatch timeWatch;
    private final Handler handler = new Handler();

    // ends session at max_session_time seconds
    private int max_session_time = 1800;
    private TimeWatch total_elapsed_timewatch;

    //break variables
    private int fixedBreakInterval = 3;
    private int num_consec_questions = 0;  //number of consecutive questions without break given
    private TimeWatch fixed_break_timewatch;
    private int fixed_break_time = 120; //break every x minutes, 2 for testing
    private boolean first_problem = true;

    //choosing question variables
    public int BASE_NUM_QS_PER_LEVEL = 3;
    public int NUM_DIFFICULTY_LEVELS = 3;
    public double MASTERY_LEVEL = 0.8;
    private int num_consec_difficulty = 0;
    private int current_difficulty_level = 1;
    private double percent_difficulty_correct = 0.0;
    private int num_correct_difficulty = 0;
    private int save_index = 0;


    private void startTimer(long delay, long period) {
        //set new Timer
        timer = new Timer();

        //init TimerTasks's job
        initializeTimerTask();

        //schedule the timer, after 5000ms, runs every max_timer_per_question
        timer.schedule(timerTask, delay, period);

        //also start new timeWatch from time 0
        timeWatch = TimeWatch.start();
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        //show the toast, for testing purposes only
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), strDate, duration);
//                        toast.show();

                        questionTimeout();
                    }
                });

            }
        };
    }

    private void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    public void questionTimeout() {
        Question question = questions.get(currentQuestionIndex);
        String questionType = question.type;
        String format = question.format;

        if (format.equals(Questions.FORMAT_FRACTION)) {
            String correct_numerator_string = Integer.toString(question.numerator);
            String correct_denominator_string = Integer.toString(question.denominator);

            //added correct answers into AnswerText1 and AnswerText2
            AnswerText1.setText(correct_numerator_string);
            AnswerText2.setText(correct_denominator_string);

        } else if (format.equals(Questions.FORMAT_TEXT)) {
            String correct_value_string = Integer.toString(question.value);
            AnswerText1.setText(correct_value_string);
        }

        String attempt = "-1";
        long questionTime = timeWatch.time(TimeUnit.MILLISECONDS);  //stores elapsed time in milliseconds
        Boolean correct;

        String timeout_string = " " + TOO_MANY_INCORRECT_POSTFIX;
        String timeout_message = "Let's try the next problem.";

        //Send message
        if (TCPClient.singleton != null)
            TCPClient.singleton.sendMessage("TIMEOUT;" + currentQuestionIndex + ";" + questionType + ";" + timeout_message + ";" + attempt + ";" + questionTime);


        RightWrongLabel.setText(timeout_string);
        SubmitButton.setText(NEXT_QUESTION_STRING);
        questionState = QState.DISPLAYCORRECT;
        AnswerText1.setEnabled(false);
        AnswerText2.setEnabled(false);
        mKeyboardView.setVisibility(View.INVISIBLE);
        mKeyboardView.setEnabled(false);
        //HintButton1.setVisibility(View.INVISIBLE);
        //HintButton2.setVisibility(View.INVISIBLE);
        //HintButton3.setVisibility(View.INVISIBLE);
        //AskRobotLabel.setVisibility(View.INVISIBLE);
        numberCorrect++;  //DANGER: is this a bug?
        stopTimerTask();
    }


    public String AssetJSONFile (String filename) throws IOException {
        AssetManager manager = this.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Load JSON file
        String json_file1 = "level1.json";
        String json_file2 = "level2.json";
        String json_file3 = "level3.json";

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            sessionNum = Integer.parseInt(extras.getString("sessionNum"));
            //json_file = "level1.json";//"Session"+sessionNum+".json";
            //if (sessionNum == 2) json_file = "level2.json";
            expGroup = Integer.parseInt(extras.getString("expGroup"));
            System.out.println("expGroup is: " + expGroup);
            startQuestionNum = Integer.parseInt(extras.getString("startQuestionNum"));
            System.out.println("startQuestionNum is: " + startQuestionNum);
            currentQuestionIndex = startQuestionNum - 2;
            fixedBreakInterval = Integer.parseInt(extras.getString("fixedBreakInterval"));
            fixed_break_time = fixedBreakInterval * 60; //start_screen provides this number in minutes

            int max_time = Integer.parseInt(extras.getString("maxTime"));
            if (max_time != -1) {  //means different max_time specified
                max_session_time = max_time;
            }
            numberBreaksGiven = Integer.parseInt(extras.getString("breaksGiven"));
        }
        //set this MathActivity as the sessionOwner for the tcpClient
        if (TCPClient.singleton != null)
            TCPClient.singleton.setSessionOwner(this);

        String json1 = "";
        String json2 = "";
        String json3 = "";
        try {
            json1 = AssetJSONFile(json_file1);
            json2 = AssetJSONFile(json_file2);
            json3 = AssetJSONFile(json_file3);
        } catch (IOException e){
            e.printStackTrace();
        }
        level1_questions = new Questions(json1);
        level2_questions = new Questions(json2);
        level3_questions = new Questions(json3);
        questions = level1_questions;

        fractionLine = (TextView) findViewById(R.id.fractionLine);
        AnswerText1 = (NoImeEditText) findViewById(R.id.editText1);
        AnswerText2 = (NoImeEditText) findViewById(R.id.editText2);
        RightWrongLabel = (TextView) findViewById(R.id.RightWrongLabel);
        CurrentQuestion = (AutoResizeTextView) findViewById(R.id.QuestionLabel);
        //HintButton1 = (Button) findViewById(R.id.hint1);
        //HintButton2 = (Button) findViewById(R.id.hint2);
        //HintButton3 = (Button) findViewById(R.id.hint3);
        //AskRobotLabel = (TextView) findViewById(R.id.textView3);
        SubmitButton = (Button) findViewById(R.id.AnswerButton);
        TitleLabel = (TextView) findViewById(R.id.TitleLabel);

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
        total_elapsed_timewatch = TimeWatch.start();
        fixed_break_timewatch = TimeWatch.start(); //initialize here but reset when we get to question 1

        if (extras.getString("startOrLoad").equals("start")) {
            Intent intent = new Intent(this, LessonActivity.class);
            NextQuestion();//startActivity(intent); //use NextQuestion() here to skip lesson just for testing breaks!
        } else {
            NextQuestion();
        }
    }

    public void disableButtons() {
        //System.out.println("MATHACTIVITY: IN disableButtons method!");
        //HintButton1.setEnabled(false);
        //HintButton2.setEnabled(false);
        //HintButton3.setEnabled(false);
        SubmitButton.setEnabled(false);
        mKeyboardView.setEnabled(false);
        //mKeyboardView.getKeyboard().getKeys().get(0).
    }

    public void enableButtons() {
        //System.out.println("MATHACTIVITY: IN enableButtons method!");
        //HintButton1.setEnabled(true);
        //HintButton2.setEnabled(true);
        //HintButton3.setEnabled(true);
        SubmitButton.setEnabled(true);
        mKeyboardView.setEnabled(true);
    }

    public void disableQuestion() {
        CurrentQuestion.setVisibility(View.INVISIBLE);
        AnswerText1.setEnabled(false);
        AnswerText2.setEnabled(false);
        mKeyboardView.setVisibility(View.INVISIBLE);
        mKeyboardView.setEnabled(false);
        SubmitButton.setVisibility(View.INVISIBLE);
        //AskRobotLabel.setVisibility(View.INVISIBLE);
        //HintButton1.setVisibility(View.INVISIBLE);
        AnswerText1.setVisibility(View.INVISIBLE);
        AnswerText2.setVisibility(View.INVISIBLE);
        fractionLine.setVisibility(View.INVISIBLE);
    }

    public void enableQuestion(String answerType){
        CurrentQuestion.setVisibility(View.VISIBLE);
        AnswerText1.setEnabled(true);
        AnswerText2.setEnabled(true);
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        SubmitButton.setVisibility(View.VISIBLE);
        //AskRobotLabel.setVisibility(View.VISIBLE);
        //HintButton1.setVisibility(View.VISIBLE);
        AnswerText1.setVisibility(View.VISIBLE);
        if (answerType.equals(Questions.FORMAT_FRACTION)){
            fractionLine.setVisibility(View.VISIBLE);
            AnswerText2.setVisibility(View.VISIBLE);
        }
        AnswerText1.requestFocus();
    }

    public void messageReceived(String message){
        System.out.println("IN MATHACTIVITY, message received from server is: " + message);
        if (message == null){
            //TODO: figure out if this bug happens and it causes the app to crash
            //possibly catch a null pointer exception?
            System.out.println("null message received from server");
        }
        else {
            if (message.equals("DONE")) {
                enableButtons();
            } else if (message.equals(Questions.FORMAT_FRACTION) || message.equals(Questions.FORMAT_TEXT)) {
                enableQuestion(message);
                enableButtons();
            } else if (expGroup == 2 && message.equals("REWARD_BREAK")) {
                takeBreak = true;
                enableButtons();

            } else if (expGroup == 3 && message.equals("FRUSTRATION_BREAK")) {
                takeBreak = true;
                enableButtons();
            }
        }
    }

    public void AnswerButtonPress(View view) {
        stopTimerTask();  // stop timer task right when answer button is pressed
        
        String format = questions.get(currentQuestionIndex).format;
        String enteredStr1 = AnswerText1.getText().toString();
        String enteredStr2 = AnswerText2.getText().toString();


        if (format.equals(Questions.FORMAT_FRACTION) &&
                (enteredStr1.equals("") || enteredStr2.equals(""))) {
            questionState = QState.INVALID;
            RightWrongLabel.setText(INVALID_STRING_FRACTION);
        } else if (format.equals(Questions.FORMAT_TEXT) && enteredStr1.equals("")){
            questionState = QState.INVALID;
            RightWrongLabel.setText(INVALID_STRING_VALUE);
        } else if (questionState == QState.INIT || questionState == QState.DISPLAYINCORRECT
                || questionState == QState.INVALID) {

            numConsecHintsRequested = 0; //reset since attempt is made here
            Question question = questions.get(currentQuestionIndex);
            String questionType = question.type;

            Boolean correct = false;

            int entered1 = Integer.parseInt(enteredStr1);
            String attempt = "";

            if (question.format.equals(Questions.FORMAT_FRACTION)){
                int entered2 = Integer.parseInt(enteredStr2);
                correct = question.numerator==entered1 && question.denominator==entered2;
                attempt = entered1 + "/" + entered2;
            } else if (question.format.equals(Questions.FORMAT_TEXT)){
                correct = question.value == entered1;
                attempt = ""+entered1;
            }

            //store elapsed questionTime
            long questionTime = timeWatch.time(TimeUnit.MILLISECONDS);  //stores elapsed time in milliseconds
            System.out.println("QuestionTime: " + Long.toString(questionTime) +"");

            //if(AnswerText.Text() == entered) {
            //include TCP server stuff
            if (correct) {
                stopTimerTask();  // immediately kill timer task if correct
                if (TCPClient.singleton != null)
                    TCPClient.singleton.sendMessage("CA;" + currentQuestionIndex + ";" + questionType + ";" + CORRECT_STRING + ";" + attempt  + ";" + questionTime);
                RightWrongLabel.setText(CORRECT_STRING);
                SubmitButton.setText(NEXT_QUESTION_STRING);
                questionState = QState.DISPLAYCORRECT;
                AnswerText1.setEnabled(false);
                AnswerText2.setEnabled(false);
                mKeyboardView.setVisibility(View.INVISIBLE);
                mKeyboardView.setEnabled(false);
                //HintButton1.setVisibility(View.INVISIBLE);
                //HintButton2.setVisibility(View.INVISIBLE);
                //HintButton3.setVisibility(View.INVISIBLE);
                //AskRobotLabel.setVisibility(View.INVISIBLE);
                numberCorrect++;
                num_correct_difficulty++;
            } else {
                attemptsRemaining--;
                numIncorrectWithoutHint++;

                String incorrect_string = "";
                String incorrect_message = "";
                String too_many_incorrect_string = TOO_MANY_INCORRECT_PREFIX + " ";
                String too_many_incorrect_message = question.spokenAnswer + " ";
                if (question.format.equals(Questions.FORMAT_FRACTION)) {
                    int entered2 = Integer.parseInt(enteredStr2);
                    incorrect_string = entered1 + " / " + entered2 + INCORRECT_POSTFIX;
                    incorrect_message = entered1 + "/" + entered2 + INCORRECT_POSTFIX;
                    attempt = entered1 + "/" + entered2;
                    too_many_incorrect_string += question.numerator + " / " + question.denominator;
                } else if (question.format.equals(Questions.FORMAT_TEXT)) {
                    incorrect_string = entered1 + INCORRECT_POSTFIX;
                    incorrect_message = entered1 + INCORRECT_POSTFIX;
                    attempt = ""+entered1;
                    too_many_incorrect_string += ""+question.value;
                }
                too_many_incorrect_string += ".";
                //too_many_incorrect_string += " " + question.explanation;
                //too_many_incorrect_message += question.spokenExplanation;

                if (attemptsRemaining > 0) {
                    if (attemptsRemaining == 1) {
                        incorrect_message += " " + attemptsRemaining + REMAINING_POSTFIX_ONE;
                        incorrect_string += " " + attemptsRemaining + REMAINING_POSTFIX_ONE;
                    }
                    else {
                        incorrect_message += " " + attemptsRemaining + REMAINING_POSTFIX;
                        incorrect_string += " " + attemptsRemaining + REMAINING_POSTFIX;
                    }
                    //Send message
                    if (TCPClient.singleton != null)
                        TCPClient.singleton.sendMessage("IA;" + currentQuestionIndex + ";" + questionType + ";" + incorrect_message + ";" + attempt);
                    RightWrongLabel.setText(incorrect_string);
                    questionState = QState.DISPLAYINCORRECT;
                    AnswerText1.setText("");
                    AnswerText2.setText("");
                    numberWrong++;
                    AnswerText1.requestFocus();

                    //adaptive group: automatically give hint if max num incorrect attempts made w/o requesting help
                    if (expGroup==1 && numIncorrectWithoutHint==NUM_INCORRECT_TO_AUTO_HINT){
                        int relevantHint = MAX_HINTS - hintsRemaining + 1;
                        System.out.println("relevant hint for AUTO hint is: " + relevantHint);
                        Button button = null;
                        if (relevantHint == 1)
                            button = HintButton1;
                        else if (relevantHint == 2)
                            button = HintButton2;
                        else if (relevantHint == 3)
                            button = HintButton3;

                        //send message that hint was automatically initiated, then send appropriate hint
                        //if (com.priyanka.TCPClient.singleton != null)
                        //    com.priyanka.TCPClient.singleton.sendMessage("AH;" + currentQuestionIndex + ";" + AUTO_HINT_VALUE);
                        if (button != null) {
                            autoHint = true;
                            HintPressed((View) button);
                        }
                    }

                } else {

                    too_many_incorrect_string += " " + TOO_MANY_INCORRECT_POSTFIX;

                    String explanation = "";
                    if (current_difficulty_level == 1){
                        if (questionType == "Multiplication") {
                            explanation = "";
                        }
                        else if (questionType == "Parentheses"){
                            explanation = "";
                        }
                    }
                    else if (current_difficulty_level == 2){
                        if (questionType == "Multiplication") {
                            explanation = "";
                        }
                        else if (questionType == "Parentheses"){
                            explanation = "";
                        }
                    }
                    else if (current_difficulty_level == 3){
                        if (questionType == "Multiplication") {
                            explanation = "";
                        }
                        else if (questionType == "Parentheses"){
                            explanation = "";
                        }
                    }
                    too_many_incorrect_message += " " + explanation;
                    too_many_incorrect_message += " " + TOO_MANY_INCORRECT_POSTFIX;

                    too_many_incorrect_string += "\n" + question.feedback.replace(";", "\n");
                    //Send message
                    if (TCPClient.singleton != null)
                        TCPClient.singleton.sendMessage("LIA;" + currentQuestionIndex + ";" + questionType + ";" + too_many_incorrect_message + ";" + attempt + ";" + questionTime);
                    RightWrongLabel.setText(too_many_incorrect_string);
                    SubmitButton.setText(NEXT_QUESTION_STRING);
                    questionState = QState.DISPLAYCORRECT;
                    AnswerText1.setEnabled(false);
                    AnswerText2.setEnabled(false);
                    mKeyboardView.setVisibility(View.INVISIBLE);
                    mKeyboardView.setEnabled(false);
                    //HintButton1.setVisibility(View.INVISIBLE);
                    //HintButton2.setVisibility(View.INVISIBLE);
                    //HintButton3.setVisibility(View.INVISIBLE);
                    //AskRobotLabel.setVisibility(View.INVISIBLE);
                    numberCorrect++;  //DANGER: is this a bug?
                }


            }
        } else if (questionState == QState.DISPLAYCORRECT){
            NextQuestion();
        }

    }

    public void HintPressed(View view){

        Question currentQuestion = questions.get(currentQuestionIndex);
        String questionType = currentQuestion.type;

        int buttonNumber = -1;

        Button button = (Button) view;

        if      (button==HintButton1) buttonNumber = 1;
        else if (button==HintButton2) buttonNumber = 2;
        else if (button==HintButton3) buttonNumber = 3;

        int newHintButtonNumber = MAX_HINTS - hintsRemaining + 1;
        numIncorrectWithoutHint = 0; //reset to 0 since hint is requested
        //if (!autoHint)
        //    numConsecHintsRequested++;
        if (!autoHint && buttonNumber==newHintButtonNumber){
            numConsecHintsRequested++;
        }

        if ((expGroup==1) && (buttonNumber==newHintButtonNumber) && (numConsecHintsRequested >= NUM_HINTS_TO_LIMIT)) {
            System.out.println("numConsecHintsRequested is: " + numConsecHintsRequested);
            System.out.println("newHintButtonNumber is: " + newHintButtonNumber);
            //send message indicating that a hint request was denied
            if (TCPClient.singleton != null)
                TCPClient.singleton.sendMessage("DH;" + currentQuestionIndex + ";" + questionType + ";" + DENIED_HINT_VALUE + ";" + buttonNumber);

        }

        else { //control group or no need to limit hints in this case
            if (buttonNumber <= newHintButtonNumber) {
                String hintMessage = "";
                if (buttonNumber == 1) {
                    hintMessage = currentQuestion.spokenHint1;
                    HintButton1.setText(REPEAT_HINT_STRING1);
                    HintButton1.setBackground(getResources().getDrawable(R.drawable.repeat_drawable));
                    HintButton2.setVisibility(View.VISIBLE);
                } else if (buttonNumber == 2) {
                    hintMessage = currentQuestion.spokenHint2;
                    HintButton2.setText(REPEAT_HINT_STRING2);
                    HintButton2.setBackground(getResources().getDrawable(R.drawable.repeat_drawable));
                    HintButton3.setVisibility(View.VISIBLE);
                } else if (buttonNumber == 3) {
                    hintMessage = currentQuestion.spokenHint3;
                    HintButton3.setText(REPEAT_HINT_STRING3);
                    HintButton3.setBackground(getResources().getDrawable(R.drawable.repeat_drawable));
                }

                if (TCPClient.singleton != null)
                    TCPClient.singleton.sendMessage("H" + buttonNumber + ";" + currentQuestionIndex + ";" + questionType + ";" + hintMessage + ";" + autoHint);
            }

            if (buttonNumber==newHintButtonNumber)
                hintsRemaining--;
            System.out.println("hintsRemaining: " + hintsRemaining);
        }
        autoHint = false;
    }

    public void assessDifficultyLevel() {
        percent_difficulty_correct = (double)num_correct_difficulty / ((double)num_consec_difficulty - 1);
        System.out.println("In assessDifficultyLevel, num_correct_difficulty is: " + num_correct_difficulty);
        System.out.println("In assessDifficultyLevel, num_consec_difficulty is: " + num_consec_difficulty);
        System.out.println("In assessDifficultyLevel, percent_difficulty_correct is: " + percent_difficulty_correct);
        if (percent_difficulty_correct >= MASTERY_LEVEL) {
            if (current_difficulty_level < NUM_DIFFICULTY_LEVELS){
                if (current_difficulty_level == 1) {
                    questions = level2_questions;
                }
                else if (current_difficulty_level == 2) {
                    questions = level3_questions;
                }
                current_difficulty_level++;
                save_index += currentQuestionIndex;
                currentQuestionIndex = 0; //reset questionIndex for new set of questions
                percent_difficulty_correct = 0;
                num_consec_difficulty = 1;
                num_correct_difficulty = 0;
            }
        }
    }

    public void NextQuestion() {
        //preliminary fixed break interval calculation
        //if (expGroup == 1 && num_consec_questions >= fixedBreakInterval) {
        //    takeBreak = true;
        //    num_consec_questions = 0;
        //}
        if (expGroup == 1 && !first_problem && fixed_break_timewatch.time(TimeUnit.SECONDS) > fixed_break_time){
            takeBreak = true;
            System.out.println("Taking break in fixed condition, completed: " + num_consec_questions + " questions.");
            num_consec_questions = 0;
        }

        if (takeBreak) {
            stopTimerTask();
            if (numberBreaksGiven % 4 == 0) {
                numberBreaksGiven++;
                startTicTacToe(); //just for testing
            } else if (numberBreaksGiven % 4 == 1) {
                numberBreaksGiven++;
                startStretchBreak();//just for testing
            } else if (numberBreaksGiven % 4 == 2) {
                numberBreaksGiven++;
                System.out.println("Break 3");
                startVisualFocus();//just for testing
            } else if (numberBreaksGiven % 4 == 3) {
                numberBreaksGiven++;
                System.out.println("Break 4");
                startMindfulnessBreak();
            }
            //if (expGroup ==1){ //took break in fixed condition
            //    fixed_break_timewatch.reset();
            //}
            takeBreak = false;
            return;
        }
        //consecutive questions without break counter
        num_consec_questions++;
        num_consec_difficulty++;


        //figure out here where to pull the question from
        currentQuestionIndex++;
        numConsecHintsRequested = 0; //reset at new question
        numIncorrectWithoutHint = 0; //reset at new question

        if (num_consec_difficulty > BASE_NUM_QS_PER_LEVEL){
            System.out.println("ASSESSING_DIFFICULTY_LEVEL");
            assessDifficultyLevel();
        }


        RightWrongLabel.setText("");
        AnswerText1.setText("");
        AnswerText2.setText("");
        //HintButton1.setVisibility(View.VISIBLE);
        //HintButton2.setVisibility(View.INVISIBLE);
        //HintButton3.setVisibility(View.INVISIBLE);
        //HintButton1.setText(REQUEST_HINT_STRING1);
        //HintButton2.setText(REQUEST_HINT_STRING2);
        //HintButton3.setText(REQUEST_HINT_STRING3);
        //AskRobotLabel.setVisibility(View.VISIBLE);
        //HintButton1.setBackground(getResources().getDrawable(R.drawable.hint_drawable));
        //HintButton2.setBackground(getResources().getDrawable(R.drawable.hint_drawable));
        //HintButton3.setBackground(getResources().getDrawable(R.drawable.hint_drawable));

        hintsRemaining = MAX_HINTS;
        attemptsRemaining = MAX_ATTEMPTS;
        String questionType = "none";

        if (currentQuestionIndex >= questions.length() || total_elapsed_timewatch.time(TimeUnit.SECONDS) > max_session_time) {
            Intent intent = new Intent(this, Completed.class);
            //send END message before displaying completed screen
            String goodbyeMessage = "Congratulations! You have completed the session. ";
            if (sessionNum < 4) {
                goodbyeMessage += "See you next time!";
            }
            else {
                goodbyeMessage += "I had a great time doing math with you! Have a great day! Bye!";
            }
            if (TCPClient.singleton != null) {
                TCPClient.singleton.sendMessage("END;" + currentQuestionIndex + ";" + questionType + ";" + goodbyeMessage);
                TCPClient.singleton.stopClient();
            }
            startActivity(intent);
            stopTimerTask(); //kill the last timer
            return;
        }

        if (currentQuestionIndex == 0 && current_difficulty_level == 1 && first_problem){
            System.out.println("starting fixed break time watch when first problem starts");
            fixed_break_timewatch.reset();
            first_problem = false;
        }
        Question question = questions.get(currentQuestionIndex);
        String newQuestion = question.question;
        questionType = question.type;
        max_time_per_question = question.maxTime * 1000;
        //String questionIntro = QUESTION_INTRO_PREFIX + questionType + QUESTION_INTRO_POSTFIX;
        String questionIntro = question.type;//question.spokenType;
        SubmitButton.setText(SUBMIT_STRING);
        CurrentQuestion.setText(newQuestion);
        questionState = QState.INIT;

        if (question.format.equals(Questions.FORMAT_FRACTION)){
            AnswerText1.setEnabled(true);
            fractionLine.setEnabled(true);
            AnswerText2.setEnabled(true);
            AnswerText1.setVisibility(View.VISIBLE);
            fractionLine.setVisibility(View.VISIBLE);
            AnswerText2.setVisibility(View.VISIBLE);
        } else if (question.format.equals(Questions.FORMAT_TEXT)){
            AnswerText1.setEnabled(true);
            fractionLine.setEnabled(false);
            AnswerText2.setEnabled(false);
            AnswerText2.setVisibility(View.VISIBLE);
            fractionLine.setVisibility(View.INVISIBLE);
            AnswerText2.setVisibility(View.INVISIBLE);
        }

        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        TitleLabel.setText(TITLE_PREFIX + " " + (save_index + currentQuestionIndex + 1));

        //Send message
        if (TCPClient.singleton != null) {
            disableQuestion();
            TCPClient.singleton.sendMessage("Q;" + currentQuestionIndex + ";" + questionType + ";" + questionIntro + ";" + question.format);
        }
        AnswerText1.requestFocus();

        // stops old timer and starts new timer task
        stopTimerTask();
        startTimer(max_time_per_question, max_time_per_question * 2);
    }

    public void startTicTacToe() {
        Intent intent = new Intent(this, TicTacToeActivity.class);
        intent.putExtra("expGroup", "" + expGroup);
        startActivity(intent);
    }

    public void startStretchBreak() {
        Intent intent = new Intent(this, StretchBreakActivity.class);
        intent.putExtra("expGroup", "" + expGroup);
        startActivity(intent);
    }

    public void startVisualFocus() {
        Intent intent = new Intent(this, VisualFocusActivity.class);
        intent.putExtra("expGroup", "" + expGroup);
        startActivity(intent);
    }

    public void startMindfulnessBreak() {
        Intent intent = new Intent(this, MindfulnessBreakActivity.class);
        intent.putExtra("expGroup", "" + expGroup);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        if (!firstTimeCallingOnResume) {
            if (TCPClient.singleton != null) {
                TCPClient.singleton.setSessionOwner(this);
            }
            fixed_break_timewatch.reset(); //reset when questions resume
            NextQuestion();
        } else {
            firstTimeCallingOnResume = false;
            //fixed_break_timewatch = TimeWatch.start(); //starts the first time MathActivity is run?
        }
    }
}
