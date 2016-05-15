package tutoringproject.breaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by aditi on 5/14/16.
 */
public class MindfulnessBreakActivity extends Activity implements TCPClientOwner {

    // XML element variables
    private Button returnButton;
    private TextView instructions;

    // Tablet text strings
    public String CLICK_RETURN_BUTTON_TEXT =
            "Click the button below to return to the tutoring session.";


    // Constructor =================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulnessbreak);

        // <expGroup> is no longer used in this class. But I'm going to leave it here just in case
        // we find a use for it in the future.
        Bundle extras = getIntent().getExtras();
        //int expGroupIndex = Integer.parseInt(extras.getString("expGroup"));

        returnButton = (Button)   findViewById(R.id.returnButton);
        instructions = (TextView) findViewById(R.id.instructions);

        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }

        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("MINDFULNESSBREAK-START;-1;-1;-1");
        }
    }

    // Button handlers =============================================================================

    public void returnButtonPressed(View view) {
        finish();
    }

    // Incoming message handler ====================================================================

    public void messageReceived(String msg) {
        System.out.println("[ MindfulnessBreakActivity ] Received the following message: " + msg);
        if (msg.equals("MINDFULNESSBREAK-DONE")) {
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
