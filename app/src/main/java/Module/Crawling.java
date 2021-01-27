package Module;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import Model.Stock;

public class Crawling {
    // 웹 크롤링용 클래스 변수
    private Document doc; // URL 정보
    private String url; // url

    // -------------- 생성자 --------------
    /**
     * 해당 종목에 대한 크롤러, https://finance.naver.com (네이버 금융)
     * @param code
     */
    public Crawling(String code) {
        // URL 정보 초기화
        url = "https://finance.naver.com/item/main.nhn?code=" + code;

        // URL의 HTML 정보 가져오기, android.os.NetworkOnMainThreadException 오류 해결을 위한 개별 스레드에서 작업 진행
        Thread docTh = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        };
        try {
            docTh.start();
            docTh.join();
            docTh.interrupt();
        } catch (InterruptedException e) { }
    }

    // Stock 객체로 들어왔을 때의 생성자
    public Crawling(Stock stock) { this(stock.getStockCode()); }

    // -------------- 기타 메서드 --------------
    // 현재가 크롤링 메서드
    public String currentPrice() {
        Log.d("크롤링 에러 테스트", doc.text());
        String[] crawlingResult = doc.select("#chart_area > div.rate_info > div.today > p.no_today").text().split(" ");
        return crawlingResult[crawlingResult.length - 1];
    }

    // 전일 종가 대비 등락 동향(▲ 또는 - 또는 ▼) 메서드
    public String change() {
        // 1 = ▲, 0 = -, -1 = ▼
        String cFlag = "-";

        String crawlingResult = doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ")[0];
        if(crawlingResult.equals("상승")) {
            cFlag = "▲";
        }else if (crawlingResult.equals("하락")) {
            cFlag = "▼";
        }else {
            cFlag = "±";
        }

        return cFlag;
    }

    // 전일 종가 대비 등락률 계산 메서드
    public String changeRate() {
        String[] crawlingResult = doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ");
        String rate = crawlingResult[crawlingResult.length - 2] + crawlingResult[crawlingResult.length - 1];
        return rate;
    }

    // 전일 종가 대비 등락 가격 계산 메서드
    public String changePrice() {
        String[] crawlingResult = doc.select("#chart_area > div.rate_info > div.today > p.no_exday > em").text().split(" ");
        return crawlingResult[1];
    }

    public String codeToName() {
         return doc.select("#middle > div.h_company > div.wrap_company > h2").text();
    }
    // -------------- Getter --------------
    public Document getDoc() { return doc; }
    public String getUrl() { return url; }
}