package View.Fragment.Calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cdst.stockoverlay.R;

import Model.Calcul;
import View.CalculatorActivity;

public class CalInputBuyFragment extends Fragment {

    private View CalInputBuyView;

    int price;
    int quantity;
    int fee;

    private Calcul calcul;
    private CalculatorActivity calculatorActivity;
    private CalOutput_BuyFragment calOutputBuyFragment = new CalOutput_BuyFragment();

    public static CalInputBuyFragment newInstance() {
        return new CalInputBuyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalInputBuyView = (View) inflater.inflate(R.layout.fragment_stockinput_buy,container,false);

        EditText price_ed = CalInputBuyView.findViewById(R.id.edit_BuyPrice);
        EditText quantity_ed = CalInputBuyView.findViewById(R.id.edit_BuyQuantity);
        EditText fee_ed = CalInputBuyView.findViewById(R.id.edit_BuyFee);


        Button Button_BuyInput = CalInputBuyView.findViewById(R.id.input_Buy_Button);//Input 버튼
        Button_BuyInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcul = new Calcul();
                calcul.setStockprice(Integer.parseInt(price_ed.getText().toString()));
                calcul.setQuantity(Integer.parseInt(quantity_ed.getText().toString()));
                calcul.setFee(Float.parseFloat(fee_ed.getText().toString()));

                Bundle bundle = new Bundle();
                bundle.putSerializable("BuyCalcul",calcul);
                calOutputBuyFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().commit();
                calculatorActivity.getBundle(bundle);
                calculatorActivity.fragmentChange(CalOutputFragment.newInstance());
            }
        });

        Button Button_Transform = CalInputBuyView.findViewById(R.id.button_cal_transform_buy); // 임시 전환 버튼
        Button_Transform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorActivity.fragmentChange(CalInputSellFragment.newInstance());
            }
        });

        Button Button_Back = CalInputBuyView.findViewById(R.id.button_backhome_buy);//임시 홈 버튼
        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorActivity.fragmentChange(CalOutputFragment.newInstance());
            }
        });

        return CalInputBuyView;
    }



    public void onAttach(Context context) {
        super.onAttach(context);
        calculatorActivity = (CalculatorActivity) getActivity();
    }

    public void onDetach() {
        super.onDetach();
        calOutputBuyFragment = null;
    }






}
