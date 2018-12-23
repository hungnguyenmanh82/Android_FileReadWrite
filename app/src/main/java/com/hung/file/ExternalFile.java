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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author HungNM2
 * Lưu y cap permission cho read/write external storage
 */
public class ExternalFile {
	public static String TAG = ExternalFile.class.getSimpleName();


	/**
	 * Class File  sẽ tạo 1 connect tới file or folder.
	 * Sau khi khởi tạo có thể check path này là file hay directory,
	 * check các quyền permission: x, r,w bằng các hàm của class này.

	  case1:
	 * trỏ vào các thư mục chuẩn của OS: Document, music,  alarm,  ring => liên quan Content Provider
	 * // mnt/sdcard/ => trỏ vào các thư mục chuẩn của OS: Document, music, alarm, ring =>Content Provider
	 * // Environment.DIRECTORY_DOWNLOADS trỏ vào thư mục /Download
	 * // ko bị xóa khi uninstall
	 *  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	 */
	public void createPublicFolder(String folderName){
//        String folderName="yourDirectoryName";
		File folder =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) +File.separator + folderName);
//		File folder =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) +File.separator + "Dir1" + File.separator + "Dir2");

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

	/**
	 case2:
	 * //mnt/sdcard/ trên Note 2 là bộ nhớ trong (sdcard trong), ko phải sdcard ngoài
	 * //ở thư mục ngang hàng với Document, music, video
	 * Environment.getExternalStorageDirectory();  // ko bị xóa khi uninstall
	 */
	public void createNoPublicFolder(String folderName ){
//        String folderName="yourDirectoryName";
		File folder =new File(Environment.getExternalStorageDirectory()+ File.separator +folderName);
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

	public static String readFileUTF8(String folderName, String fileName){

	    String state = Environment.getExternalStorageState(); //  mnt/sdcard/
	    if (!Environment.MEDIA_MOUNTED.equals(state)) {
	    	Log.e(TAG, "external TAG permision fail");
	    	return null;
	    }    
		
		try {	
			File folder = new File(Environment.getExternalStorageDirectory() + 
								File.separator + folderName  );
			if(!folder.exists()){
				if(!folder.mkdirs()){
					Log.e(TAG, "make folder false" + folderName);
					return null;
				}
			}

            // ====================== giống như Java Core =========================================
			//if file exist, it will be replace in default
			File file = new File(folder, fileName);
			if( !(file.exists() && file.isFile())){
				Log.e(TAG,"file name is not exist:"+ fileName);
				return null;//file ko ton tai
			}
			
			FileInputStream inputStream= new FileInputStream(file); //sequence byte of file no buffer
			InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8"); //byte to charset convert 8k char buffer
			BufferedReader buffReader = new BufferedReader(reader,2048); //buffer for char 1024 char buffer

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

    public static byte[] readFileBytes(String folderName, String fileName){

        String st;

        String state = Environment.getExternalStorageState(); //  mnt/sdcard/
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e(TAG, "external TAG permision fail");
            return null;
        }

        try {
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + folderName  );
            if(!folder.exists()){
                if(!folder.mkdirs()){
                    Log.e(TAG, "make folder false" + folderName);
                    return null;
                }
            }

            // ====================== giống như Java Core =========================================
            //if file exist, it will be replace in default
            File file = new File(folder, fileName);
            if( !(file.exists() && file.isFile())){
                Log.e(TAG,"file name is not exist:"+ fileName);
                return null;//file ko ton tai
            }

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

    public static String readFileLineUTF8(String iFolder, String fileName){
        String st;

        String state = Environment.getExternalStorageState(); //  mnt/sdcard/
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e(TAG, "external TAG permision fail");
            return null;
        }

        // ====================== giống như Java Core =========================================
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + iFolder  );
        if(!folder.exists()){
            if(!folder.mkdirs()){
                Log.e(TAG, "make folder false" + iFolder);
                return null;
            }
        }

        //if file exist, it will be replace in default
        File file = new File(folder, fileName);
        if( !(file.exists() && file.isFile())){
            Log.e(TAG,"file name is not exist:"+ fileName);
            return null;//file ko ton tai
        }

        try {

            FileInputStream inputStream= new FileInputStream(file); //sequence byte of file no buffer
            InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8"); //byte to charset convert 8k char buffer
            BufferedReader br = new BufferedReader(reader,2048); //buffer for char 1024 char buffer
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

	public static boolean writeFileUTF8(String folderName, String fileName, String content){

	    String state = Environment.getExternalStorageState();
	    if (!Environment.MEDIA_MOUNTED.equals(state)) {
	    	Log.e(TAG, "external TAG permision fail");
	    	return false;
	    }
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if(!folder.exists()){
            if(!folder.mkdirs()){
                Log.e(TAG, "make folder false" + folderName);
                return false;
            }
        }

        //if file exist, it will be replace in default
        File file = new File(folder, fileName);

        // ====================== giống như Java Core =========================================
		try {
			FileOutputStream OutputStream= new FileOutputStream(file); //sequence byte of file no buffer
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


    public static boolean writeFilebytes(String folderName, String fileName, byte[] byteArray, boolean isAppend){

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e(TAG, "external TAG permision fail");
            return false;
        }
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if(!folder.exists()){
            if(!folder.mkdirs()){
                Log.e(TAG, "make folder false" + folderName);
                return false;
            }
        }

        //if file exist, it will be replace in default
        File file = new File(folder, fileName);

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

}
