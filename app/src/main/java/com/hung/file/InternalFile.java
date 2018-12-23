package com.hung.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import androidx.core.content.ContextCompat;

/**
 * @author HungNM2
 * Doc ghi file UTF8
 * App luon co permission voi Internal Storage

 case1: (hay dùng)
 * // lưu: /data/data/pakage_name/files folder
 *      Context.getFilesDir();
 * 	//creates an app-specific directory; hidden from users; deleted with the app

 case2: it dùng
 * // lưu: /data/data/pakage_name/cache folder
 *       Context.getCacheDir(); //ko nên dùng, vì hệ thống có thể xóa khi cần
 */
public class InternalFile {
	public static String TAG = InternalFile.class.getSimpleName();

	/**
	 * Class File  sẽ tạo 1 connect tới file or folder.
	 * Sau khi khởi tạo có thể check path này là file hay directory,
	 * check các quyền permission: x, r,w bằng các hàm của class này.
	 */
	public void createFolder(Context context,String folderName){
		File folder =new File(context.getFilesDir()+ File.separator +folderName);
//		File folder =new File(Environment.getExternalStorageDirectory()+File.separator + "Dir1" + File.separator + "Dir2");

		boolean success =true;
//		if(folder.isDirectory()){}

		if(!folder.exists()){
			success = folder.mkdir();//tạo 1 folder
		}
		if(success){
			// Do something on success
		}else{
			// Do something else on failure
		}

	}

    public static byte[] readFileBytes(Context context,String folderName, String fileName){

        File file =new File(context.getFilesDir()+ File.separator +folderName,fileName);

        // ====================== giống như Java Core =========================================
        if( !(file.exists() && file.isFile())){
            Log.e(TAG,"file name is not exist:"+ fileName);
            return null;//file ko ton tai
        }

        try {
            FileInputStream inputStream= new FileInputStream(file); //sequence byte of file no buffer
            BufferedInputStream bStream = new BufferedInputStream(inputStream,2048); //buffer size = 2048

            //Read text from file
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;

            while (true) {
                n = bStream.read(buf);
                if(n < 0){//ket thuc doc log -1
                    //da doc den cuoi roi => buffer is empty
                    bStream.close();
                    break;//stop reading
                }else{
                    out.write(buf,0,n);
                }
            }

            return out.toByteArray();
        } catch (Exception e) {
            Log.v(TAG,Log.getStackTraceString(e));
        }

        return null;
    }

	public static String readFileUTF8(Context context,String folderName, String fileName){

		File file =new File(context.getFilesDir()+ File.separator +folderName,fileName);

		// ====================== giống như Java Core =========================================
		if( !(file.exists() && file.isFile())){
			Log.e(TAG,"file name is not exist:"+ fileName);
			return null;//file ko ton tai
		}
		try {
			FileInputStream inputStream= new FileInputStream(file); //sequence byte of file no buffer
			InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8"); //byte to charset convert 8k char buffer
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

	public static String readFileLineUTF8(Context context,String folderName, String fileName){

		String st;

		File file =new File(context.getFilesDir()+ File.separator +folderName,fileName);
		// ====================== giống như Java Core =========================================
		if( !(file.exists() && file.isFile())){
			Log.e(TAG,"file name is not exist:"+ fileName);
			return null;//file ko ton tai
		}

		try {
			FileInputStream inputStream= new FileInputStream(file); //sequence byte of file no buffer
			InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8"); //byte to charset convert 8k char buffer
			BufferedReader br = new BufferedReader(reader); //buffer for char 1024 char buffer

			//Read text from file
			//
			StringBuilder sb = new StringBuilder();
			String line;

			while((line = br.readLine())!=null){
				sb.append(line);
				sb.append('\n');
			}
			br.close();

			return sb.toString();
		} catch (Exception e) {
			Log.v(TAG,Log.getStackTraceString(e));
		}

		return null;
	}

    public static boolean writeFilebytes(Context context,String folderName, String fileName, byte[] byteArray, boolean isAppend){

        File file =new File(context.getFilesDir()+ File.separator +folderName,fileName);

        // ====================== giống như Java Core =========================================
        try {
            //if file exist, it will be replace in default
            FileOutputStream os= new FileOutputStream(file, isAppend); //sequence byte of file no buffer
            BufferedOutputStream buffOutStream= new BufferedOutputStream(os, 1024); //buffer for char 1024 char buffer
            //

            buffOutStream.write(byteArray,0, byteArray.length);
            buffOutStream.flush();
            buffOutStream.close();

            return true;
        } catch (Exception e) {
            Log.v(TAG,Log.getStackTraceString(e));
        }

        return false;
    }

	public static boolean writeFileUTF8(Context context,String folderName, String fileName, String content, boolean isAppend){

		File file =new File(context.getFilesDir()+ File.separator +folderName,fileName);

        // ====================== giống như Java Core =========================================
		try {
			//if file exist, it will be replace in default
			FileOutputStream OutputStream= new FileOutputStream(file, isAppend); //sequence byte of file no buffer
			OutputStreamWriter writer = new OutputStreamWriter(OutputStream,"UTF-8"); //byte to charset convert 8k char buffer
			BufferedWriter buffWriter= new BufferedWriter(writer, 1024); //buffer for char 1024 char buffer
			//

			buffWriter.write(content);

			buffWriter.flush();
			buffWriter.close();

			return true;
		} catch (Exception e) {
			Log.v(TAG,Log.getStackTraceString(e));
		}

		return false;
	}
}
