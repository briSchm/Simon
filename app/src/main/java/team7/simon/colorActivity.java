package team7.simon;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;

/**
 * Created by brianaschmidt on 2/27/2017.
 */

//Multiplayer, where multiple players choose one color/button, and only press it when that button flashes on the sequence. Simon builds the sequence.

public class colorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_color);

        findViewById(R.id.info).setOnClickListener(new AboutListener());

    }

    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html>" +
                    "<h2>How to play: </h2>" +
                    "<p>Repeat the longest sequence of signals.</p><br>" +
                    "<p>Choose skill level 4</p><br>" +
                    "<p>SIMON will show the first color, and the player repeats the signal with their color choice.</p><br>" +
                    "<p>The player will only push their lens in proper sequence.</p><br>" +
                    "<p>To play a new game just click on the skill level.</p>" + "</html>";
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("Ok", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}