package View.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.needfor.stockoverlay.R;

import Model.User;
import Module.DBA;
import View.LoginActivity;
import View.MainActivity;

public class NicknameDialog {
    private Context context;

    public NicknameDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(User user, LoginActivity loginActivity) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        // 다이얼로그 크기 조정
        Window window = dlg.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_nickname);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 밖에 클릭해도 종료 안 되게
        dlg.setCancelable(false);

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText message = (EditText) dlg.findViewById(R.id.dialog_nickname);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_price_okButton);

        message.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(message.getText().toString())) {
                    Toast.makeText(context, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                    // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                    new DBA().initNickname(context.getDatabasePath("User"), user, message.getText().toString());
                    new DBA().initInterestedStocks(context.getDatabasePath("User"), user);

                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                    ((LoginActivity) context).startActivity(new Intent(loginActivity, MainActivity.class));
                    ((LoginActivity) context).finish();
                }
            }
        });
    }
}
