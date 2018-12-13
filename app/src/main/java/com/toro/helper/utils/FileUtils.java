package com.toro.helper.utils;

import android.os.Environment;

import java.io.File;

/**
 * Create By liujia
 * on 2018/10/30.
 **/
public class FileUtils {

    public static File getImageCacheDir() {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
    }


}
