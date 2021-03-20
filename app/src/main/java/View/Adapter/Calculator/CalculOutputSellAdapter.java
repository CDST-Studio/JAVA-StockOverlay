package View.Adapter.Calculator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdst.stockoverlay.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Model.Calcul;

public class CalculOutputSellAdapter extends BaseAdapter {
    private static ArrayList<Calcul> sellList = new ArrayList<Calcul>();
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
        if(context == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list_item5,parent,false);
        }

        TextView n_Price_Text = convertView.findViewById(R.id.N_BUY_Price_Text);
        TextView n_Price_Number = convertView.findViewById(R.id.N_BUY_Price_Number);
        TextView n_Quantity_Text = convertView.findViewById(R.id.N_Buy_Quantity_Text);
        TextView n_Quantity_Number = convertView.findViewById(R.id.N_Buy_Quantity_Number);



        n_Price_Text.setText(Integer.toString(position + 1) + "차 매도 주가:");
        n_Price_Number.setText(Integer.toString(sellList.get(position).getStockprice()));
        n_Quantity_Text.setText(Integer.toString(position + 1) + "차 매도 수량");
        n_Quantity_Number.setText(Integer.toString(sellList.get(position).getQuantity()));

        return convertView;
    }

    public void addItem(Calcul calcul){
        this.sellList.add(calcul);
        Log.v("output","addItem 발동 : " + Integer.toString(sellList.size()));
    }
    public ArrayList<Calcul> getList(){
        return sellList;
    }
}
