package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Model.Stock;
import ViewModel.stockViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //데이터 바인딩
        //createStock();
        setContentView(R.layout.activity_main);

        //Observer
        stockViewModel model = new ViewModelProvider.of(this).get(stockViewModel.class);

        ArrayList<Stock> noLiveStockList = model.getStockList().getValue(); // 이거 시현이가 쓰면 된다.

        final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                //여기에서 값 업데이트
            }
        };

        model.getStockList().observe(this, stockObserver);
        //Observer

        Button Button_search = binding.ButtonSearch;
        Button_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callSearchStockActivity();
            }
        });

        Button Button_Bookmark = binding.ButtonBookmark;
        Button_Bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                callBookmarkActivity();
            }
        });



    }
    private void callSearchStockActivity(){ //main에서 상세 검색 창으로 이동
        Intent search_stockIntent = new Intent(this, Search_Stock.class);
        startActivity(search_stockIntent);
    }

    private void callBookmarkActivity(){ //main에서 bookmark로 이동
        Intent search_BookmarkIntent = new Intent(this, Bookmark.class);
        startActivity(search_BookmarkIntent);
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


