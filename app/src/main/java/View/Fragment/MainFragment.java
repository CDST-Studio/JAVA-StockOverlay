package View.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.needfor.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;
import View.MainActivity;
import View.Adapter.ListViewAdapter;
import ViewModel.MainViewModel;
import ViewModel.Thread.PriceThread;

public class MainFragment extends Fragment {
    public static int MAINFRAGMENT_ON_ACTIVITY = 0;

    private AdView mAdView;
    private ListViewAdapter adapter;
    private MainViewModel model;
    private View viewGroup;

    private ArrayList<Stock> stocks = new ArrayList<>();
    private Thread priceTh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (View) inflater.inflate(R.layout.fragment_main, container,false);

        // 애드몹 광고창
        mAdView = viewGroup.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // 실행중으로 변경
        MAINFRAGMENT_ON_ACTIVITY = 1;

        // RequestActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();

        // viewmodel 초기화
        model = new ViewModelProvider(this).get(MainViewModel.class);

        // 상단 안내 문구 중 "손익" 관련 문구 제어 명령줄
        TextView profit = (TextView)viewGroup.findViewById(R.id.profit);
        if(MainActivity.PURCHASE_PRICE_INPUT_FLAG == 1) {
            if(!profit.getText().equals("손익")) profit.setText("손익");
        }else {
            profit.setText("");
        }

        // 번들 안의 텍스트 불러오기
        ArrayList<Parcelable> text = bundle.getParcelableArrayList("stocks");
        ListView listview = viewGroup.findViewById(R.id.stocklist);
        adapter = new ListViewAdapter();
        adapter.setMainViewModel(model);

        // fragment1의 TextView에 전달 받은 text 띄우기
        for(int i=0; i<text.size(); i++) {
            stocks.add((Stock)text.get(i));
            adapter.addItem(stocks.get(i).getName(), stocks.get(i).getStockCode(), stocks.get(i).getCurrentPrice(), stocks.get(i).getChangePrice(), stocks.get(i).getChangeRate(), stocks.get(i).getChange());
        }
        listview.setAdapter(adapter);

        // 옵저버
        final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                adapter.setItem(model.getStockList().getValue());
            }
        };

        //옵저버 스타트
        model.getStockList().observe(getViewLifecycleOwner(),stockObserver);

        // 쓰레드 스타트
        priceTh = new Thread(new PriceThread());
        priceTh.start();

        return viewGroup;
    }

    @Override
    public void onStop() {
        super.onStop();

        MAINFRAGMENT_ON_ACTIVITY = 0;
        priceTh.interrupt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!priceTh.isInterrupted()) priceTh.interrupt();
    }
}