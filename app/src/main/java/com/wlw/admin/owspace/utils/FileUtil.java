package com.wlw.admin.owspace.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
public class FileUtil {
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String AD_PATH = SD_PATH + "/Owspace";

    public static void createSdDir() {
        File file = new File(AD_PATH);
        if (!file.exists()) {
            boolean create = file.mkdirs();
            Log.e("FileUtil", "createDir=" + create);
        } else {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdirs();
            }
        }
    }

    public static boolean isFileExist(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return false;
        }
        File localFile = new File(AD_PATH + "/" + paramString);
        return localFile.exists();
    }

    public static File createFile(String fileName) throws IOException {
        File file = new File(AD_PATH, fileName);
        file.createNewFile();
        return file;
    }

    public static List<String> getAllAD() {
        File file = new File(FileUtil.AD_PATH);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                list.add(f.getAbsolutePath());
            }
        }
        return list;
    }
}
