package Module;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Crawling {
    // 해당 주식의 종목코드
    private String stockCode;

    // 웹 크롤링용 클래스 변수
    private Document doc; // URL 정보
    private String url; // url

    // -------------- 생성자 --------------
    /**
     * 해당 종목코드에 대한 크롤러, https://finance.naver.com (네이버 금융)
     * @param stockCode
     */
    public Crawling(String stockCode) {
        Log.d("start", "Crawling init start");
        this.stockCode = stockCode;

        // URL 정보 초기화
        url = "https://finance.naver.com/item/main.nhn?code=" + stockCode;
        Log.d("crawling", "URL: " + url);

        // URL의 HTML 정보 가져오기
        Thread docTh = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    Log.d("Doc text", doc.html());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            docTh.start();
            docTh.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("end", "Crawling init end");
    }

    // -------------- 기타 메서드 --------------
    // 현재가 크롤링 메서드
    public String currentPrice() {
        return doc.select("#chart_area > div.rate_info > div.today > p.no_today > em.no_down").text().split(" ")[0];
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

    // -------------- Getter, Setter --------------
    public Document getDoc() {
        return doc;
    }
}