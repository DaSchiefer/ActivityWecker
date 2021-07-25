package com.schieferkoenigs.activitywecker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.schieferkoenigs.activitywecker.R;
import com.schieferkoenigs.activitywecker.listener.StepDetectorListener;
import com.schieferkoenigs.activitywecker.util.Constants;
import com.schieferkoenigs.activitywecker.util.Variables;



public class StepDetectorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private StepDetectorListener stepDetectorListener = null;

    /**Weißt dem Button seine Funktion zu
     * startet den Sensor Manager sowie den StepDetectorListener Konstruktor
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("StepDetectorActivity", "onCreate: ENTER ");

        Variables.counter = 0;
        //Initialisieren
        setContentView(R.layout.stepdetector_main);
        TextView amountSteps = findViewById(R.id.amountSteps);
        TextView neededSteps = findViewById(R.id.neededSteps);
        neededSteps.setText("Walk " + Constants.STEPS + " Steps to stop the Alarm!");
        super.onCreate(savedInstanceState);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Auf Knopfdruck wird man wieder auf die Home Activity gebracht, wenn die benötigen
             * Steps bereits erreicht wurden
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (Variables.counter == 1) {
                    startActivity(new Intent(StepDetectorActivity.this, HomeActivity.class));
                } else {
                    Snackbar.make(v, "You haven't reached the needed steps yet.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        stepDetectorListener = new StepDetectorListener(Variables.alarmManager, amountSteps);
    }

    @Override
    protected void onResume() {

        // Aufruf des StepDetectorListener zum Schrittzählen
        Log.i("StepDetectorActivity", "onResume: ENTER ");
        super.onResume();


        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            sensorManager.registerListener(stepDetectorListener, countSensor, SensorManager.SENSOR_DELAY_UI);

        } else {
            Toast.makeText(this, "Sensor Not Found", Toast.LENGTH_SHORT).show();
        }

    }


}
