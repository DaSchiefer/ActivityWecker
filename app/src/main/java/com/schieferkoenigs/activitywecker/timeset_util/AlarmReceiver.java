package com.schieferkoenigs.activitywecker.timeset_util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.schieferkoenigs.activitywecker.util.Variables;

import java.time.LocalDateTime;

/**
 * Gibt extras von TimeSetActivity an den RingtoneService weiter
 * Startet die Benachrichtigung wenn der pending Intent für den Alarm los geht
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("in the receiver", "at " + LocalDateTime.now());

        String get_your_string = intent.getExtras().getString("extra");

        Log.e("What is the key?", get_your_string);

        Intent service_intent = new Intent(context, RingtoneService.class);

        // gibt extra von TimeSetActivity an den RingtoneService weiter
        service_intent.putExtra("extra", get_your_string);
        // Benachrichtigung wird ausgegeben sobald der Weckruf startet
        if (get_your_string.equals("yes")) {
            Variables.notifications.setNotification(Variables.timeSetString);
        }
        context.startService(service_intent);
    }
}
