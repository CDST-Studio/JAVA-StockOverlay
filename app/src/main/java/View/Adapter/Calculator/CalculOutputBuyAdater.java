package View.Adapter.Calculator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdst.stockoverlay.R;
import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;

import java.util.ArrayList;
import java.util.List;

import Model.Calcul;

public class CalculOutputBuyAdater extends BaseAdapter {
    private static ArrayList<Calcul> buyList = new ArrayList<>();
    public CalculOutputBuyAdater(){}
    public CalculOutputBuyAdater(Context mcontext, ArrayList<Calcul> mbuyList){
        this.buyList = mbuyList;
    }

    @Override
    public int getCount() {
        return buyList.size();
    }

    @Override
    public Object getItem(int position) {
        return buyList.get(position);
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

        convertView.setBackgroundColor(414141);

        LinearLayout background = (LinearLayout) convertView.findViewById(R.id.cal_list);
        background.setBackgroundResource(R.drawable.border_1_yellow_side);

        TextView n_Price_Text = convertView.findViewById(R.id.N_BUY_Price_Text);
        TextView n_Price_Number = convertView.findViewById(R.id.N_BUY_Price_Number);
        TextView n_Quantity_Text = convertView.findViewById(R.id.N_Buy_Quantity_Text);
        TextView n_Quantity_Number = convertView.findViewById(R.id.N_Buy_Quantity_Number);

        if(buyList != null) {
            n_Price_Text.setText(Integer.toString(position + 1) + "차 매수 주가:");
            n_Price_Number.setText(Integer.toString(buyList.get(position).getStockprice()));
            n_Quantity_Text.setText(Integer.toString(position + 1) + "차 매수 수량");
            n_Quantity_Number.setText(Integer.toString(buyList.get(position).getQuantity()));
        }
        return convertView;
    }

    public ArrayList<Calcul> getList(){
        return buyList;
    }
    public void addItem(Calcul calcul){
        this.buyList.add(calcul);
    }

}
