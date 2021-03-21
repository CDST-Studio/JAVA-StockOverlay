package View.Fragment.Calculator;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Calcul;
import View.Adapter.Calculator.CalculOutputBuyAdater;
import View.Adapter.Calculator.CalculOutputSellAdapter;
import View.CalculatorActivity;

public class CalOutput_ProfitLossFragment extends Fragment {

    private View CalProfitLossView;

    private Calcul calcul;
    private CalculatorActivity calculatorActivity;
    private CalculOutputBuyAdater calculOutputBuyAdater = new CalculOutputBuyAdater();
    private CalculOutputSellAdapter calculOutputSellAdapter = new CalculOutputSellAdapter();
    private ArrayList<Calcul> sellList = new ArrayList<>();
    private ArrayList<Calcul> buyList = new ArrayList<>();

    public static CalOutput_ProfitLossFragment newInstance() {
        return new CalOutput_ProfitLossFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalProfitLossView = (View) inflater.inflate(R.layout.fragment_stockoutput_profitloss,container,false);

        TextView totalProfit_Text = CalProfitLossView.findViewById(R.id.Total_ProfitLoss);
        TextView totalTex_Text = CalProfitLossView.findViewById(R.id.Total_Tex);
        TextView totalFee_Text = CalProfitLossView.findViewById(R.id.Total_Buy_Tex);


        float totalProfit = 0;
        double totalTex = 0;
        float totalFee = 0;

        int totalBuy = 0;
        int totalBuyQuantity = 0;
        float totalBuyFee = 0;

        int totalSell = 0;
        int totalSellQuantity = 0;
        float totalSellFee = 0;


        if(totalProfit == 0) Log.v("output","초기화는 잘되넹?");

        buyList = calculOutputBuyAdater.getList();
        sellList = calculOutputSellAdapter.getList();

        for(int i = 0; i < sellList.size(); i++){
            totalSell += (float) sellList.get(i).getStockprice();
            Log.v("output","totalSell/" + Integer.toString(i) + "/" + Float.toString(totalSell));
            totalSellQuantity += sellList.get(i).getQuantity();
            totalSellFee += sellList.get(i).getFee();
        }
        Log.v("output","totalSell : " + Float.toString(totalSell));
        totalSellFee = totalSellFee / totalSellQuantity;
        totalSellFee = totalSellFee * totalSell;
        totalTex = totalSell * 0.0021;
        totalProfit += totalSell - totalSellFee - totalTex;
        totalFee += totalSellFee;
        Log.v("output","매도 계산 후 : " + Float.toString(totalProfit));

        //총 매수 비용 계산
        for(int i = 0; i < buyList.size(); i++){
            totalBuy += buyList.get(i).getStockprice();
            totalBuyQuantity += buyList.get(i).getQuantity();
            totalBuyFee += buyList.get(i).getFee();
        }
        totalBuyFee = totalBuyFee / totalBuyQuantity; //평균 수수료 계산
        totalBuyFee = totalBuyFee * totalBuy; //평균 수수료 * 매수 비용 = 총 수수료 금액
        totalProfit = totalProfit - totalBuy - totalBuyFee;
        totalFee += totalBuyFee;

        Log.v("output","Set 직전 : " + Float.toString(totalProfit));
        totalProfit_Text.setText(Float.toString(totalProfit));
        totalTex_Text.setText(Double.toString(totalTex));
        totalFee_Text.setText(Float.toString(totalFee));



        Button Button_Back = CalProfitLossView.findViewById(R.id.avg_ProfitLoss_back); // 초기 화면 돌아감
        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorActivity.fragmentChange(CalOutputFragment.newInstance());
            }
        });

        return CalProfitLossView;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        calculatorActivity = (CalculatorActivity) getActivity();
    }

    public void onDetach() {
        super.onDetach();
    }
}
