package Model;

public class Calcul {
    private int buyorsell;
    private int stockprice;
    private String name;

    //public Calcul(int )
    //----------------------개셋---------------------------
    public void setBuyorsell(int bos){this.buyorsell = bos;}
    public int getBuyorsell(){return this.buyorsell;}

    public void setStockprice(int stockpric){this.stockprice = stockpric;}
    public int getStockprice(){return this.stockprice;}

    public void setName(String name){this.name = name;}
    public String getName(){return this.name;}

}
