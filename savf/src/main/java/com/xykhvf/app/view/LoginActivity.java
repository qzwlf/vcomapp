package com.xykhvf.app.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.OnServiceStatusListener;
import com.vcom.publiclibrary.utils.DateUtils;
import com.vcom.publiclibrary.utils.NetworkUtils;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.StringUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.model.AreaModel;
import com.xykhvf.app.model.PrintModel;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.response.Dispense;
import com.xykhvf.app.model.response.ResLoginModel;
import com.xykhvf.app.presenter.LoginPresenter;
import com.xykhvf.app.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionGen;


public class LoginActivity extends Activity implements ILoginView {

    @BindView(R.id.txtLoginName)
    EditText txtLoginName;
    @BindView(R.id.txtLoginPwd)
    EditText txtLoginPwd;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvVerson)
    TextView tvVerson;
    @BindView(R.id.spinnerDisp)
    Spinner spinnerDisp;
    private LoginPresenter mPresenter = null;
    private long mExitTime = 0;
    private static Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.tvVerson.setText("版本号:" + SysEnv.getVersion());
        requestPermissions();
        mPresenter = new LoginPresenter(this, this);
        //SharedPreferencesUtils.putValue(SysEnv.context, Constants.URI, "https://xykjvf.com");
        //UpdateUtils.init(this);
        //UpdateUtils.checkUpdateOkHttp();
        mPresenter.getUri();
    }


    @Override
    public void setLoginName(String text) {
        this.txtLoginName.setText(text);
    }

    @Override
    public void setLoginPwd(String text) {
        this.txtLoginPwd.setText(text);
    }

    @Override
    public void setList(List<Dispense> list) {
        SharedPreferencesUtils.putValue(SysEnv.context, Constants.URI, list.get(0).getUri());
        SharedPreferencesUtils.putValue(SysEnv.context, Constants.MCH_NAME, list.get(0).getName());
        SharedPreferencesUtils.putValue(SysEnv.context, Constants.PHOTO, list.get(0).getLogo_url());
        List<Map<String, String>> mapList = new ArrayList<>();
        for (Dispense model : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", model.getUri());
            map.put("name", model.getName());
            map.put("logo", model.getLogo_url());
            mapList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, mapList, R.layout.layout_area_item, new String[]{"id", "name", "logo"},
                new int[]{R.id.tv_item_id, R.id.tv_item_name, R.id.tv_item_logo});

        spinnerDisp.setAdapter(adapter);
        spinnerDisp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = view.findViewById(R.id.tv_item_id);
                TextView logoView = view.findViewById(R.id.tv_item_logo);
                TextView nameView = view.findViewById(R.id.tv_item_name);
                String uri = idView.getText().toString();
                String logo = logoView.getText().toString();
                SharedPreferencesUtils.putValue(SysEnv.context, Constants.URI, uri);
                SharedPreferencesUtils.putValue(SysEnv.context, Constants.PHOTO, logo);
                SharedPreferencesUtils.putValue(SysEnv.context, Constants.MCH_NAME, nameView.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void setVersion(String version) {

    }

    @Override
    public void loginResult(Result<ResLoginModel> result) {
        if (result != null) {
            if (result.getData() != null) {
                SysEnv.SETTING_MODEL.setLogin(true);
                SysEnv.SETTING_MODEL.setSessionKey(result.getData().getToken());
                SysEnv.SETTING_MODEL.setUserId(result.getData().getUser_data().getLogin_id());
                SysEnv.SETTING_MODEL.setLogUri(result.getData().getUser_data().getLog_uri());
                SysEnv.SETTING_MODEL.setExpiresn(result.getData().getExpires_in());
                SharedPreferencesUtils.putValue(SysEnv.context, Constants.TOKEN, result.getData().getToken());
                Factory.getIAreaDaoInstance().save(result.getData().getUser_data().getAreas());
                //Factory.getIAreaDaoInstance().obsSave(result.getData().getUser_data().getAreas());
                startActivity(new Intent(this, MainActivity.class));
            }
            Toast.makeText(this, result.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "登录失败，接口异常", Toast.LENGTH_LONG).show();
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
    protected void onDestroy() {
        super.onDestroy();
        setLoginName("");
        setLoginPwd("");
    }

    private void requestPermissions() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.INTERNET,
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

    @OnClick(R.id.btnLogin)
    public void onViewClicked() {
        String name = txtLoginName.getText().toString().trim();
        String pwd = txtLoginPwd.getText().toString().trim();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入用户名及密码", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isAvailable = false;
        try {
            isAvailable = NetworkUtils.getInstance().init(this).isAvailable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isAvailable) {
            mPresenter.Login(name, pwd);
        } else {
            Toast.makeText(this, "当前网络不可用，请检查网络连接！", Toast.LENGTH_LONG).show();
        }
    }
}
