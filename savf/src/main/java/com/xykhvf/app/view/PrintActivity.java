package com.xykhvf.app.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vcom.publiclibrary.utils.DateUtils;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.model.PrintModel;
import com.xykhvf.app.presenter.PrintPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PrintActivity extends AppCompatActivity {

    @BindView(R.id.img_result)
    ImageView imgResult;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvMchName)
    TextView tvMchName;
    @BindView(R.id.tvRoomName)
    TextView tvRoomName;
    @BindView(R.id.tvFeeDetail)
    TextView tvFeeDetail;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.tvPayType)
    TextView tvPayType;
    @BindView(R.id.tvPayAmount)
    TextView tvPayAmount;
    @BindView(R.id.btnPrint)
    Button btnPrint;
    @BindView(R.id.btnGoHome)
    Button btnGoHome;
    private TextView tvTitle;
    private ImageButton btnBack, btnExit;

    private static Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    PrintPresenter mPresenter = null;
    PrintModel mPrintModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        mPresenter = new PrintPresenter(this);
        mPrintModel = mGson.fromJson(getIntent().getStringExtra("info"), PrintModel.class);
        initView(mPrintModel);
    }

    void initView(PrintModel model) {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("打印凭证");
        btnBack = findViewById(R.id.btnBack);
        btnExit = findViewById(R.id.btnExit);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnExit.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(PrintActivity.this).setTitle("是否退出")
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
        if (mPrintModel != null) {
            tvOrderNo.setText(model.getOrderNo());
            tvFeeDetail.setText(model.getFeeDetail());
            tvPayAmount.setText(model.getAmount());
            tvPayTime.setText(model.getPayTime());
            tvMchName.setText(model.getMchName());
            tvRoomName.setText(model.getRoomName());
            tvPayType.setText(model.getPayType());
        }
    }

    @OnClick({R.id.btnPrint, R.id.btnGoHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnPrint:
                if (mPrintModel != null) {
                    mPresenter.print(mPrintModel);
                }
                break;
            case R.id.btnGoHome:
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }
    }
}
