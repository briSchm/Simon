package team7.simon;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * Created by brianaSchmidt on 2/27/2017.
 */

//Give the sequence at full from the start, player repeats, simon does a new full sequence.

public class SimonGameActivity extends AppCompatActivity {

    private enum Colors {RED, YELLOW, GREEN, BLUE};

    private Pattern sequencePattern;

    private boolean player = false;

    private final static int HIGH_SCORE = 0;

    private int score = 0;

    private Button redB, greenB, yellowB, blueB;

    private TextView currentScore, highScore;

    private SoundPool soundPool;

    private Set<Integer> sounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon);

        findViewById(R.id.start).setOnClickListener(new StartGameListener());
        findViewById(R.id.info).setOnClickListener(new AboutListener()); //set listener for info

        sounds = new HashSet<Integer>();
    }

    private class Pattern {
        Random rnd;
        Vector<Integer> pattern;

        Pattern() {
            rnd = new Random();
            pattern = new Vector(2,8);
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
                    "<p>Repeat the ever-increasing random signals that SIMON generates.</p><br>" +
                    "<p>Click the start button</p><br>" +
                    "<p>Simon will show the pattern, and you repeat it</p><br>" +
                    "<p>To play a new game just click the start button.</p>" + "</html>";
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("Ok", null); //includes ok button

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        greenB = (Button) findViewById(R.id.green);
        redB = (Button) findViewById(R.id.red);
        blueB = (Button) findViewById(R.id.blue);
        yellowB = (Button) findViewById(R.id.yellow);

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setAudioAttributes(attrBuilder.build());
        spBuilder.setMaxStreams(4);
        soundPool = spBuilder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) { // success
                    sounds.add(sampleId);
                    Log.i("SOUND", "Sound loaded " + sampleId);
                } else {
                    Log.i("SOUND", "Error cannot load sound status = " + status);
                }
            }
        });

        final int PianoGreenID = soundPool.load(this, R.raw.f_6, 1);
        greenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(PianoGreenID);
            }
        });

        final int PianoRedId = soundPool.load(this, R.raw.f_4, 1);
        redB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(PianoRedId);
            }
        });

        final int PianoYellowId = soundPool.load(this, R.raw.f_3, 1);
        yellowB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(PianoYellowId);
            }
        });

        final int PianoBlueId = soundPool.load(this, R.raw.d_7, 1);
        blueB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(PianoBlueId);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;

            sounds.clear();
        }
    }

    private void playSound(int soundId) {
        if (sounds.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }
}
