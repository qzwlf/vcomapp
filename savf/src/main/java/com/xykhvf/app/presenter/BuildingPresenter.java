package com.xykhvf.app.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.vcom.publiclibrary.utils.WifiLManager;
import com.xykhvf.app.Factory;
import com.xykhvf.app.model.AreaModel;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.view.IBuildingView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

public class BuildingPresenter {
    private static final String TAG = BuildingPresenter.class.getName();
    private Context mContext = null;
    private IBuildingView mBuildingView = null;
    private Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public BuildingPresenter(Context context, IBuildingView view) {
        mContext = context;
        mBuildingView = view;
    }

    public void getBuilding(long id) {
        Factory.getIApiDaoInstance().getBuilding(id, SharedPreferencesUtils.getValue(SysEnv.context, Constants.TOKEN, ""), new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Result<List<AreaModel>> result = mGson.fromJson(response, new TypeToken<Result<List<AreaModel>>>() {
                }.getType());
                if (result.getCode() == 0)
                    mBuildingView.setList(result.getData());
            }
        });
    }

    public void getFirstArea() {
        Factory.getIAreaDaoInstance().findAllByObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ares -> {
                    if (ares != null && ares.size() > 0) {
                        mBuildingView.setAreaName(ares.get(0).getName());
                        getBuilding(ares.get(0).getId());
                        SharedPreferencesUtils.putValue(SysEnv.context, Constants.AREA_ID, ares.get(0).getId().toString());
                        SharedPreferencesUtils.putValue(SysEnv.context, Constants.AREA_NAME, ares.get(0).getName());
                    }
                });
    }

    public void selectArea() {
        Factory.getIAreaDaoInstance().findAllByObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ares -> {
                    mBuildingView.selectArea(ares);
                });
    }

    public void setAreaName(String str) {
        mBuildingView.setAreaName(str);
    }

    public void getMerchat() {
        String mac = WifiLManager.getMac(mContext);
        Factory.getIApiDaoInstance().getMerchat(mac, SharedPreferencesUtils.getValue(SysEnv.context, Constants.TOKEN, "")
                , new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        Result<String> result = mGson.fromJson(response, Result.class);
                        if (result.getCode() == 0) {
                            SharedPreferencesUtils.putValue(SysEnv.context, Constants.MERCHANT, result.getData());
                        }
                    }
                });
    }
}
