package View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.needfor.stockoverlay.R;

import java.util.Objects;

import Model.Stock;
import ViewModel.MainViewModel;

public class PurchasePriceDialog {
    private Context context;

    public PurchasePriceDialog(Context context) { this.context = context; }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(MainViewModel mainViewModel, Stock stock) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 다이얼로그 크기 조정
        Window window = dlg.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_purchase_price);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText price = (EditText) dlg.findViewById(R.id.dialog_purchase_price);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_price_okButton);
        final String[] purchasePrice = {""};

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

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int profitLoss = Integer.parseInt(stock.getCurrentPrice().replaceAll(",","")) - Integer.parseInt(purchasePrice[0]);
                StringBuffer price = new StringBuffer(Integer.toString(profitLoss));
                for(int i=2; i<price.length()-1; i = i+2+(i/2 + 1)) {
                    price.insert((price.length()-1)-i, ",");
                }
                stock.setPurchasePrice(price.toString());

                int idx = 0;
                for(Stock s : Objects.requireNonNull(mainViewModel.getStockList().getValue())) {
                    if(s.getName().equals(stock.getName())) break;
                    if(mainViewModel.getStockList().getValue().size() != idx +1 ) idx++;
                }
                mainViewModel.getStockList().getValue().remove(idx);
                mainViewModel.getStockList().getValue().add(idx, stock);

                dlg.dismiss();
            }
        });
    }
}
