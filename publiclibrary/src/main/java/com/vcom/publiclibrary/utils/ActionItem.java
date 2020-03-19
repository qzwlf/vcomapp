package com.vcom.publiclibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Lifa on 2016-07-11 14:49.
 */
public class ActionItem {
    //定义图片对象
    public Drawable mDrawable;
    //定义文本对象
    public CharSequence mTitle;
    public int mId;

    public ActionItem(Drawable drawable, CharSequence title) {
        this.mDrawable = drawable;
        this.mTitle = title;
    }

    public ActionItem(Context context, int titleId, int drawableId) {
        this.mTitle = context.getResources().getText(titleId);
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }

    public ActionItem(Context context, CharSequence title, int drawableId) {
        this.mTitle = title;
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }

    public ActionItem(Context context, CharSequence title, int drawableId, int id) {
        this.mTitle = title;
        this.mDrawable = context.getResources().getDrawable(drawableId);
        mId = id;
    }
}
