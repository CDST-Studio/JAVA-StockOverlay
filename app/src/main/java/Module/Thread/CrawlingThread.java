package Module.Thread;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CrawlingThread extends Thread {
    // 해당 주식의 종목코드
    private String stockCode;

    // 웹 크롤링용 클래스 변수
    private Document doc; // URL 정보
    private String url; // url

    /**
     * 종목코드를 생성자의 매개변수에 입력하여 객체를 선언한다.
     * @param stockCode = 종목코드
     */
    public CrawlingThread(String stockCode) { this.stockCode = stockCode; }

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

    // 현재가 크롤링 메서드
    public int currentPrice() {
        return Integer.parseInt(doc.select("#content").text());
    }

    // 전일 종가 대비 등락 동향(▲ 또는 - 또는 ▼) 메서드
    public int change() {
        // 1 = ▲, 0 = -, -1 = ▼
        int cFlag = 0;

        return cFlag;
    }

    // 전일 종가 대비 등락 가격 계산 메서드
    public int changePrice() {
        int cPrice = 0;

        return cPrice;
    }

    // 전일 종가 대비 등락률 계산 메서드
    public double changeRate() {
        double cRate = 0.0;

        return cRate;
    }

    // 종목명에서 종목코드로 바꾸는 메서드
    public String nameToCode() {
        String code = "";

        return code;
    }

    // 종목코드에서 종목명으로 바꾸는 메서드
    public String codeToName() {
        String name = "";

        return name;
    }
}