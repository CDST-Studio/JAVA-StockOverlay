package View;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import Model.Stock;
import com.needfor.stockoverlay.R;


public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Stock> listViewItemList = new ArrayList<Stock>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() { }

    // Adapter에 사용되는 데이터의 개수를 리턴.
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView StockName = (TextView) convertView.findViewById(R.id.stockname) ;
        TextView StockCode = (TextView) convertView.findViewById(R.id.stockcode) ;
        TextView CurrentPrice = (TextView) convertView.findViewById(R.id.currentprice);
        TextView ChangePrice = (TextView) convertView.findViewById(R.id.changeprice);
        TextView ChangeRate = (TextView) convertView.findViewById(R.id.changerate);
        TextView Change = (TextView) convertView.findViewById(R.id.change) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Stock listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        StockName.setText(listViewItem.getName());
        StockCode.setText(listViewItem.getStockCode());
        CurrentPrice.setText(listViewItem.getCurrentPrice());
        ChangePrice.setText(listViewItem.getChangePrice());
        ChangeRate.setText(listViewItem.getChangeRate());
        Change.setText(listViewItem.getChange());

        // 텍스트 색상 변경
        StockName.setTextColor(Color.WHITE);
        StockCode.setTextColor(Color.LTGRAY);
        if(Change.getText().equals("▲")) {
            CurrentPrice.setTextColor(Color.parseColor("#F84747"));
            Change.setTextColor(Color.parseColor("#F84747"));
            ChangeRate.setTextColor(Color.parseColor("#F84747"));
            ChangePrice.setTextColor(Color.parseColor("#F84747"));
        }else if(Change.getText().equals("▼")) {
            CurrentPrice.setTextColor(Color.parseColor("#87CEFA"));
            Change.setTextColor(Color.parseColor("#87CEFA"));
            ChangeRate.setTextColor(Color.parseColor("#87CEFA"));
            ChangePrice.setTextColor(Color.parseColor("#87CEFA"));
        }else {
            CurrentPrice.setTextColor(Color.LTGRAY);
            Change.setTextColor(Color.LTGRAY);
            ChangeRate.setTextColor(Color.LTGRAY);
            ChangePrice.setTextColor(Color.LTGRAY);
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 임시 함수
    public void addItem(String stockname, String stockcode, String currentprice, String changeprice
        , String changerate, String change) {
        Stock item = new Stock();

        item.setName(stockname);
        item.setStockCode(stockcode);
        item.setCurrentPrice(currentprice);
        item.setChangePrice(changeprice);
        item.setChangeRate(changerate);
        item.setChange(change);

        listViewItemList.add(item);
    }
}