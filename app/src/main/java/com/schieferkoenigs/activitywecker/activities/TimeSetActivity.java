package com.schieferkoenigs.activitywecker.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.schieferkoenigs.activitywecker.timeset_util.AlarmReceiver;
import com.schieferkoenigs.activitywecker.R;
import com.schieferkoenigs.activitywecker.util.Constants;
import com.schieferkoenigs.activitywecker.util.MyFiles;
import com.schieferkoenigs.activitywecker.util.Variables;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static android.app.AlarmManager.INTERVAL_DAY;


public class TimeSetActivity extends AppCompatActivity {
    TimePicker alarmTimePicker;
    AlarmManager alarmManager;
    TextView alarmText;
    Context context;
    PendingIntent pending_intent;

    /**
     * Weist den Buttons ihre Funktion zu, sowie initialisiert die benutzen Widgets und Services
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeset_main);
        //Initialisierung des Buttons zum Beenden der Activity
        FloatingActionButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());
        this.context = this;
        // initialize stuff
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmTimePicker = findViewById(R.id.alarmTimePicker);
        alarmTimePicker.setIs24HourView(true);
        alarmText = findViewById(R.id.alarmText);
        Button start_alarm = findViewById(R.id.start_alarm);
        Button stop_alarm = findViewById(R.id.stop_alarm);
        Spinner ringtoneSpinner = findViewById(R.id.ringtoneSpinner);

        // create intent for alarm_reciever
        Intent my_intent = new Intent(this.context, AlarmReceiver.class);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            /**
             * Übergibt die gesetzte Uhrzeit sowie den ausgewählten klingelton als String
             * an deren Files
             * Erstellt das pending intent für den Alarm
             * @param view
             */
            @Override
            public void onClick(View view) {

                int hour = alarmTimePicker.getHour();
                int minute = alarmTimePicker.getMinute();

                LocalDateTime wakeUpTime = LocalDateTime.now().withHour(hour).withMinute(minute).withSecond(0);

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);
                Variables.hours = hour;
                Variables.minutes = minute;
                String setTimeString = "Alarm set to: " + hour_string + ":" + (minute < 10 ? "0" : "") + minute_string;
                Context context = getApplicationContext();
                //erzeugung der SaveFile mit dem String inhalt von setTimeString

                MyFiles.saveTime(context, setTimeString);
                Variables.timeSetString = setTimeString;
                alarmText.setText(setTimeString);

                //gibt Wecker an ob ein Alarm gesetzt ist
                my_intent.putExtra("extra", "yes");

                //Pending intent mit der eingestellten Zeit wird erzeugt
                pending_intent = PendingIntent.getBroadcast(TimeSetActivity.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Variables.pending_intent = pending_intent;

                //Alarm manager wird gestellt
                long timeInMillis = wakeUpTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, INTERVAL_DAY, pending_intent);
                Variables.alarmManager = alarmManager;
                String selectedItem = ringtoneSpinner.getSelectedItem().toString();
                MyFiles.saveRingtone(context, selectedItem);

            }
        });

        stop_alarm.setOnClickListener(new View.OnClickListener() {
            /**
             * Auf Knopfdruck wird die bisher gesetzte Zeit wieder auf 0 zurückgesetzt, sowie die angelegte File gelöscht
             * @param v
             */
            @Override
            public void onClick(View v) {
                alarmText.setText("Alarm off!");
                Variables.hours = 0;
                Variables.minutes = 0;
                if (pending_intent != null) {
                    alarmManager.cancel(pending_intent);
                }
                Context context = getApplicationContext();
                File path = context.getFilesDir();
                File file = new File(path, Constants.TIME_FILE_NAME);
                file.delete();
                Variables.timeSetString = Constants.TIME_DEFAULT_TEXT;
                //Signal an den Wecker, dass der Alarm gelöscht wurde
                my_intent.putExtra("extra", "no");

                sendBroadcast(my_intent);
            }
        });
        //hier wird dem ringtoneSpinner die array liste zugewiesen
        Log.i("LocalService", "Before Spinner");
        ringtoneSpinner.setAdapter(new ArrayAdapter<>(context
                , android.R.layout.simple_list_item_1, MyFiles.getRingtonesList(this)));
    }

    /**
     * startet falls der Alarm laeuft die StepDetectorActivity
     */
    @Override
    public void onResume() {
        super.onResume();
        //Automatischer Activity Wechsel zu dem StepCounter wenn die Musik laueft/Alarm aktiv ist
        if (Variables.mediaSong != null && Variables.mediaSong.isPlaying()) {
            startActivity(new Intent(TimeSetActivity.this, StepDetectorActivity.class));
        }
    }
}


