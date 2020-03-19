package com.xykhvf.app.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ums.AppHelper;
import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.model.PayModel;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.request.ReqPayModel;
import com.xykhvf.app.model.response.ResCheckPay;
import com.xykhvf.app.model.response.ResSubmitPay;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.view.IPayView;
import com.xykhvf.app.view.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.Call;

public class PayPresenter {
    private static final String TAG = PayPresenter.class.getName();
    private static final String APP_ID = "64c60a69396b4a64bcc45fc329e9f29e";
    private PayActivity mActivity = null;
    private IPayView mPayView = null;
    private Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public PayPresenter(PayActivity activity, IPayView view) {
        mActivity = activity;
        mPayView = view;
    }

    public void checkPay(String ids) {
        Factory.getIApiDaoInstance().checkPay(ids, SharedPreferencesUtils.getValue(SysEnv.context, Constants.TOKEN, ""), new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                Result<ResCheckPay> result = mGson.fromJson(response, new TypeToken<Result<ResCheckPay>>() {
                }.getType());
                if (result.getCode() == 0 && result.getData() != null) {
                    mPayView.setPayInfo(result.getData());
                }
            }
        });
    }

    public void scanPay(String price, String orderNo) {
        PayModel model = new PayModel();
        model.setAppId(APP_ID);
        model.setExtOrderNo(orderNo);
        model.setExtBillNo(orderNo);
        JSONObject json = new JSONObject();
        try {
            json.put("appId", APP_ID);//appId
            json.put("amt", price);
            json.put("tradeType", "all");
            json.put("isNeedPrintReceipt", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppHelper.callTrans(mActivity, "POS 通", "扫一扫", json);

    }

    public void posPay(String price, String orderNo) {
        PayModel model = new PayModel();
        model.setAppId(APP_ID);
        model.setExtOrderNo(orderNo);
        model.setExtBillNo(orderNo);
        JSONObject json = new JSONObject();
        try {
            json.put("appId", APP_ID);//appId
            json.put("amt", price);
            json.put("isNeedPrintReceipt", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppHelper.callTrans(mActivity, "银行卡收款", "消费", json);

    }

    public void paySubmit(ReqPayModel res) {
        Factory.getIApiDaoInstance().submitPay(res, SharedPreferencesUtils.getValue(SysEnv.context, Constants.TOKEN, ""), new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i(TAG, "onError: " + e.getMessage());
                Result r = mGson.fromJson(e.getMessage(), Result.class);
                ResSubmitPay result = new ResSubmitPay();
                result.setCode(r.getCode());
                result.setMsg(r.getMsg());
                mPayView.payResult(result);
            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                ResSubmitPay result = mGson.fromJson(response, ResSubmitPay.class);
                mPayView.payResult(result);
            }
        });
    }
}
