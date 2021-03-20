package View.Fragment.Calculator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Calcul;
import View.Adapter.Calculator.CalculOutputSellAdapter;
import View.CalculatorActivity;

public class CalOutput_SellFragment extends Fragment {

    private View CalSellView;
    int price;
    int quantity;
    int fee;
    private CalculatorActivity calculatorActivity;
    private Bundle bundle;

    public CalOutput_SellFragment(){

    }

    public static CalOutput_SellFragment newInstance() {
        return new CalOutput_SellFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalSellView = (View) inflater.inflate(R.layout.fragment_stockoutput_sell,container,false);

        ListView listView = CalSellView.findViewById(R.id.N_Sell_List);
        CalculOutputSellAdapter adapter = new CalculOutputSellAdapter();
        listView.setAdapter(adapter);

        bundle = calculatorActivity.bundle;
        if(bundle != null){
            Calcul bundleCalcul;
            bundleCalcul = (Calcul) bundle.getSerializable("SellCalcul");
            adapter.addItem(bundleCalcul);
        }

        if(adapter.getList().size() > 0) totalOutput(adapter);

        Button Button_Back = CalSellView.findViewById(R.id.avg_Sell_Button_back); // 초기 화면 돌아감
        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorActivity.fragmentChange(CalOutputFragment.newInstance());
            }
        });

        return CalSellView;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        calculatorActivity = (CalculatorActivity) getActivity();
    }

    public void onDetach() {
        super.onDetach();
    }

    public void totalOutput(CalculOutputSellAdapter mAdater){
        /*더러우니까 토탈 관련한거 여기다가 박았습니다.*/

        int i = 0;
        int total_price = 0;
        int total_Quantity = 0;
        float total_fee = 0;
        ArrayList<Calcul> sellCalcul = new ArrayList<>();

        TextView buy_Quantity = CalSellView.findViewById(R.id.Sell_Quantity);
        TextView buy_Price_Total = CalSellView.findViewById(R.id.Total_Sell_Price);
        TextView buy_Price = CalSellView.findViewById(R.id.Sell_Price);
        TextView buy_Tex_Total = CalSellView.findViewById(R.id.Total_Sell_Tex);

        sellCalcul = mAdater.getList();
        while (i < sellCalcul.size()){
            total_price += sellCalcul.get(i).getStockprice();
            total_Quantity += sellCalcul.get(i).getQuantity();
            total_fee += sellCalcul.get(i).getFee();
            i++;
        }

        buy_Quantity.setText(Integer.toString(total_Quantity));
        buy_Price_Total.setText(Integer.toString(total_price));
        buy_Price.setText(Integer.toString(total_price / total_Quantity));
        buy_Tex_Total.setText(Float.toString(total_fee / sellCalcul.size()));


    }
}
