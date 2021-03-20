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

import com.cdst.stockoverlay.R;

import Model.Calcul;
import View.CalculatorActivity;

public class CalInputSellFragment extends Fragment {

    private View CalInputSellView;

    private Calcul calcul;
    private CalculatorActivity calculatorActivity;

    private CalOutput_SellFragment calOutputSellFragment = new CalOutput_SellFragment();


    public static CalInputSellFragment newInstance() {
        return new CalInputSellFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalInputSellView = (View) inflater.inflate(R.layout.fragment_stockinput_sell,container,false);

        EditText price_ed = CalInputSellView.findViewById(R.id.edit_SellPrice);
        EditText quantity_ed = CalInputSellView.findViewById(R.id.edit_SellQuantity);
        EditText fee_ed = CalInputSellView.findViewById(R.id.edit_SellPrice);


        Button Button_BuyInput = CalInputSellView.findViewById(R.id.input_Sell_Button); //Input 버튼
        Button_BuyInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcul.setStockprice(Integer.parseInt(price_ed.getText().toString()));
                calcul.setQuantity(Integer.parseInt(quantity_ed.getText().toString()));
                calcul.setFee(Float.parseFloat(fee_ed.getText().toString()));

                Bundle bundle = new Bundle();
                bundle.putSerializable("SellCalcul",calcul);
                calOutputSellFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().commit();
                calculatorActivity.getBundle(bundle);
                calculatorActivity.fragmentChange(CalOutputFragment.newInstance());
            }
        });

        Button Button_Transform = CalInputSellView.findViewById(R.id.button_cal_transform_sell); // 임시 전환 버튼
        Button_Transform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorActivity.fragmentChange(CalInputBuyFragment.newInstance());
            }
        });

        Button Button_Back = CalInputSellView.findViewById(R.id.button_backhome_sell); //임시 홈 버튼
        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorActivity.fragmentChange(CalOutputFragment.newInstance());
            }
        });

        return CalInputSellView;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        calculatorActivity = (CalculatorActivity) getActivity();
    }

    public void onDetach() {
        super.onDetach();
        calOutputSellFragment = null;
    }
}
