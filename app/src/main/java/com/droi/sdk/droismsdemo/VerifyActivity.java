package com.droi.sdk.droismsdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.droi.sdk.sms.DroiSmsFragment;
import com.droi.sdk.sms.OnDroiSmsFragmentInteractionListener;

public class VerifyActivity extends FragmentActivity implements OnDroiSmsFragmentInteractionListener {

    Fragment smsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.droi_example_verify_activity);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        smsFragment = fm.findFragmentById(R.id.main_container);
        if (smsFragment == null) {
            smsFragment = DroiSmsFragment.newInstance("template1");
            transaction.replace(R.id.main_container, smsFragment);
            transaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(String s, String s1, boolean b) {
        if (b) {
            Toast.makeText(this, R.string.droi_sms_verify_success, Toast.LENGTH_SHORT).show();
            //处理国家码，手机号码。进行保存、跳转之类的操作
            this.finish();
        }
    }
}
