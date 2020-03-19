package com.vcom.publiclibrary.utils;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Lifa on 2016-04-18.
 */
public class PowerUtils {
    private static PowerManager mPowerManager = null;
    private static PowerManager.WakeLock mWakeLock = null;
    private static PowerManager.WakeLock mAcquireWakeLock = null;
    private static final String WAKETAG = "MyWake";
    private static final String WAKELOCKTAG = "MyWakeLock";
    private static final String TAG = "PowerUtils";

    public static void inflate(Context context, int lockLevel) {
        if (mPowerManager == null) {
            mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        }
        if (mWakeLock == null)
            mWakeLock = mPowerManager.newWakeLock(lockLevel, WAKETAG);
    }

    public static PowerManager getPowerManager(Context context) {
        if (mPowerManager == null)
            return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        else
            return mPowerManager;
    }

    private static void setWakeLock(int lockLevel) {
        mPowerManager = getPowerManager(SysEnv.context);
        mWakeLock = mPowerManager.newWakeLock(lockLevel, WAKETAG);
        mWakeLock.acquire();
    }

    public static void setWakeLock(PowerManager powerManager, int lockLevel) {
        if (mWakeLock == null)
            mWakeLock = powerManager.newWakeLock(lockLevel, WAKELOCKTAG);
        // mWakeLock.setReferenceCounted(true);
        mWakeLock.acquire();
    }

    /**
     * 唤醒CPU
     */
    public static void partialWekeLock() {
        mPowerManager = getPowerManager(SysEnv.context);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.PARTIAL_WAKE_LOCK, WAKELOCKTAG);
        mWakeLock.acquire();
        Log.i(TAG, "partialWekeLock: *********");
    }

    /**
     * 唤醒CPU和屏幕（亮一下）
     */
    public static void dimlWekeLock() {
        mPowerManager = getPowerManager(SysEnv.context);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, WAKELOCKTAG);
        mWakeLock.acquire();
    }

    /**
     * 唤醒CPU和长亮屏幕
     */
    public static void brightWekeLock() {
        mPowerManager = getPowerManager(SysEnv.context);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, WAKELOCKTAG);
        mWakeLock.acquire();
    }

    /**
     * 唤醒CPU、屏幕键盘长亮
     */
    public static void fullWekeLock() {
        mPowerManager = getPowerManager(SysEnv.context);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, WAKELOCKTAG);
        mWakeLock.acquire();
    }

    public static void wekeLockRelease() {
        try {
            if (mWakeLock != null) {
                mWakeLock.release();
                mWakeLock = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 释放电源锁
     */
    public static void release() {
        try {
            if (mAcquireWakeLock != null)
                mAcquireWakeLock.release();
        } catch (Exception e) {
        }
    }


    public static void acquire() {
        mPowerManager = getPowerManager(SysEnv.context);
        mAcquireWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.SCREEN_DIM_WAKE_LOCK, WAKETAG);
        mAcquireWakeLock.acquire();
        Log.i(TAG, "acquire: *******");
    }

    public static boolean isScreenOn() {
        if (mPowerManager == null)
            mPowerManager = getPowerManager(SysEnv.context);
        return mPowerManager.isScreenOn();
    }

}
