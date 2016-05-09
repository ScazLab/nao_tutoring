package tutoringproject.breaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by arsalan on 5/9/16.
 */
public class LessonActivity extends Activity implements TCPClientOwner {
    // XML element variables
    private Button beginButton;
    private TextView content;

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
        content     = (TextView) findViewById(R.id.content);

        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }
        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("LESSON-START;-1;-1;" + START_MSG);
        }
    }

    // Button handlers =============================================================================

    public void beginButtonPressed(View view) {
        finish();
    }

    // Incoming message handler ====================================================================

    public void messageReceived(String msg) {
        System.out.println("[ LessonActivity ] Received the following message: " + msg);
        if (msg.equals("LESSON-START")) {
            enableButtons();
            content.setText(CLICK_BEGIN_BUTTON_TEXT);
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
