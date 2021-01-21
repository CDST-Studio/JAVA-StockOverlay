package View;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.needfor.stockoverlay.R;
import Model.Stock;
import Module.Search;
import Module.Parsing;
import View.Fragment.SearchableFragment;

public class SearchActivity extends AppCompatActivity {

    private Search search;
    private AssetManager assetManager;
    private Parsing parsing;
    private SearchableFragment searchableFragment;
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
            public boolean onQueryTextSubmit(String query) { //입력결과

                query = query.replaceAll("(^\\p{Z}+|\\p{Z}+$)",""); // 문자열 공백 제거

               try{ // 검색어가 숫자가 아니면, 대문자로 변환
                   int checknum = Integer.parseInt(query);
               }
               catch (NumberFormatException e) {
                   query = query.toUpperCase();
               }

                stock = search.searchStock(assetManager, query);
                String name= stock.getName();
                String code= stock.getStockCode();

                try {
                    if(name != null && code != null) {
                        searchableFragment = new SearchableFragment(); //검색할 때 마다 프래그먼트 초기화
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("code", code);
                        searchableFragment.setArguments(bundle);
                        stock = search.searchStock(assetManager, query);
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_result, searchableFragment).commitAllowingStateLoss();
                        Toast.makeText(SearchActivity.this, "검색완료", Toast.LENGTH_SHORT).show();
                        return true; }
                }catch (NumberFormatException e) {
                    return false; }

                Toast.makeText(SearchActivity.this, "검색어를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) { //입력중
                return false;
            }
        });


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

}

