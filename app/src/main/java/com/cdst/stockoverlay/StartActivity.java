package com.cdst.stockoverlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import View.LoginActivity;

public class StartActivity extends AppCompatActivity {
    private String versionName = BuildConfig.VERSION_NAME;
    private Double version = Double.parseDouble(versionName);
    private String [] permission_list = {
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 애드몹 초기화
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("App").document("Version");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double CURRENT_VERSION = Double.parseDouble(document.getData().get("version").toString());
                        if(version < CURRENT_VERSION) {
                            AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getApplicationContext())
                                    .setTitle("업데이트 필요")
                                    .setMessage("앱 버전이 낮습니다.\n" +
                                            "스토어에서 업데이트 해주시기 바랍니다.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                                            String url = "https://play.google.com/store/apps/details?id=com.cdst.stockoverlay";
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                            AlertDialog msgDlg = msgBuilder.create(); msgDlg.show();
                        }else {
                            // 권한 확인
                            checkPermission();

                            startActivity(new Intent(StartActivity.this, LoginActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR, Document not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR, Collection not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //  -------------- 다른 앱 위에 그리기 권한 및 각종 권한을 사용자에게 요구하는 소스 코드 -------------
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