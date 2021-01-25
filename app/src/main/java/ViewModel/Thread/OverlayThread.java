package ViewModel.Thread;

import android.util.Log;

import java.util.ArrayList;

import Model.Stock;
import Module.Crawling;
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
                Log.v("thread","쓰레드 작동중 Overlay");
                priceCompare();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    private void priceCompare(){
        mModel = new OverlayViewModel();
        tStockList = new ArrayList<Stock>();
        tStockList = mModel.getStockList().getValue();//LiveData Get

        int changeFlag = 0;
        Log.v("이름을 세번 부르면 죽는데", "송대석");

        //반복문으로 모든 값을 비교 하여 변경점이 있으면 값 Input
        //Log.v("threada","쓰레드 동작중");
        if(mModel.getStockList().getValue().size() != 0) {
            for (int i = 0; i < tStockList.size(); i++) {
                Log.v("Thread",mModel.getStockList().getValue().get(i).getName() + "/" + mModel.getStockList().getValue().get(i).getCurrentPrice() + "/" + mModel.getStockList().getValue().get(i).getStockCode());
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
