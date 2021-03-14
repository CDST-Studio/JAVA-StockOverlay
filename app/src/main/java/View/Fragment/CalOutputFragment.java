package View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import View.Adapter.CalculAdapter;

public class CalOutputFragment  extends Fragment {

    private View CalOutputview;
    private RecyclerView recyclerView;
    private CalculAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> datalist;//혹시나 해서 ㅎㅎ;;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CalOutputview = (View) inflater.inflate(R.layout.fragment_stockoutput_2, container,false);

        //recyclerView = (RecyclerView) CalOutputview.findViewById(R.id.cal_list);
        //recyclerView.setHasFixedSize(true);

        //layoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(layoutManager);

        //init();

        //adapter = new CalculAdapter(datalist);
        //recyclerView.setAdapter(adapter);


        return CalOutputview;
    }

    void init(){
        datalist = new ArrayList<>();
        datalist.add("총 매수");
        datalist.add("총 매도");
        datalist.add("총 손익");
    }

}
