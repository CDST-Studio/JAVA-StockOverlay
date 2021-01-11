package ViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import Model.Stock;
import Module.Crawling;

public class PriceThread extends stockViewModel implements Runnable {

    private stockViewModel mModel;
    private Crawling crawling;
    private ArrayList<Stock> stockList;


    @Override
    public void run() {

        try {
            Thread.sleep(1000);
            priceCompare();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void priceCompare(){

        stockList = mModel.getStockList().getValue();//LiveData Get

        int changeFlag = 0;

        //반복문으로 모든 값을 비교 하여 변경점이 있으면 값 Input
        for(int i =0; i < stockList.size(); i++){
            crawling = new Crawling(mModel.getStockList().getValue().get(i));
            if(stockList.get(i).getCurrentPrice() != crawling.currentPrice()) {//새 값을 가져와서 현재값 비교
                stockList.get(i).setCurrentPrice(crawling.currentPrice());//
                stockList.get(i).setChangeRate(crawling.changeRate());
                stockList.get(i).setChange(crawling.change());
                stockList.get(i).setChangePrice(crawling.changePrice());

                changeFlag = 1;
            }

        }
        if(changeFlag == 1) mModel.getStockList().setValue(stockList);
    }

}
