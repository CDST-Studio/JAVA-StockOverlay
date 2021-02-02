package View.Fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.needfor.stockoverlay.R;

import java.util.ArrayList;

import Model.Stock;
import Module.Search;
import View.Adapter.ListSearchAdapter;
import ViewModel.MainViewModel;

public class SearchFragment extends Fragment {
    private View viewGroup;
    private AssetManager assetManager;

    private Search search;
    private String name, code;
    private ArrayList<Stock> stock;

    private ListView listview;
    private SearchView searchView;
    private ListSearchAdapter adapter;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (View) inflater.inflate(R.layout.fragment_search, container,false);

        assetManager = getResources().getAssets();
        search = new Search();

        mainViewModel = new MainViewModel();
        adapter = new ListSearchAdapter();
        adapter.setMainViewModel(mainViewModel);

        listview = (ListView) viewGroup.findViewById(R.id.search_list);
        searchView = (SearchView) viewGroup.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //입력결과
                // 키보드 내리기
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                
                query = query.trim();
                query = query.replaceAll("(^\\p{Z}+|\\p{Z}+$)",""); // 문자열 공백 제거

                try{ // 검색어가 숫자가 아니면, 대문자로 변환
                    int checknum = Integer.parseInt(query);
                }
                catch (NumberFormatException e) {
                    query = query.toUpperCase();
                }

                stock = search.searchStock(assetManager, query);
                adapter.resetItems();
                if(stock != null && stock.size() > 0) { //검색할 때 마다 프래그먼트 초기화
                    for(int i=0; i<stock.size(); i++) { // SearchableFragment로 값 전달
                        name = stock.get(i).getName();
                        code = stock.get(i).getStockCode();
                        adapter.addItem(name, code);
                    }
                    listview.setAdapter(adapter);
                    return true;
                }
                Toast.makeText(getContext(), "검색어를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //입력중
                return false;
            }
        });

        return viewGroup;
    }
}
