package ViewModel;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import Model.Stock;

public class OverlayViewModel {

    private static MutableLiveData<ArrayList<Stock>> stockList;
    private ArrayList<Stock> templeStockList;

    public OverlayViewModel(){
        if(stockList == null){
            stockList = new MutableLiveData<ArrayList<Stock>>();
            templeStockList = new ArrayList<Stock>();
        }

    }

    public OverlayViewModel(ArrayList<Stock> s) {
        stockList.setValue(s);
        templeStockList = new ArrayList<Stock>();
        templeStockList = stockList.getValue();
    }




    public void addStockList(Stock stock){
        if(templeStockList == null){
            templeStockList = new ArrayList<Stock>();
        }
        templeStockList = stockList.getValue();
        templeStockList.add(stock);
        stockList.setValue(this.templeStockList);
    }

    public MutableLiveData<ArrayList<Stock>> getStockList(){ return stockList; }
}
