package View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.needfor.stockoverlay.R;

import java.util.ArrayList;

import Module.DBA;
import View.Service.OverlayService;
import View.MainActivity;

public class SettingFragment extends Fragment {
    private ViewGroup viewGroup;
    private View mView;

    private int interestedStockSize;
    private String nickname;

    private TextView stockSize;
    private TextView nick;
    private EditText delay;
    private Switch purchaseSwich;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container,false);

        // 관심종목 개수 세기 및 닉네임 가져오기
        ArrayList<String> interestedStocks = new DBA().getInterestedStocks(getActivity().getDatabasePath("User"));
        if(interestedStocks.get(0).equals("-")) interestedStockSize = 0;
        else interestedStockSize = interestedStocks.size();
        nickname = new DBA().getNickname(getActivity().getDatabasePath("User"));

        // 텍스트 지정
        stockSize = (TextView)viewGroup.findViewById(R.id.stock_size);
        nick = (TextView)viewGroup.findViewById(R.id.setting_nickname);
        stockSize.setText("관심종목 : " + String.valueOf(interestedStockSize) + "개");
        nick.setText(nickname);

        // delay 시간
        delay = (EditText)viewGroup.findViewById(R.id.stockboard_delaytime);
        if(getConfigValue(getContext(), "delayTime") != null) delay.setText(getConfigValue(getContext(), "delayTime"));
        delay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                setConfigValue(getContext(), "delayTime", s.toString()+ "000");
                OverlayService.delayTime = Integer.parseInt(s.toString() + "000");
            }
        });

        // 매입가 입력 스위치
        purchaseSwich = (Switch)viewGroup.findViewById(R.id.setting_purcahse);
        if(getConfigValue(getContext(), "purchaseSwitch") != null) {
            if(getConfigValue(getContext(), "purchaseSwitch").equals("ON")) {
                purchaseSwich.setChecked(true);
            }else {
                purchaseSwich.setChecked(false);
            }
        }
        purchaseSwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setConfigValue(getContext(), "purchaseSwitch", "ON");
                    MainActivity.PURCHASE_PRICE_INPUT_FLAG = 1;
                } else {
                    setConfigValue(getContext(), "purchaseSwitch", "OFF");
                    MainActivity.PURCHASE_PRICE_INPUT_FLAG = 0;
                }
            }
        });

        return viewGroup;
    }

    // Preference 읽기
    public static String getConfigValue(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, null);
    }

    // Preference 쓰기
    public static void setConfigValue(Context context, String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
