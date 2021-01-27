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

import java.util.ArrayList;

import Model.Stock;
import Module.DBA;
import View.Fragment.SearchableFragment;

public class ListSearchAdapter extends BaseAdapter{
    private ArrayList<Stock> listViewItemList = new ArrayList<Stock>() ;
    private ArrayList<Stock> stock;
    private int check = 1;
    private Bundle bundle;
    private DBA dba;
    private String name;
    private Intent inten;
    private SearchableFragment fragment;

    public ListSearchAdapter() {
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

        TextView StockName = (TextView) convertView.findViewById(R.id.search_name) ;
        TextView StockCode = (TextView) convertView.findViewById(R.id.search_code) ;

        Stock listViewItem = listViewItemList.get(position);

        StockName.setText(listViewItem.getName());
        StockCode.setText(listViewItem.getStockCode());

        Button bookmark  = convertView.findViewById(R.id.Button_bookmark);

        bundle = new Bundle();
        fragment = new SearchableFragment();
        //stock = fragment.getArguments().getParcelableArrayList("stock");

        if(fragment.getArguments()!=null){
            name = fragment.getArguments().getString("name"+pos);
        }



        bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch(check){
                    case 1:
                        bookmark.setBackgroundResource(R.drawable.ic_bookmark_click);
                        //dba.addInterestedStocks(("/data/data/com.needfor.stockoverlay/databases"), user, name);
                        Toast.makeText(context, name+"Test"+pos, Toast.LENGTH_SHORT).show();
                        check=0;
                        break;
                    case 0:
                        bookmark.setBackgroundResource(R.drawable.ic_bookmark);
                        //dba.subInterestedStocks("/data/data/com.needfor.stockoverlay/databases",user,name);
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
