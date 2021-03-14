package View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cdst.stockoverlay.R;

public class CalInputBuyFragment extends Fragment {

    private View CalInputBuyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalInputBuyView = (View) inflater.inflate(R.layout.fragment_stockinput_buy,container,false);

        return CalInputBuyView;
    }
}
