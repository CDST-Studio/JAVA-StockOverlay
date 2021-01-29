package ViewModel.Thread;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import Model.Stock;
import Module.Crawling;
import ViewModel.MainViewModel;

public class PriceThread extends MainViewModel implements Runnable {
    private MainViewModel mModel;
    private ArrayList<Stock> tStockList;

    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep(1000);
                priceCompare();
            }
        } catch (InterruptedException e) { }
    }

    private void priceCompare(){
        mModel = new MainViewModel();
        tStockList = new ArrayList<Stock>();
        tStockList = mModel.getStockList().getValue();//LiveData Get

        int changeFlag = 0;

        //반복문으로 모든 값을 비교 하여 변경점이 있으면 값 Input
        if(mModel.getStockList().getValue() != null) {
            for (int i = 0; i < tStockList.size(); i++) {
                Log.v("dubae",mModel.getStockList().toString());
                Crawling crawling = new Crawling(mModel.getStockList().getValue().get(i));
                if(!(tStockList.get(i).getCurrentPrice().equals(crawling.currentPrice()))) {//새 값을 가져와서 현재값 비교
                    tStockList.get(i).setCurrentPrice(crawling.currentPrice());//
                    tStockList.get(i).setChangeRate(crawling.changeRate());
                    tStockList.get(i).setChange(crawling.change());
                    tStockList.get(i).setChangePrice(crawling.changePrice());

                    changeFlag = 1;
                }
            }
        }
        if(changeFlag == 1) mModel.getStockList().postValue(tStockList);
    }
}
