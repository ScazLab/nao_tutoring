package tutoringproject.breaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by arsalan on 4/13/16.
 */
public class TicTacToeActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe);
    }

    public void returnButtonPressed(View view) {
        finish();
    }
}
