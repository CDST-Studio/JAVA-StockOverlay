package View.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.Adapter.ListEditAdapter;
import View.Service.ItemTouchHelperCallback;
import ViewModel.MainViewModel;

public class EditFragment extends Fragment {
    private ArrayList<Stock> stocks = new ArrayList<>();

    private View EditView;
    private RecyclerView recyclerView;
    private ListEditAdapter adapter;
    private MainViewModel model;
    private AdView mAdView;
    private ItemTouchHelper helper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 초기화
        init(inflater, container);

        // viewmodel 초기화
        model = new ViewModelProvider(this).get(MainViewModel.class);
        adapter.setMainViewModel(model);

        ArrayList<Stock> stockList = model.getStockList().getValue();
        if(stockList != null && stockList.size()>0) {
            for (int i = 0; i < stockList.size(); i++) {
                stocks.add(stockList.get(i));
                adapter.addItem(stocks.get(i).getName(), stocks.get(i).getStockCode());
            }
        }
        recyclerView.setAdapter(adapter);

        // 옵저버
        final Observer<ArrayList<Stock>> stockObserver = new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stocks) {
                adapter.setItem(model.getStockList().getValue());
            }
        };

        //옵저버 스타트
        model.getStockList().observe(getViewLifecycleOwner(),stockObserver);

        return EditView;
    }

    // ---------------- 리사이클러뷰 및 광고 생성하는 메서드 ----------------
    public void init(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        EditView = (View)inflater.inflate(R.layout.fragment_edit, container, false);

        recyclerView = (RecyclerView)EditView.findViewById(R.id.edit_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ListEditAdapter();

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(recyclerView);

        // 애드몹 광고창
        mAdView = EditView.findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
