package Module;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import Model.Stock;

public class Crawling {
    // 해당 주식의 종목코드
    private Stock stock;

    // 웹 크롤링용 클래스 변수
    private Document doc; // URL 정보
    private String url; // url

    // -------------- 생성자 --------------
    /**
     * 해당 종목코드에 대한 크롤러, https://finance.naver.com (네이버 금융)
     * @param stock
     */
    public Crawling(Stock stock) {
        Log.d("start", "Crawling init start");
        this.stock = stock;

        // URL 정보 초기화
        url = "https://finance.naver.com/item/main.nhn?code=" + this.stock.getStockCode();
        Log.d("crawling", "URL: " + url);

        // URL의 HTML 정보 가져오기, android.os.NetworkOnMainThreadException 오류 해결을 위한 개별 스레드에서 작업 진행
        Thread docTh = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
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
        return doc.select("#chart_area > div.rate_info > div.today > p.no_today > em").text().split(" ")[0];
    }

    // 전일 종가 대비 등락 동향(▲ 또는 - 또는 ▼) 메서드
    public String change() {
        // 1 = ▲, 0 = -, -1 = ▼
        String cFlag = "-";

        String crawlingResult = doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ")[0];
        if(crawlingResult.equals("상승")) {
            cFlag = "▲";
        }else if (crawlingResult.equals("하향")) {
            cFlag = "▼";
        }else {
            cFlag = "-";
        }

        return cFlag;
    }

    // 전일 종가 대비 등락 가격 계산 메서드
    public String changePrice() {
        return doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ")[1];
    }

    // 전일 종가 대비 등락률 계산 메서드
    public String changeRate() {
        String rate = doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ")[3]
                + doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ")[4]
                + doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ")[6];
        return rate;
    }

    // -------------- Getter --------------
    public Document getDoc() { return doc; }
    public String getUrl() { return url; }
}