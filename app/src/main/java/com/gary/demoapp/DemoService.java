package com.gary.demoapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by Gary on 2015/8/3.
 */
public class DemoService extends Service{
    static final String ACTION_UPDATE = "om.gary.demoapp.widget.clock.action.UPDATE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
