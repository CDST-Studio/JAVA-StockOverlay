package Module;

import android.util.Log;

import java.util.HashMap;

import Model.Stock;

public class Search {
    // -------------- 커스텀 메서드 --------------
    // 종목 검색 메서드
    public Stock searchStock(String target) {
        HashMap<String, String> dbDate = null;
        Stock result = new Stock("삼성전자", "005930", "032604");

        try {
            // 검색값이 종목코드인지 종목명인지 구분짓는 변수
            int isCode = Integer.parseInt(target);

            Crawling crawling = new Crawling(target);
            String name = crawling.codeToName();
            //result = new DBAccess().readStock(name);
        }catch (NumberFormatException e) {
            //result = new DBAccess().readStock(target);
        }
        return result;
    }
}
