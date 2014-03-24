package com.arik.glassCopter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by arik-so on 3/23/14.
 */
public class CopterActivity extends Activity{

    private TextView helloView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Activity has been created! Hello World!");

        // this sets the content by layoutResourceID
        // I have no idea how to get the stupid layoutResourceID
        // this.setContentView(234);

        // just use Google stuff instead
        this.setContentView(new TuggableView(this, R.layout.chopperfield_view));

        this.helloView = (TextView)findViewById(R.id.textView);



        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix    = new float[9];

        SensorManager sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);

        SensorEventListener sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                // beep();

                switch (sensorEvent.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        System.arraycopy(sensorEvent.values, 0, mValuesAccel, 0, 3);
                        break;

                    case Sensor.TYPE_MAGNETIC_FIELD:
                        System.arraycopy(sensorEvent.values, 0, mValuesMagnet, 0, 3);
                        break;
                }


                SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
                SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
                final CharSequence test;
                test = "results: " + mValuesOrientation[0] +" "+mValuesOrientation[1]+ " "+ mValuesOrientation[2];


                helloView.setText("α: "+ Math.round(Math.toDegrees(mValuesOrientation[0])) +"\nβ: " + Math.round(Math.toDegrees(mValuesOrientation[1])) + "\nγ: " + Math.round(Math.toDegrees(mValuesOrientation[2])) );
                // helloView.setText("α: "+ mValuesOrientation[0] +"\nβ: " + mValuesOrientation[1] + "\nγ: " + mValuesOrientation[2] );


                // helloView.setText(test);



                /* if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

                    System.arraycopy(sensorEvent.values, 0, mValuesAccel, 0, 3);
                    helloView.setText("α: "+Math.round(mValuesAccel[0] * 10)/10.0 +"\nβ: " + Math.round(mValuesAccel[1] * 10)/10.0 + "\nγ: " + Math.round(mValuesAccel[2]*10)/10.0);

                } */

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }

        };

        setListeners(sensorManager, sensorEventListener);
    }

    // Register the event listener and sensor type.
    public void setListeners(SensorManager sensorManager, SensorEventListener mEventListener){

        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStart() {
        super.onStart();



        try {
            Thread.sleep(2500);

            beep();

            this.helloView.setText("Arik's text.");
        } catch (InterruptedException e) {
            this.helloView.setText("Thread insomnia.");
        }

    }

    private void beep(){

        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);

    }

}
