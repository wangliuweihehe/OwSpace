package com.wlw.admin.owspace.utils;


import com.wlw.admin.owspace.model.util.HttpUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author admin
 */
public class OkHttpImageDownloader {
    public static void download(String url) {
        final Request request = new Request.Builder().url(url).build();
        HttpUtils.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(getClass().getSimpleName(), e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                FileUtil.createSdDir();
                String url = response.request().url().toString();
                int index = url.lastIndexOf("/");
                String pictureName = url.substring(index + 1);
                if (FileUtil.isFileExist(pictureName)) {
                    return;
                }
                FileOutputStream fos = new FileOutputStream(FileUtil.createFile(pictureName));
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream inputStream = responseBody.byteStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    inputStream.close();
                    fos.close();
                }

            }
        });

    }
}
