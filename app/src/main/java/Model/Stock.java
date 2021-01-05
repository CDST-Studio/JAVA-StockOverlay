package Model;

import android.util.Log;

import org.jsoup.nodes.Document;

import java.io.Serializable;

import Model.Thread.Crawling_Thread;
import Model.Thread.FireStore_Thread;

public class Stock implements Serializable {
    // 웹 크롤링용 클래스 변수
    private Document doc; // URL 정보

    // 해당 주식 관련 클래스 변수
    private String name; // 종목명
    private String stockCode; // 종목코드
    private String detailCode; // 업종코드

    /**
     * 생성자의 매개변수에 종목명을 입력하여 객체를 선언한다.
     * @param name = 종목명
     */
    public Stock(String name) {
        this.name = name;
        Log.d("start", "Init Start");

        FireStore_Thread FireStoreTh = new FireStore_Thread(this.name, this.stockCode, this.detailCode);
        Crawling_Thread CrawlingTh = new Crawling_Thread(this.stockCode, this.doc);
        try {
            FireStoreTh.start();
            FireStoreTh.join();
            CrawlingTh.start();
            CrawlingTh.join();

            Log.d("Complete Init", "name: " + this.name + ", stockCode: " + stockCode + ", detailCode: " + detailCode);
            Log.d("Doc detail", doc.text());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("end", "Init end");
    }

    // 해당 종목의 현재가 메서드
    public int currentPrice() {
        int price = Integer.parseInt(doc.select("#chart_area > div.rate_info > div.today > div.no_today > em.no_up").text());
        Log.d("price", "Current price :" + price);

        return price;
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