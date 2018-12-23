package com.hung.file;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 Bản chất là lưu vào internal storage giống lưu file nhưng đc android hỗ trợ.
 Dù lấy ra ở đâu thì nó vẫn là 1 singletone đồng nhất.
 Android tạo ra một kiểu Map trên file lưu trữ. Việc ghi đọc là trên Map này =>
 đây là khác biệt giữa SharedPreference với việc lưu dữ liệu vào File trên android.
 Nhược điểm là dùng kiểu map key nên tốn bộ nhớ, chỉ phục vụ lưu dữ liệu ít. =>
 vì lưu trong storage nên khi uninstall ứng dụng sẽ bị xóa. Và nó hiển thị size ở trong app manager của Android OS.

 File dùng với outputStream/inputStream tương ứng với khái niệm open/close file.
 SharedPreference dùng cơ chế singletone nên ko có khái niệm open/close:
 •	mở file khi đc gọi lần đầu => load lên HashMap trên RAM
 •	đọc đều tiến hành trên 1 instance duy nhất là HashMap
 •	ghi từ HashMap vào file (? Về performance write)
 Lưu ý: tốc độ đọc từ thẻ nhớ sdcard class 10 rất chậm (10MB/s). HD là 100MB/s.
 Hệ điều hành android tiêu tốn nhiều RAM hơn Window rất nhiều.
 Gọi SharedPreference có nghĩa là luôn lưu bộ nhớ trên RAM, ko có cách nào giải phóng nó.


 */

/**
 * có 2 cách tạo SharePreference:
 *  + Toàn cục trong phạm vi App dung id để xác định.
 *  + trong nội bộ Activity => chỉ có duy nhất file tương ứng với Activity.
 *  Cách dùng SharePreference với 2 phương pháp trên là như nhau
 */
public class SharePreferenceUtil {
    public static String TAG = SharePreferenceUtil.class.getSimpleName();
    /**
     * cach1: Reference la toan cuc tren toan App. Bat ky Activity goi toi deu nhu nhau
     */
    public void sharePreferenceApplication(Context context){
        String name_id = "id_reference_name"; // = 1 file
        //name_id giống tên file. Nếu chưa tồn tại thì tạo mới. Nếu tồn tại rồi thì mở nó ra.
        //tên là duy nhất trong toàn app. Bất kỳ context nào gọi đều refer đến duy nhất nó.
        SharedPreferences sharedPref =context.getSharedPreferences(name_id, Context.MODE_PRIVATE);//tạo map từ context

        //============================== write ============================================
        //Nên dùng apply() thay vì commit() để tránh ghi vào file làm giảm performance của hệ thống.
        // Android sẽ lo phần save vào file ta ko phải quan tâm. Các lệnh đọc từ file ra vẫn chính xác với cập nhật mới nhất.
        SharedPreferences.Editor editor = sharedPref.edit();

        // hightScore_key_id là string duy nhất mà ta chọn đại diện cho giá trí để read/write từ map của sharedpreference
        String highScore_key_id = "highScore"; // la key cua HashMap
        int scoreValue = 10;
        editor.putInt(highScore_key_id, scoreValue); //chưa lưu vào file

        editor.commit();//lưu editor vào file (synchronous) => trả về false nếu ko thành công

        //Nên dùng apply() thay vì commit()
        scoreValue = 13;
        editor.putInt(highScore_key_id, scoreValue); //chưa lưu vào file
        editor.apply(); //asynchronous command => trả về ngay mà ko quan tâm tới việc ghi hay ko. Android lo phan ghi xuong file

        //============================== read =============================================
        int defaultValue = 1; //gia tri mac dinh neu ko tim thay highScore_key_id
        int highScore = sharedPref.getInt(highScore_key_id,defaultValue);
        Log.d(TAG,"SharePreferenceUtil=" + highScore);

    }

    /**
     * giống cach 1 o tren , nhưng sharedRref này chỉ dùng ở duy nhất activity tạo ra nó vào đc
     * Activity chinh la id dai dien cho file đó.
     */
    public void sharePreferenceActivity(Activity activity){
        //co che singleton => luc nao can thi lay, ko can luu bien toan cuc lam gi
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);//tạo map từ context

        //============================== write ============================================
        //Nên dùng apply() thay vì commit() để tránh ghi vào file làm giảm performance của hệ thống.
        // Android sẽ lo phần save vào file ta ko phải quan tâm. Các lệnh đọc từ file ra vẫn chính xác với cập nhật mới nhất.
        SharedPreferences.Editor editor = sharedPref.edit();

        // hightScore_key_id là string duy nhất mà ta chọn đại diện cho giá trí để read/write từ map của sharedpreference
        String highScore_key_id = "highScore"; // la key cua HashMap
        int scoreValue = 10;
        editor.putInt(highScore_key_id, scoreValue); //chưa lưu vào file

        editor.commit();//lưu editor vào file (synchronous) => trả về false nếu ko thành công

        //Nên dùng apply() thay vì commit()
        scoreValue = 13;
        editor.putInt(highScore_key_id, scoreValue); //chưa lưu vào file
        editor.apply(); //asynchronous command => trả về ngay mà ko quan tâm tới việc ghi hay ko. Android lo phan ghi xuong file

        //============================== read =============================================
        int defaultValue = 1; //gia tri mac dinh neu ko tim thay highScore_key_id
        int highScore = sharedPref.getInt(highScore_key_id,defaultValue);
        Log.d(TAG,"SharePreferenceUtil=" + highScore);

    }
}
