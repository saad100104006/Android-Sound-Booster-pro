package com.kevinboone.android_simple_eq;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class SongInfoReceiver {
    private static OnMetaChanged callback;
    private static Context context = null;
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint({"DefaultLocale"})
        public void onReceive(Context context, Intent intent) {
            String artist = null;
            String track = null;
            Bundle extra = intent.getExtras();
            try {
                for (String key : extra.keySet()) {
                    if (artist == null && (key.toLowerCase().equals("artist") || key.toLowerCase().equals("artist_name"))) {
                        artist = extra.getString(key);
                    } else if (track == null && (key.toLowerCase().equals("track") || key.toLowerCase().equals("track_name"))) {
                        track = extra.getString(key);
                    }
                }
            } catch (Exception e) {
            }
            if (SongInfoReceiver.callback != null) {
                SongInfoReceiver.callback.onMetaChanged(artist, track);
            }
        }
    };

    public interface OnMetaChanged {
        void onMetaChanged(String str, String str2);
    }

    public static void init(Context ctx, OnMetaChanged omc) {
        callback = omc;
        context = ctx;
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.htc.music.playstatechanged");
        iF.addAction("com.htc.music.playbackcomplete");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("com.miui.player.playstatechanged");
        iF.addAction("com.miui.player.playbackcomplete");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.playstatechanged");
        iF.addAction("com.real.IMP.playbackcomplete");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED");
        iF.addAction("com.sonyericsson.music.TRACK_COMPLETED");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sonyericsson.music.playbackcomplete");
        iF.addAction("com.sonyericsson.music.playstatechanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.rdio.android.playstatechanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.playstatechanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.playbackcomplete");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.sec.android.app.music.playstatechanged");
        iF.addAction("com.sec.android.app.music.playbackcomplete");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.playstatechanged");
        iF.addAction("com.amazon.mp3.playstatechanged");
        iF.addAction("com.rhapsody.playstatechanged");
        iF.addAction("com.maxmpz.audioplayer.playstatechanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("fm.last.android.playbackpaused");
        iF.addAction("fm.last.android.playbackcomplete");
        iF.addAction("com.adam.aslfms.notify.playstatechanged");
        iF.addAction("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
        iF.addAction("com.spotify.music.metadatachanged");
        iF.addAction("com.spotify.music.playbackstatechanged");
        context.registerReceiver(receiver, iF);
    }

    public static void stop() {
        context.unregisterReceiver(receiver);
    }
}
