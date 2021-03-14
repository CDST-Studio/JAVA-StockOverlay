package View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cdst.stockoverlay.R;

import

public class CalInputBuyFragment extends Fragment {

    private View CalInputBuyView;

    int price;
    int quantity;
    int fee;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalInputBuyView = (View) inflater.inflate(R.layout.fragment_stockinput_buy,container,false);

        EditText price_ed = CalInputBuyView.findViewById(R.id.edit_BuyPrice);
        EditText quantity_ed = CalInputBuyView.findViewById(R.id.edit_BuyQuantity);
        EditText fee_ed = CalInputBuyView.findViewById(R.id.edit_BuyFee);

        int price = Integer.parseInt(price_ed.getText().toString());
        int quantity = Integer.parseInt(quantity_ed.getText().toString());
        int fee = Integer.parseInt(fee_ed.getText().toString());

        Button button = CalInputBuyView.findViewById(R.id.input_Buy_Button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("price",price);
                bundle.putInt("quantity",quantity);
                bundle.putInt("fee",fee);

                Intent intent = new Intent(getActivity(), CalculatorActivity.class);
            }
        });

        Bundle bundle = new Bundle();
        bundle.putInt("price",price);



        return CalInputBuyView;
    }
}
