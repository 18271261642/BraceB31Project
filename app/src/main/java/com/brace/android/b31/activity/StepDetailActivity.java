package com.brace.android.b31.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.adapter.SportDetailAdapter;
import com.brace.android.b31.bean.BraceCommB31Db;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.bean.BraceHalfHourSportBean;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.widget.BraceCusSleepView;
import com.brace.android.b31.view.widget.BraceCusStepDetailView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * Date 2019/11/28
 */
public class StepDetailActivity extends BaseActivity {


    @BindView(R.id.commentackImg)
    ImageView commentackImg;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.rateCurrDateLeft)
    ImageView rateCurrDateLeft;
    @BindView(R.id.rateCurrdateTv)
    TextView rateCurrdateTv;
    @BindView(R.id.braceCusSportView)
    BraceCusStepDetailView braceCusSportView;
    @BindView(R.id.sportDetailRecyclerView)
    RecyclerView sportDetailRecyclerView;

    private List<BraceHalfHourSportBean> halfHourSportBeanList;
    private SportDetailAdapter sportDetailAdapter;

    private List<Integer> setSourList;

    private Gson gson = new Gson();

    private String currDay = BraceUtils.getCurrentDate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail_layout);
        ButterKnife.bind(this);

        initViews();

        initData();

        findSportData(currDay);

    }

    private void initData() {
        setSourList = new ArrayList<>();
    }

    private void findSportData(String dayStr) {
        rateCurrdateTv.setText(dayStr);
        String bleMac = BaseApplication.getBaseApplication().getBleMac();
        if(bleMac == null)
            return;
        try {
            setSourList.clear();
            halfHourSportBeanList.clear();
            List<BraceCommB31Db> stepDeDbList = BraceCommDbInstance.getBraceCommDbInstance()
                    .findSavedDataForType(bleMac,dayStr, Constant.DB_TYPE_SPORT);
            if(stepDeDbList == null){
                braceCusSportView.setSourList(setSourList);
                sportDetailAdapter.notifyDataSetChanged();
                return;
            }

            BraceCommB31Db braceCommB31Db = stepDeDbList.get(0);
            String sportStr = braceCommB31Db.getDataSourceStr();
            List<BraceHalfHourSportBean> halfHourSportBeans = gson.fromJson(sportStr,new TypeToken<List<BraceHalfHourSportBean>>(){}.getType());

            halfHourSportBeanList.addAll(halfHourSportBeans);
            sportDetailAdapter.notifyDataSetChanged();


            Map<String,Object> sportMap = BraceUtils.setHalfDateMap();
            for(BraceHalfHourSportBean bs : halfHourSportBeans){
                sportMap.put(bs.getTime().getColck(),bs.getStepValue());
            }
            //遍历map的key
            Set set = sportMap.keySet();
            //转换为数组
            Object[] objects = set.toArray();
            if (objects == null)
                return;
            Arrays.sort(objects);
            for (Object ob : objects) {
                setSourList.add(Integer.valueOf(sportMap.get(ob) + ""));
            }
            braceCusSportView.setSourList(setSourList);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText("运动详情");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sportDetailRecyclerView.setLayoutManager(linearLayoutManager);
        sportDetailRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        halfHourSportBeanList = new ArrayList<>();
        sportDetailAdapter = new SportDetailAdapter(this,halfHourSportBeanList,R.layout.item_sport_healty_data_layout);
        sportDetailRecyclerView.setAdapter(sportDetailAdapter);


    }

    @OnClick({R.id.commentackImg, R.id.rateCurrDateLeft,
            R.id.rateCurrDateRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentackImg:
                finish();
                break;
            case R.id.rateCurrDateLeft:
                changeDayData(true);
                break;
            case R.id.rateCurrDateRight:
                changeDayData(false);
                break;
        }
    }

    /**
     * 根据日期切换数据
     */
    private void changeDayData(boolean left) {
        String date = BraceUtils.obtainAroundDate(currDay, left);
        if (date.equals(currDay) || date.isEmpty()) {
            return;
        }
        currDay = date;
        findSportData(currDay);
    }
}
