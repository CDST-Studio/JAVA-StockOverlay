package ViewModel;

import android.content.res.AssetManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;

import Model.Stock;
import Model.User;
import Module.DBA;


public class stockViewModel extends ViewModel {
    static private MutableLiveData<ArrayList<Stock>> stockList;
    private DBA dbAccess;
    private ArrayList<Stock> eStockList;

    public stockViewModel(){
        //this.stockList = new MutableLiveData<ArrayList<Stock>>();
        //stockList.postValue();
        ArrayList<Stock> eStockList = new ArrayList<Stock>();

    }
    public  stockViewModel(stockViewModel s){
        stockList = s.getStockList();
    }

    public void initStockList(AssetManager assetManager, File DB){
        stockList = new MutableLiveData<>();
        eStockList = new ArrayList<Stock>();

        ArrayList<String> getStockNameList = new DBA().getInterestedStocks(DB);


        for(String getStockName : getStockNameList) eStockList.add(new DBA().getStock(assetManager, getStockName));

        stockList.setValue(eStockList);
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
