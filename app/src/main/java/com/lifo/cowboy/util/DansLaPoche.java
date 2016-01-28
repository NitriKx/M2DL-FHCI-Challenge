package com.lifo.cowboy.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stav on 28/01/2016.
 */
public class DansLaPoche  implements SensorEventListener {
    private final float seuil;
    private boolean estDansLaPoche;
    private SensorManager mSensorManager;
    private Sensor capteurLumiere;
    private Context context;
    private List<DansLaPocheListener> listeners;

    public DansLaPoche(Context context) {
        this(context, 0.5f);

    }

    public DansLaPoche(Context context, float seuil) {
        this.seuil = seuil;
        estDansLaPoche = false;
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        capteurLumiere = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        listeners = new ArrayList<DansLaPocheListener>();
    }

    public void ajoutListener(DansLaPocheListener listener) {
        listeners.add(listener);

        if (listeners.size() == 1) {
            mSensorManager.registerListener(this,capteurLumiere, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    public void retirerListener(DansLaPocheListener listener) {
        listeners.remove(listener);

        if (listeners.size() == 0) {
            mSensorManager.unregisterListener(this);
        }
    }

    private void prevenirListener() {
        for (DansLaPocheListener listener:listeners
                ) {
            if (estDansLaPoche) {
                listener.misDansLaPoche();
            } else {
                listener.sortiDeLaPoche();
            }
        }
    }

    private boolean majDansLaPoche(boolean bool) {
        boolean variation = false;

        if (estDansLaPoche != bool) {
            variation = true;
        }
        estDansLaPoche = bool;

        return variation;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        if (lux < seuil) {
            if (majDansLaPoche(true)) {
                prevenirListener();
            }
        } else {
            if (majDansLaPoche(false)) {
                prevenirListener();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
