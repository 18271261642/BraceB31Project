package com.brace.android.b31.ble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * 接收电话的广播
 * Created by Admin
 * Date 2019/11/29
 */
public class BlePhoneAlertReceiver extends BroadcastReceiver {





    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == null)
            return;
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        if(action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){

        }
    }
}
