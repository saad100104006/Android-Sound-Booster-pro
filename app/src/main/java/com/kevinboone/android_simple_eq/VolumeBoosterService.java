package com.kevinboone.android_simple_eq;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build.VERSION;
import android.os.IBinder;

import java.util.Calendar;

public class VolumeBoosterService extends Service {
    private static AlarmManager alarmManager = null;
    private static AudioManager audio = null;
    private static Calendar calendar = null;
    private static Context context;
    private static Equalizer equalizer = null;
    private static boolean init = false;
    private static LoudnessEnhancer loudness = null;
    private static SharedPreferences mySettings = null;
    private static boolean useEQ = true;

    public static void init(Context c) {
        context = c;
        if (!init) {
            audio = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
            alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            mySettings = context.getSharedPreferences("VATSpkPro", 0);
            calendar = Calendar.getInstance();
            useEQ = VERSION.SDK_INT < 19;
            if (useEQ) {
                equalizer = getEqualizer();
            } else {
                loudness = getLoudnessEnhancer();
            }
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        init(this);
    }

    public void onStart(Intent intent, int startId) {
        checkBoost(mySettings.getInt("boost", 0));
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        checkBoost(mySettings.getInt("boost", 0));
        return 1;
    }

    public void onDestroy() {
    }

    @SuppressLint({"NewApi"})
    private static void checkBoost(int boost) {
        if (boost > 0) {
            if (useEQ) {
                boostEqualizer(boost);
            } else {
                boostLoudness(boost);
            }
            setNextUpdate();
        } else if (useEQ) {
            if (equalizer != null) {
                equalizer.setEnabled(false);
                equalizer.release();
                equalizer = null;
            }
        } else if (loudness != null) {
            loudness.setEnabled(false);
            loudness.release();
            loudness = null;
        }
    }

    private static void setNextUpdate() {
        try {
            PendingIntent pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0, new Intent(context.getApplicationContext(), VolumeBoosterService.class), 0);
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(13, 5);
            alarmManager.set(0, calendar.getTimeInMillis(), pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"NewApi"})
    public static LoudnessEnhancer getLoudnessEnhancer() {
        try {
            return new LoudnessEnhancer(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Equalizer getEqualizer() {
        try {
            return new Equalizer(1, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void boostEqualizer(int boost) {
        if (equalizer == null) {
            equalizer = getEqualizer();
        }
        if (equalizer != null) {
            try {
                float level = (((float) boost) / 100.0f) * ((float) equalizer.getBandLevelRange()[1]);
                short nBands = equalizer.getNumberOfBands();
                for (short i = (short) 0; i < nBands; i = (short) (i + 1)) {
                    equalizer.setBandLevel(i, (short) ((int) level));
                }
                equalizer.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static void boostLoudness(int boost) {
        float level = (((float) boost) / 100.0f) * 8000.0f;
        if (loudness == null) {
            loudness = getLoudnessEnhancer();
        }
        if (loudness != null) {
            try {
                loudness.setTargetGain((int) level);
                loudness.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setVolume(int vol, int boost, Context c) {
        init(c);
        checkBoost(boost);
        Editor editor = mySettings.edit();
        editor.putInt("boost", boost);
        editor.commit();
        audio.setStreamVolume(3, (int) ((((float) vol) / 100.0f) * ((float) audio.getStreamMaxVolume(3))), 8);
    }
}
