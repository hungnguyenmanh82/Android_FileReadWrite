package com.hung.file;

import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 File ở trong thư mục này khi compile sẽ đc giữ nguyên định dạng binary.
 Nếu để trong thư mục drawable thì sẽ bị nén ảnh bởi trình biên dịch.
 Raw file thuộc thư mục Resource“/res/raw” =>
 nên nó có tính chất của resource: hỗ trợ ngôn ngữ, xoay màn hình,.. (thêm suffix vào sau raw => /res/raw-landscape).
 main/assets
 main/java
 main/res/raw
 */

/**
 * Raw file chi dung de Read, ko dung de write
 */
public class RawFile {
    public static String TAG = RawFile.class.getSimpleName();

    public static String readRawFileUTF8(Context context){

        InputStream ins = context.getResources().openRawResource(R.raw.test_raw_file);
        // ====================== giống như Java Core =========================================
        try {
            InputStreamReader reader = new InputStreamReader(ins,"UTF-8"); //byte to charset convert 8k char buffer
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

    public static byte[] readRawFileBytes(Context context){

        InputStream in = context.getResources().openRawResource(R.raw.test_raw_file);

        // ====================== giống như Java Core =========================================
        try {
            //ko write ra file mà write ra buffer trên RAM để convert to bytes array
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            Log.v(TAG,Log.getStackTraceString(e));
        }

        return null;
    }
}
