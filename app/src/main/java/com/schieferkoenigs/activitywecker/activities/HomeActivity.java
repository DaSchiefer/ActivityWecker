package com.schieferkoenigs.activitywecker.activities;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import com.schieferkoenigs.activitywecker.R;
import com.schieferkoenigs.activitywecker.util.MyFiles;
import com.schieferkoenigs.activitywecker.util.Notifications;
import com.schieferkoenigs.activitywecker.util.Variables;


public class HomeActivity extends AppCompatActivity {

    /**
     * Button funktionen werden gesetzt, File mit der Weckzeit wird ausgelesen falls vorhanden
     * notification channel wird aufgerufen
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.home_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialisierung des Buttons zum wechsel zu der TimeSet Activity
        FloatingActionButton changeButton = findViewById(R.id.changeButton);
        changeButton.setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, TimeSetActivity.class)));

        TextView selectedTime = findViewById(R.id.selectedTime);
        // Lesen der SpeicherFile für die String Ausgabe der Weckzeit
        Variables.timeSetString = MyFiles.getTimeFromFile(getApplicationContext());
        selectedTime.setText(Variables.timeSetString);

        //der Notification Channel wird erzeugt und in der Variablen Klasse gespeichert
        Notifications notifications = new Notifications(this.getBaseContext());
        notifications.createNotificationChannel();
        Variables.notifications = notifications;
    }

    /**
     * Aktualisiert die Weckzeitausgabe
     * und startet falls der Alarm laeuft die StepDetectorActivity
     */
    @Override
    public void onResume() {
        super.onResume();
        TextView selectedTime = findViewById(R.id.selectedTime);
        // Lesen der SpeicherFile für die String Ausgabe der Weckzeit
        selectedTime.setText(Variables.timeSetString);
        //Automatischer Activity Wechsel zu dem StepCounter wenn die Musik laueft/Alarm aktiv ist
        if (Variables.mediaSong != null && Variables.mediaSong.isPlaying()) {
            startActivity(new Intent(HomeActivity.this, StepDetectorActivity.class));
        }
    }

    public void onPause() {
        super.onPause();
    }
}
