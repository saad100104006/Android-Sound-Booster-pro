package com.kevinboone.android_simple_eq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity {


    ToggleButton t1, t2, t3;
    ToggleButton toogleBoost;
    ImageView imageView1, imageView2, imageView3;
    TextView percents;
    int flag = 0;
    int flag2 = 0;
    int flag3 = 0;
    MediaPlayer mp = null;
    Singleton m_Inst = Singleton.getInstance();
    private AudioManager myAudioManager;
    int tempPer = 2;
    private boolean music, call, notification;
    ImageButton musics;
    private int boostFlag = 0;

    private String artist = null;
    private int volume = 50;
    private int boost = 0;
    private ImageView boostButton = null;
    private SeekBar boostSlider = null;
    private Handler h = null;
    private Runnable r = null;
    private String track = null;
    private Vibrator vib = null;
    private AudioManager audio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        this.vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        t1 = (ToggleButton) findViewById(R.id.toggle1);
        t2 = (ToggleButton) findViewById(R.id.toggle2);
        t3 = (ToggleButton) findViewById(R.id.toggle3);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        percents = (TextView) findViewById(R.id.text);
        musics = (ImageButton) findViewById(R.id.music);
        toogleBoost = (ToggleButton) findViewById(R.id.toggleSwitch);


        Typeface face= Typeface.createFromAsset(getAssets(), "android_7.ttf");
        percents.setTypeface(face);


        // this.mySettings = getSharedPreferences("VATSpkPro", 0);
        VolumeBoosterService.init(this);


        this.h = new Handler();

        SongInfoReceiver.init(this, new SongInfoReceiver.OnMetaChanged() {
            public void onMetaChanged(String a, String t) {
                MainActivity.this.artist = a;
                MainActivity.this.track = t;
                //   MainActivity.this.updateSongInfo();
            }
        });


        musics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startActivity(new Intent("android.intent.action.MUSIC_PLAYER"));
                } catch (Exception e) {

                }

            }
        });


        toogleBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (boostFlag == 0) {

                    Toast.makeText(MainActivity.this, "Boost is ON!",
                            Toast.LENGTH_LONG).show();
                    boostFlag = 1;
                    percents.setVisibility(View.VISIBLE);


                } else if (boostFlag == 1) {

                    Toast.makeText(MainActivity.this, "Boost is OFF!",
                            Toast.LENGTH_LONG).show();

                    percents.setVisibility(View.GONE);

                    boostFlag = 0;


                    MainActivity.this.boost = 0;
                    setVolume();
                    MainActivity.this.vib.vibrate(0);


                }

            }
        });


        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flag == 0) {
                    imageView1.setImageResource(R.drawable.musical_on);
                    music = true;
                    // managerOfSound();

                    flag = 1;
                } else {
                    imageView1.setImageResource(R.drawable.musical_off);
                    music = false;

                    flag = 0;
                }


            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flag2 == 0) {

                    imageView2.setImageResource(R.drawable.telephone_on);
                    call = true;
                    // managerOfSound();
                    flag2 = 1;
                } else {
                    imageView2.setImageResource(R.drawable.telephone_off);
                    call = false;
                    flag2 = 0;
                }


            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flag3 == 0) {

                    imageView3.setImageResource(R.drawable.alarm_on);
                    notification = true;
                    // managerOfSound();
                    flag3 = 1;
                } else {
                    imageView3.setImageResource(R.drawable.alarm_off);
                    notification = false;
                    flag3 = 0;
                }


            }
        });


        // Scaling mechanism, as explained on:

        m_Inst.InitGUIFrame(this);

        //  RelativeLayout panel = new RelativeLayout(this);

        RelativeLayout panel = (RelativeLayout) findViewById(R.id.top);
        setContentView(panel);

       /* TextView tv = new TextView(this);
        tv.setText("Rotary knob control\nRadu Motisan 2013\nwww.pocketmagic.net");
        tv.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        panel.addView(tv, lp);*/

        //final TextView tv2 = new TextView(this); tv2.setText("");
        percents.setText("");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        //percents.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //  panel.addView(percents);
        if (percents.getParent() != null)
            ((ViewGroup) percents.getParent()).removeView(percents); // <- fix
        panel.addView(percents, lp);


        RoundKnobButton rv = new RoundKnobButton(this, R.drawable.stator, R.drawable.rotoron, R.drawable.rotoroff,
                m_Inst.Scale(350), m_Inst.Scale(350));
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        panel.addView(rv, lp);

        rv.setRotorPercentage(0);
        rv.SetListener(new RoundKnobButton.RoundKnobButtonListener() {
            public void onStateChange(boolean newstate) {
                Toast.makeText(MainActivity.this, "New state:" + newstate, Toast.LENGTH_SHORT).show();
            }

            public void onRotate(final int percentage) {
                percents.post(new Runnable() {
                    public void run() {

                        if (boostFlag == 1) {

                            percents.setText("\n" + "Boost " + percentage + "%\n");


                            //boosting sound

                            int tempBoost=percentage * 2;

                            MainActivity.this.boost = tempBoost;
                            MainActivity.this.setVolume();
                            MainActivity.this.vib.vibrate(percentage);

                        }


                      //  setVolumeControlStream(AudioManager.STREAM_ALARM);
                        if (percentage > tempPer) {

                            //myAudioManager.setStreamVolume(AudioManager.STREAM_RING,AudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);

                          //  myAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

                            if(notification) {

                                myAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, percentage / 10, 0);
                            }
                            if(music) {sour
                                myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, percentage / 10, 0);
                            }
                            if(call) {
                                myAudioManager.setStreamVolume(AudioManager.STREAM_RING, percentage / 10, 0);
                            }

                        }
                        else if(percentage<tempPer){


                          //  myAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);


                            if(notification) {

                                myAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, percentage / 10, 0);
                            }
                            if(music) {
                                myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, percentage / 10, 0);
                            }
                            if(call) {
                                myAudioManager.setStreamVolume(AudioManager.STREAM_RING, percentage / 10, 0);
                            }


                        }


                        tempPer = percentage;
                    }
                });
            }
        });



        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);


    }


    private void setVolume() {
        VolumeBoosterService.setVolume(this.volume, this.boost, this);
        //updateVolumeInfo();
    }


    public void onBoostClicked(View view) {
        this.volume = 100;
        this.boost = 100;
        setVolume();
        this.vib.vibrate(10);
    }


    protected void onResume() {
        super.onResume();
        this.volume = (int) (100.0f * (((float) this.audio.getStreamVolume(3)) / ((float) this.audio.getStreamMaxVolume(3))));
    }


    public void onPlayButtonClick(View v) {

    }


}
