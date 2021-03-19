package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Calcul implements Parcelable {
    private int fee;
    private int stockprice;
    private int quantity;

    protected Calcul(Parcel in) {
        fee = in.readInt();
        stockprice = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Calcul> CREATOR = new Creator<Calcul>() {
        @Override
        public Calcul createFromParcel(Parcel in) {
            return new Calcul(in);
        }

        @Override
        public Calcul[] newArray(int size) {
            return new Calcul[size];
        }
    };

    //public Calcul(int )
    //----------------------개셋---------------------------
    public void setFee(int bos){this.fee = bos;}
    public int getFee(){return this.fee;}

    public void setStockprice(int stockpric){this.stockprice = stockpric;}
    public int getStockprice(){return this.stockprice;}

    public void setQuantity(int quantiti ){this.quantity = quantiti;}
    public int getQuantity(){return this.quantity;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fee);
        dest.writeInt(stockprice);
        dest.writeInt(quantity);
    }
}
