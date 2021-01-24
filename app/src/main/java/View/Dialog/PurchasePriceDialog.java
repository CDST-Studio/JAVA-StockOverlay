package View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.needfor.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;
import Model.User;
import Module.DBA;
import View.LoginActivity;
import View.Service.OverlayService;

public class PurchasePriceDialog {
    private Context context;

    public PurchasePriceDialog(Context context) { this.context = context; }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(Stock stock) {

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
                stock.setPurchasePrice(purchasePrice[0]);
                sendStock(stock);
                dlg.dismiss();
            }
        });
    }

    // 오버레이 뷰 서비스로 관심종목 전달
    public void sendStock(Stock stock) {
        Intent intent = new Intent(context, OverlayService.class);
        intent.putExtra("stock", stock);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        Log.d("메세지 전달", "MainFragment");
    }
}
