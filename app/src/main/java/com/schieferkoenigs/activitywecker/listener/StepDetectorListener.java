package com.schieferkoenigs.activitywecker.listener;

import android.app.AlarmManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.TextView;


import com.schieferkoenigs.activitywecker.util.Constants;
import com.schieferkoenigs.activitywecker.util.Variables;


public class StepDetectorListener implements SensorEventListener {

    private int steps = 0;
    private AlarmManager alarmManager;
    private TextView textView;

    /**
     * Durch Aufruf werden die onSensorChanged benötigten Variablen übergeben(Constructor)
     *
     * @param alarmManager
     * @param textView
     */
    public StepDetectorListener(AlarmManager alarmManager, TextView textView) {
        Log.i("StepDetectorListener", "Constructor ");
        this.alarmManager = alarmManager;
        this.textView = textView;
    }


    /**
     * Erhöht den Schrittzähler wenn der Sensor betätigt wird und stoppt gegebenenfalls den Alarm
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("StepDetectorListener", "onSensorChanged ");
        if (steps >= Constants.STEPS) {
            if (alarmManager != null) {
                Variables.mediaSong.stop();
            }
            if (textView != null) {
                Variables.counter = 1;
                textView.setText("Press the Button to get back!");
            }
        } else {
            steps++;
            textView.setText(Integer.toString(steps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}



