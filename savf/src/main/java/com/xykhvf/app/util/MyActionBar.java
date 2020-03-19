package com.xykhvf.app.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xykhvf.app.R;


/**
 * Created by Administrator on 2017-09-08.
 */
public class MyActionBar extends LinearLayout {
    private ImageButton btnBack = null;
    private TextView tvTitle = null;

    public MyActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_action_bar_menu, this, true);
        this.btnBack = findViewById(R.id.btnBack);
        this.tvTitle = findViewById(R.id.tvTitle);
        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setBtnBack(int imgId) {
        this.btnBack.setImageDrawable(getResources().getDrawable(imgId));
    }


    public void setTitle(String text, float size, int color) {
        this.tvTitle.setText(text);
        this.tvTitle.setTextSize(size);
        this.tvTitle.setTextColor(color);
    }
}
