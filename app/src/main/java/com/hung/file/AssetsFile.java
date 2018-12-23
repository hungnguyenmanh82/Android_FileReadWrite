package com.hung.file;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Assets file la noi dung chua trong APK file dc luu o Android OS system.
 * Assets file chi dung de Read, ko dung de write
 *
 *  main/assets
 *  main/java
 *  main/res/raw
 */
public class AssetsFile {
    public static String TAG = AssetsFile.class.getSimpleName();

    /**
     *
     * @param context
     * @param folderName
     * @param fileName include extention
     * @return
     */
    public static String readRawFileUTF8(Context context,String folderName, String fileName){

        try {
            InputStream is = context.getResources().getAssets().open(folderName + File.separator + fileName);
            // ====================== giống như Java Core =========================================
            InputStreamReader reader = new InputStreamReader(is,"UTF-8"); //byte to charset convert 8k char buffer
            BufferedReader buffReader = new BufferedReader(reader); //buffer for char 1024 char buffer

            //Read text from file
            StringBuilder stBuilder = new StringBuilder();
            char[] buf = new char[1024];
            int n;

            while (true) {
                n = buffReader.read(buf);
                if(n < 0){//ket thuc doc log -1
                    //da doc den cuoi roi => buffer is empty
                    buffReader.close();
                    break;//stop reading
                }else{
                    stBuilder.append(buf,0,n);
                }
            }

            return stBuilder.toString();
        } catch (Exception e) {
            Log.v(TAG,Log.getStackTraceString(e));
        }

        return null;
    }

    public static byte[] readRawFileBytes(Context context,String folderName, String fileName){

        try {
            InputStream is = context.getResources().getAssets().open(folderName + File.separator + fileName);
            // ====================== giống như Java Core =========================================
            //ko write ra file mà write ra buffer trên RAM để convert to bytes array
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            Log.v(TAG,Log.getStackTraceString(e));
        }

        return null;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void CopyAssetsFiles(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] fileNames = null;
        try {
            fileNames = assetManager.list("");//lấy list file trong asset folder của apk
            for(String str: fileNames)
                Log.d(TAG, "file: " + str);
        } catch (IOException e) {
            e.printStackTrace();
        }

//
        if (fileNames != null) {
            for (String fileName : fileNames) {
                if(fileName.contains("images") || fileName.contains("sounds") || fileName.contains("webkit") ||
                        fileName.contains("libsystem")) {
                    continue;
                }

                InputStream in;
                OutputStream out;
                try {
                    in = assetManager.open(fileName);
                    // /data/data/package_name
                    out = new FileOutputStream(context.getApplicationInfo().dataDir + "/" + fileName);
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // cấp quyền chạy cho file:
        try {
            Runtime.getRuntime().exec("chmod 755 " + context.getApplicationInfo().dataDir + "/tun2socks");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
