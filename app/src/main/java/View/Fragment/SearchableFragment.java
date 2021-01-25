package View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.needfor.stockoverlay.R;

import View.ListSearchAdapter;

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

