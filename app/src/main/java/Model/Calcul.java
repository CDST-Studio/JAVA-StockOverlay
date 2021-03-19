package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Calcul implements Serializable {
    private int fee;
    private int stockprice;
    private int quantity;

    public Calcul(Parcel in) {
        fee = in.readInt();
        stockprice = in.readInt();
        quantity = in.readInt();
    }

    public static final Parcelable.Creator<Calcul> CREATOR = new Parcelable.Creator<Calcul>() {
        @Override
        public Calcul createFromParcel(Parcel in) {
            return new Calcul(in);
        }

        @Override
        public Calcul[] newArray(int size) {
            return new Calcul[size];
        }
    };

    public Calcul() {

    }

    //public Calcul(int )
    //----------------------개셋---------------------------
    public void setFee(int bos){this.fee = bos;}
    public int getFee(){return this.fee;}

    public void setStockprice(int stockpric){this.stockprice = stockpric;}
    public int getStockprice(){return this.stockprice;}

    public void setQuantity(int quantiti ){this.quantity = quantiti;}
    public int getQuantity(){return this.quantity;}


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fee);
        dest.writeInt(stockprice);
        dest.writeInt(quantity);
    }
}
