package Model.Thread;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Crawling_Thread extends Thread {
    // 해당 주식의 종목코드
    private String stockCode;

    // 웹 크롤링용 클래스 변수
    private Document doc; // URL 정보
    private String url; // url

    /**
     * 종목코드를 생성자의 매개변수에 입력하여 객체를 선언한다.
     * @param stockCode = 종목코드
     */
    public Crawling_Thread(String stockCode) { this.stockCode = stockCode; }

    @Override
    public void run() {
        try {
            Log.d("start", "Crawling_Thread Start");
            Log.d("Crawling target", "stockCode: " + stockCode);

            // URL 정보 초기화
            url = "https://finance.naver.com/item/main.nhn?code=" + stockCode;
            Log.d("crawling", "URL: " + url);

            // 해당 URL의 HTML 정보 가져오기
            doc = Jsoup.connect(url).get();
            Log.d("Doc text", doc.text());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Log.d("end", "Crawling_Thread end");
    }

    public int crawlingCurrentPrice() {
        return Integer.parseInt(doc.select("#chart_area > div.rate_info > div.today > div.no_today > em.no_up").text());
    }
}