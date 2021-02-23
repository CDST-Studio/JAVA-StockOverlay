package ViewModel.Thread;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cdst.stockoverlay.R;
import com.cdst.stockoverlay.StartActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import Model.Stock;
import Module.Crawling;
import ViewModel.OverlayViewModel;

public class OverlayThread extends OverlayViewModel implements Runnable {
    private Context context;
    private OverlayViewModel mModel;
    private ArrayList<Stock> tStockList;

    @Override
    public void run() {
        mModel = new OverlayViewModel();
        tStockList = new ArrayList<Stock>();
        tStockList = mModel.getStockList().getValue();//LiveData Get

        while(!Thread.currentThread().isInterrupted()) {
            try {
                if(isMarketTime()) {
                    Thread.sleep(1000);
                    onlyAlertTargetProfit();
                    priceCompare();
                }else onlyAlertTargetProfit();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void priceCompare() {
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

                // 목표수익 달성여부 확인 후 알림
                if(tStockList.get(i).getTargetProfit() != null && tStockList.get(i).getProfitAndLoss() != null) achievedTargetProfit(i);
            }
        }
        if(changeFlag == 1) mModel.getStockList().postValue(tStockList);
    }

    // 가격비교 없이 오직 목표가 달성만 알려주는 메서드
    private void onlyAlertTargetProfit() {
        if(mModel.getStockList().getValue() != null) {
            for (int i = 0; i < tStockList.size(); i++) {
                // 목표수익 달성여부 확인 후 알림
                if(tStockList.get(i).getTargetProfit() != null && tStockList.get(i).getProfitAndLoss() != null) achievedTargetProfit(i);
            }
        }
    }

    // 목표수익 달성 시 알림해주는 메서드
    public void achievedTargetProfit(int idx) {
        Stock targetStock = mModel.getStockList().getValue().get(idx);

        String targetProfit = targetStock.getTargetProfit().replace(",", "");
        String profitAndLoss = targetStock.getProfitAndLoss().replace(",", "");
        if(targetProfit.equals(profitAndLoss) && targetStock.isNotification()) {
            //Notification용 채널 생성
            createNotificationChannel(targetStock.getName());

            Intent intent = new Intent(context, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, targetStock.getName())
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(targetStock.getName())
                    .setContentText("목표수익 "+targetStock.getTargetProfit()+"₩ 달성")
                    .setTicker("관심종목, "+targetStock.getName()+" 목표수익 달성알림")
                    // 알림시 진동과 소리 설정
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    // 우선순위 설정
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Notification 알림 클릭시 반응
                    .setContentIntent(pendingIntent) // Intent 실행(특정 Actitivy 실행)
                    .setAutoCancel(true); // 클릭시 해당 Notification 삭제

            // 알림 표시
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(idx, builder.build());

            // 알람 중복 방지
            targetStock.setNotification(false);
        }
    }

    // Notification 알림용 채널 생성 메서드
    private void createNotificationChannel(String stockName) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(stockName, name, importance);
            channel.setDescription(stockName+"의 Notification 채널");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

    public void setContext(Context context) { this.context = context; }
}
