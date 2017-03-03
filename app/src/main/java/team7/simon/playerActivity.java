package team7.simon;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;

/**
 * Created by brianaschmidt on 2/27/2017.
 */

// Multiplayer, where the current player adds the next sequence and passes it on.

public class playerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_adds);

        findViewById(R.id.info).setOnClickListener(new AboutListener()); //set listener for info

    }

    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
             /*Message in AlertDialog box when user clicks the info button*/
            String message = "<html>" +
                    "<h2>How to play: </h2>" +
                    "<p>Create a longer and longer sequence of signals.</p><br>" +
                    "<p>Click on the start button.</p><br>" +
                    "<p>SIMON will give the first sequence. Repeat the first signal and add one more.</p><br>" +
                    "<p>Repeat the two signals and add one more, and so on.</p><br>" +
                    "<p>To play a new game just click on the start button.</p>" + "</html>";
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("Ok", null); //includes ok button

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
