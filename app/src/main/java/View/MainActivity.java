package View;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.ActivityMainBinding;

import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.Fragment.MainFragment;
import View.Service.OverlayService;
import ViewModel.stockViewModel;

public class MainActivity extends AppCompatActivity {
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    private BottomNavigationView bottomNavigationView;
    private Messenger mServiceMessenger = null;
    private boolean mIsBound;

    private String[] exStocks = {"삼성전자", "NAVER", "카카오", "셀트리온"};
    private ArrayList<Stock> stocks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToolbar(); // 상단 네비게이션 바 생성
        createBottomNavigation(); // 하단 네비게이션 바 생성

        MainFragment mainFragment = new MainFragment();

        // 제일 처음 띄워줄 뷰를 세팅, commit();까지 해줘야 함
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_zone, mainFragment).commitAllowingStateLoss();

        // 종목 초기화 및 관심종목 프래그먼트로 전달
        for(int i=0; i<exStocks.length; i++)  stocks.add(new DBA().getStock(getResources().getAssets(), exStocks[i]));
        // 번들객체 생성, text값 저장
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("stocks",stocks);
        // mainFragment로 번들 전달
        mainFragment.setArguments(bundle);
    }

    //  -------------- 앱바(액션바) 및 메뉴 생성 메서드 --------------
    public void createToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //  -------------- 하단 네비게이션 바 생성 메서드 --------------
    @SuppressLint("ResourceType")
    public void createBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView); // 하단 액션바(네비게이션 바, 툴바)
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { 
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) { 
                switch (menuItem.getItemId()) { 
                    case R.id.tab1:

                        return true;
                    case R.id.tab2:
                         startActivity(new Intent(MainActivity.this, SearchActivity.class));
                         return true;
                    case R.id.tab3:
                         return true;
                    case R.id.tab4:
                        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

                        int overlayServiceFlag = 0;
                        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {
                            if (OverlayService.class.getName().equals(rsi.service.getClassName())) {
                                overlayServiceFlag = 1;
                                Toast.makeText(getApplicationContext(), "스톡보드가 이미 실행 중입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(overlayServiceFlag == 0) checkPermission();
                        return true;
                    default: return false; 
                } 
            } 
        });
    }

    //  -------------- 스톡보드 실행용 메서드 모음 --------------
    /** 다른 앱 위에 그리기 권한 및 각종 권한을 사용자에게 요구하는 메서드 */
    public void checkPermission() {
        // 마시멜로우 이상일 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 다른앱 위에 그리기 체크
            if (!Settings.canDrawOverlays(this)) {
                Uri uri = Uri.fromParts("package" , getPackageName(), null);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startOverlay();
            }
        } else {
            startOverlay();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                finish();
            } else {
                startOverlay();
            }
        }
    }

    void startOverlay(){
        Intent intent = new Intent(getApplicationContext(), OverlayService.class);
        intent.putExtra("stocks", stocks);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    //  -------------- 기타 메서드 --------------
    /*private void createStock() { // 주식 레이아웃 동적 생성 크롤링한 Array 출력
        LinearLayout Layout_stock = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Layout_stock.setLayoutParams(params);
        Button btn = new Button(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        btn.setLayoutParams(lp);
        btn.setText("test");
        Layout_stock.addView(btn);
        setContentView(Layout_stock);
        //TextView textstock = new TextView(getApplication());
    }
     */

}


