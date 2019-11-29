package com.brace.android.b31;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.bean.BraceCommB31Db;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.ble.BleConnDataOperate;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.google.gson.Gson;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
	

    private static final String TAG = "MainActivity";

    String bleMac ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bleMac = BaseApplication.getBaseApplication().getBleMac();

    }


    @OnClick({R.id.getDataBtn, R.id.disBtn,R.id.findDbBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getDataBtn:
                BleConnDataOperate.getBleConnDataOperate().readAllDeviceData(true);
                break;
            case R.id.disBtn:
                SpUtils.setParam(this, Constant.CONN_BLE_MAC,"");
                BleConnDataOperate.getBleConnDataOperate().disBleConn();
                break;
            case R.id.findDbBtn:
                findDbData();
                break;
        }
    }

    private void findDbData() {
        List<BraceCommB31Db> list = BraceCommDbInstance.getBraceCommDbInstance().findSavedDataForType(bleMac,"2019-11-06",Constant.DB_TYPE_SPORT);

        Log.e(TAG,"----------查询数据-="+new Gson().toJson(list));
    }
}
