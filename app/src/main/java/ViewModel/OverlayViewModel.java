package ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Model.Stock;

public class OverlayViewModel extends ViewModel {
    private static MutableLiveData<ArrayList<Stock>> stockList;
    private ArrayList<Stock> templeStockList;

    public OverlayViewModel(){
        if(stockList == null){
            stockList = new MutableLiveData<ArrayList<Stock>>();
            templeStockList = new ArrayList<Stock>();

            stockList = new MainViewModel().getStockList();
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
    public static void setStockList(MutableLiveData<ArrayList<Stock>> stockList) { OverlayViewModel.stockList = stockList; }
}
