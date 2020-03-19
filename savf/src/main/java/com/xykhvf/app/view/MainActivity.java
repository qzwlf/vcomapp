package com.xykhvf.app.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SyncContext;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.vcom.publiclibrary.utils.DateUtils;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.util.UMSUpdateHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionGen;


public class MainActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.rBtnhome)
    RadioButton rBtnhome;
    @BindView(R.id.rBtnset)
    RadioButton rBtnset;
    @BindView(R.id.main_tab_group)
    RadioGroup mainTabGroup;

    private TabHost mTabHost = null;
    private TabHost.TabSpec mSpec = null;
    private Intent mIntent = null;
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTabHost = getTabHost();
        initTabHost();
        mainTabGroup.setOnCheckedChangeListener(this);
        requestPermissions();
    }

    void initTabHost() {
        mIntent = new Intent(this, BuildingActivity.class);
        mSpec = mTabHost.newTabSpec("查询缴费").setIndicator("查询缴费").setContent(mIntent);
        mTabHost.addTab(mSpec);


        mIntent = new Intent(this, ReportActivity.class);
        mSpec = mTabHost.newTabSpec("统  计").setIndicator("统  计").setContent(mIntent);
        mTabHost.addTab(mSpec);
    }

    private void requestPermissions() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.NFC
                ).request();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rBtnhome:
                rBtnhome.setChecked(true);
                rBtnset.setChecked(false);
                mTabHost.setCurrentTabByTag("查询缴费");
                break;
            case R.id.rBtnset:
                rBtnhome.setChecked(false);
                rBtnset.setChecked(true);
                mTabHost.setCurrentTabByTag("统  计");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                SApplication.appExit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String lastCheckTime = SharedPreferencesUtils.getValue(SysEnv.context, Constants.LAST_UPDATE);
        String updateTime = "";//DateUtils.getNow(DateUtils.FORMAT_SHORT);
        if (!lastCheckTime.equals(updateTime)) {
            checkUpdate();
            SharedPreferencesUtils.putValue(SysEnv.context, Constants.LAST_UPDATE, updateTime);
        }
    }

    /**
     * 检查是否需要升级
     */
    public void checkUpdate() {
        UMSUpdateHelper.isNeedUpdate(this, new UMSUpdateHelper.UpdateStateCallback() {
            @Override
            public void onSuccess(String info) {
                Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
                downloadFile();
            }

            @Override
            public void onFail(String info) {
                Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStateChange(String stateInfo) {
            }

            @Override
            public void onProgress(int i) {
            }
        });
    }

    private void downloadFile() {
        ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle(SysEnv.context.getText(R.string.app_name));
        mProgressDialog.setMessage("正在下载新版本，请稍候...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        UMSUpdateHelper.downloadFile(this, new UMSUpdateHelper.UpdateStateCallback() {
            @Override
            public void onSuccess(String info) {
                mProgressDialog.setMessage(info);
                mProgressDialog.dismiss();
            }

            @Override
            public void onFail(String info) {
                mProgressDialog.setMessage(info);
                mProgressDialog.dismiss();
            }

            @Override
            public void onStateChange(String stateInfo) {
                mProgressDialog.setMessage(stateInfo);
            }

            @Override
            public void onProgress(int i) {
                mProgressDialog.setProgress(Math.round((i * 100)));
            }
        });
    }
}
