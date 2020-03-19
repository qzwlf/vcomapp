package com.xykhvf.app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vcom.publiclibrary.adapter.CommonAdapter;
import com.vcom.publiclibrary.adapter.ViewHolder;
import com.vcom.publiclibrary.utils.ListUtils;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.fragment.FeeFragment;
import com.xykhvf.app.model.FeeSummary;
import com.xykhvf.app.model.response.Fee;
import com.xykhvf.app.model.response.House;
import com.xykhvf.app.presenter.FeePresenter;
import com.xykhvf.app.util.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeeActivity extends Activity implements IFeeView {

    @BindView(R.id.tvAreaName)
    TextView tvAreaName;
    @BindView(R.id.rvFee)
    RecyclerView mRecyclerView;
    RecyclerView mRecyclerFeeItem;
    @BindView(R.id.btnPay)
    Button btnPay;
    @BindView(R.id.tvBuildingHouseName)
    TextView tvBuildingHouseName;
    private String buildingName, houseId, houseNumber;
    private LinearLayoutManager mLayoutManager = null;
    private CommonAdapter<FeeSummary> mFeeSummaryAdapter = null;
    private CommonAdapter<Fee> mAdapter = null;
    private FeePresenter mFeePresenter;
    private List<Fee> mFees = new ArrayList<>();
    private List<FeeSummary> mFeeSummary = null;
    private TextView tvTitle;
    private ImageButton btnBack, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);
        ButterKnife.bind(this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerFeeItem.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerFeeItem.setHasFixedSize(true);
//        mRecyclerFeeItem.setLayoutManager(mLayoutManager);
        initView();
        Intent it = getIntent();
        buildingName = SharedPreferencesUtils.getValue(SysEnv.context, Constants.BUILDING_NAME);
        houseId = it.getStringExtra("houseId");
        houseNumber = it.getStringExtra("houseNumber");
        this.tvAreaName.setText(SharedPreferencesUtils.getValue(SysEnv.context, Constants.AREA_NAME, ""));
        this.tvBuildingHouseName.setText(buildingName + houseNumber);
        mFeePresenter = new FeePresenter(this, this);
        mFeePresenter.getFees(Long.parseLong(houseId));
    }

    void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("费用详情");
        btnBack = findViewById(R.id.btnBack);
        btnExit = findViewById(R.id.btnExit);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(FeeActivity.this).setTitle("是否退出")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SApplication.appExit();
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                dialog.setCanceledOnTouchOutside(false);
            }
        });
    }

    @OnClick(R.id.btnPay)
    public void onViewClicked() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> ids = new ArrayList<>();
        for (Fee f : mFees) {
            if (f.getSelected()) {
                ids.add(f.getId().toString());
                stringBuilder.append(String.format("\r\n%s:%s:%s元;", f.getDate(), f.getType(), f.getAmount()));
            }
        }
        if (ids.size() > 0) {
            SharedPreferencesUtils.putValue(SysEnv.context, Constants.FEE_DESC_PRINT, stringBuilder.toString());
            SharedPreferencesUtils.putValue(SysEnv.context, Constants.FEE_DESC, stringBuilder.toString().replaceFirst("\r\n", ""));
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra("ids", ListUtils.join(ids));
            intent.putExtra("arrIds", ids.toArray());
            startActivity(intent);
        } else {
            Toast.makeText(this, "请选择缴费项目", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setList(List<Fee> list, Context context, RecyclerView recyclerView) {
        mFees.addAll(list);
        for (Fee f : list) {
            if (f.getMerchant().equals(SharedPreferencesUtils.getValue(SysEnv.context, Constants.MERCHANT, ""))) {
                f.setSelected(true);
            } else {
                f.setSelected(false);
            }
        }
        mAdapter = new CommonAdapter<Fee>(context, R.layout.layout_fee_item, list) {
            @Override
            public void convert(ViewHolder holder, Fee info) {
                holder.setText(R.id.tvFeeType, "费用类型：" + info.getType());
                holder.setText(R.id.tvFee, "金额：" + info.getAmount());
                if (info.getSelected()) {
                    holder.setImageResource(R.id.imgSelect, R.drawable.checkbox_selected);
                } else {
                    holder.setImageResource(R.id.imgSelect, R.drawable.checkbox_unselected);
                }
                holder.setTag(R.id.imgSelect, info);
                holder.setOnClickListener(R.id.imgSelect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fee fee = (Fee) v.getTag();
                        for (Fee f : mFees) {
                            if (f.getId().equals(fee.getId())) {
                                if (fee.getSelected()) {
                                    f.setSelected(false);
                                } else {
                                    if (f.getMerchant().equals(SharedPreferencesUtils.getValue(SysEnv.context, Constants.MERCHANT, ""))) {
                                        f.setSelected(true);
                                    }
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        recyclerView.setAdapter(mAdapter);
        recyclerView.refreshDrawableState();
    }

    @Override
    public void setMonthFee(List<FeeSummary> list) {
        mFeeSummary = new ArrayList<>();
        for (FeeSummary f : list) {
            f.setSelected(true);
            mFeeSummary.add(f);
        }
        mFeeSummaryAdapter = new CommonAdapter<FeeSummary>(FeeActivity.this, R.layout.layout_fee, list) {
            @Override
            public void convert(ViewHolder holder, FeeSummary info) {
                holder.setText(R.id.tvGroupDate, info.getDate());
                holder.setText(R.id.tvGroupFee, "合计：" + info.getTotalFee());
                RecyclerView recyclerView = holder.itemView.findViewById(R.id.rvFeeItem);
                LinearLayoutManager manager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                holder.setImageResource(R.id.imgGroupSelect, R.drawable.checkbox_selected);
                if (info.getSelected()) {
                    holder.setImageResource(R.id.imgGroupSelect, R.drawable.checkbox_selected);
                } else {
                    holder.setImageResource(R.id.imgGroupSelect, R.drawable.checkbox_unselected);
                }
                holder.setTag(R.id.imgGroupSelect, info);
                holder.setOnClickListener(R.id.imgGroupSelect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeeSummary fee = (FeeSummary) v.getTag();
                        for (FeeSummary f : mFeeSummary) {
                            if (f.getDate().equals(fee.getDate())) {
                                if (fee.getSelected()) {
                                    f.setSelected(false);
                                } else {
                                    f.setSelected(true);
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
                holder.setOnClickListener(R.id.llGroupFee, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recyclerView.getVisibility() == View.GONE) {
                            mFeePresenter.getFeeItems(info.getDate(), holder.itemView.getContext(), recyclerView);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mFeeSummaryAdapter);
        mRecyclerView.refreshDrawableState();
    }
}
