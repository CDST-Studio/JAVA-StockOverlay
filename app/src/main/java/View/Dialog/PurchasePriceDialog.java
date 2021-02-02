package View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.needfor.stockoverlay.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import Model.Stock;
import Module.DBA;
import ViewModel.MainViewModel;

public class PurchasePriceDialog {
    private Context context;
    private static File DATABASE_PATH;
    private static String NICKNAME;
    private int DB_LOADED_FLAG;

    private EditText price;
    private EditText target_price;
    private Button okButton;
    private Button cancle;

    public PurchasePriceDialog(Context context) {
        this.context = context;
        DATABASE_PATH = context.getDatabasePath("User");
        NICKNAME = new DBA().getNickname(DATABASE_PATH);
        DB_LOADED_FLAG = 0;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(MainViewModel mainViewModel, Stock stock) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        // 다이얼로그 크기 조정
        Window window = dlg.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_purchase_price);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 밖에 클릭해도 종료 안 되게
        dlg.setCancelable(false);

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        target_price = (EditText) dlg.findViewById(R.id.dialog_target_price);
        price = (EditText) dlg.findViewById(R.id.dialog_purchase_price);
        okButton = (Button) dlg.findViewById(R.id.dialog_price_okButton);
        cancle = (Button) dlg.findViewById(R.id.dialog_cancle);

        String savedPurchasePrice = new DBA().getPurchasePrice(DATABASE_PATH, stock.getName());
        String savedTargetProfit = new DBA().getTargetProfit(DATABASE_PATH, stock.getName());

        final String[] purchasePrice = {""};
        if(!savedPurchasePrice.equals("-")) {
            price.setText(savedPurchasePrice.replace(",", ""));
            purchasePrice[0] = savedPurchasePrice.replace(",", "");
        }
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                purchasePrice[0] = s.toString();
            }
        });

        final String[] targetPrice = {""};
        if(!savedTargetProfit.equals("-")) {
            target_price.setText(savedTargetProfit.replace(",", ""));
            targetPrice[0] = savedTargetProfit.replace(",", "");
        }
        target_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                targetPrice[0] = s.toString();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(purchasePrice[0]) && savedPurchasePrice.equals("-")) {
                    Toast.makeText(context, "매입가 입력은 필수입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    if(purchasePrice[0].equals("") && targetPrice[0].equals("")) {
                        stock.nullPurchase();
                        stock.nullTargetProfit();
                        new DBA().subPurchasePrice(DATABASE_PATH, NICKNAME, stock.getName());
                    }else {
                        stock.setPurchasePrice(purchasePrice[0]);
                        stock.setProfitAndLoss();
                        new DBA().addPurchasePrice(DATABASE_PATH, NICKNAME, stock.getName(), stock.getPurchasePrice());
                    }

                    if(!targetPrice[0].equals("")) {
                        stock.setTargetProfit(targetPrice[0]);
                        new DBA().addTargetProfit(DATABASE_PATH, NICKNAME, stock.getName(), stock.getTargetProfit());
                    }else if(stock.getPurchasePrice() != null) {
                        stock.nullTargetProfit();
                        new DBA().subTargetProfit(DATABASE_PATH, NICKNAME, stock.getName());
                    }

                    stock.setNotification(true);

                    int idx = 0;
                    for (Stock s : Objects.requireNonNull(mainViewModel.getStockList().getValue())) {
                        if (s.getName().equals(stock.getName())) break;
                        if (mainViewModel.getStockList().getValue().size() != idx + 1) idx++;
                    }

                    ArrayList<Stock> stocklist = mainViewModel.getStockList().getValue();
                    stocklist.set(idx, stock);
                    mainViewModel.getStockList().setValue(stocklist);

                    dlg.dismiss();
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}
