package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.ActivityMainBinding;
import View.ListViewAdapter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);


        //stockViewModel model = new ViewModelProvider(this).get(stockViewModel.class); // Observer
        //ArrayList<Stock> noLiveStockList = model.getStockList().getValue(); // 이거 시현이가 쓰면 된다.

        ListView listview = (ListView)findViewById(R.id.ListStock);
        adapter = new ListViewAdapter();
        for (int i = 0; i < 50; i++) {
            adapter.addItem("주식이름"+i ,"주식코드"+i,"현재가격"+i,
                    "변동가격"+i,"변동률"+i,"▲");
        }
        listview.setAdapter(adapter);

        /*final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                //여기에서 값 업데이트
            }
        };

        model.getStockList().observe(this, stockObserver); //Observer */

        Button Button_search = binding.ButtonSearch;
        Button_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callSearchStockActivity();
            }
        });



    }
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


