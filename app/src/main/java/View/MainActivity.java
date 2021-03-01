package View;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cdst.stockoverlay.StartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;
import Model.User;
import Module.DBA;
import View.Adapter.ListEditAdapter;
import View.Fragment.EditFragment;
import View.Fragment.MainFragment;
import View.Fragment.SearchFragment;
import View.Fragment.SettingFragment;
import View.Service.OverlayService;
import ViewModel.MainViewModel;
import ViewModel.OverlayViewModel;

public class MainActivity extends AppCompatActivity {
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    public static int PURCHASE_PRICE_INPUT_FLAG = 1;

    private BottomNavigationView bottomNavigationView;

    private MainFragment mainFragment = new MainFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private EditFragment editFragment = new EditFragment();

    private ArrayList<Stock> stocks = new ArrayList<>();
    private User user = new User();

    private MainViewModel mainViewModel;
    private OverlayViewModel viewModel;
    private Observer<ArrayList<Stock>> overlayObserver;

    private ImageButton editBtn;
    private Button editComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToolbar(); // 상단 네비게이션 바 생성
        createBottomNavigation(); // 하단 네비게이션 바 생성

        // 매입가 입력여부 체크
        if(getConfigValue(getApplicationContext(), "purchaseSwitch") != null) {
            if(getConfigValue(getApplicationContext(), "purchaseSwitch").equals("ON")) {
                PURCHASE_PRICE_INPUT_FLAG = 1;
            }else {
                PURCHASE_PRICE_INPUT_FLAG = 0;
            }
        }

        // 편집 버튼
        editBtn = findViewById(R.id.edit_button);
        // 편집 완료 버튼
        editComplete = findViewById(R.id.edit_complete);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit 프래그먼트로 교채
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_zone, editFragment).commitAllowingStateLoss();
                editComplete.setVisibility(View.VISIBLE);
            }
        });
        editComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_zone, mainFragment).commitAllowingStateLoss();
                editComplete.setVisibility(View.INVISIBLE);
            }
        });

        // 유저 초기화
        user.setNickName(new DBA().getNickname(getDatabasePath("User")));
        new DBA().initInterestedStocks(getDatabasePath("User"), user);

        // 종목 초기화 및 관심종목 프래그먼트로 전달
        for (int i = 0; i < user.getInterestedStocks().size(); i++) {
            stocks.add(new DBA().getStock(getAssets(), user.getInterestedStocks().get(i).toString()));
        }

        //오버레이 observer를 여기에 set
        //라이브데이터에 observer를 add하기 위해서는 라이브데이터는 항상 Provider를 통해 생성해 줘야한다.
        //하지만 백그라운드에서 돌아갈 방법이 없으므로 Main에서 set 한 후 스톡보드 실행 지점에서 observer를 activty하고 스톡보드 종류 지점에서 removeObserver를 해준다.
        //이렇게 하는 이유는 observerForever은 owner가 항상 active상태인것처럼 동작하므로 자동으로 해제되지 않는다.
        viewModel = new ViewModelProvider(this).get(OverlayViewModel.class);
        mainFragment.setOverlayViewModel(viewModel);

        // 스톡보드 상태 초기화
        setConfigValue(getApplicationContext(), "stockBoardStart", "stop");

        // 제일 처음 띄워줄 뷰를 세팅, commit();까지 해줘야 함
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_zone, mainFragment).commitAllowingStateLoss();

        // 매인 뷰모델에 값 할당
        mainViewModel = new MainViewModel();
        mainViewModel.getStockList().setValue(stocks);

        //Observer 정의
        overlayObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stocks) {
                new OverlayService().overServiceObserver();
            }
        };
    }

    // -------------- 앱바(액션바) 및 메뉴 생성 메서드 --------------
    public void createToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    // -------------- 하단 네비게이션 바 생성 메서드 --------------
    @SuppressLint("ResourceType")
    public void createBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView); // 하단 액션바(네비게이션 바, 툴바)
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                editComplete.setVisibility(View.INVISIBLE);
                switch (menuItem.getItemId()) {
                    case R.id.tab1:
                        // Setting 프래그먼트로 교채
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_zone, settingFragment).commitAllowingStateLoss();
                        return true;
                    case R.id.tab2:
                        // Search 프래그먼트로 교채
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_zone, searchFragment).commitAllowingStateLoss();
                        return true;
                    case R.id.tab3:
                        // Main 프래그먼트로 교채
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_zone, mainFragment).commitAllowingStateLoss();
                        return true;
                    case R.id.tab4:
                        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

                        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {
                            if (OverlayService.class.getName().equals(rsi.service.getClassName())) {
                                Toast.makeText(getApplicationContext(), "스톡보드가 이미 실행 중입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(getConfigValue(getApplicationContext(),"stockBoardStart").equals("stop") && viewModel.getStockList().getValue() != null) checkPermission();
                        else if(viewModel.getStockList().getValue() == null) Toast.makeText(getApplicationContext(), "관심종목 추가 후 스톡보드를 켜주세요.", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) finish();
            else startOverlay();
        }
    }

    public void startOverlay() {
        Intent intent = new Intent(getApplicationContext(), OverlayService.class);
        intent.putExtra("stocks", stocks);

        viewModel.getStockList().observeForever(overlayObserver);
        setConfigValue(getApplicationContext(),"stockBoardStart", "start");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent);
        else startService(intent);
    }

    //  -------------- 기타 메서드 --------------
    // Preference 읽기
    public static String getConfigValue(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, null);
    }

    // Preference 쓰기
    public static void setConfigValue(Context context, String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}


