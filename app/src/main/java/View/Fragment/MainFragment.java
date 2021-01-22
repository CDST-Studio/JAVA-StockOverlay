package View.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.databinding.CustomListItemBinding;

import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.ListViewAdapter;
import View.Service.OverlayService;
import ViewModel.stockViewModel;

public class MainFragment extends Fragment{
    private CustomListItemBinding binding;

    private ListViewAdapter adapter;
    private View viewGroup;
    //private ViewGroup viewGroup;
    private ArrayList<Stock> stocks = new ArrayList<>();
    public stockViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (View) inflater.inflate(R.layout.fragment_main, container,false);

        // LocalBroadcast 등록
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.parkho.broadcat.local");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(OverlayService.mMsgReceiver, intentFilter);

        //RequestActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();

        model = new ViewModelProvider(this).get(stockViewModel.class);

        //번들 안의 텍스트 불러오기
        ArrayList<Parcelable> text = bundle.getParcelableArrayList("stocks");
        ListView listview = viewGroup.findViewById(R.id.stocklist);
        adapter = new ListViewAdapter();

        //fragment1의 TextView에 전달 받은 text 띄우기
        for(int i=0; i<text.size(); i++) {
            stocks.add((Stock)text.get(i));
            adapter.addItem(stocks.get(i).getName(), stocks.get(i).getStockCode(), stocks.get(i).getCurrentPrice(), stocks.get(i).getChangePrice(), stocks.get(i).getChangeRate(), stocks.get(i).getChange());
            model.addStockList(stocks.get(i));
        }
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
                Log.v("threada", "Observer발동");
                adapter.setItem(model.getStockList().getValue());
                sendStocks(model.getStockList().getValue());
                /*
                for(int i = 0; i < model.getStockList().getValue().size(); i++){
                    Log.v("databind","LiveDataList : " + model.getStockList().getValue().get(i).getName());
                }
                */
            }
        };
        model.getStockList().observe(getViewLifecycleOwner(),stockObserver);

        return viewGroup;
    }

    // 오버레이 뷰 서비스로 관심종목 전달
    public void sendStocks(ArrayList<Stock> stocklist) {
        Intent intent = new Intent(getContext(), OverlayService.class);
        intent.putExtra("stocks", stocklist);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        Log.d("메세지 전달", "MainFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // LocalBroadcast 등록 해제
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(OverlayService.mMsgReceiver);
    }
}
