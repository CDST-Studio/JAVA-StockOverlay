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
    private MutableLiveData<ArrayList<Stock>> stockList;
    private DBA dbAccess;
    private ArrayList<Stock> eStockList;

    public stockViewModel(){
        this.stockList = new MutableLiveData<>();
        stockList.setValue(new ArrayList<Stock>());

        ArrayList<Stock> eStockList = new ArrayList<Stock>();
    }

    public void initStockList(AssetManager assetManager, File DB){
        stockList = new MutableLiveData<>();

        ArrayList<String> getStockNameList = new DBA().getInterestedStocks(DB);

        for(String s : getStockNameList) Log.d("DB read test", s);

        for(String getStockName : getStockNameList) eStockList.add(new DBA().getStock(assetManager, getStockName));

        stockList.setValue(eStockList);
    }


    public MutableLiveData<ArrayList<Stock>> getStockList(){
        if(stockList == null){
            stockList = new MutableLiveData<>();//ArrayList<Stock>
        }
        return stockList;
    }
    
}
