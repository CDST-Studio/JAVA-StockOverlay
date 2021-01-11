package com.needfor.stockoverlay;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import Model.Stock;
import Model.User;
import Module.Crawling;
import Module.DBA;
import Module.Parsing;
import Module.Search;

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
        AssetManager assetManager = getResources().getAssets();
        Stock s = new Stock("삼성전자");
        new DBA().initStock(assetManager, s);
        */

        /*
        관심종목 관련 예제
        // 관심종목 추가
        new DBA().addInterestedStocks(getDatabasePath("Stock"), "NAVER");
        
        // 관심종목 모두 불러오기
        ArrayList<String> test = new DBA().getInterestedStocks(getDatabasePath("Stock"));
        for(String s:test) Log.d("DB read test", s);

        // 관심종목 삭제
        new DBA().subInterestedStocks(getDatabasePath("Stock"), "현대자동차");

        test = new DBA().getInterestedStocks(getDatabasePath("Stock"));
        for(String s:test) Log.d("DB read test", s);
         */

        /*
        크롤링 예제
        AssetManager assetManager = getResources().getAssets();
        Crawling ct = new Crawling(new Parsing().getStockCode(assetManager, "삼성전자"));
        Log.d("Crawling test", ct.currentPrice());
        Log.d("Crawling test", ct.change());
        Log.d("Crawling test", ct.changePrice());
        Log.d("Crawling test", ct.changeRate());
        Log.d("Crawling test", ct.codeToName());
         */

        /*
        검색 예제
        AssetManager assetManager = getResources().getAssets();

        Stock searchStock1 = new Search().searchStock(assetManager,"삼성전자");
        Log.d("Search test", searchStock1.getName());
        Log.d("Search test", searchStock1.getStockCode());
        Log.d("Search test", searchStock1.getDetailCode());
        Log.d("Search test", searchStock1.getChange());
        Log.d("Search test", searchStock1.getChangeRate());
        Log.d("Search test", searchStock1.getChangePrice());

        Stock searchStock2 = new Search().searchStock(assetManager,"086900");
        Log.d("Search test 2", searchStock2.getName());
        Log.d("Search test 2", searchStock2.getStockCode());
        Log.d("Search test 2", searchStock2.getDetailCode());
        Log.d("Search test 2", searchStock2.getChange());
        Log.d("Search test 2", searchStock2.getChangeRate());
        Log.d("Search test 2", searchStock2.getChangePrice());
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