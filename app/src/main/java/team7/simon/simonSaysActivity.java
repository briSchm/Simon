package team7.simon;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;

/**
 * Created by brianaschmidt on 2/27/2017.
 */

// Traditional Simon game.

public class simonSaysActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon_says);

        findViewById(R.id.info).setOnClickListener(new AboutListener());

    }

    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html>" +
                    "<h2>How to play: </h2>" +
                    "<p>Repeat correct longer and longer sequence of signals.</p><br>" +
                    "<p>Choose skill level 1, 2, 3, or 4</p><br>" +
                    "<p>SIMON will give the first signal, and the player repeats the signal by repeating it.</p><br>" +
                    "<p>SIMON will duplicate the first signal and add one more. The player will repeat these signals. Simon will repeat these signals.</p><br>" +
                    "<p>To play a new game choose a new skill level or play the same one6.</p>" + "</html>";
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("Ok", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
