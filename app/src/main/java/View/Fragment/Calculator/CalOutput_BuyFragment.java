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
    int price;
    int quantity;
    int fee;
    private ArrayList<Calcul> buyCalcul = new ArrayList<>();
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
        if(bundle != null) {
            Calcul bundleCalcul;
            bundleCalcul = (Calcul) bundle.getSerializable("BuyCalcul");
            buyCalcul.add(bundleCalcul);
            adapter.addItem(bundleCalcul);
        }



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
}
