package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    private String id; // ID
    private String pwd; // Password
    private ArrayList interestedStocks; // 관심종목

    // -------------- 생성자 --------------
    /**
     * 유저에 대한 기본 정보
     * @param id
     * @param pwd
     * @param interestedStocks
     */
    public User(String id, String pwd, ArrayList<String> interestedStocks) {
        this.id = id;
        this.pwd = pwd;
        this.interestedStocks = interestedStocks;
    }

    // Parcelable 사용을 위한 생성자
    protected User(Parcel source) {
        id = source.readString();
        pwd = source.readString();

        String stock = source.readString();
        while(stock != null) {
            interestedStocks.add(stock);
            stock = source.readString();
        }
    }

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
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(pwd);
        parcel.writeTypedList(interestedStocks);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
