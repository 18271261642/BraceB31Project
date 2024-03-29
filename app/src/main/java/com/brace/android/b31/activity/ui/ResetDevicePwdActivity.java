package com.brace.android.b31.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.utils.SpUtils;
import com.brace.android.b31.utils.ToastUtil;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IPwdDataListener;
import com.veepoo.protocol.model.datas.PwdData;
import com.veepoo.protocol.model.enums.EPwdStatus;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重置设备密码
 * Created by Admin
 * Date 2019/11/25
 */
public class ResetDevicePwdActivity extends BaseActivity {


    @BindView(R.id.commentackImg)
    ImageView commentackImg;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.resetPwdOldPwdEdit)
    EditText resetPwdOldPwdEdit;
    @BindView(R.id.resetPwdNewPwdEdit)
    EditText resetPwdNewPwdEdit;
    @BindView(R.id.resetPwdAgainNewPwdEdit)
    EditText resetPwdAgainNewPwdEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd_layout);
        ButterKnife.bind(this);

        initViews();


    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText("重置设备密码");

    }

    @OnClick({R.id.commentackImg, R.id.resetPwdBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentackImg:
                finish();
                break;
            case R.id.resetPwdBtn:
                if(BleConnStatus.CONNDEVICENAME == null)
                    return;
                String oldPwd = resetPwdOldPwdEdit.getText().toString();
                String newPwd = resetPwdNewPwdEdit.getText().toString();
                String againNewPwd = resetPwdAgainNewPwdEdit.getText().toString();
                if(BraceUtils.isEmpty(oldPwd) || BraceUtils.isEmpty(newPwd) || BraceUtils.isEmpty(againNewPwd))
                    return;
                resetDevicePwd(oldPwd,newPwd,againNewPwd);


                break;
        }
    }

    private void resetDevicePwd(String oldPwd, final String newPwd, String againNewPwd) {
        String pwdStr = (String) SpUtils.getParam(BaseApplication.getBaseApplication(),Constant.DEVICE_PWD_KEY,"0000");
        if(BraceUtils.isEmpty(pwdStr))
            pwdStr = "0000";


        if( !BraceUtils.isNumeric(againNewPwd)){
            ToastUtil.showShort(ResetDevicePwdActivity.this,"请输入数字密码!");
            return;
        }

        if(!pwdStr.equals(oldPwd)){
            ToastUtil.showShort(ResetDevicePwdActivity.this, getResources().getString(R.string.string_old_password_incorrect));
            return;
        }

        if (!newPwd.equals(againNewPwd)){
            ToastUtil.showShort(ResetDevicePwdActivity.this, getResources().getString(R.string.string_two_passwords_are_different));
            return;
        }

        //长度只能等于4位
        if(againNewPwd.length() !=4){
            ToastUtil.showShort(ResetDevicePwdActivity.this, getResources().getString(R.string.input_new_password));
            return;
        }

        BaseApplication.getVPOperateManager().modifyDevicePwd(iBleWriteResponse, new IPwdDataListener() {
            @Override
            public void onPwdDataChange(PwdData pwdData) {
                Log.e("密码", "-----pwdData=" + pwdData.toString());
                if (pwdData.getmStatus() == EPwdStatus.SETTING_SUCCESS) {
                    SpUtils.setParam(ResetDevicePwdActivity.this, Constant.DEVICE_PWD_KEY, newPwd);
                    ToastUtil.showShort(ResetDevicePwdActivity.this, getResources().getString(R.string.string_reset_password_successfully));
                    finish();
                }
            }
        }, againNewPwd);

    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };
}
