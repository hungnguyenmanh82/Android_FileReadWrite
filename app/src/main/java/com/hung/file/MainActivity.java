package com.hung.file;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Xem mục AndroidTest.
 * Vì phần này liên quan tới Phone nên phần Unit test phải chạy trên Androidtest (Ko phải phần test).
 * step1:
 *   + voi android sdk <23 => permission o manifest file.
 *   + android sdk >= 23 (android M) thì cap quyen  động như ở duoi
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sdk = 23 => Android 6.0, check permission at runtime
        if (Build.VERSION.SDK_INT >= 23) {
            askForPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    },//for both GPS and Network Provider
                    REQUEST_PERMISSIONS_CODE);
            //Manifest.permission.READ_LOGS => chỉ ránh cho System App, vì thế PackageManager.PERMISSION_GRANTED = fail
            // nếu check và request Permission này sẽ tạo ra vòng lặp ở MainThread chiếm dụng Mainthread => Click buttons not working

        }
    }

    static final int REQUEST_PERMISSIONS_CODE = 1982;


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (checkSelfPermission(permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                //permision which have not been granted need to be request again
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            //nếu gửi 2 lệnh request liên tiếp thì, request đầu tiên sẽ đc xử lý, request sau sẽ bị loại bỏ
            // vì thế chỉ gửi từng request 1 thôi
            // nếu có nhiều permision thì phải đưa chúng vào Array cho 1 request thôi (dã test)
            requestPermissions(permissionsToRequest.toArray(new
                    String[permissionsToRequest.size()]), requestCode);
        }
    }

    /*
     * Permission on for UI (Activity, Fragment). Service can not receive the response from the OS
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {//dùng chung permission code hoặc riêng thì tùy
            //check xem permission nào đc granted/deny
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //
                        Log.d(TAG, "WRITE_EXTERNAL_STORAGE ok");
                    } else {
                        //

                    }
                }
            }
        }

    }

}
