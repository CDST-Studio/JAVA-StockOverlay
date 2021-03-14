package View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Module.DBA;
import View.Service.OverlayService;
import View.MainActivity;
import View.ManualActivity;

public class SettingFragment extends Fragment {
    private ViewGroup viewGroup;

    private EditText delay;
    private Switch purchaseSwich;
    private Button manual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container,false);

        // delay 시간
        delay = (EditText)viewGroup.findViewById(R.id.stockboard_delaytime);
        if(getConfigValue(getContext(), "delayTime") != null) {
            String time = getConfigValue(getContext(), "delayTime");
            delay.setText(time.substring(0, time.length()-3));
        }
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
                if (isChecked) {
                    setConfigValue(getContext(), "purchaseSwitch", "ON");
                    MainActivity.PURCHASE_PRICE_INPUT_FLAG = 1;
                } else {
                    setConfigValue(getContext(), "purchaseSwitch", "OFF");
                    MainActivity.PURCHASE_PRICE_INPUT_FLAG = 0;
                }
            }
        });

        manual = (Button)viewGroup.findViewById(R.id.manual_button);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ManualActivity.class));
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
