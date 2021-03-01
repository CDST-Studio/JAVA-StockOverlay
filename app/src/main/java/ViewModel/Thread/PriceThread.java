package ViewModel.Thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import Model.Stock;
import Module.Crawling;
import ViewModel.MainViewModel;

public class PriceThread extends MainViewModel implements Runnable {
    private MainViewModel mModel;
    private ArrayList<Stock> tStockList;

    @Override
    public void run() {
        mModel = new MainViewModel();
        tStockList = new ArrayList<Stock>();

        while(!Thread.currentThread().isInterrupted()) {
            try {
                if(isMarketTime()) {
                    Thread.sleep(1000);
                    priceCompare();
                }
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void priceCompare() {
        int changeFlag = 0;
        //반복문으로 모든 값을 비교 하여 변경점이 있으면 값 Input
        if(mModel.getStockList().getValue() != null) {
            tStockList = mModel.getStockList().getValue();//LiveData Get
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

    private boolean isMarketTime() {
        boolean result = true;

        // 현재 시스템 시간 구하기, UTC(영국 그리니치 천문대 기준 +9시간(32400000 밀리초) 해야 한국 시간)
        long nowTime = System.currentTimeMillis() + 32400000;
        // 출력 형태를 위한 formmater
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.KOREA);
        // format에 맞게 출력하기 위한 문자열 변환
        String dTime = formatter.format(nowTime);

        int hour = 0;
        int min = 0;
        if(!dTime.split(":")[0].equals("") && !dTime.split(":")[0].equals("00")) hour = Integer.parseInt(dTime.split(":")[0].replace("0", ""));
        if(!dTime.split(":")[1].equals("") && !dTime.split(":")[1].equals("00")) min = Integer.parseInt(dTime.split(":")[1].replace("0", ""));

        if(hour >= 9 && hour <= 15) {
            if(hour == 15 && min > 30) result = false;
        }else {
            result = false;
        }

        return result;
    }
}
