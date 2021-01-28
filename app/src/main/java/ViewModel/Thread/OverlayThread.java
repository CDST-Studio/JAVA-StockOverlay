package ViewModel.Thread;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import Model.Stock;
import Module.Crawling;
import View.Fragment.MainFragment;
import ViewModel.MainViewModel;
import ViewModel.OverlayViewModel;

public class OverlayThread extends OverlayViewModel implements Runnable {
    private OverlayViewModel mModel;
    private ArrayList<Stock> tStockList;

    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep(4000);
                priceCompare();
            }
        } catch (InterruptedException e) { }
    }

    private void priceCompare(){
        mModel = new OverlayViewModel();
        tStockList = new ArrayList<Stock>();
        tStockList = mModel.getStockList().getValue();//LiveData Get

        int changeFlag = 0;

        //반복문으로 모든 값을 비교 하여 변경점이 있으면 값 Input
        if(mModel.getStockList().getValue() != null) {
            for (int i = 0; i < tStockList.size(); i++) {
                Crawling crawling = new Crawling(mModel.getStockList().getValue().get(i));
                //아래줄도 에러의 주 범
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
