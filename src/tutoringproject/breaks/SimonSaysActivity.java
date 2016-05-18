package tutoringproject.breaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by aditi on 5/14/16.
 */
public class SimonSaysActivity extends Activity implements TCPClientOwner {

    // XML element variables
    private TextView instructions;
    private Button returnButton;

    // Constructor =================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simonsays);

        // <expGroup> is no longer used in this class. But I'm going to leave it here just in case
        // we find a use for it in the future.
        Bundle extras = getIntent().getExtras();
        //int expGroupIndex = Integer.parseInt(extras.getString("expGroup"));

        //boardButtons[0][0] = (Button)   findViewById(R.id.boardButton0);
        instructions       = (TextView) findViewById(R.id.instructions);
        returnButton       = (Button)   findViewById(R.id.returnButton);

        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }

        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("SIMONSAYS-START;-1;-1;-1");
        }
    }

    // Button handlers =============================================================================

    public void boardButtonPressed(View view) {
        //TODO: fill this out
    }

    public void returnButtonPressed(View view) {
        finish();
    }


    // Incoming message handler ====================================================================

    /**
     * This method directs the course of the activity based on the messages that it receives from
     * nao_server.py.
     */
    public void messageReceived(String msg) {
        System.out.println("[ SimonSaysActivity ] Received the following message: " + msg);
    }

    // Button disabling and enabling methods =======================================================

    public void disableButtons() {
        disableBoardButtons();
        disableReturnButton();
    }

    public void disableBoardButtons() {

    }

    public void disableReturnButton() {
        returnButton.setEnabled(false);
    }

    public void enableReturnButton() {
        returnButton.setEnabled(true);
    }
}
