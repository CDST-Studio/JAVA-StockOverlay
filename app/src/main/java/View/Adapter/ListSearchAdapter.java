package View.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.needfor.stockoverlay.R;

import java.io.File;
import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import ViewModel.MainViewModel;

public class ListSearchAdapter extends BaseAdapter{
    private ArrayList<Stock> listViewItemList = new ArrayList<Stock>();

    private DBA DBA;
    private File file;
    private String user;
    private TextView StockName, StockCode;

    private ArrayList<Stock> stocks;
    private MainViewModel mainViewModel;

    public ListSearchAdapter() {
    }

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        DBA = new DBA();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list_item2, parent, false);
        }

        StockName = (TextView) convertView.findViewById(R.id.search_name) ;
        StockCode = (TextView) convertView.findViewById(R.id.search_code) ;

        Stock listViewItem = listViewItemList.get(position);

        StockName.setText(listViewItem.getName());
        StockCode.setText(listViewItem.getStockCode());

        if(mainViewModel.getStockList().getValue() == null) stocks = new ArrayList<>();
        else stocks = mainViewModel.getStockList().getValue();

        file = context.getDatabasePath("User");
        user = DBA.getNickname(file);

        Button bookmark = convertView.findViewById(R.id.Button_bookmark);
        ArrayList<String> interestedStocks = new DBA().getInterestedStocks(context.getDatabasePath("User"));
        final int[] flag = {0};
        for(int i=0; i<interestedStocks.size(); i++) {
            if(listViewItem.getName().equals(interestedStocks.get(i))) {
                flag[0] = 1;
                break;
            }
        }

        if(flag[0] == 1) bookmark.setBackgroundResource(R.drawable.ic_bookmark_click);
        else bookmark.setBackgroundResource(R.drawable.ic_bookmark);

        bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(getConfigValue(context,"stockBoardStart").equals("start")) {
                    Toast.makeText(context, "스톡보드 종료 후 관심종목을 추가해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    String stockName = listViewItem.getName();
                    Log.d("수정 대상", stockName);
                    switch (flag[0]) {
                        case 1:
                            try {
                                bookmark.setBackgroundResource(R.drawable.ic_bookmark);
                                DBA.subInterestedStocks(file, user, stockName);

                                int idx = 0;
                                Log.d("수정 전 크기", Integer.toString(stocks.size()));
                                for (int i = 0; i < stocks.size(); i++) {
                                    Log.d("수정전 관심종목", "stock: " + stocks.get(i).getName());
                                    if (stocks.get(i).getName().equals(stockName)) idx = i;
                                }
                                stocks.remove(idx);

                                mainViewModel.getStockList().setValue(stocks);
                                flag[0] = 0;

                                Log.d("수정 인덱스", Integer.toString(idx));
                                Log.d("수정 후 크기", Integer.toString(stocks.size()));
                                for (int i = 0; i < stocks.size(); i++) Log.d("수정된 관심종목", "stock: " + stocks.get(i).getName());
                            } catch (Exception e) {
                                Log.d("ERROR", "검색 후 관심종목 삭제 실패");
                            }
                            break;
                        case 0:
                            try {
                                bookmark.setBackgroundResource(R.drawable.ic_bookmark_click);
                                DBA.addInterestedStocks(file, user, stockName);

                                Log.d("수정 전 크기", Integer.toString(stocks.size()));
                                for (int i = 0; i < stocks.size(); i++) Log.d("수정전 관심종목", "stock: " + stocks.get(i).getName());

                                stocks.add(DBA.getStock(context.getAssets(), stockName));
                                mainViewModel.getStockList().setValue(stocks);
                                flag[0] = 1;

                                Log.d("수정 후 크기", Integer.toString(stocks.size()));
                                for (int i = 0; i < stocks.size(); i++) Log.d("수정된 관심종목", "stock: " + stocks.get(i).getName());
                            } catch (Exception e) {
                                Log.d("ERROR", "검색 후 관심종목 추가 실패");
                            }
                            break;
                    }
                }
            }
        });
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String searchname, String searchcode) {
        Stock item = new Stock();
        item.setName(searchname);
        item.setStockCode(searchcode);
        listViewItemList.add(item);
    }

    public void resetItems() {
        listViewItemList = new ArrayList<Stock>();
    }

    // Preference 읽기
    public static String getConfigValue(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, null);
    }

}
