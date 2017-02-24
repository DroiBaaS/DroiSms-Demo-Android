package com.droi.sdk.droismsdemo;

import android.app.Application;

import com.droi.sdk.core.Core;
import com.droi.sdk.sms.DroiSms;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Core.initialize(this);
        DroiSms.initialize(this);
        //DroiUpdate.setUpdateOnlyWifi(true);
    }
}
