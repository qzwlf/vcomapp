package com.vcom.publiclibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lifa on 2016-04-20.
 */
public class AssetUtils {
    private static AssetManager mAssetManager = null;

    public static AssetManager getAssetManaget(Context context) {
        return context.getAssets();
    }

    public static InputStream open(String fileName, int assetModel) throws IOException {
        try {
            return mAssetManager.open(fileName, assetModel);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
