package com.needfor.stockoverlay;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import View.LoginActivity;
import View.MainActivity;
import View.Service.OverlayService;

public class StartActivity extends AppCompatActivity {
    private String [] permission_list = {
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        checkPermission();

        //startActivity(new Intent(StartActivity.this, LoginActivity.class));
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
    }

    //  -------------- 다른 앱 위에 그리기 권한 및 각종 권한을 사용자에게 요구하는 소스 코드 --------------
    public void checkPermission() {
        // 마시멜로우 이상일 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 각 권한의 허용 여부를 확인한다.
            for(String permission : permission_list){
                // 권한 허용 여부를 확인한다.
                int chk = checkCallingOrSelfPermission(permission);
                // 거부 상태라고 한다면
                if(chk == PackageManager.PERMISSION_DENIED){
                    // 사용자에게 권한 허용여부를 확인하는 창을 띄운다.
                    requestPermissions(permission_list, 0);
                }
            }
        }
    }
}