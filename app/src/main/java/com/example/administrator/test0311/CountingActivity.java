package com.example.administrator.test0311;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class CountingActivity extends ActionBarActivity {
    int count = 1;
    TextView a;
    private MediaPlayer mediaPlayer = null;
    private int finalTime;
    private int timeElapsed;
    private int[] files = {R.raw.one, R.raw.two, R.raw.three, R.raw.four, R.raw.five, R.raw.six, R.raw.seven, R.raw.eight, R.raw.nine, R.raw.ten, R.raw.eleven, R.raw.twelve, R.raw.thirteen, R.raw.fourteen, R.raw.fifteen};
    private String[] sound = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);
        final TextView timetext = (TextView) findViewById(R.id.time);
        a = timetext;
        final Intent intent = getIntent();
        final int time = intent.getIntExtra("time", 0);

        SharedPreferences pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        isMute = pref.getBoolean("mute", false);

        if (time != 0) {
            final Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (count <= time && !isFinishing()) {
                        Message m = new Message();
                        m.what = count;
                        mHandler.sendMessage(m);
                    } else {
                        timer.purge();
                        timer.cancel();
                        finish();
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        } else {
            timetext.setText("끝!");
        }


        String picturePath = pref.getString("picturePath", null);
        Bitmap backGroundImage = BitmapFactory.decodeFile(picturePath);
        RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
        background.setBackground(new BitmapDrawable(getResources(), backGroundImage));

        String audioPath = pref.getString("audioPath", null);
        if (audioPath != null) sound = audioPath.split("\\|\\|\\|");
        Log.d("sound", "sound[0] = " + sound[0] + ", monday_audioP = " + audioPath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_counting, menu);

        //noinspection SimplifiableIfStatement
        if (isMute) {
            menu.getItem(0).setTitle("소리OFF");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_sound) {
            if ("소리ON".equals(item.getTitle())) {
                item.setTitle("소리OFF");
                isMute = true;
            } else {
                item.setTitle("소리ON");
                isMute = false;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play() throws IOException {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (count <= files.length) {

            Log.d("sound", "sound[" + (count - 1) + "] = " + sound[count - 1]);
            if (sound[count - 1] != null && !"null".equals(sound[count - 1])) {
                Log.d("sound", "get From sound[]");
                mediaPlayer = MediaPlayer.create(this, Uri.parse(sound[count - 1]));
            } else {
                Log.d("sound", "get From file[]");
                mediaPlayer = MediaPlayer.create(this, files[count - 1]);
            }
            finalTime = mediaPlayer.getDuration();
            mediaPlayer.start();
            timeElapsed = mediaPlayer.getCurrentPosition();
        }


        if (isMute) {
            mediaPlayer.setVolume(0, 0);
        } else {
            mediaPlayer.setVolume(0, 1);
        }

    }

    public void pause() {
        mediaPlayer.pause();
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            a.setText("" + msg.what);
            try {
                play();

                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}

