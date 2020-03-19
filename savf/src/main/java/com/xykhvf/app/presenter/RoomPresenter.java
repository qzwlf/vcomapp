package com.xykhvf.app.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.model.AreaModel;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.response.House;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.view.IRoomView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

public class RoomPresenter {

    private static final String TAG = BuildingPresenter.class.getName();
    private Context mContext = null;
    private IRoomView mRoomView = null;
    private Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public RoomPresenter(Context context, IRoomView view) {
        mContext = context;
        mRoomView = view;
    }

    public void getRooms(long id) {
        Factory.getIApiDaoInstance().getHouses(id, SharedPreferencesUtils.getValue(SysEnv.context, Constants.TOKEN, "")
                , new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, "onError: " + e.getMessage(), e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Result<List<House>> result = mGson.fromJson(response, new TypeToken<Result<List<House>>>() {
                        }.getType());
                        if (result.getCode() == 0) {
                            Factory.getIHouseDaoInstance().update(result.getData());
                            mRoomView.setList(result.getData());
                        }
                    }
                });
    }

    public void getRoomsByNo(String no) {
        Factory.getIHouseDaoInstance().findNumbere(no)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取食品列表的请求结果
                .subscribe(houses -> {
                    mRoomView.setList(houses);
                });
    }
}
