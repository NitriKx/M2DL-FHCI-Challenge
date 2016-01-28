package com.lifo.cowboy.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Stav on 28/01/2016.
 */
public class LuminositeService {

    private static final float seuil = 0.5f;

    private static boolean estDansLaPoche = false;


    public boolean estDansLaPoche(Context context) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor capteurLumiere = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        mSensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float lux = event.values[0];
                if (lux < seuil) {
                    estDansLaPoche = true;
                } else {
                    estDansLaPoche = false;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, capteurLumiere, SensorManager.SENSOR_DELAY_NORMAL);

        return estDansLaPoche;
    }

    //
    //   SINGLETON
    //

    private static final LuminositeService __instance = new LuminositeService();

    private LuminositeService() {}

    public static LuminositeService getInstance() {
        return __instance;
    }
}
