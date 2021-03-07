package ViewModel;

import android.content.res.AssetManager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.ArrayList;

import Model.Stock;
import Module.DBA;

public class MainViewModel extends ViewModel implements Serializable {
    private static MutableLiveData<ArrayList<Stock>> stockList;
    private DBA dbAccess;
    private ArrayList<Stock> eStockList;

    public MainViewModel(){
        ArrayList<Stock> eStockList = new ArrayList<Stock>();
    }
    public MainViewModel(MainViewModel s) {
        stockList = s.getStockList();
    }


    public void initStockList(AssetManager assetManager, ArrayList<String> eStockList){
        stockList = new MutableLiveData<>();

        ArrayList<Stock> stockArrayList = new ArrayList<>();
        for(int i=0; i<eStockList.size(); i++) {
            stockArrayList.add(new DBA().getStock(assetManager, eStockList.get(i)));
        }
        stockList.setValue(stockArrayList);
    }

    public void addStockList(Stock stock){
        if(stockList == null) stockList = new MutableLiveData<>();//테스트를 위한 코드입니다.
        eStockList = stockList.getValue();
        if(eStockList == null) eStockList = new ArrayList<Stock>();
        eStockList.add(stock);

        stockList.setValue(this.eStockList);
    }

    public MutableLiveData<ArrayList<Stock>> getStockList(){
        if(stockList == null){
            stockList = new MutableLiveData<ArrayList<Stock>>();//ArrayList<Stock>
        }
        return stockList;
    }
}