package ViewModel.Thread;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.needfor.stockoverlay.R;
import com.needfor.stockoverlay.StartActivity;

import java.util.ArrayList;

import Model.Stock;
import Module.Crawling;
import ViewModel.OverlayViewModel;

public class OverlayThread extends OverlayViewModel implements Runnable {
    private Context context;
    private OverlayViewModel mModel;
    private ArrayList<Stock> tStockList;

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(4000);
                priceCompare();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void priceCompare() {
        mModel = new OverlayViewModel();
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

                // 목표수익 달성여부 확인 후 알림
                achievedTargetProfit(i);
            }
        }
        if(changeFlag == 1) mModel.getStockList().postValue(tStockList);
    }

    // 목표수익 달성 시 알림해주는 메서드
    public void achievedTargetProfit(int idx) {
        Stock targetStock = mModel.getStockList().getValue().get(idx);

        String targetProfit = targetStock.getTargetProfit().replace(",", "");
        String profitAndLoss = targetStock.getProfitAndLoss().replace(",", "");
        if(targetProfit.equals(profitAndLoss)) {
            //Notification용 채널 생성
            createNotificationChannel(targetStock.getName());

            Intent intent = new Intent(context, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, targetStock.getName())
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(targetStock.getName())
                    .setContentText("목표수익 "+targetStock.getTargetProfit()+"원 달성")
                    .setSubText("증권앱에서 매도해주세요")
                    .setTicker("흑우상향, "+targetStock.getName()+" 목표수익 달성알림")
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

    public void setContext(Context context) { this.context = context; }
}
