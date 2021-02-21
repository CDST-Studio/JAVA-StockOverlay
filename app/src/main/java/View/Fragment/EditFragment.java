package View.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;
import View.Adapter.ListEditAdapter;
import View.Service.ItemTouchHelperCallback;
import ViewModel.MainViewModel;

public class EditFragment extends Fragment {

    private View EditView;
    private RecyclerView recyclerView;
    private ListEditAdapter adapter;
    private MainViewModel model;
    private AdView mAdView;
    private ArrayList<Stock> stocks = new ArrayList<>();
    private ArrayList<Stock> test = new ArrayList<>();
    private Thread priceTh;
    private ItemTouchHelper helper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EditView = (View)inflater.inflate(R.layout.fragment_edit, container, false);

        init();

        // 애드몹 광고창
        mAdView = EditView.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // RequestActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();

        // viewmodel 초기화
        model = new ViewModelProvider(this).get(MainViewModel.class);

        // 번들 안의 텍스트 불러오기
        ArrayList<Parcelable> text = bundle.getParcelableArrayList("stocks");
        adapter.setMainViewModel(model);

        // fragment1의 TextView에 전달 받은 text 띄우기
        if(text!=null && text.size()>0) {
            for (int i = 0; i < text.size(); i++) {
                stocks.add((Stock) text.get(i));
                adapter.addItem(stocks.get(i).getName(), stocks.get(i).getStockCode());
            }
        }
        recyclerView.setAdapter(adapter);

        // 옵저버
        final androidx.lifecycle.Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stockArray) {
                adapter.setItem(model.getStockList().getValue());
            }
        };

        //옵저버 스타트
        model.getStockList().observe(getViewLifecycleOwner(),stockObserver);

        return EditView;
    }

    // 리사이클러뷰 생성하는 메소드
    public void init() {
        recyclerView = (RecyclerView)EditView.findViewById(R.id.edit_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ListEditAdapter();

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(recyclerView);
    }
}
