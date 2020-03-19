package com.xykhvf.app.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xykhvf.app.R;


/**
 * Created by Administrator on 2017-09-07.
 */
public class ImageTextButton extends LinearLayout {
    private ImageView mImgView;
    private TextView mBigTextView;
    private TextView mTextView;

    public ImageTextButton(Context context) {
        super(context);
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.image_text_button, this, true);

        this.mImgView = (ImageView) findViewById(R.id.btnImage);
        this.mBigTextView = (TextView) findViewById(R.id.btnBigText);
        this.mTextView = (TextView) findViewById(R.id.btnText);
        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImgResource(int resourceID) {
        this.mImgView.setImageResource(resourceID);
    }

    public void setBigText(String text) {
        this.mBigTextView.setText(text);
    }

    public void setBigTextColor(int color) {
        this.mBigTextView.setTextColor(color);
    }

    public void setBigTextSize(float size) {
        this.mBigTextView.setTextSize(size);
    }

    public void setText(String text) {
        this.mTextView.setText(text);
    }

    public void setTextColor(int color) {
        this.mTextView.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.mTextView.setTextSize(size);
    }

}
