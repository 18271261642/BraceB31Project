package com.brace.android.b31.activity.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.aigestudio.wheelpicker.widgets.ProfessionPick;
import com.aigestudio.wheelpicker.widgets.ProvincePick;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.ble.BleConnStatus;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.ILongSeatDataListener;
import com.veepoo.protocol.model.datas.LongSeatData;
import com.veepoo.protocol.model.enums.ELongSeatStatus;
import com.veepoo.protocol.model.settings.LongSeatSetting;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 久坐设置
 * Created by Admin
 * Date 2019/11/19
 */
public class LongSitRemendActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.commentackImg)
    ImageView commentackImg;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.longSitToggleBtn)
    ToggleButton longSitToggleBtn;
    @BindView(R.id.showB31LongSitStartTv)
    TextView showB31LongSitStartTv;
    @BindView(R.id.showB30LongSitEndTv)
    TextView showB30LongSitEndTv;
    @BindView(R.id.showB31LongSitTv)
    TextView showB31LongSitTv;


    //


    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;
    private HashMap<String, ArrayList<String>> minuteMapList;
    ArrayList<String> longTimeLit;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_sit_layout);
        ButterKnife.bind(this);


        initViews();

        initData();

        readLongSitData();

    }

    private void initData() {
        hourList = new ArrayList<>();

        minuteList = new ArrayList<>();

        minuteMapList = new HashMap<>();


        longTimeLit = new ArrayList<>();


        for (int i = 30; i <= 240; i++) {
            longTimeLit.add(i + "");
        }

        for (int i = 0; i < 60; i++) {
            if (i == 0) {
                minuteList.add("00");
            } else if (i < 10) {
                minuteList.add("0" + i);
            } else {
                minuteList.add(i + "");
            }
        }
        for (int i = 8; i <= 18; i++) {
            if (i < 10) {
                hourList.add("0" + i + "");
                minuteMapList.put("0" + i + "", minuteList);

            } else {
                hourList.add(i + "");
                minuteMapList.put(i + "", minuteList);

            }
        }

    }


    private void readLongSitData() {
        BaseApplication.getVPOperateManager().readLongSeat(iBleWriteResponse, new ILongSeatDataListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLongSeatDataChange(LongSeatData longSeatData) {
                int startHour = longSeatData.getStartHour();
                int startMin = longSeatData.getStartMinute();

                int endHour = longSeatData.getEndHour();
                int endMine = longSeatData.getEndMinute();

                //开始时间
                showB31LongSitStartTv.setText((startHour<=9?0+""+startHour : startHour) + ":" + (startMin<=9?0+""+startMin : startMin));
                //结束时间
                showB30LongSitEndTv.setText((endHour<=9?0+""+endHour : endHour) + ":" + (endMine<=9?0+""+endMine : endMine));
                //时长
                showB31LongSitTv.setText(longSeatData.getThreshold()+"mine");
                longSitToggleBtn.setChecked(longSeatData.isOpen());

            }
        });
    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText("久坐提醒");
        longSitToggleBtn.setOnCheckedChangeListener(this);
    }


    @OnClick({R.id.commentackImg, R.id.b31LongSitStartRel,
            R.id.b31LongSitEndRel, R.id.b31LongSitTimeRel,
            R.id.b31LongSitSaveBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentackImg:
                finish();
                break;
            case R.id.b31LongSitStartRel:
                chooseStartEndDate(0);
                break;
            case R.id.b31LongSitEndRel:
                chooseStartEndDate(1);
                break;
            case R.id.b31LongSitTimeRel:
                chooseLongTime();
                break;
            case R.id.b31LongSitSaveBtn:
                saveLongSitData();
                break;
        }
    }



    private void saveLongSitData() {
        if (BleConnStatus.CONNDEVICENAME != null) {
            String startD = showB31LongSitStartTv.getText().toString().trim();
            int startHour = Integer.valueOf(StringUtils.substringBefore(startD, ":").trim());
            int startMine = Integer.valueOf(StringUtils.substringAfter(startD, ":").trim());
            String endD = showB30LongSitEndTv.getText().toString().trim();
            int endHour = Integer.valueOf(StringUtils.substringBefore(endD, ":").trim());
            int endMine = Integer.valueOf(StringUtils.substringAfter(endD, ":").trim());
            //时长
            String longD = showB31LongSitTv.getText().toString().trim();
            int longTime = Integer.valueOf(StringUtils.substringBefore(longD, "min").trim());
            BaseApplication.getVPOperateManager().settingLongSeat(iBleWriteResponse, new LongSeatSetting(startHour, startMine, endHour, endMine, longTime, longSitToggleBtn.isChecked()), new ILongSeatDataListener() {
                @Override
                public void onLongSeatDataChange(LongSeatData longSeatData) {
                    Log.e("久坐", "----longSeatData=" + longSeatData.toString());
                    if (longSeatData.getStatus() == ELongSeatStatus.OPEN_SUCCESS || longSeatData.getStatus() == ELongSeatStatus.CLOSE_SUCCESS) {
                        Toast.makeText(LongSitRemendActivity.this,  getResources().getString(R.string.settings_success), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
            });
        }
    }



    //选择时间
    private void chooseStartEndDate(final int code) {
        ProvincePick starPopWin = new ProvincePick.Builder(LongSitRemendActivity.this, new ProvincePick.OnProCityPickedListener() {
            @Override
            public void onProCityPickCompleted(String province, String city, String dateDesc) {
                if (code == 0) {  //开始时间
                    showB31LongSitStartTv.setText(province + ":" + city);
                }
                else if (code == 1) {    //结束时间
                    showB30LongSitEndTv.setText(province + ":" + city);

                }

            }
        }).textConfirm(getResources().getString(R.string.confirm)) //text of confirm button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .setProvinceList(hourList) //min year in loop
                .setCityList(minuteMapList) // max year in loop
                .build();
        starPopWin.showPopWin(LongSitRemendActivity.this);
    }


    //设置时长
    private void chooseLongTime() {
        ProfessionPick professionPick = new ProfessionPick.Builder(LongSitRemendActivity.this, new ProfessionPick.OnProCityPickedListener() {
            @Override
            public void onProCityPickCompleted(String profession) {
                showB31LongSitTv.setText(profession + "min");
            }
        }).textConfirm(getResources().getString(R.string.confirm)) //text of confirm button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .setProvinceList(longTimeLit) //min year in loop
                .dateChose("30") // date chose when init popwindow
                .build();
        professionPick.showPopWin(LongSitRemendActivity.this);
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isPressed())
            return;
        longSitToggleBtn.setChecked(isChecked);
    }
}
