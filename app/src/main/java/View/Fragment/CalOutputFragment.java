package View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cdst.stockoverlay.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Model.Calcul;
import View.Adapter.CalculAdapter;

public class CalOutputFragment  extends Fragment {

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
        CalOutputview = (View) inflater.inflate(R.layout.fragment_stockoutput_2, container,false);

        Button buyButton = CalOutputview.findViewById(R.id.avg_Buy_Button);
        Button sellButton = CalOutputview.findViewById(R.id.avg_Sell_Button);
        Button totalButton = CalOutputview.findViewById(R.id.Button_Total_ProfitLoss);

        LinearLayout buyLinear = CalOutputview.findViewById(R.id.cal_Buy_Linear);
        LinearLayout sellLinear = CalOutputview.findViewById(R.id.cal_sell_Linear);
        LinearLayout totalLinear = CalOutputview.findViewById(R.id.cal_total_Linear);

        ListView buy_Cacul_List = CalOutputview.findViewById(R.id.N_Buy_List);

        Bundle bundle = getArguments();
        buyList = (ArrayList<Calcul>) bundle.getSerializable("BuyList");
        sellList = (ArrayList<Calcul>) bundle.getSerializable("SellList");

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(buyLinear.getVisibility() == View.GONE){
                    buyLinear.setVisibility(View.VISIBLE);
                    //buy_Cacul_List.add
                }
                else{
                    buyLinear.setVisibility(View.GONE);
                }
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sellLinear.getVisibility() == View.GONE){
                    sellLinear.setVisibility(View.VISIBLE);
                }
                else {
                    sellLinear.setVisibility(View.GONE);
                }
            }
        });

        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalLinear.getVisibility() == View.GONE){
                    totalLinear.setVisibility(View.VISIBLE);
                }
                else{
                    totalLinear.setVisibility(View.GONE);
                }
            }
        });


        return CalOutputview;
    }
}
