package com.xykhvf.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xykhvf.app.view.LoginActivity;

public class VfBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
                    String packageName = intent.getData().getSchemeSpecificPart();
                    if (intent.getData().getSchemeSpecificPart().equals(context.getPackageName())) {
                        Intent mainActivityIntent = new Intent(context, LoginActivity.class);
                        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mainActivityIntent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
