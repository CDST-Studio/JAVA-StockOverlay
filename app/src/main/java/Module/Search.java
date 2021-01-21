package Module;

import android.content.res.AssetManager;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Stock;

public class Search {
    // -------------- 커스텀 메서드 --------------
    /**
     * assetManager: getApplicationContext().getAssets(), target: stockName
     * @param assetManager
     * @param target
     * @return
     */
    // 종목 검색 메서드
    public ArrayList<Stock> searchStock(AssetManager assetManager, String target) {
        HashMap<String, String> dbDate = null;
        ArrayList<Stock> result = new ArrayList<>();

        try {
            // 검색값이 종목코드인지 종목명인지 구분짓는 변수
            int isCode = Integer.parseInt(target);

            Crawling crawling = new Crawling(target);
            String name = crawling.codeToName();
            result = new DBA().searchStocks(assetManager, name);
        }catch (NumberFormatException e) {
            result = new DBA().searchStocks(assetManager, target);
        }
        return result;
    }
}
