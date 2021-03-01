package View.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.cdst.stockoverlay.R;

import java.io.File;
import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.Service.ItemTouchHelperListener;
import ViewModel.MainViewModel;
import ViewModel.OverlayViewModel;

public class ListEditAdapter extends RecyclerView.Adapter<ListEditAdapter.MainHolder> implements ItemTouchHelperListener {
    private ArrayList<Stock> listViewItemList = new ArrayList<>();
    private ArrayList<Stock> stocks;

    private File file;
    private String user;
    private Stock listViewItem;
    private MainHolder mainHolder;

    private static MainViewModel mainViewModel;
    private OverlayViewModel overlayViewModel = new OverlayViewModel();

    private DBA dba = new DBA();

    // -------------- 생성자 --------------
    public ListEditAdapter() { }

    // -------------- 뷰모델 세팅 --------------
    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    //-------------- 객체지정 및 이벤트 처리 --------------
    public class MainHolder extends RecyclerView.ViewHolder  {
        public TextView StockName, StockCode;
        public Button remove;
        public String stockName;

        @SuppressLint("ClickableViewAccessibility")
        public MainHolder(@NonNull View view) {
            super(view);
            StockName = view.findViewById(R.id.edit_name);
            StockCode = view.findViewById(R.id.edit_code);
            remove = view.findViewById(R.id.Button_remove);

            file = view.getContext().getDatabasePath("User");
            user = dba.getNickname(file);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        listViewItem = listViewItemList.get(pos);
                        stockName = listViewItem.getName();

                        try {
                            stocks = mainViewModel.getStockList().getValue();
                            int idx = 0;
                            for (int i = 0; i < stocks.size(); i++) {
                                if (stocks.get(i).getName().equals(stockName)) idx = i;
                            }
                            dba.subInterestedStocks(file, user, stockName);
                            stocks.remove(idx);
                            mainViewModel.getStockList().setValue(stocks);
                            overlayViewModel.getStockList().setValue(stocks);
                        } catch (Exception e) { }
                    }
                }
            });
        }
    }

    //-------------- 뷰홀더 생성 --------------
    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item3, parent, false);
        mainHolder = new MainHolder(holderView);
        return mainHolder;
    }

    //-------------- pos가 필요한 객체 결합 --------------
    @Override
    public void onBindViewHolder(@NonNull MainHolder mainHolder, int position) {
        listViewItem = listViewItemList.get(position);
        mainHolder.StockName.setText(listViewItem.getName());
        mainHolder.StockCode.setText(listViewItem.getStockCode());
    }

    //-------------- 수정사항 저장 메서드 --------------
    public void saveEditedList() {
        // 데이터 저장
        mainViewModel.getStockList().setValue(listViewItemList);
        overlayViewModel.getStockList().setValue(listViewItemList);

        // DB 반영
        ArrayList<String> result = new ArrayList<>();
        for(int i=0; i<listViewItemList.size(); i++) result.add(listViewItemList.get(i).getName());

        dba.updateInterestedStocks(file, result);
    }

    //-------------- 움직임 감지 메서드 --------------
    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        Stock stock = listViewItemList.get(from_position);
        //이동할 객체 삭제
        listViewItemList.remove(from_position);
        // 이동하고 싶은 position에 추가
        listViewItemList.add(to_position, stock);
        // Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);

        return true;
    }

    //-------------- ArrayList에 아이템 추가 --------------
    public void addItem(String searchname, String searchcode) {
        Stock item = new Stock();
        item.setName(searchname);
        item.setStockCode(searchcode);
        listViewItemList.add(item);
    }

    //-------------- 뷰모델 세팅 관련 메서드 --------------
    public void setItem(ArrayList<Stock> stocks) {
        if(stocks.size() > 0) {
            if (stocks.size() < listViewItemList.size()) {
                int flag = 0;
                for (int i = 0; i < listViewItemList.size(); i++) {
                    for (int k = 0; k < stocks.size(); k++) {
                        if (listViewItemList.get(i).getName().equals(stocks.get(k).getName()))
                            break;
                        if (k == stocks.size() - 1) flag = 1;
                    }
                    if (flag == 1) {
                        listViewItemList.remove(i);
                        flag = 0;
                    }
                }
            } else if (stocks.size() > listViewItemList.size()) {
                for (int i = listViewItemList.size(); i < stocks.size(); i++) {
                    listViewItemList.add(stocks.get(i));
                }
            } else {
                for (int i = 0; i < stocks.size(); i++)
                    if (listViewItemList.get(i) != stocks.get(i)) {
                        listViewItemList.set(i, stocks.get(i));
                    }
            }

            for (int i = 0; i < listViewItemList.size(); i++) {
                listViewItemList.get(i).setChange(stocks.get(i).getChange());
                listViewItemList.get(i).setChangePrice(stocks.get(i).getChangePrice());
                listViewItemList.get(i).setChangeRate(stocks.get(i).getChangeRate());
                listViewItemList.get(i).setCurrentPrice(stocks.get(i).getCurrentPrice());
                this.notifyDataSetChanged();
            }
        }else {
            listViewItemList = new ArrayList<>();
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() { return  listViewItemList.size(); }
}