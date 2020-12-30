package Model;

import java.io.Serializable;

public class Stock implements Serializable {
    private String name; // 종목명
    private int stockCode; // 종목코드
    private int price; // 현재가

    public Stock(String name) {
        this.name = name;
    }

    // 해당 종목의 현재가 함수
    public int currentPrice(String code) {
        int price = 0;

        return price;
    }

    // 전일 종가 대비 변동(▲ 또는 - 또는 ▼)관련 함수
    public int change(String code) {
        // 1 = ▲, 0 = -, -1 = ▼
        int flag = 0;

        return flag;
    }

    // 종목명에서 종목코드로 바꾸는 함수
    public String nameToCode(String name) {
        String code = "";

        return code;
    }

    // 종목코드에서 종목명으로 바꾸는 함수
    public String codeToName(String code) {
        String name = "";

        return name;
    }
}