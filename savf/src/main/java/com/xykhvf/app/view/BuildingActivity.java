package com.xykhvf.app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vcom.publiclibrary.adapter.CommonAdapter;
import com.vcom.publiclibrary.adapter.ViewHolder;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.model.AreaModel;
import com.xykhvf.app.presenter.BuildingPresenter;
import com.xykhvf.app.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuildingActivity extends Activity implements IBuildingView {
    private static String TAG = BuildingActivity.class.getName();
    @BindView(R.id.tvAreaName)
    TextView tvAreaName;
    @BindView(R.id.rvHouses)
    RecyclerView mRecyclerView;
    @BindView(R.id.changeArea)
    LinearLayout changeArea;

    private LinearLayoutManager mLayoutManager = null;
    private CommonAdapter<AreaModel> mAdapter = null;
    private AlertDialog mDialog;
    private AlertDialog.Builder mAlertBuilder;
    private BuildingPresenter mBuildingPresenter = null;
    private TextView tvTitle;
    private ImageButton btnBack, btnExit;
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        ButterKnife.bind(this);
        mAlertBuilder = new AlertDialog.Builder(this);
        mAlertBuilder.setTitle("请选择小区");
        mBuildingPresenter = new BuildingPresenter(this, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initView();
        mBuildingPresenter.getFirstArea();
        mBuildingPresenter.getMerchat();
    }

    void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("首页");
        btnBack = findViewById(R.id.btnBack);
        btnExit = findViewById(R.id.btnExit);
        btnBack.setVisibility(View.INVISIBLE);
        btnExit.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(BuildingActivity.this).setTitle("是否退出")
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


    @OnClick(R.id.changeArea)
    public void onViewClicked() {
        mBuildingPresenter.selectArea();
    }

    @Override
    public void selectArea(List<AreaModel> list) {
        LayoutInflater factory = LayoutInflater.from(BuildingActivity.this);
        View view = factory.inflate(R.layout.layout_area, null);
        List<Map<String, String>> mapList = new ArrayList<>();
        for (AreaModel model : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", model.getId().toString());
            map.put("name", model.getName());
            mapList.add(map);
        }
        ListView listView = view.findViewById(R.id.lvArea);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        SimpleAdapter adapter = new SimpleAdapter(this, mapList, R.layout.layout_area_item, new String[]{"id", "name"},
                new int[]{R.id.tv_item_id, R.id.tv_item_name});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = view.findViewById(R.id.tv_item_id);
                TextView nameView = view.findViewById(R.id.tv_item_name);
                SharedPreferencesUtils.putValue(SysEnv.context, Constants.AREA_ID, idView.getText().toString());
                SharedPreferencesUtils.putValue(SysEnv.context, Constants.AREA_NAME, nameView.getText().toString());
                BuildingActivity.this.tvAreaName.setText(nameView.getText().toString());
                mBuildingPresenter.setAreaName(nameView.getText().toString());
                mBuildingPresenter.getBuilding(Long.parseLong(idView.getText().toString()));
                mDialog.dismiss();
            }
        });
        mAlertBuilder.setView(view);
        mDialog = mAlertBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    @Override
    public void setAreaName(String str) {
        this.tvAreaName.setText(str);
    }

    @Override
    public void setList(List<AreaModel> list) {
        mAdapter = new CommonAdapter<AreaModel>(BuildingActivity.this, R.layout.layout_houses_item, list) {
            @Override
            public void convert(ViewHolder holder, final AreaModel info) {
                holder.setText(R.id.tv_house_name, info.getName());
                holder.setTag(R.id.tv_house_name, info);

                holder.setOnClickListener(R.id.tv_house_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AreaModel model = (AreaModel) v.getTag();
                        Intent intent = new Intent(BuildingActivity.this, RoomActivity.class);
                        intent.putExtra("buildingId", model.getId().toString());
                        intent.putExtra("buildingName", model.getName());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refreshDrawableState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (mAdapter != null) {
            mAdapter = null;
        }
        if (mAlertBuilder != null) {
            mAlertBuilder = null;
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
