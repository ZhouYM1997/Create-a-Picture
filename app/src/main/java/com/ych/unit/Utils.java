package com.ych.unit;
import android.content.Context;
import android.util.Log;

import com.ych.Fragment.LeftFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static String assetFilePath(Context context, String assetName) {
        File file = new File(context.getFilesDir(), assetName);
        System.out.println(assetName);
        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            File myFile = new File(file.getAbsolutePath());

            if (myFile.exists()) {
                Log.i("sss", "onCreate: =文件存在=");
            } else {
                Log.i("sss", "onCreate: =文件不存在=");
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e("pytorchandroid", "Error process asset " + assetName + " to file path");
        }
        return null;
    }


}