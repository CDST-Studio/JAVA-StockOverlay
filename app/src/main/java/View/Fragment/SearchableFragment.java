package View.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.se.omapi.SEService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.needfor.stockoverlay.R;
import View.SearchActivity;
import android.view.View.OnClickListener;

import View.ListSearchAdapter;
import ViewModel.stockViewModel;

public class SearchableFragment  extends Fragment  {

    private Button bookmark;
    private ListSearchAdapter adapter;
    private View SearchalbeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SearchalbeView = (View)inflater.inflate(R.layout.fragment_searchable, container, false);
        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String code = getArguments().getString("code");
            ListView listview = SearchalbeView.findViewById(R.id.search_list);
            adapter = new ListSearchAdapter();
            adapter.addItem(name, code);




            
            listview.setAdapter(adapter);
        }

        return SearchalbeView;
    }
}

