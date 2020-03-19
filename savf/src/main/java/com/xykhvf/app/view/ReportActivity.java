package com.xykhvf.app.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vcom.publiclibrary.adapter.CommonAdapter;
import com.vcom.publiclibrary.adapter.ViewHolder;
import com.vcom.publiclibrary.utils.DateUtils;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.model.request.ReqReport;
import com.xykhvf.app.model.response.ResReport;
import com.xykhvf.app.presenter.ReportPresenter;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends AppCompatActivity implements IReportView {

    @BindView(R.id.tvTimeStart)
    TextView tvTimeStart;
    @BindView(R.id.tvTimeEnd)
    TextView tvTimeEnd;

    ReportPresenter mReportPresenter;
    @BindView(R.id.rvReport)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvAllCount)
    TextView tvAllCount;
    @BindView(R.id.tvAllFee)
    TextView tvAllFee;
    @BindView(R.id.llReportCount)
    LinearLayout llReportCount;

    private TextView tvTitle;
    private ImageButton btnBack, btnExit;

    private LinearLayoutManager mLayoutManager = null;
    private CommonAdapter<ResReport> mAdapter = null;
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        mReportPresenter = new ReportPresenter(this, this);
        initView();
    }

    void initView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        tvTimeStart.setText(DateUtils.getNow(DateUtils.FORMAT_SHORT));
        tvTimeEnd.setText(DateUtils.getNow(DateUtils.FORMAT_SHORT));
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("统计");
        btnBack = findViewById(R.id.btnBack);
        btnBack.setVisibility(View.INVISIBLE);
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(ReportActivity.this).setTitle("是否退出")
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


        ReqReport req = new ReqReport();
        req.setStart_date(DateUtils.getNow(DateUtils.FORMAT_SHORT));
        req.setEnd_date(DateUtils.getNow(DateUtils.FORMAT_SHORT));
        mReportPresenter.getReport(req);
    }

    @OnClick({R.id.tvTimeStart, R.id.tvTimeEnd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTimeStart:
                showDatePickDlg(tvTimeStart);
                break;
            case R.id.tvTimeEnd:
                showDatePickDlg(tvTimeEnd);
                break;
        }

    }

    protected void showDatePickDlg(TextView tv) {
        ReqReport req = new ReqReport();
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                req.setStart_date(tvTimeStart.getText().toString());
                req.setEnd_date(tvTimeEnd.getText().toString());
                mReportPresenter.getReport(req);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        if (tv.getId() == R.id.tvTimeEnd) {
            DatePicker datePicker = pickerDialog.getDatePicker();
            //datePicker.setMaxDate(new Date().getTime());//设置日期的上限日期
            datePicker.setMinDate(DateUtils.parse(tvTimeStart.getText().toString(), DateUtils.FORMAT_SHORT).getTime());//设置日期的下限日期，其中是参数类型是long型，为日期的时间戳
        }
        pickerDialog.show();
    }

    @Override
    public void setData(List<ResReport> list) {

        mAdapter = new CommonAdapter<ResReport>(ReportActivity.this, R.layout.layout_report_item, list) {
            @Override
            public void convert(ViewHolder holder, ResReport info) {
                holder.setText(R.id.tvReportCount, info.getPay_string() + "笔数：" + info.getCount());
                holder.setText(R.id.tvReportFee, "交易金额：" + info.getAmount());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refreshDrawableState();
        if (list != null || list.size() > 0) {
            int allCount = 0;
            double allFee = 0d;
            for (ResReport item : list) {
                allFee += item.getAmount();
                allCount += item.getCount();
            }
            tvAllCount.setText(allCount + "");
            tvAllFee.setText(allFee + "");
            llReportCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //对返回键和Home键处理，按下时程序至于后台运行
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_HOME);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (keyCode == KeyEvent.KEYCODE_HOME && event.getAction() == KeyEvent.ACTION_DOWN) {
            startActivity(it);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                startActivity(it);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
