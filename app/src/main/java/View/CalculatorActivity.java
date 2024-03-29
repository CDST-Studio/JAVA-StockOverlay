package View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Calcul;
import View.Fragment.Calculator.CalInputBuyFragment;
import View.Fragment.Calculator.CalOutputFragment;
import View.Fragment.Calculator.CalInputSellFragment;


public class CalculatorActivity extends AppCompatActivity  {

    private CalOutputFragment outputFragment = new CalOutputFragment();
    private CalInputBuyFragment calInputBuyFragment = new CalInputBuyFragment();
    private CalInputSellFragment calInputSellFragment = new CalInputSellFragment();

    private Button plustBtn;
    private ArrayList<Calcul> buylist;
    private ArrayList<Calcul> selllist;
    private int index = 0;
    public Bundle bundle;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        plustBtn = (Button) findViewById(R.id.plusButton);
        plustBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlag();
                fragmentChange(CalInputBuyFragment.newInstance());
            }
        });
        fragmentChange(CalOutputFragment.newInstance());
    }


    private double caculBuy(String name, int amount, int stockPrice){
        return (amount * stockPrice) + caculCommission(name, amount, stockPrice) + ((amount * stockPrice) * 0.0043);//매수금액 + 수수료 + 유관기관제비용
    }
    private double caculSell(String name, int amount, int stockPrice){
        return (amount * stockPrice) - caculCommission(name, amount, stockPrice) - ((amount * stockPrice) * 0.0043) - ((amount * stockPrice) * 0.0021);//매도금액 - 수수료 - 유관기관제비용 - 세금
    }
    private double caculCommission(String name, int amount, int stockPrice){
        return amount * stockPrice * stockCommission(name);
    }

    public void fragmentChange(Fragment fragment){ //Cal 액티비티에서 화면 전환을 위한 함수
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_CalZone, fragment).commit();
    }

    private double stockCommission(String name){
        switch (name){
            case "나무증권":
                return 0.01;
            case "영웅문":
                return 0.015;
            case "한국투자증권":
                return 0.0140527;
            case "신한투자증권":
                return 0.1891639;
            default:
                return 0.015;
        }

    }

    public void getBundle(Bundle bundle){
        this.bundle = bundle;
    }

    public void setFlag(){
        if (flag) flag = false;
        else flag = true;
    }
    public boolean getFlag(){return flag;}
}