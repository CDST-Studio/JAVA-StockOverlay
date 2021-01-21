package View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.needfor.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;

public class ListSearchAdapter extends BaseAdapter  {
    private ArrayList<Stock> listViewItemList = new ArrayList<Stock>() ;
    private int check = 1;

    public ListSearchAdapter() {
    }

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

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

        bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch(check){
                    case 1:
                        bookmark.setBackgroundResource(R.drawable.ic_bookmark_click);
                        check=0;
                        break;
                    case 0:
                        bookmark.setBackgroundResource(R.drawable.ic_bookmark);
                        check=1;
                        break;
                }

                /*if(check=false) {
                bookmark.setBackgroundResource(R.drawable.ic_bookmark_click);
                check=true; }
                if(check=true){
                bookmark.setBackgroundResource(R.drawable.ic_bookmark);
                check=false;
                }*/
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
