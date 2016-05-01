package tutoringproject.breaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by arsalan on 4/14/16.
 */
public class StretchBreakActivity extends Activity implements TCPClientOwner {
    private Button returnButton;
    private TextView instructions;

    public String START_MSG =
        "Let's take a break and stretch! Go ahead and stand up. Follow my lead.";

    public String CLICK_RETURN_BUTTON_TEXT =
        "Click the button below to return to the tutoring session.";

    // Constructor =================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretchbreak);

        returnButton = (Button)   findViewById(R.id.returnButton);
        instructions = (TextView) findViewById(R.id.instructions);

        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }
        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("STRETCHBREAK-START;-1;-1;" + START_MSG);
        }
    }

    // Button handlers =============================================================================

    public void returnButtonPressed(View view) {
        finish();
    }

    // Incoming message handler ====================================================================

    public void messageReceived(String msg) {
        System.out.println("[ StretchBreakActivity ] Received the following message: " + msg);
        if (msg.equals("STRETCHBREAK-DONE")) {
            enableButtons();
            instructions.setText(CLICK_RETURN_BUTTON_TEXT);
        }
    }

    // Button disabling and enabling methods =======================================================

    public void disableButtons() {
        returnButton.setEnabled(false);
    }

    public void enableButtons() {
        returnButton.setEnabled(true);
    }
}
