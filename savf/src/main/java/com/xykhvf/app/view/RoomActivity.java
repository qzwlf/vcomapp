package com.xykhvf.app.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.vcom.publiclibrary.adapter.CommonAdapter;
import com.vcom.publiclibrary.adapter.ViewHolder;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.R;
import com.xykhvf.app.SApplication;
import com.xykhvf.app.model.response.House;
import com.xykhvf.app.presenter.RoomPresenter;
import com.xykhvf.app.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RoomActivity extends AppCompatActivity implements IRoomView {

    @BindView(R.id.svRoom)
    SearchView svRoom;
    @BindView(R.id.tvAreaName)
    TextView tvAreaName;
    @BindView(R.id.rvRooms)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvBuildingName)
    TextView tvBuildingName;
    private String buildingId, buildingName;
    private RoomPresenter mRoomPresenter;
    private TextView tvTitle;
    private ImageButton btnBack, btnExit;
    private LinearLayoutManager mLayoutManager = null;
    private CommonAdapter<House> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Intent it = getIntent();
        buildingId = it.getStringExtra("buildingId");
        buildingName = it.getStringExtra("buildingName");
        SharedPreferencesUtils.putValue(SysEnv.context, Constants.BUILDING_ID, buildingId);
        SharedPreferencesUtils.putValue(SysEnv.context, Constants.BUILDING_NAME, buildingName);
        this.tvAreaName.setText(SharedPreferencesUtils.getValue(SysEnv.context, Constants.AREA_NAME, ""));
        this.tvBuildingName.setText(buildingName);
        mRoomPresenter = new RoomPresenter(this, this);
        mRoomPresenter.getRooms(Long.parseLong(buildingId));
        initSearchView();
    }

    void initSearchView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("房间信息");
        btnBack = findViewById(R.id.btnBack);
        btnExit = findViewById(R.id.btnExit);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnExit.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(RoomActivity.this).setTitle("是否退出")
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
        svRoom.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRoomPresenter.getRoomsByNo(newText);
                return false;
            }
        });

    }

    @Override
    public void setList(List<House> list) {
        mAdapter = new CommonAdapter<House>(RoomActivity.this, R.layout.layout_room_item, list) {
            @Override
            public void convert(ViewHolder holder, House info) {
                holder.setText(R.id.tvHouseName, "房号：" + info.getNumber());
                holder.setText(R.id.tvAmount, "未缴金额：" + info.getAmount());
                holder.setTag(R.id.llHouseItem, info);
                holder.setOnClickListener(R.id.llHouseItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        House house = (House) v.getTag();
                        Intent intent = new Intent(RoomActivity.this, FeeActivity.class);
                        intent.putExtra("houseId", house.getId().toString());
                        intent.putExtra("houseNumber", house.getNumber());
                        SharedPreferencesUtils.putValue(SysEnv.context, Constants.ROOM_NAME, house.getNumber());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refreshDrawableState();
    }

    @OnClick(R.id.svRoom)
    public void onViewClicked() {
    }
}
