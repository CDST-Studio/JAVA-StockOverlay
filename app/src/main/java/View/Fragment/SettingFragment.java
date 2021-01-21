package View.Fragment;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import Module.DBA;
import View.Service.OverlayService;

public class SettingFragment extends Fragment {
    private ViewGroup viewGroup;
    private View mView;

    private int interestedStockSize;
    private String nickname;

    private TextView nick;
    private TextView stockSize;
    private EditText delay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container,false);
        mView = inflater.inflate(R.layout.fragment_setting, null);

        // 관심종목 개수 세기 및 닉네임 가져오기
        ArrayList<String> interestedStocks = new DBA().getInterestedStocks(getActivity().getDatabasePath("User"));
        if(interestedStocks.get(0).equals("-")) interestedStockSize = 0;
        else interestedStockSize = interestedStocks.size();
        nickname = new DBA().getNickname(getActivity().getDatabasePath("User"));
        Log.d("stocksize", String.valueOf(interestedStockSize));
        Log.d("nickname", nickname);

        // 텍스트 지정
        stockSize = (TextView)mView.findViewById(R.id.stock_size);
        nick = (TextView)mView.findViewById(R.id.setting_nickname);
        stockSize.setText("관심종목 : " + String.valueOf(interestedStockSize) + "개");
        nick.setText(nickname);
        
        // delay 시간
        delay = (EditText)mView.findViewById(R.id.stockboard_delaytime);
        delay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                OverlayService.delayTime = Integer.parseInt(s.toString() + "000");
            }
        });

        return viewGroup;
    }
}
