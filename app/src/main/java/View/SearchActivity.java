package View;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.needfor.stockoverlay.R;

public class SearchActivity extends AppCompatActivity {

    private SearchableFragment SearchableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        createToolbar();

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
}
