package ViewModel.Thread;

import java.util.ArrayList;

import Model.Stock;
import Module.Crawling;
import ViewModel.MainViewModel;

public class PriceThread extends MainViewModel implements Runnable {
    private MainViewModel mModel;
    private ArrayList<Stock> tStockList;

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                priceCompare();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void priceCompare() {
        mModel = new MainViewModel();
        tStockList = new ArrayList<Stock>();
        tStockList = mModel.getStockList().getValue();//LiveData Get

        int changeFlag = 0;

        //반복문으로 모든 값을 비교 하여 변경점이 있으면 값 Input
        if(mModel.getStockList().getValue() != null) {
            for (int i = 0; i < tStockList.size(); i++) {
                Crawling crawling = new Crawling(mModel.getStockList().getValue().get(i));

                //새 값을 가져와서 현재값 비교
                if(!(tStockList.get(i).getCurrentPrice().equals(crawling.currentPrice()))) {
                    tStockList.get(i).setCurrentPrice(crawling.currentPrice());//
                    tStockList.get(i).setChangeRate(crawling.changeRate());
                    tStockList.get(i).setChange(crawling.change());
                    tStockList.get(i).setChangePrice(crawling.changePrice());
                    tStockList.get(i).setProfitAndLoss();

                    changeFlag = 1;
                }
            }
        }
        if(changeFlag == 1) mModel.getStockList().postValue(tStockList);
    }
}
