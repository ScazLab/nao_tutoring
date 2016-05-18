package tutoringproject.breaks;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by aditi on 5/14/16.
 */
public class VisualFocusActivity extends Activity implements TCPClientOwner {

    // XML element variables
    private Button[]boardButtons = new Button[36];
    private TextView instructions;
    private Button returnButton;
    private Button currentRoundButton;
    private Random gen = new Random();

    private ArrayList<String> symbols = new ArrayList<String>();
    private ArrayList<String> pairs = new ArrayList<String>();

    //public variables
    public int NUM_BUTTONS = 36;
    public int NUM_SYMBOL_PAIRS;

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

        currentRoundButton = boardButtons[0];

        //initialize game symbols
        symbols.add("p");
        pairs.add("q");
        symbols.add("+");
        pairs.add("x");
        symbols.add("5");
        pairs.add("S");
        symbols.add("B");
        pairs.add("8");
        symbols.add("Z");
        pairs.add("2");
        NUM_SYMBOL_PAIRS = symbols.size();

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
        String robot_speech = "";
        if (button == currentRoundButton){
            //chose button correctly, have robot say nice job
            //TODO: randomly select a phrase
        }
        else {
            //choose button incorrectly, move on to next round?
        }
        disableBoardButtons();
        highlightUniqueButton();

        //send tcp message
        if (TCPClient.singleton != null) {
            TCPClient.singleton.sendMessage("VISUALFOCUS-ROUNDOVER;-1;-1;" + robot_speech);
        }
    }

    public void highlightUniqueButton() {
        currentRoundButton.setBackgroundColor(Color.YELLOW);
    }

    public void returnButtonPressed(View view) {
        finish();
    }


    // Game-playing methods ========================================================================
    public void nextRound(){
        //choose a symbol pair randomly
        int pick = gen.nextInt(NUM_SYMBOL_PAIRS);
        String symbol = symbols.get(pick);
        String pair = pairs.get(pick);
        currentRoundButton.setBackgroundColor(Color.GRAY); //TODO: figure out what the default button color is

        //randomly select one button and set this as the currentAnswer
        int pickButton = gen.nextInt(NUM_BUTTONS);
        //put the symbol on all buttons and put the pair on the randomly selected button
        currentRoundButton = boardButtons[pickButton];
        for (int i=0; i<NUM_BUTTONS; i++){
            if (i == pickButton){
                boardButtons[i].setText(pair);
            }
            else {
                boardButtons[i].setText(symbol);
            }
        }

        enableBoardButtons();

    }

    // Incoming message handler ====================================================================

    /**
     * This method directs the course of the activity based on the messages that it receives from
     * nao_server.py.
     */
    public void messageReceived(String msg) {
        System.out.println("[ VisualFocusActivity ] Received the following message: " + msg);

        if (msg.equals("VISUALFOCUS-START")) {
            nextRound();
        }
        else if (msg.equals("VISUALFOCUS-ROUNDOVER")) {
            nextRound();
        }
        else if (msg.equals("VISUALFOCUS-END")) {
            enableReturnButton();
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
