package View.Fragment.Calculator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Calcul;
import View.Adapter.Calculator.CalculAdapter;
import View.CalculatorActivity;

public class CalOutputFragment  extends Fragment {


    public static CalOutputFragment newInstance() {
        return new CalOutputFragment();
    }

    private CalculatorActivity calculatorActivity;
    private View CalOutputview;
    private RecyclerView recyclerView;
    private CalculAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> datalist;//혹시나 해서 ㅎㅎ;;
    private ArrayList<Calcul> buyList;
    private ArrayList<Calcul> sellList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalOutputview = (View) inflater.inflate(R.layout.fragment_stockoutput, container,false);

        Button buyButton = CalOutputview.findViewById(R.id.avg_Buy_Button);
        Button sellButton = CalOutputview.findViewById(R.id.avg_Sell_Button);
        Button totalButton = CalOutputview.findViewById(R.id.Button_Total_ProfitLoss);

        Bundle bundle = getArguments();
        //buyList = (ArrayList<Calcul>) bundle.getSerializable("BuyList");
        //sellList = (ArrayList<Calcul>) bundle.getSerializable("SellList");

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { calculatorActivity.fragmentChange(CalOutput_BuyFragment.newInstance()); }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { calculatorActivity.fragmentChange(CalOutput_SellFragment.newInstance()); }
        });

        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { calculatorActivity.fragmentChange(CalOutput_ProfitLossFragment.newInstance()); }
        });

        return CalOutputview;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        calculatorActivity = (CalculatorActivity) getActivity();
    }

    public void onDetach() {
        super.onDetach();
    }
}
