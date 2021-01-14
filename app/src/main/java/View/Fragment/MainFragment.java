package View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.ActivityMainBinding;

import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.ListViewAdapter;
import ViewModel.stockViewModel;

public class MainFragment extends Fragment {
    private ActivityMainBinding binding;
    private ListViewAdapter adapter;
    private ViewGroup viewGroup;

    @Nullable 
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main, container,false);

        //createStock();
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //데이터 바인딩

        //Observer
        stockViewModel model = new ViewModelProvider(this).get(stockViewModel.class);
        ArrayList<Stock> noLiveStockList = model.getStockList().getValue(); // 이거 시현이가 쓰면 된다.

        ListView listview = viewGroup.findViewById(R.id.stocklist);
        adapter = new ListViewAdapter();
        String[] exStocks = {"삼성전자", "NAVER", "카카오", "셀트리온"};
        for (int i = 0; i < exStocks.length; i++) {
            Stock stock = new Stock(exStocks[i]);
            new DBA().initStock(getResources().getAssets(), stock);
            adapter.addItem(stock.getName(), stock.getStockCode(), stock.getCurrentPrice(), stock.getChangePrice(), stock.getChangeRate(), stock.getChange());
        }
        listview.setAdapter(adapter);

        Button search = viewGroup.findViewById(R.id.Button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //stockViewModel model = new ViewModelProvider(this).get(stockViewModel.class); // Observer
        //ArrayList<Stock> noLiveStockList = model.getStockList().getValue(); // 이거 시현이가 쓰면 된다.

        /*
        final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                //여기에서 값 업데이트
            }
        };

        model.getStockList().observe(this, stockObserver); //Observer
        */

        return viewGroup; 
    }
}
