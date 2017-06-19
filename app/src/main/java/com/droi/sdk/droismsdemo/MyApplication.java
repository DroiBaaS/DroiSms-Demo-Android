package com.droi.sdk.droismsdemo;

import android.app.Application;

import com.droi.sdk.core.Core;
import com.droi.sdk.sms.DroiSms;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Core.initialize(this);
        // 由于短信需要收费，请使用您自己的appId和apikey
        DroiSms.initialize(this,"您的apikey");
    }
}
