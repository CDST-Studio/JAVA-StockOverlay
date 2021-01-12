package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    @SuppressWarnings("unchecked")
    private String nickName;
    private ArrayList interestedStocks; // 관심종목

    // -------------- 생성자 --------------
    /**
     * 유저에 대한 기본 정보
     * @param nickName
     * @param interestedStocks
     */
    public User(String nickName, ArrayList<String> interestedStocks) {
        this.nickName = nickName;
        this.interestedStocks = interestedStocks;
    }
    public User(ArrayList<String> interestedStocks) { this.interestedStocks = interestedStocks; }
    
    // 기본 생성자
    public User() { }

    // Parcelable 사용을 위한 생성자
    protected User(Parcel source) {
        String stock = source.readString();
        while(stock != null) {
            interestedStocks.add(stock);
            stock = source.readString();
        }
    }

    // -------------- Getter --------------
    public ArrayList getInterestedStocks() { return interestedStocks; }
    public void setInterestedStocks(ArrayList interestedStocks) { this.interestedStocks = interestedStocks; }

    // -------------- Parcelable --------------
    // Parcelable 사용을 위한 creator 객체 생성
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) { parcel.writeTypedList(interestedStocks); }

    @Override
    public int describeContents() {
        return 0;
    }
}
