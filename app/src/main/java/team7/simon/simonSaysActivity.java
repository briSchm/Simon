package team7.simon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by brianaschmidt on 2/27/2017.
 */

//SIMON generates a random sequence and the player attempts to follow it.

public class simonSaysActivity extends AppCompatActivity implements View.OnClickListener {

    // button delay
    private final int CLICK = 800;
    private final int BUTTONS = 4;

    //Button positions
    private final int TOP = 1;
    private final int TOP_SECOND = 2;
    private final int MIDDLE = 3;
    private final int BOTTOM = 4;
    private final int SEQ = 100;

    //thread variables
    private Timer timer;
    private Timer respond;
    private int currIM, currDR, currD = CLICK;
    private int count = 0;
    private int playerCount = 0;

    private SimonStartingTask simonStartingTask;
    private boolean delay = false;
    private final int[] firstStep = {R.drawable.go};
    private int[] sequence;
    private boolean lost = false;
    private boolean simonTurn = true;
    private int currentSequence = 0;
    private int currCycle = 0;
    private Random rnd = new Random();
    private boolean clickable;
    private int rightButton = 0;
    private boolean check;
    private int high = 0;
    private int current = 0;
    private TextView highScore;
    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;
    private int sound_oneID, sound_twoID, sound_threeID, sound_fourID;
    private NewRoundTask newRoundTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_adds);

        findViewById(R.id.info).setOnClickListener(new AboutListener()); //set listener for info

        soundsLoaded = new HashSet<Integer>(); //get sounds

        sequence = new int[SEQ];

        ImageButton image = (ImageButton) findViewById(R.id.green_button);
        image.setOnClickListener(this);
        image = (ImageButton) findViewById(R.id.blue_button);
        image.setOnClickListener(this);
        image = (ImageButton) findViewById(R.id.red_button);
        image.setOnClickListener(this);
        image = (ImageButton) findViewById(R.id.yellow_button);
        image.setOnClickListener(this);

        //start button
        Button b = (Button) findViewById(R.id.start);
        b.setOnClickListener(this);

        setUpSequence();
    }

    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /*Message in AlertDialog box when user clicks the info button*/
            String message = "<html>" +
                    "<h2>How to play: </h2>" +
                    "<p>Repeat the ever-increasing random sequences that SIMON generates.</p><br>" +
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
    protected void onStop() {
        super.onStop();

        //save HighScore for activity
        highScore = (TextView) findViewById(R.id.high);

        //save highscore for game
        SharedPreferences prefs = this.getSharedPreferences("myfinalPrefs",
                Context.MODE_PRIVATE);
        high = prefs.getInt("score", 0);

        if (high > current) {
            highScore.setText(Integer.toString(high));
        } else {
            high = current;
            highScore.setText(Integer.toString(high));
            prefs.edit().putInt("score", high).apply();
        }
    }

    private void setUpSequence() {
        if (!lost) {
            for (int i = 0; i < currentSequence; i++) {
                sequence[i] = rnd.nextInt(4) + 1;
            }
        }
        sequence[currentSequence] = rnd.nextInt(4) + 1;

        currentSequence++;
    }

    private void play() {
        if (!lost) {
            currCycle = 0;
            playNextGame();
        }
    }

    private void pressedImage(int im) {
        ImageButton i = (ImageButton) findViewById(currIM);
        i.setImageResource(im);
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new BTask(), currD);
        }
    }

    private void resetImageButton(int im) {
        ImageButton i = (ImageButton) findViewById(currIM);
        i.setImageResource (im);
        if (simonTurn) {
            playNextGame();
        }

        else {
            if (respond == null && check && !simonTurn && !lost) {
                respond = new Timer();
                respond.schedule(new TimesUpTask(), currD * BUTTONS);
                playerCount++;
            }
        }
    }

    private void playNextGame() {
        if (sequence[currCycle] > 0) {
            switch (sequence[currCycle]) {
                case 1:
                    currCycle++;
                    currIM = R.id.green_button;
                    currDR = R.drawable.green_button;
                    pressedImage(R.drawable.green_pressed);
                    playSound(sound_oneID);
                    break;
                case 2:
                    currCycle++;
                    currIM = R.id.blue_button;
                    currDR = R.drawable.blue_button;
                    pressedImage(R.drawable.blue_pressed);
                    playSound(sound_twoID);
                    break;
                case 3:
                    currCycle++;
                    currIM = R.id.red_button;
                    currDR = R.drawable.red_button;
                    pressedImage(R.drawable.red_pressed);
                    playSound(sound_threeID);
                    break;
                case 4:
                    currCycle++;
                    currIM = R.id.yellow_button;
                    currDR = R.drawable.yellow_button;
                    pressedImage(R.drawable.yellow_pressed);
                    playSound(sound_fourID);
                    break;
            }
        } else {
            simonTurn = false;
            if (respond == null) {
                respond = new Timer();
                respond.schedule(new TimesUpTask(), currD * BUTTONS);
                playerCount++;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(clickable) {
            if (timer == null ) {
                switch (v.getId()) {
                    case R.id.green_button:
                        PauseForPlayer();
                        currIM = R.id.green_button;
                        currDR = R.drawable.green_button;
                        check = ButtonClicked(TOP);
                        if (check) {
                            pressedImage(R.drawable.green_pressed);
                            playSound(sound_oneID);
                        }
                        break;
                    case R.id.blue_button:
                        PauseForPlayer();
                        currIM = R.id.blue_button;
                        currDR = R.drawable.blue_button;
                        check = ButtonClicked(TOP_SECOND);
                        if (check) {
                            pressedImage(R.drawable.blue_pressed);
                            playSound(sound_twoID);
                        }
                        break;
                    case R.id.red_button:
                        PauseForPlayer();
                        currIM = R.id.red_button;
                        currDR = R.drawable.red_button;
                        check = ButtonClicked(MIDDLE);
                        if (check) {
                            pressedImage(R.drawable.red_pressed);
                            playSound(sound_threeID);
                        }
                        break;
                    case R.id.yellow_button:
                        PauseForPlayer();
                        currIM = R.id.yellow_button;
                        currDR = R.drawable.yellow_button;
                        check = ButtonClicked(BOTTOM);
                        if (check) {
                            pressedImage(R.drawable.yellow_pressed);
                            playSound(sound_fourID);
                        }
                        break;
                }
            } else {
                gameOver();
            }
            rightButton ++;
            if(check && rightButton >= currentSequence){
                NewScore();
                PauseForPlayer();
                startNextRound();
            }
        } else {
            gameOver();
        }
        if(v.getId() == R.id.start){
            newGame();
        }
    }

    private void newGame(){
        PauseForPlayer();
//        for(int i =0; i < currentSequence; i++){
//            sequence[i] = 0;
//        }
        TextView tv =(TextView) findViewById(R.id.game_score);
        tv.setText(String.valueOf(0));
        tv = (TextView)findViewById(R.id.game_over);
        tv.setText("");
        currentSequence=0;
        clearScore();
        simonTurn = true;
        lost = false;
        PauseForPlayer();
        setUpSequence();
        startWaiting();

    }

    private boolean ButtonClicked(int input){
        if(input == sequence [rightButton]){
            return true;
        }else {
            gameOver();
            return false;
        }
    }

    private void gameOver(){
        PauseForPlayer();
        for(int i =0; i< currentSequence; i++){
            sequence[i] =0;
        }
        currentSequence=0;
        clearScore();
        lost = true;

        simonTurn = false;
        if(newRoundTask != null){
            newRoundTask = null;
        }
        gameOverToast();
    }

    private void NewScore(){
        TextView tv =(TextView)findViewById(R.id.game_score);
        current = currentSequence;
        tv.setText(String.valueOf(current));

        if(current > high){
            tv =(TextView) findViewById(R.id.high);
            high = current;
            tv.setText(String.valueOf(high));
        }
    }

    private void gameOverToast(){
        clickable = false;
        TextView tv = (TextView)findViewById(R.id.game_over);
        tv.setText("Game Over!");
    }

    private class BTask extends TimerTask {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resetImageButton(currDR);
                }
            });
            count++;
            StopButton();
        }
    }

    private class TimesUpTask extends TimerTask {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameOverToast();
                    gameOver();
                }
            });
        }
    }

    private void StopButton() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if(simonStartingTask != null) {
            simonStartingTask.cancel(true);
            simonStartingTask = null;
        }
    }

    private void PauseForPlayer() {
        if (respond != null) {
            respond.cancel();
            respond = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // SoundPool
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder sb = new SoundPool.Builder();
        sb.setAudioAttributes(attrBuilder.build());
        sb.setMaxStreams(2);
        soundPool = sb.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundsLoaded.add(sampleId);
                }
            }
        });

        // Load sounds
        sound_oneID = soundPool.load(this, R.raw.f_6, 1);
        sound_twoID = soundPool.load(this, R.raw.f_5, 1);
        sound_threeID = soundPool.load(this, R.raw.f_4, 1);
        sound_fourID = soundPool.load(this, R.raw.f_3, 1);

        delay = true;

        clearScore();
        startWaiting();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StopButton();
        PauseForPlayer();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;

            soundsLoaded.clear();
        }
    }

    private void playSound (int iSound) {
        if (soundsLoaded.contains(iSound) && !lost) {
            soundPool.play(iSound, 1.0f, 1.0f, 0, 0, (float) currD / (float) CLICK);
        }
    }

    private void startWaiting(){

        if (simonStartingTask != null && simonStartingTask.getStatus() == AsyncTask.Status.FINISHED) {
            simonStartingTask = null;
        }

        if(simonStartingTask == null) {
            simonStartingTask = new SimonStartingTask();
            simonStartingTask.execute();

        }
    }

    class SimonStartingTask extends AsyncTask<Void, Integer, Integer> {
        ImageView im = (ImageView) findViewById(R.id.number);
        View layout = findViewById(R.id.output);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            clickable = false;
            im.setVisibility(View.VISIBLE);
            layout.setBackgroundColor(Color.WHITE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                for(int i = 0; i < firstStep.length; i++) {
                    if(delay){
                        Thread.sleep(200);
                    }
                    publishProgress(i);
                    Thread.sleep(1000);
                }
                return null;
            }
            catch (InterruptedException e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            im.setImageResource(R.drawable.go);
            im.setVisibility(View.INVISIBLE);
            delay = false;
            clickable = true;

            play();
        }
    }

    private void clearScore(){
        rightButton = 0;
        currCycle = 0;
    }

    private void startNextRound(){
        newRoundTask = new NewRoundTask();
        clearScore();
        newRoundTask.execute();
    }

    private class NewRoundTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            setUpSequence();
            PauseForPlayer();
            simonTurn = true;
            play();
        }
    }
}
