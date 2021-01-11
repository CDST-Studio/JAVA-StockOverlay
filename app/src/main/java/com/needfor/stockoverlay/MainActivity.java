package com.needfor.stockoverlay;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import Model.User;
import Module.Crawling;
import Module.DBA;
import Module.Parsing;

public class MainActivity extends AppCompatActivity {
    private String [] permission_list = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkPermission();

        /*
        주식 객체 예제
        Stock s = new Stock("삼성전자", "005930", "032604");
         */

        new DBA().initInterestedStocks(getDatabasePath("Stock"), "삼성전자");
        ArrayList<String> test = new DBA().readInterestedStocks(getDatabasePath("Stock"));
        Log.d("DB read test", test.get(0));
        /*
        AssetManager assetManager = getApplicationContext().getAssets();
        Crawling ct = new Crawling(new Parsing().getStockCode(assetManager, "삼성전자"));
        Log.d("Crawling test", ct.currentPrice());
        Log.d("Crawling test", ct.change());
        Log.d("Crawling test", ct.changePrice());
        Log.d("Crawling test", ct.changeRate());
        Log.d("Crawling test", ct.codeToName());
         */
        
        /*
        검색 예제
        Stock searchStock1 = new Search().searchStock("삼성전자");
        Stock searchStock2 = new Search().searchStock("086900");
         */
    }

    // 권한 확인 메서드
    public void checkPermission(){
        // 현재 안드로이드 버전이 6.0 미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
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