package View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cdst.stockoverlay.R;

public class CaculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        
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
}