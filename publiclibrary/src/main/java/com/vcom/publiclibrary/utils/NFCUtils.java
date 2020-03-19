package com.vcom.publiclibrary.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.widget.Toast;

/**
 * Created by Lifa on 2016-08-22.
 */
public class NFCUtils {
    private static NfcAdapter mNfcAdapter = null;

    public static boolean isNFCSupport() {
        // 初始化设备支持NFC功能
        mNfcAdapter = NfcAdapter.getDefaultAdapter(SysEnv.context);
        if (mNfcAdapter == null)
            return false;
        else return true;
    }

    private void initNFCData(Context context, PendingIntent pendingIntent) {

        // 判定设备是否支持NFC或启动NFC
        if (!isNFCSupport()) {
            Toast.makeText(context, "该设备不支持NFC功能", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(context, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            return;
        }
        initNFC(context, pendingIntent);
    }

    private void initNFC(Context context, PendingIntent pendingIntent) {
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // 新建IntentFilter，使用的是第二种的过滤机制
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
    }


}
