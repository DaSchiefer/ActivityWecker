package com.schieferkoenigs.activitywecker.timeset_util;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.schieferkoenigs.activitywecker.util.MyFiles;
import com.schieferkoenigs.activitywecker.util.Variables;

public class RingtoneService extends Service {

    private MediaPlayer media_song;
    private boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Analysiert den vom Alarm Reciever erhaltenen Intent
     * Je nach Parameterwert in den Extras wird der Alarm und damit der MediaPlayer gestartet oder gestoppt
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        //fetch "extra"
        String state = intent.getExtras().getString("extra");
        // case zuweisung anhand der set und unset Buttons in der TimeSetActivity (yes= set)
        assert state != null;
        switch (state) {
            case "yes":
                startId = 1;
                break;
            case "no":
                startId = 0;
                break;
            default:
                startId = 0;
        }
        //Media Player wird gestartet sobald startId auf 1 gesetzt wird
        if (!this.isRunning && startId == 1) {
            int drawableResourceId = this.getResources().getIdentifier(MyFiles.getRingtoneFromFile(this), "raw", this.getPackageName());
            media_song = MediaPlayer.create(this, drawableResourceId);
            media_song.start();
            media_song.setLooping(true);
            Variables.mediaSong = media_song;

            this.isRunning = true;
        }
        // Media Player wird gestoppt wenn startID auf 0 gesetzt wird
        else if (this.isRunning && startId == 0) {
            media_song.stop();
            media_song.reset();
            this.isRunning = false;

        } else if (!this.isRunning && startId == 0) {
            this.isRunning = false;
        } else if (this.isRunning && startId == 1) {
            this.isRunning = false;
        } else {
            Log.e("else", "how did you reach this?");
        }

        return START_NOT_STICKY;
    }
}
