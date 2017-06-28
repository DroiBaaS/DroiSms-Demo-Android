package com.droi.sdk.droismsdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.droi.sdk.DroiError;
import com.droi.sdk.core.Core;
import com.droi.sdk.core.SmsCoreHelper;
import com.droi.sdk.sms.DroiSms;
import com.droi.sdk.sms.DroiSmsCallback;
import com.droi.sdk.sms.DroiSmsError;

import java.util.HashMap;

public class MainActivity extends Activity {
    private EditText phone;
    private EditText code;
    private EditText country;
    private Button getCodeButton;
    private Button confirmButton;
    private Context mContext;

    private String phoneNumber = "";
    private String countryCode = "";
    private String codeValue = "";
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.droi_example_activity_main);
        TextView deviceId = (TextView) findViewById(R.id.device_id);
        deviceId.setText(SmsCoreHelper.getDeviceId());
        TextView appId = (TextView) findViewById(R.id.app_id);
        appId.setText(SmsCoreHelper.getAppId());
        TextView channel = (TextView) findViewById(R.id.channel);
        channel.setText(Core.getChannelName(this));

        Button fragmentButton = (Button) findViewById(R.id.fragment);
        final Intent intent = new Intent(this, VerifyActivity.class);
        fragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        Button fragmentButton2 = (Button) findViewById(R.id.fragment2);
        final Intent intent2 = new Intent(this, VerifyActivity2.class);
        fragmentButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });

        getCodeButton = (Button) this.findViewById(R.id.get_code);
        confirmButton = (Button) this.findViewById(R.id.confirm);
        phone = (EditText) this.findViewById(R.id.phone_number);
        code = (EditText) this.findViewById(R.id.pin_code);
        country = (EditText) this.findViewById(R.id.country_code);
        phone.requestFocus();

        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone != null) {
                    phoneNumber = phone.getText().toString();
                    countryCode = country.getText().toString();
                    Log.i(TAG, countryCode + phoneNumber);
                    HashMap<String, String> extra = new HashMap<String, String>();
                    extra.put("name", "wangyan");
                    extra.put("time", "中午");
                    DroiSms.getSMSCodeWithExtraInBackground(countryCode, phoneNumber, "template2", new DroiSmsCallback() {
                        @Override
                        public void onResult(DroiError droiSmsError) {
                            Log.i(TAG, droiSmsError.getCode() + ":" + droiSmsError.getAppendedMessage());
                            if (droiSmsError.isOk()) {
                                CountDownTimerUtils mCountDownTimerUtils;
                                mCountDownTimerUtils = new CountDownTimerUtils(60000, 1000);
                                mCountDownTimerUtils.start();
                                Toast.makeText(mContext, getString(R.string.droi_sms_get_code_success), Toast.LENGTH_SHORT).show();
                            } else {
                                String errorInfo;
                                switch (droiSmsError.getCode()) {
                                    case DroiSmsError.ERROR:
                                        errorInfo = getString(R.string.droi_sms_error);
                                        break;
                                    case DroiSmsError.ILLEGAL_PHONE_NO:
                                        errorInfo = getString(R.string.droi_sms_error_illegal_phone_num);
                                        break;
                                    case DroiSmsError.OVER_LIMIT:
                                        errorInfo = getString(R.string.droi_sms_error_over_limit);
                                        break;
                                    case DroiSmsError.REQ_INTERVAL_TOO_SHORT:
                                        errorInfo = getString(R.string.droi_sms_error_req_interval_to_short);
                                        break;
                                    default:
                                        errorInfo = getString(R.string.droi_sms_error);
                                }
                                Toast.makeText(mContext, errorInfo, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, extra);
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone != null && code != null) {
                    phoneNumber = phone.getText().toString();
                    codeValue = code.getText().toString();
                    DroiSms.verifySMSCodeInBackground(phoneNumber, codeValue, new DroiSmsCallback() {
                        @Override
                        public void onResult(DroiError droiSmsError) {
                            Log.i(TAG, droiSmsError.getCode() + ":" + droiSmsError.getAppendedMessage());
                            if (droiSmsError.isOk()) {
                                Toast.makeText(mContext, getString(R.string.droi_sms_verify_success), Toast.LENGTH_SHORT).show();
                            } else {
                                String errorInfo;
                                switch (droiSmsError.getCode()) {
                                    case DroiSmsError.ERROR:
                                        errorInfo = getString(R.string.droi_sms_error);
                                        break;
                                    case DroiSmsError.ILLEGAL_PHONE_NO:
                                        errorInfo = getString(R.string.droi_sms_error_illegal_phone_num);
                                        break;
                                    case DroiSmsError.SMS_CODE_EMPTY:
                                        errorInfo = getString(R.string.droi_sms_error_code_empty);
                                        break;
                                    case DroiSmsError.SMS_CODE_NOT_MATCH:
                                        errorInfo = getString(R.string.droi_sms_error_code_not_match);
                                        break;
                                    case DroiSmsError.SMS_CODE_OVER_TRIES:
                                        errorInfo = getString(R.string.droi_sms_error_code_retry);
                                        break;
                                    default:
                                        errorInfo = getString(R.string.droi_sms_error);
                                }
                                Toast.makeText(mContext, errorInfo, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private class CountDownTimerUtils extends CountDownTimer {

        public CountDownTimerUtils(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCodeButton.setClickable(false); //设置不可点击
            getCodeButton.setText(mContext.getString(R.string.droi_sms_get_code_again)
                    + "(" + millisUntilFinished / 1000 + ")");
        }

        @Override
        public void onFinish() {
            getCodeButton.setText(mContext.getString(R.string.droi_sms_get_code));
            getCodeButton.setClickable(true);//重新获得点击
        }
    }
}
