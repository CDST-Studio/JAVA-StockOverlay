package View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.CustomListItemBinding;

import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.ListViewAdapter;
import ViewModel.stockViewModel;

public class MainFragment extends Fragment{
    private CustomListItemBinding binding;
    private ListViewAdapter adapter;
    private View viewGroup;

    private ArrayList<Stock> stocks = new ArrayList<>();
    public stockViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (View) inflater.inflate(R.layout.fragment_main, container,false);


        binding = CustomListItemBinding.inflate(getLayoutInflater()); //데이터 바인딩
        //RequestActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();
        //번들 안의 텍스트 불러오기
        ArrayList<Parcelable> text = bundle.getParcelableArrayList("stocks");
        //fragment1의 TextView에 전달 받은 text 띄우기
        for(int i=0; i<text.size(); i++) stocks.add((Stock)text.get(i));

        model = new ViewModelProvider(this).get(stockViewModel.class);

        ListView listview = viewGroup.findViewById(R.id.stocklist);
        adapter = new ListViewAdapter();

        Stock stock = new Stock();

        String[] exStocks = {"삼성전자", "NAVER", "동일제강", "셀트리온"};
        for (int i = 0; i < exStocks.length; i++) {
            Stock pstock = new Stock(exStocks[i]);
            new DBA().initStock(getResources().getAssets(), pstock);
            adapter.addItem(pstock.getName(), pstock.getStockCode(), pstock.getCurrentPrice(), pstock.getChangePrice(), pstock.getChangeRate(), pstock.getChange());

            model.addStockList(pstock);
            new stockViewModel().addStockList(pstock);//test를 위해 여기서 add
            Log.v("LiveData","add 현황 : " + pstock.getName());
            Log.v("LiveData","size 현황 : " + model.getStockList().getValue().size());
            Log.v("LiveData", "size 이름 : " + model.getStockList().getValue().get(0).getName());
            //stock.setName(pstock.getName());
            //stock.setStockCode(pstock.getStockCode());
            //stock.setCurrentPrice(pstock.getCurrentPrice());
            //stock.setChangePrice(pstock.getChangePrice());
            //stock.setChangeRate(pstock.getChangeRate());
            //stock.setChange(pstock.getChange());
            //binding.setStock(stock);
        }

        for (int i = 0; i < stocks.size(); i++) adapter.addItem(stocks.get(i).getName(), stocks.get(i).getStockCode(), stocks.get(i).getCurrentPrice(), stocks.get(i).getChangePrice(), stocks.get(i).getChangeRate(), stocks.get(i).getChange());
        listview.setAdapter(adapter);


        /*테스트*/
        Button search = viewGroup.findViewById(R.id.Button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                Log.v("threada","Observer발동");
                for(int k = 0; k < exStocks.length; k++){
                    Stock pstock = model.getStockList().getValue().get(k);
                    stock.setName(pstock.getName());
                    stock.setStockCode(pstock.getStockCode());
                    stock.setCurrentPrice(pstock.getCurrentPrice());
                    stock.setChangePrice(pstock.getChangePrice());
                    stock.setChangeRate(pstock.getChangeRate());
                    stock.setChange(pstock.getChange());
                    binding.setStock(stock);
                    //adapter.addItem(pstock.getName(), pstock.getStockCode(), pstock.getCurrentPrice(), pstock.getChangePrice(), pstock.getChangeRate(), pstock.getChange());
                }
            }
        };

        model.getStockList().observe(getViewLifecycleOwner(),stockObserver);

        return viewGroup;
    }
}
