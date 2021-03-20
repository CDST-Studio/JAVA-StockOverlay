package View.Adapter.Calculator;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdst.stockoverlay.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Model.Calcul;

public class CalculOutputSellAdapter extends BaseAdapter {
    private static ArrayList<Calcul> sellList = new ArrayList<>();
    public CalculOutputSellAdapter(){
    }
    public CalculOutputSellAdapter(Context mcontext, ArrayList<Calcul> mSellList){
        this.sellList = mSellList;
    }

    @Override
    public int getCount() {
        return sellList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list_item5,parent,false);
        }

        LinearLayout background = (LinearLayout) convertView.findViewById(R.id.cal_list);
        background.setBackgroundResource(R.drawable.border_2_yellow_side);

        TextView n_Price_Text = convertView.findViewById(R.id.N_BUY_Price_Text);
        TextView n_Price_Number = convertView.findViewById(R.id.N_BUY_Price_Number);
        TextView n_Quantity_Text = convertView.findViewById(R.id.N_Buy_Quantity_Text);
        TextView n_Quantity_Number = convertView.findViewById(R.id.N_Buy_Quantity_Number);

        if(sellList != null) {
            n_Price_Text.setText(Integer.toString(position + 1) + "차 매도 주가:");
            n_Price_Number.setText(Integer.toString(sellList.get(position).getStockprice()));
            n_Quantity_Text.setText(Integer.toString(position + 1) + "차 매도 수량");
            n_Quantity_Number.setText(Integer.toString(sellList.get(position).getQuantity()));
        }

        return convertView;
    }

    public ArrayList<Calcul> getList(){
        return sellList;
    }
    public void addItem(Calcul calcul){
        this.sellList.add(calcul);
    }

}
