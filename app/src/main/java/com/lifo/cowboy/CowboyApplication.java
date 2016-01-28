package com.lifo.cowboy;

import android.support.multidex.MultiDexApplication;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Benoît Sauvère on 28/01/16.
 */
public class CowboyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }

    @Override
    public void onTerminate() {
        FlowManager.destroy();
        super.onTerminate();
    }
}
