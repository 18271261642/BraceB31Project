package com.brace.android.b31.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.adapter.HeartDetailAdapter;
import com.brace.android.b31.bean.BraceCommB31Db;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.bean.BraceHalfHeartBean;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.widget.BraceCusHeartView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 心率详细页面
 * Created by Admin
 * Date 2019/11/8
 */
public class HeartDetailActivity extends BaseActivity {

    @BindView(R.id.commentackImg)
    ImageView commentackImg;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.rateCurrdateTv)
    TextView rateCurrdateTv;
    @BindView(R.id.cusHeartDetailView)
    BraceCusHeartView cusHeartDetailView;
    @BindView(R.id.heartDetailRecyclerView)
    RecyclerView heartDetailRecyclerView;

    private Gson gson = new Gson();

    private String currDay = BraceUtils.getCurrentDate();

    private  List<BraceHalfHeartBean> heartResultList;
    private HeartDetailAdapter heartDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_detail_layout);
        ButterKnife.bind(this);


        initViews();

        findDataForDay(currDay);

    }

    //查询数据库中的数据
    private void findDataForDay(String currentDate) {
        rateCurrdateTv.setText(currentDate);
        heartResultList.clear();
        try {
            String mac = BaseApplication.getBaseApplication().getBleMac();
            if (mac == null)
                return;
            List<BraceCommB31Db> heartList = BraceCommDbInstance.getBraceCommDbInstance().findSavedDataForType(mac, currentDate, Constant.DB_TYPE_HEART);
            if (heartList == null) {
                cusHeartDetailView.setCanvasBeanLin(true);
                cusHeartDetailView.setRateDataList(new ArrayList<Integer>());
                heartDetailAdapter.notifyDataSetChanged();
                return;
            }
            List<BraceHalfHeartBean> htList = gson.fromJson(heartList.get(0)
                    .getDataSourceStr(), new TypeToken<List<BraceHalfHeartBean>>() {
            }.getType());

            Log.e("心率","----------htList="+gson.toJson(htList));


            heartResultList.addAll(htList);
            heartDetailAdapter.notifyDataSetChanged();

            List<Integer> heartLt = new ArrayList<>();
            Map<String, Object> timeMap = BraceUtils.setHalfDateMap();
            for (BraceHalfHeartBean bHeart : htList) {
                timeMap.put(bHeart.getTime().getColck(), bHeart.getRateValue());
            }

            Set set = timeMap.keySet();
            Object[] objects = set.toArray();
            if (objects == null)
                return;
            Arrays.sort(objects);
            for (Object ob : objects) {
                heartLt.add(Integer.valueOf(timeMap.get(ob) + ""));
            }
            cusHeartDetailView.setCanvasBeanLin(true);
            cusHeartDetailView.setRateDataList(heartLt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText("心率详情");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        heartDetailRecyclerView.setLayoutManager(linearLayoutManager);
        heartDetailRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        heartResultList = new ArrayList<>();
        heartDetailAdapter = new HeartDetailAdapter(HeartDetailActivity.this,heartResultList,R.layout.item_sport_healty_data_layout);
        heartDetailRecyclerView.setAdapter(heartDetailAdapter);

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
        findDataForDay(currDay);
    }
}
