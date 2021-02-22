package View.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;
import View.Dialog.NicknameDialog;
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

        if(NicknameDialog.SIGN_IN_FLAG == 1) {
            // 내부저장소에 저장된다는 경고문구
            new AlertDialog.Builder(getContext())
                    .setTitle("필독사항")
                    .setMessage("해당 사유에 대한 책임은 본인에게 있습니다.\n" +
                            "1. 본 앱의 모든 데이터는 내부에 저장되므로\n앱 삭제시 데이터를 복구할 수 없습니다.\n" +
                            "2. 본 앱의 갱신속도가 느릴 수 있으므로\n유의해주시기 바랍니다.\n" +
                            "3. 본 앱은 데이터를 데이터를 사용합니다.\n\n" +
                            "사용설명서는 설정에 있습니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which){
                            Toast.makeText(getContext(), "환영합니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
            NicknameDialog.SIGN_IN_FLAG = 0;
        }

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

        // 상단 안내 문구 중 "손익", "목표수익" 관련 문구 제어 명령줄
        TextView profit = (TextView)viewGroup.findViewById(R.id.profit);
        TextView targetProfit = (TextView)viewGroup.findViewById(R.id.targetprice);
        if(MainActivity.PURCHASE_PRICE_INPUT_FLAG == 1) {
            profit.setText("손익");
            targetProfit.setText("목표수익");
        }else {
            profit.setText("");
            targetProfit.setText("");
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
        priceTh.interrupt();
        MAINFRAGMENT_ON_ACTIVITY = 0;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!priceTh.isInterrupted()) priceTh.interrupt();
    }
}