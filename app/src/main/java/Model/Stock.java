package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Stock implements Parcelable {
    private String name; // 종목명
    private String stockCode; // 종목코드
    private String detailCode; // 업종코드
    private String currentPrice; // 현재가
    private String change; // 등락 동향
    private String changeRate; // 등락율
    private String changePrice; // 등락 가격
    private String purchasePrice; // 매입가

    // -------------- 생성자 --------------
    /**
     * 해당 종목에 대한 Stock 객체의 모든 객체 변수 초기화 생성자
     * @param name
     * @param stockCode
     * @param detailCode
     * @param currentPrice
     * @param change
     * @param changeRate
     * @param changePrice
     */
    public Stock(String name, String stockCode, String detailCode, String currentPrice, String change, String changeRate, String changePrice) {
        this.name = name;
        this.stockCode = stockCode;
        this.detailCode = detailCode;
        this.currentPrice = currentPrice;
        this.change = change;
        this.changeRate = changeRate;
        this.changePrice = changePrice;
    }

    /**
     * 해당 종목명 초기화 생성자
     * @param name
     */
    public Stock(String name) { this.name = name; }
    public Stock(){};

    // Parcelable 사용을 위한 생성자
    protected Stock(Parcel source) {
        name = source.readString();
        stockCode = source.readString();
        detailCode = source.readString();
        currentPrice = source.readString();
        change = source.readString();
        changeRate = source.readString();
        changePrice = source.readString();
        purchasePrice = source.readString();
    }

    // -------------- Getter, Setter --------------
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }

    public String getDetailCode() { return detailCode; }
    public void setDetailCode(String detailCode) { this.detailCode = detailCode; }

    public String getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(String currentPrice) { this.currentPrice = currentPrice; }

    public String getChange() { return change; }
    public void setChange(String change) { this.change = change; }

    public String getChangeRate() { return changeRate; }
    public void setChangeRate(String changeRate) { this.changeRate = changeRate; }

    public String getChangePrice() { return changePrice; }
    public void setChangePrice(String changePrice) { this.changePrice = changePrice; }

    public String getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(String purchasePrice) { this.purchasePrice = purchasePrice; }

    // -------------- Parcelable --------------
    // Parcelable 사용을 위한 creator 객체 생성
    public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {
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
        parcel.writeString(currentPrice);
        parcel.writeString(change);
        parcel.writeString(changeRate);
        parcel.writeString(changePrice);
        parcel.writeString(purchasePrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}