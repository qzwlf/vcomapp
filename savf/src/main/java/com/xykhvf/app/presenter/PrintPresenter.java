package com.xykhvf.app.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.printer.OnPrintResultListener;
import com.ums.upos.sdk.printer.PrinterManager;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.OnServiceStatusListener;
import com.vcom.publiclibrary.util.okhttp.OkHttpUtils;
import com.vcom.publiclibrary.util.okhttp.callback.BitmapCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.R;
import com.xykhvf.app.model.PrintModel;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.util.View2Img;

import okhttp3.Call;

public class PrintPresenter {
    private Activity mActivity = null;
    private TextView tvOrderNo, tvMchName, tvRoomName, tvFeeDesc, tvPayTime, tvPayType, tvPayAmount;
    private ImageView imageView;
    private View mPrintView;

    public PrintPresenter(Activity activity) {
        mActivity = activity;
        mPrintView = mActivity.getLayoutInflater().inflate(R.layout.layout_print, null);
    }

    public void print(PrintModel model) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        imageView = mPrintView.findViewById(R.id.image_logo);
        tvOrderNo = mPrintView.findViewById(R.id.tv_print_num);
        tvMchName = mPrintView.findViewById(R.id.tv_print_mch_name);
        tvRoomName = mPrintView.findViewById(R.id.tv_print_room);
        tvFeeDesc = mPrintView.findViewById(R.id.tv_print_fee_desc);
        tvPayTime = mPrintView.findViewById(R.id.tv_print_time);
        tvPayType = mPrintView.findViewById(R.id.tv_print_type);
        tvPayAmount = mPrintView.findViewById(R.id.tv_print_fee);
        tvOrderNo.setText("订单号：" + model.getOrderNo());
        tvMchName.setText("商户名称：" + model.getMchName());
        tvRoomName.setText("房    　号：" + model.getRoomName());
        tvFeeDesc.setText("费用信息：\r\n" + model.getFeeDetail());
        tvPayTime.setText("缴费时间：" + model.getPayTime());
        tvPayType.setText("缴费方式：" + model.getPayType());
        tvPayAmount.setText("缴费金额：" + model.getAmount() + "元");
        OkHttpUtils.get().url(SharedPreferencesUtils.getValue(SysEnv.context, Constants.PHOTO)).build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                Bitmap bitmap = View2Img.viewToImage(mPrintView, width, height);
                startPrint(bitmap);
            }

            @Override
            public void onResponse(Bitmap response) {
                if (response != null && !response.equals(""))
                    imageView.setImageBitmap(response);
                Bitmap bitmap = View2Img.viewToImage(mPrintView, width, height);

                startPrint(bitmap);
            }
        });
    }

    private void startPrint(Bitmap bitmap) {
        try {
            BaseSystemManager.getInstance().deviceServiceLogin(
                    mActivity, null, "99999998",//设备ID，生产找后台配置
                    new OnServiceStatusListener() {
                        @Override
                        public void onStatus(int arg0) {//arg0可见ServiceResult.java
                            if (0 == arg0 || 2 == arg0 || 100 == arg0) {//0：登录成功，有相关参数；2：登录成功，无相关参数；100：重复登录。
                                try {
                                    PrinterManager printer = new PrinterManager();
                                    printer.initPrinter();
                                    printer.setBitmap(bitmap);
                                    printer.startPrint(new OnPrintResultListener() {

                                        @Override
                                        public void onPrintResult(int arg0) {//arg0可见ServiceResult.java
                                            // TODO Auto-generated method stub
                                            //登出，以免占用U架构服务
                                            try {
                                                BaseSystemManager.getInstance().deviceServiceLogout();
                                            } catch (SdkException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
