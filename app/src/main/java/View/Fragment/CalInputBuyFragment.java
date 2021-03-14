package View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cdst.stockoverlay.R;

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

        String pricea = price_ed.getText().toString();

        return CalInputBuyView;
    }
}
