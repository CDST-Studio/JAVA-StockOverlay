package ViewModel;

import android.content.res.AssetManager;
import android.util.Log;

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
    public MainViewModel(MainViewModel s){
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

/**
*옛날 아주 먼 옛날 코딩나라에는 착한 경호와 알수없는 시현이가 살았답니다.
 * 하지만 어느날 아주 무서운 송대석이 나타나 코딩 나라의 주민들을 노예처럼 부려먹기 시작했답니다.
 * 이에 강한 반감을 가진 시현이는 병사를 모았고, 경호의 희생으로 결국 송대석을 물리치는데 성공했답니다.
 * 그렇게 코딩나라에는 제 2의 암흑기찾아왔답니다.
 * Happy Ending
 */

