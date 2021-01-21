package ViewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import Model.Stock;
import Module.Crawling;
import View.Fragment.MainFragment;

public class PriceThread extends stockViewModel implements Runnable {

    private stockViewModel mModel;
    private Crawling crawling;
    private ArrayList<Stock> tStockList;


    @Override
    public void run() {

        try {
            while(true) {
                Thread.sleep(10000);
                priceCompare();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void priceCompare(){
        mModel = new stockViewModel();
        tStockList = new ArrayList<Stock>();
        tStockList = mModel.getStockList().getValue();//LiveData Get

        int changeFlag = 0;

        //반복문으로 모든 값을 비교 하여 변경점이 있으면 값 Input
        //Log.v("threada","쓰레드 동작중");
        if(mModel.getStockList().getValue().size() != 0) {
            for (int i = 0; i < tStockList.size(); i++) {
                crawling = new Crawling(mModel.getStockList().getValue().get(i));
                if(!(tStockList.get(i).getCurrentPrice().equals(crawling.currentPrice()))) {//새 값을 가져와서 현재값 비교
                    Log.v("threada","값 변화 감지");
                    Log.v("threada",  "종목 명 : " + tStockList.get(i).getName() + "/" + "현재 값 : " + tStockList.get(i).getCurrentPrice() + "/" + "변경 값 : " + crawling.currentPrice());
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
