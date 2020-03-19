package com.vcom.publiclibrary.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Created by Lifa on 2016-04-18.
 */


public class AlarmUtils {
    private static final String TAG = "AlarmUtils";

    public static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * @Title: 指定时间后进行更新赛事信息(有如闹钟的设置)
     * @Author: Lifa
     * @Time: 2016-06-24 10:37
     */
    public static void sendUpdateBroadcast(Context ctx, Class<?> cls) {
        AlarmManager am = getAlarmManager(ctx);
        // 秒后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
        Intent i = new Intent(ctx, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
    }

    /**
     * @Title: 取消定时执行(有如闹钟的取消)
     * @Author: Lifa
     * @Time: 2016-06-24 10:37
     */
    public static void cancelUpdateBroadcast(Context ctx, Class<?> cls) {
        AlarmManager am = getAlarmManager(ctx);
        Intent i = new Intent(ctx, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.cancel(pendingIntent);
    }


    /**
     * @Title: 周期性的执行某项操作，可以用来保持APP不被Kill
     * @Author: Lifa
     * @Time: 2016-06-24 10:38
     */
    public static void sendUpdateBroadcastRepeat(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        AlarmManager am = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pendingIntent);
    }

}
