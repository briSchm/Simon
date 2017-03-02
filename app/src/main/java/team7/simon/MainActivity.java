package team7.simon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by brianaschmidt on 2/27/2017.
 */

public class MainActivity extends AppCompatActivity {

    enum MediaState {NOT_READY, PLAYING, PAUSED, STOPPED}; /*media player features*/

    private MediaPlayer mediaPlayer;
    private MediaState mediaState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.play_imageButton).setOnClickListener(new StartListener()); /*when player hits the play button*/
        findViewById(R.id.pause_imageButton).setOnClickListener(new PauseListener()); /*when player hits the pause button*/
        findViewById(R.id.stop_imageButton).setOnClickListener(new StopListener()); /*when player hits the stop button*/

        Button b = (Button) findViewById(R.id.play);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        SimonGameActivity.class /*when user presses the play button, it opens the SimonGameActivity*/
                );
                startActivity(intent);
            }
        });

        b = (Button) findViewById(R.id.copyright);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        aboutActivity.class /*when user presses the play button, it opens the aboutActivity*/
                );
                startActivity(intent);
            }
        });

        b = (Button) findViewById(R.id.info);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.hasbro.com/common/documents/3f4e2ca0206011ddbd0b0800200c9a66/620962835056900B10D1688756D7BA4A.pdf"); /*open in PDF viewer*/
                Intent intent = new Intent(Intent.ACTION_VIEW, uri); /*when user presses the info button, it opens the browser*/
                startActivity(intent);
            }
        });

        b = (Button) findViewById(R.id.version1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        simonSaysActivity.class /*when user presses the version1 button, it opens the simonSaysActivity*/
                );
                startActivity(intent);
            }
        });

        b = (Button) findViewById(R.id.version2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        playerActivity.class /*when user presses the version2 button, it opens the playerActivity*/
                );
                startActivity(intent);
            }
        });

        b = (Button) findViewById(R.id.version3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        colorActivity.class /*when user presses the version3 button, it opens the SimonGameActivity*/
                );
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAudio(); /*when audio is paused, it stops playing*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Music:", "----------------------- app stopped");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaState = MediaState.NOT_READY; /*when user opens outside the app, for example with a phone call; the audio stops*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume(); /*audio plays again*/
        playAudio();
    }

    class StartListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            playAudio(); /*when the audio starts, it should play*/
        }
    }

    class PauseListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                mediaState = MediaState.PAUSED; /*when audio is paused, it should pause*/
            }
        }
    }

    class StopListener implements  View.OnClickListener {
        @Override
        public void onClick(View v) {
            stopAudio(); /*when audio is stopped, it shouldn't play*/
        }
    }

    private void playAudio() {
        if (mediaPlayer == null) {
            mediaState = MediaState.NOT_READY;

            mediaPlayer = MediaPlayer.create(getApplicationContext(),
                    R.raw.out_of_time); /*gets the mp3 file*/
            mediaPlayer.setLooping(true); /*begins to loop the file*/

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i("Music:", "---------------- ready to play");

                    mediaPlayer.start();
                    mediaState = MediaState.PLAYING; /*begins playing music*/
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.i("Music:", "---------------- problem playing sound"); /*if there is an error playing the audio, it won't start*/
                    return false;
                }
            });
        } else if (mediaState == MediaState.PAUSED){
            mediaPlayer.start();
            mediaState = MediaState.PLAYING; /*else if the audio is paused, it will begin playing*/
        } else if (mediaState == MediaState.STOPPED) {
            mediaPlayer.prepareAsync();
            mediaState = MediaState.NOT_READY; /*else if the audio is stopped, it will be not ready*/
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaState = MediaState.STOPPED; /*if the audio is stopped, it shouldn't play*/
        }

    }
}
