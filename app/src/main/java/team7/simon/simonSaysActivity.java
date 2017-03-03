package team7.simon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;

/**
 * Created by brianaschmidt on 2/27/2017.
 */

// Traditional Simon game.

public class simonSaysActivity extends AppCompatActivity {
    private enum Colors {RED, YELLOW, GREEN, BLUE};

    private Pattern sequencePattern;

    private boolean player = false;

    private final static int HIGH_SCORE = 0;

    private int score = 0;

    private Button redB, greenB, yellowB, blueB;

    private TextView currentScore, highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon);

        findViewById(R.id.start).setOnClickListener(new StartGameListener());
        findViewById(R.id.info).setOnClickListener(new AboutListener()); //set listener for info

    }

    private class Pattern {
        Random rnd;
        Vector<Integer> pattern;

        Pattern() {
            rnd = new Random();
            pattern = new Vector(13,8);
        }
        int nextPattern() {
            return rnd.nextInt(4)+1;
        }
        int getSize() {
            return pattern.size();
        }
    }

    class StartGameListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

    protected class PlaySimon extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
             /*Message in AlertDialog box when user clicks the info button*/
            String message = "<html>" +
                    "<h2>How to play: </h2>" +
                    "<p>Repeat correct longer and longer sequence of signals.</p><br>" +
                    "<p>Click the start button.</p><br>" +
                    "<p>SIMON will give the first signal, and the player repeats the signal by repeating it.</p><br>" +
                    "<p>SIMON will duplicate the first signal and add one more. The player will repeat these signals. Simon will repeat these signals.</p><br>" +
                    "<p>To play a new game, just click the start button.</p>" + "</html>";
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("Ok", null); //includes ok button

            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }
}
