package tutoringproject.breaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by aditi on 5/14/16.
 */
public class VisualFocusActivity extends Activity implements TCPClientOwner {

    // XML element variables
    private Button[]boardButtons = new Button[36];
    private TextView instructions;
    private Button returnButton;

    //public methods
    public int NUM_BUTTONS = 36;

    // Constructor =================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualfocus);

        // <expGroup> is no longer used in this class. But I'm going to leave it here just in case
        // we find a use for it in the future.
        Bundle extras = getIntent().getExtras();
        //int expGroupIndex = Integer.parseInt(extras.getString("expGroup"));

        //Do not know of a more efficient way to do this.
        boardButtons[0] = (Button)   findViewById(R.id.boardButton0);
        boardButtons[1] = (Button)   findViewById(R.id.boardButton1);
        boardButtons[2] = (Button)   findViewById(R.id.boardButton2);
        boardButtons[3] = (Button)   findViewById(R.id.boardButton3);
        boardButtons[4] = (Button)   findViewById(R.id.boardButton4);
        boardButtons[5] = (Button)   findViewById(R.id.boardButton5);
        boardButtons[6] = (Button)   findViewById(R.id.boardButton6);
        boardButtons[7] = (Button)   findViewById(R.id.boardButton7);
        boardButtons[8] = (Button)   findViewById(R.id.boardButton8);
        boardButtons[9] = (Button)   findViewById(R.id.boardButton9);
        boardButtons[10] = (Button)   findViewById(R.id.boardButton10);
        boardButtons[11] = (Button)   findViewById(R.id.boardButton11);
        boardButtons[12] = (Button)   findViewById(R.id.boardButton12);
        boardButtons[13] = (Button)   findViewById(R.id.boardButton13);
        boardButtons[14] = (Button)   findViewById(R.id.boardButton14);
        boardButtons[15] = (Button)   findViewById(R.id.boardButton15);
        boardButtons[16] = (Button)   findViewById(R.id.boardButton16);
        boardButtons[17] = (Button)   findViewById(R.id.boardButton17);
        boardButtons[18] = (Button)   findViewById(R.id.boardButton18);
        boardButtons[19] = (Button)   findViewById(R.id.boardButton19);
        boardButtons[20] = (Button)   findViewById(R.id.boardButton20);
        boardButtons[21] = (Button)   findViewById(R.id.boardButton21);
        boardButtons[22] = (Button)   findViewById(R.id.boardButton22);
        boardButtons[23] = (Button)   findViewById(R.id.boardButton23);
        boardButtons[24] = (Button)   findViewById(R.id.boardButton24);
        boardButtons[25] = (Button)   findViewById(R.id.boardButton25);
        boardButtons[26] = (Button)   findViewById(R.id.boardButton26);
        boardButtons[27] = (Button)   findViewById(R.id.boardButton27);
        boardButtons[28] = (Button)   findViewById(R.id.boardButton28);
        boardButtons[29] = (Button)   findViewById(R.id.boardButton29);
        boardButtons[30] = (Button)   findViewById(R.id.boardButton30);
        boardButtons[31] = (Button)   findViewById(R.id.boardButton31);
        boardButtons[32] = (Button)   findViewById(R.id.boardButton32);
        boardButtons[33] = (Button)   findViewById(R.id.boardButton33);
        boardButtons[34] = (Button)   findViewById(R.id.boardButton34);
        boardButtons[35] = (Button)   findViewById(R.id.boardButton35);

        instructions       = (TextView) findViewById(R.id.instructions);
        returnButton       = (Button)   findViewById(R.id.returnButton);

        // Transfer control of TCP client from MathActivity to this activity.
        if (TCPClient.singleton != null ) {
            TCPClient.singleton.setSessionOwner(this);
        }

        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("VISUALFOCUS-START;-1;-1;-1");
        }
    }

    // Button handlers =============================================================================

    public void boardButtonPressed(View view) {
        Button button = (Button)view;
    }

    public void returnButtonPressed(View view) {
        finish();
    }


    // Game-playing methods ========================================================================
    public void startRound(){

    }

    // Incoming message handler ====================================================================

    /**
     * This method directs the course of the activity based on the messages that it receives from
     * nao_server.py.
     */
    public void messageReceived(String msg) {
        System.out.println("[ VisualFocusActivity ] Received the following message: " + msg);

        if (msg.equals("VISUALFOCUS-START")) {
            startRound();
        }
    }

    // Button disabling and enabling methods =======================================================

    public void disableButtons() {
        disableBoardButtons();
        disableReturnButton();
    }

    public void disableBoardButtons() {
        for (int i=0; i<NUM_BUTTONS; i++){
            boardButtons[i].setEnabled(false);
        }
    }

    public void enableBoardButtons() {
        for (int i=0; i<NUM_BUTTONS; i++){
            boardButtons[i].setEnabled(true);
        }
    }

    public void disableReturnButton() {
        returnButton.setEnabled(false);
    }

    public void enableReturnButton() {
        returnButton.setEnabled(true);
    }
}
