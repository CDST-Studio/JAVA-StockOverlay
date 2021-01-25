package View.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.CustomListItemBinding;

import java.util.ArrayList;

import Model.Stock;
import View.ListViewAdapter;
import ViewModel.MainViewModel;
import ViewModel.Thread.PriceThread;

public class MainFragment extends Fragment{
    private CustomListItemBinding binding;

    private ListViewAdapter adapter;
    private View viewGroup;
    private ArrayList<Stock> stocks = new ArrayList<>();
    private MainViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (View) inflater.inflate(R.layout.fragment_main, container,false);

        // RequestActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();

        // viewmodel 초기화
        model = new ViewModelProvider(this).get(MainViewModel.class);

        // 번들 안의 텍스트 불러오기
        ArrayList<Parcelable> text = bundle.getParcelableArrayList("stocks");
        ListView listview = viewGroup.findViewById(R.id.stocklist);
        adapter = new ListViewAdapter();

        // fragment1의 TextView에 전달 받은 text 띄우기
        for(int i=0; i<text.size(); i++) {
            stocks.add((Stock)text.get(i));
            adapter.addItem(stocks.get(i).getName(), stocks.get(i).getStockCode(), stocks.get(i).getCurrentPrice(), stocks.get(i).getChangePrice(), stocks.get(i).getChangeRate(), stocks.get(i).getChange());
            model.addStockList(stocks.get(i));
        }
        listview.setAdapter(adapter);

        // 옵저버
        final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                adapter.setItem(model.getStockList().getValue());
            }
        };
        model.getStockList().observe(getViewLifecycleOwner(),stockObserver);


        // 쓰레드 스타트
        new Thread(new PriceThread()).start();

        return viewGroup;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
