package ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import Model.Stock;
import Model.User;
import Module.Crawling;
import Module.DBAccess;


public class stockViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Stock>> stockList;
    private String[] getStockNameList;
    private DBAccess dbAccess;
    private ArrayList<Stock> eStockList;


    User user;

    public stockViewModel(){
        this.stockList = new MutableLiveData<>();
        stockList.setValue(new ArrayList<Stock>());

        ArrayList<Stock> eStockList = new ArrayList<Stock>();
    }

    public MutableLiveData<ArrayList<Stock>> getStockList(){
        if(stockList == null){//처음 불러올때 DB 값 불러오기
            stockList = new MutableLiveData<ArrayList<Stock>>();
            getStockNameList = dbAccess.readAllInterestedStock(user);

            for(int i = 0; i < getStockNameList.length; i++){
                eStockList.add(dbAccess.readStock(getStockNameList[i]));
            }
            stockList.setValue(eStockList);
        }
        return stockList;
    }
}
