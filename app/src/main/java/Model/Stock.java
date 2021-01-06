package Model;

import android.os.Parcel;
import android.os.Parcelable;

import Module.Thread.CrawlingThread;

public class Stock implements Parcelable {
    private String name; // 종목명
    private String stockCode; // 종목코드
    private String detailCode; // 업종코드

    // -------------- 생성자 --------------
    /**
     * 생성자의 매개변수에 종목명을 입력하여 객체를 선언한다.
     * @param name = 종목명
     */
    public Stock(String name, String stockCode, String detailCode) {
        this.name = name;
        this.stockCode = stockCode;
        this.detailCode = detailCode;
    }

    // Parcelable 사용을 위한 생성자
    protected Stock(Parcel source) {
        name = source.readString();
        stockCode = source.readString();
        detailCode = source.readString();
    }

    // -------------- Getter, Setter --------------
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }

    public String getDetailCode() { return detailCode; }
    public void setDetailCode(String detailCode) { this.detailCode = detailCode; }

    // -------------- Parcelable --------------
    // Parcelable 사용을 위한 creator 객체 생성
    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel source) {
            return new Stock(source);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(stockCode);
        parcel.writeString(detailCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}