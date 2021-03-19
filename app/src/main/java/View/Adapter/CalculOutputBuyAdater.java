package View.Adapter;

import android.content.Context;
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
    private Context context;
    private ArrayList<Calcul> buyList = new ArrayList<>();

    public CalculOutputBuyAdater(Context mcontext, ArrayList<Calcul> mbuyList){
        this.context = mcontext;
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
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_stockoutput_2, parent, false);
            View
        }
        return null;
    }

    public class CalculOutputBuyHolder{
        private TextView
    }
}
