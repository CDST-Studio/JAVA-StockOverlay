package View.Fragment.Calculator;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Calcul;
import View.Adapter.Calculator.CalculOutputBuyAdater;
import View.CalculatorActivity;


public class CalOutput_BuyFragment extends Fragment {

    private View CalBuyView;
    private CalculatorActivity calculatorActivity;
    private Bundle bundle;


    public CalOutput_BuyFragment() {
    }

    public static CalOutput_BuyFragment newInstance() {
        return new CalOutput_BuyFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalBuyView = (View) inflater.inflate(R.layout.fragment_stockoutput_buy,container,false);

        ListView listView = CalBuyView.findViewById(R.id.N_Buy_List);
        CalculOutputBuyAdater adapter = new CalculOutputBuyAdater();
        listView.setAdapter(adapter);

        bundle = calculatorActivity.bundle;

        if(calculatorActivity.getFlag() && (bundle != null) && (bundle.getSerializable("BuyCalcul") != null)) {
            Calcul bundleCalcul;
            bundleCalcul = (Calcul) bundle.getSerializable("BuyCalcul");
            adapter.addItem(bundleCalcul);
            calculatorActivity.setFlag();
        }
        totalOutput(adapter);





        Button Button_Back = CalBuyView.findViewById(R.id.avg_Buy_Button_back); // 초기 화면 돌아감
        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorActivity.fragmentChange(CalOutputFragment.newInstance());
            }
        });

        return CalBuyView;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        calculatorActivity = (CalculatorActivity) getActivity();
    }

    public void onDetach() {
        super.onDetach();
    }

    public void totalOutput(CalculOutputBuyAdater mAdater){
        /*더러우니까 토탈 관련한거 여기다가 박았습니다.*/

        int i = 0;
        int total_price = 0;
        int total_Quantity = 0;
        float total_fee = 0;
        ArrayList<Calcul> buyCalcul = new ArrayList<>();

        TextView buy_Quantity = CalBuyView.findViewById(R.id.Buy_Quantity);
        TextView buy_Price_Total = CalBuyView.findViewById(R.id.Total_Buy_Price);
        TextView buy_Price = CalBuyView.findViewById(R.id.Buy_Price);
        TextView buy_Tex_Total = CalBuyView.findViewById(R.id.Total_Buy_Tex);

        buyCalcul = mAdater.getList();
        while (i < buyCalcul.size()){
            total_price += buyCalcul.get(i).getStockprice();
            total_Quantity += buyCalcul.get(i).getQuantity();
            total_fee += buyCalcul.get(i).getFee();
            i++;
        }

        if(total_Quantity != 0) {
            buy_Quantity.setText(Integer.toString(total_Quantity));
            buy_Price_Total.setText(Integer.toString(total_price));
            buy_Price.setText(Integer.toString(total_price / total_Quantity));
            buy_Tex_Total.setText(Float.toString(total_fee / total_Quantity));
        }
        else{
            buy_Quantity.setText("0");
            buy_Price_Total.setText("0");
            buy_Price.setText("0");
            buy_Tex_Total.setText("0");
        }

    }

}
