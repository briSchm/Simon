package team7.simon;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * Created by brianaSchmidt on 2/27/2017.
 */

public class SimonGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon);

        findViewById(R.id.info).setOnClickListener(new AboutListener());
    }

    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html>" +
                    "<h2>How to play: </h2>" +
                    "<p>Repeat the ever-increasing random signals that SIMON generates.</p><br>" +
                    "<p>Choose what skill level you want: 1, 2, 3, or 4</p><br>" +
                    "<p>Simon will show the pattern, and you repeat it</p><br>" +
                    "<p>To play a new game just click on the same skill level or pick a new one</p>" + "</html>";
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("Ok", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}