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
    private CalculInteface calculInteface;
    private Calcul calcul;
    private CalculatorActivity calculatorActivity;

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

        //int price = Integer.parseInt(price_ed.getText().toString());
        //int quantity = Integer.parseInt(quantity_ed.getText().toString());
        //int fee = Integer.parseInt(fee_ed.getText().toString());


        Button Button_BuyInput = CalInputBuyView.findViewById(R.id.input_Buy_Button);//Input 버튼
        Button_BuyInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(calculInteface != null){
                   calcul.setStockprice(Integer.parseInt(price_ed.getText().toString()));
                   calcul.setQuantity(Integer.parseInt(quantity_ed.getText().toString()));
                   calcul.setFee(Integer.parseInt(fee_ed.getText().toString()));
                   calculInteface.setCalculList(calcul);
               }
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

        Bundle bundle = new Bundle();
        bundle.putInt("price",price);
        return CalInputBuyView;
    }

    public interface CalculInteface{
        void setCalculList(Calcul calcul);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        calculatorActivity = (CalculatorActivity) getActivity();

        if(context instanceof CalculInteface){
            calculInteface = (CalculInteface) context;
        } else{
            throw new RuntimeException(context.toString() + "must implement CalculInteface");
        }

    }

    public void onDetach() {
        super.onDetach();
        calculInteface = null;
    }




}
