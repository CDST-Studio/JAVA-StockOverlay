package View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.needfor.stockoverlay.R;

import java.io.File;
import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.Fragment.SearchableFragment;
import ViewModel.MainViewModel;

public class ListSearchAdapter extends BaseAdapter{
    private ArrayList<Stock> listViewItemList = new ArrayList<Stock>() ;
    private int check = 1;
    private DBA dba;
    private String user, name;
    private File file;
    private TextView StockName, StockCode;
    private Stock listViewItem;

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
        final int pos = position;
        final Context context = parent.getContext();
        dba = new DBA();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list_item2, parent, false);
        }

        StockName = (TextView) convertView.findViewById(R.id.search_name) ;
        StockCode = (TextView) convertView.findViewById(R.id.search_code) ;

        listViewItem = listViewItemList.get(pos);

        StockName.setText(listViewItem.getName());
        StockCode.setText(listViewItem.getStockCode());

        file = context.getDatabasePath("User");
        user = dba.getNickname(file);

        Button bookmark = convertView.findViewById(R.id.Button_bookmark);
        bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                name = listViewItemList.get(pos).getName();
                switch(check){
                    case 1:
                        bookmark.setBackgroundResource(R.drawable.ic_bookmark_click);
                        try {
                            dba.addInterestedStocks(file, user, name);

                            stocks = mainViewModel.getStockList().getValue();
                            stocks.add(new DBA().getStock(context.getAssets(), name));

                            mainViewModel.getStockList().setValue(stocks);
                        } catch (Exception e){ Toast.makeText(context, name+" 북마크 추가 실패", Toast.LENGTH_SHORT).show(); }
                        check=0;
                        break;
                    case 0:
                        bookmark.setBackgroundResource(R.drawable.ic_bookmark);
                        try{
                            dba.subInterestedStocks(file, user, name);

                            stocks = mainViewModel.getStockList().getValue();
                            stocks.remove(new DBA().getStock(context.getAssets(), name));

                            mainViewModel.getStockList().setValue(stocks);
                        }
                        catch (Exception e){ Toast.makeText(context, name+ " 북마크 해제 실패", Toast.LENGTH_SHORT).show(); }
                        check=1;
                        break;
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

}
