package tutoringproject.breaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by arsalan on 4/14/16.
 */
public class StretchBreakActivity extends Activity implements TCPClientOwner {
    private enum ExpGroup { FIXED, REWARD, FRUSTRATION }

    private ExpGroup expGroup = ExpGroup.FIXED;

    // XML element variables
    private Button returnButton;
    private TextView instructions;

    // Public variables ============================================================================

    // Speech strings
    /* These strings are no longer necessary. The start speech is now generated in nao_server.py.
    public HashMap<ExpGroup, String> START_MSGS = new HashMap<ExpGroup, String>() {{
        put(ExpGroup.FIXED,
            "Let's take a break to stretch.");
        put(ExpGroup.REWARD,
            "You're doing really well! You deserve a break. Let's stretch.");
        put(ExpGroup.FRUSTRATION,
            "Why don't we take a break and stretch. The mental rest will be good for you.");
    }};
    */

    // Tablet text strings
    public String CLICK_RETURN_BUTTON_TEXT =
        "Click the button below to return to the tutoring session.";

    // Constructor =================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretchbreak);

        // <expGroup> is no longer used in this class. But I'm going to leave it here just in case
        // we find a use for it in the future.
        Bundle extras = getIntent().getExtras();
        int expGroupIndex = Integer.parseInt(extras.getString("expGroup"));
        if (expGroupIndex == 1) {
            expGroup = ExpGroup.FIXED;
        } else if (expGroupIndex == 2) {
            expGroup = ExpGroup.REWARD;
        } else if (expGroupIndex == 3) {
            expGroup = ExpGroup.FRUSTRATION;
        }

        returnButton = (Button)   findViewById(R.id.returnButton);
        instructions = (TextView) findViewById(R.id.instructions);

        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }

        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("STRETCHBREAK-START;-1;-1;-1");
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
