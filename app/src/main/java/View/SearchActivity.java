package View;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.needfor.stockoverlay.R;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;

import Model.Stock;
import Module.DBA;
import Module.Search;
import Module.Parsing;
import View.Fragment.SearchableFragment;

public class SearchActivity extends AppCompatActivity {

    private List<String> list;
    private SearchableFragment SearchableView;
    private Search search;
    private AssetManager assetManager;
    private ArrayList<String> arraylist;
    private ListViewAdapter adapter;
    private ListView listView;
    private Parsing parsing;
    private String test;
    private boolean checkquery;
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        createToolbar();

        assetManager = getResources().getAssets();
        parsing = new Parsing();
        search = new Search();

        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                checkquery = CheckNumber(query);
                if (checkquery == true) {
                    try {
                        test = parsing.getCode(assetManager, query, "detail_code"); //주식 코드 검색
                        SearchableFragment searchableFragment = new SearchableFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", test);
                        bundle.putString("code", query);
                        searchableFragment.setArguments(bundle);
                        stock = search.searchStock(assetManager, query);
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_result, searchableFragment).commitAllowingStateLoss();
                        Toast.makeText(SearchActivity.this, "코드 :" + stock, Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }

                if (checkquery = false) {
                    test = parsing.getCode(assetManager, query, "code"); //주식 이름 검색
                    if (test != null) {
                        SearchableFragment searchableFragment = new SearchableFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", query);
                        bundle.putString("code", test);
                        searchableFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_result, searchableFragment).commitAllowingStateLoss();
                        Toast.makeText(SearchActivity.this, "이름 :" + query, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(SearchActivity.this, "입력하고있는 단어 = "+newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });


    }

    // ---------- 프래그먼트 생성 ----------
    public void createPragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            SearchableView = new SearchableFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.search_result, SearchableView)
                    .commit();
        } else {
            SearchableView = (SearchableFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.search_result);
        }

    }

    //  -------------- 앱바(액션바) 및 메뉴 생성 메서드 --------------
    public void createToolbar() {
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean CheckNumber(String query) {
        try {
            Double.parseDouble(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

