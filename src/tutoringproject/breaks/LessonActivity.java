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
    private NoImeEditText AnswerText1;
    private NoImeEditText AnswerText2;
    private Button AnswerButton1;
    private Button AnswerButton2;
    private KeyboardView mKeyboardView;

    public final int MAX_NUM_DIGITS = 6;

    // Public variables ============================================================================

    // Speech strings
    public String START_MSG =
        "Before we start answering questions, let's review how order of operations works.";

    // Tablet text strings
    public String CLICK_BEGIN_BUTTON_TEXT =
        "Click the button below to begin the tutoring session.";

    // Constructor =================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        beginButton = (Button)   findViewById(R.id.beginButton);
        content     = (TextView) findViewById(R.id.content2);
        AnswerText1 = (NoImeEditText) findViewById(R.id.answer1);
        AnswerText2 = (NoImeEditText) findViewById(R.id.answer2);
        AnswerButton1 = (Button) findViewById(R.id.AnswerButton);
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

        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }

        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("LESSON-START;-1;-1;" + START_MSG);
        }
    }

    // Button handlers =============================================================================

    public void exampleSubmit1(View view) {
        
    }

    public void exampleSubmit2(View view) {

    }

    public void beginButtonPressed(View view) {
        finish();
    }

    // Incoming message handler ====================================================================

    public void messageReceived(String msg) {
        System.out.println("[ LessonActivity ] Received the following message: " + msg);
        if (msg.equals("LESSON-START")) {
            enableButtons();
            //content.setText(CLICK_BEGIN_BUTTON_TEXT);
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
