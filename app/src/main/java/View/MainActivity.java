package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.ActivityMainBinding;

import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.Service.OverlayService;
import ViewModel.stockViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToolbar();
        //createStock();
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //데이터 바인딩

        //Observer
        stockViewModel model = new ViewModelProvider(this).get(stockViewModel.class);
        ArrayList<Stock> noLiveStockList = model.getStockList().getValue(); // 이거 시현이가 쓰면 된다.

        //stockViewModel model = new ViewModelProvider(this).get(stockViewModel.class); // Observer
        //ArrayList<Stock> noLiveStockList = model.getStockList().getValue(); // 이거 시현이가 쓰면 된다.

        ListView listview = (ListView)findViewById(R.id.ListStock);
        adapter = new ListViewAdapter();
        for (int i = 0; i < 50; i++) {
            if(i==0) {
                Stock stock = new Stock("삼성전자");
                new DBA().initStock(getResources().getAssets(), stock);
                adapter.addItem(stock.getName(), stock.getStockCode(), stock.getCurrentPrice(), stock.getChangePrice(), stock.getChangeRate(), stock.getChange());
            }
            adapter.addItem("주식이름"+i ,"주식코드"+i,"현재가격"+i,
                    "변동가격"+i,"변동률"+i,"▲");
        }
        listview.setAdapter(adapter);

        Button search = findViewById(R.id.Button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        /*final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                //여기에서 값 업데이트
            }
        };

        model.getStockList().observe(this, stockObserver); //Observer
        */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout :
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //  -------------- 기타 메서드 --------------
    private void callSearchStockActivity(){ //main에서 상세 검색 창으로 이동
        Intent search_stockIntent = new Intent(this, SearchActivity.class);
        startActivity(search_stockIntent);
    }

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


