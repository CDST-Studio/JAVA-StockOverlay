package View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private int checkFlag = 0;

    public NicknameDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(User user, LoginActivity loginActivity) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

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
        final EditText message = (EditText) dlg.findViewById(R.id.dialog_purchase_price);
        final Button checkNickname = (Button) dlg.findViewById(R.id.checkNickname);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_price_okButton);

        message.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DEL :
                        checkFlag = 0;
                        break;
                }
                return false;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFlag == 1) {
                    // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                    // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                    new DBA().initNickname(context.getDatabasePath("User"), user, message.getText().toString());
                    new DBA().initInterestedStocks(context.getDatabasePath("User"), user);
                    Log.d("User init test", "닉네임: " + user.getNickName() + ", 관심종목 개수: " + user.getInterestedStocks().size());

                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                    ((LoginActivity) context).startActivity(new Intent(loginActivity, MainActivity.class));
                    ((LoginActivity) context).finish();
                }else {
                    Toast.makeText(context, "닉네임 중복체크를 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(message.getText())) {
                    Toast.makeText(context, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    if (new DBA().isExistNickname(message.getText().toString())) {
                        Toast.makeText(context, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        checkFlag = 1;
                        Toast.makeText(context, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
