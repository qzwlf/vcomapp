package com.xykhvf.app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.ums.AppHelper;
import com.vcom.publiclibrary.utils.ArrayUtils;
import com.vcom.publiclibrary.utils.DateUtils;
import com.vcom.publiclibrary.utils.ListUtils;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.StringUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.model.PayResult;
import com.xykhvf.app.model.PrintModel;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.request.ReqPayModel;
import com.xykhvf.app.model.response.ResCheckPay;
import com.xykhvf.app.model.response.ResSubmitPay;
import com.xykhvf.app.presenter.PayPresenter;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.util.ImageTextButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends AppCompatActivity implements IPayView {

    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvPayPrice)
    TextView tvPayPrice;
    @BindView(R.id.btnScanPay)
    ImageTextButton btnScanPay;//扫一扫
    @BindView(R.id.btnPospay)
    ImageTextButton btnPospay;//银行卡
    @BindView(R.id.btnOffline)
    ImageTextButton btnOffline;//现金
    private String mOrderNo, mIds;
    private Double mAmount;
    private PayPresenter mPresenter;
    private static Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private TextView tvTitle;
    private ImageButton btnBack, btnExit;
    private PrintModel mPrintModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initView();
        mPresenter = new PayPresenter(this, this);
        Intent intent = getIntent();
        String ids = intent.getStringExtra("ids");
        mPrintModel = new PrintModel();
        mPresenter.checkPay(ids);
    }

    void initView() {
        this.btnScanPay.setImgResource(R.drawable.alipay);
        this.btnScanPay.setBigText(this.getString(R.string.scanText));
        this.btnScanPay.setText(this.getString(R.string.scanDesc));

        this.btnPospay.setImgResource(R.drawable.pospay);
        this.btnPospay.setBigText(this.getString(R.string.pospayText));
        this.btnPospay.setText(this.getString(R.string.pospayDesc));

        this.btnOffline.setImgResource(R.drawable.cashpay);
        this.btnOffline.setBigText(this.getString(R.string.cashpayText));
        this.btnOffline.setText(this.getString(R.string.cashpayDesc));

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("支付方式");
        btnBack = findViewById(R.id.btnBack);
        btnExit = findViewById(R.id.btnExit);
        btnBack.setOnClickListener(v -> {
            finish();

        });
        btnExit.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(PayActivity.this).setTitle("是否退出")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SApplication.appExit();
                        }
                    }).setNegativeButton("取消", null)
                    .show();
            dialog.setCanceledOnTouchOutside(false);
        });
    }

    @Override
    public void setPayInfo(ResCheckPay res) {
        mOrderNo = res.getOrder();
        mAmount = res.getAmount();
        mIds = res.getIds();
        tvOrderNo.setText("订单号：" + mOrderNo);
        tvPayPrice.setText("￥" + mAmount);
        mPrintModel.setAmount(mAmount.toString());
        mPrintModel.setOrderNo(mOrderNo);
        mPrintModel.setPayTime(DateUtils.getNow(DateUtils.FORMAT_LONG));
        String roomName = SharedPreferencesUtils.getValue(SysEnv.context, Constants.AREA_NAME) + "-"
                + SharedPreferencesUtils.getValue(SysEnv.context, Constants.BUILDING_NAME) + "-"
                + SharedPreferencesUtils.getValue(SysEnv.context, Constants.ROOM_NAME);
        mPrintModel.setRoomName(roomName);
        mPrintModel.setFeeDetail(SharedPreferencesUtils.getValue(SysEnv.context, Constants.FEE_DESC_PRINT));
        mPrintModel.setMchName(SharedPreferencesUtils.getValue(SysEnv.context, Constants.MCH_NAME));
    }

    @Override
    public void payResult(ResSubmitPay result) {
        if (result.getCode() == 0) {
            Intent it = new Intent(this, PrintActivity.class);
            it.putExtra("info", mGson.toJson(mPrintModel));
            //it.putExtra("buildingId", SharedPreferencesUtils.getValue(SysEnv.context, Constants.BUILDING_ID));
            //it.putExtra("buildingName", SharedPreferencesUtils.getValue(SysEnv.context, Constants.BUILDING_NAME));
            startActivity(it);
        }
        Toast.makeText(this, result.getMsg(), Toast.LENGTH_LONG).show();
    }


    @OnClick({R.id.btnScanPay, R.id.btnPospay, R.id.btnOffline})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnScanPay://扫一扫
                mPrintModel.setPayType("扫一扫");
                mPresenter.scanPay(mAmount + "", mOrderNo);
                break;
            case R.id.btnPospay://银行卡
                mPrintModel.setPayType("银行卡");
                mPresenter.posPay(mAmount + "", mOrderNo);
                break;
            case R.id.btnOffline://现金
                mPrintModel.setPayType("现金");
                checkPwd();
                break;
        }
    }

    private void checkPwd() {
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(this).builder()
                .setTitle("请输入密码")
                .setEditType(129)
                .setEditText("请输入密码");
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = myAlertInputDialog.getContentEditText().getText().toString();
                if (val.equals(SysEnv.SETTING_MODEL.getLoginPwd())) {
                    paySubmit("202");
                    myAlertInputDialog.dismiss();
                } else {
                    Toast.makeText(PayActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                }

            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();
    }

    private void paySubmit(String payType) {
        ReqPayModel model = new ReqPayModel();
        model.setAmount(mAmount + "");
        model.setData(mIds);
        model.setOrderid(mOrderNo);
        model.setPay_type(payType);
        model.setUms_orderid(DateUtils.getTimestampNow() + "");
        mPresenter.paySubmit(model);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "调用失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (AppHelper.TRANS_REQUEST_CODE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                if (data != null) {
                    Map<String, String> map = AppHelper.filterTransResult(data);
                    StringBuilder result = new StringBuilder();
                    String transData = "";
                    result.append(AppHelper.TRANS_APP_NAME + ":" + map.get(AppHelper.TRANS_APP_NAME) + "\r\n");
                    result.append(AppHelper.TRANS_BIZ_ID + ":" + map.get(AppHelper.TRANS_BIZ_ID) + "\r\n");
                    result.append(AppHelper.RESULT_CODE + ":" + map.get(AppHelper.RESULT_CODE) + "\r\n");
                    result.append(AppHelper.RESULT_MSG + ":" + map.get(AppHelper.RESULT_MSG) + "\r\n");
                    result.append(AppHelper.TRANS_DATA + ":" + map.get(AppHelper.TRANS_DATA) + "\r\n");
                    transData = map.get(AppHelper.TRANS_DATA);
                    PayResult payResult = mGson.fromJson(transData, PayResult.class);

                    if (map.get(AppHelper.RESULT_CODE).equals("0")) {
                        if (payResult.getResCode().equals("00")) {
                            int payType = 2;
                            switch (map.get(AppHelper.TRANS_BIZ_ID).trim()) {
                                case "扫一扫":
                                    payType = 200;
                                    break;
                                case "消费":
                                    payType = 201;
                                    break;
                            }
                            paySubmit(payType + "");
                        } else {
                            Toast.makeText(this, payResult.getResDesc(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, this.getString(R.string.payfail), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
