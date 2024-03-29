package View.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;
import java.util.Iterator;

import Model.Stock;
import View.MainActivity;
import ViewModel.OverlayViewModel;
import ViewModel.Thread.OverlayThread;

import static com.google.gson.reflect.TypeToken.get;

public class OverlayService extends Service {
    private WindowManager.LayoutParams params;
    private LayoutInflater inflate;
    private WindowManager wm;
    private View mView;

    public static int delayTime;
    private Iterator<Stock> iteratorStock;

    private Thread stockBoardTh;
    private TextView stockName;
    private TextView currentPrice;
    private TextView changePrice;
    private TextView changeRate;
    private TextView purchasePrice;
    private Button overlayCancle;

    private Thread priceTh;
    private OverlayViewModel overlayViewModel;

    private Stock targetStock = new Stock();
    private static ArrayList<Stock> stocks = new ArrayList<>();

    /** 스톡보드 스레드 실행용 핸들러 */
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            if(!iteratorStock.hasNext() || iteratorStock == null) iteratorStock = stocks.iterator();
            else {
                // 다음 종목 가져오기
                targetStock = iteratorStock.next();
                
                // 텍스트 설정
                stockName.setSelected(true);
                stockName.setText(targetStock.getName());

                currentPrice.setSelected(true);
                if (targetStock.getCurrentPrice().equals("로딩중")) currentPrice.setText("로딩중");
                else currentPrice.setText(targetStock.getCurrentPrice());

                changePrice.setSelected(true);
                if (targetStock.getChangePrice().equals("로딩중")) changePrice.setText("로딩중");
                else changePrice.setText(targetStock.getChange() + targetStock.getChangePrice());

                changeRate.setSelected(true);
                if (targetStock.getChangeRate().equals("로딩중")) changeRate.setText("로딩중");
                else changeRate.setText(targetStock.getChangeRate());

                if (MainActivity.PURCHASE_PRICE_INPUT_FLAG == 1) {
                    if (targetStock.getPurchasePrice() == null) {
                        purchasePrice.setText("매입가");
                    } else {
                        if (targetStock.getProfitAndLoss().length() > 7)
                            purchasePrice.setText(targetStock.getProfitChange() + targetStock.getProfitAndLoss().substring(0, 7) + "…");
                        else
                            purchasePrice.setText(targetStock.getProfitChange() + targetStock.getProfitAndLoss());
                    }
                } else {
                    purchasePrice.setText("");
                }

                // 텍스트 애니매이션 설정
                Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stockboard_text);
                stockName.startAnimation(translate);
                currentPrice.startAnimation(translate);
                changePrice.startAnimation(translate);
                changeRate.startAnimation(translate);
                if (MainActivity.PURCHASE_PRICE_INPUT_FLAG == 1)
                    purchasePrice.startAnimation(translate);

                // 텍스트 색상 변경
                stockName.setTextColor(Color.parseColor("#80000000"));
                if (targetStock.getChange().equals("▲")) {
                    currentPrice.setTextColor(Color.parseColor("#80FF0000"));
                    changePrice.setTextColor(Color.parseColor("#80FF0000"));
                    changeRate.setTextColor(Color.parseColor("#80FF0000"));
                } else if (targetStock.getChange().equals("▼")) {
                    currentPrice.setTextColor(Color.parseColor("#800000FF"));
                    changePrice.setTextColor(Color.parseColor("#800000FF"));
                    changeRate.setTextColor(Color.parseColor("#800000FF"));
                } else {
                    currentPrice.setTextColor(Color.parseColor("#80696969"));
                    changePrice.setTextColor(Color.parseColor("#80696969"));
                    changeRate.setTextColor(Color.parseColor("#80696969"));
                }
                if (MainActivity.PURCHASE_PRICE_INPUT_FLAG == 1) {
                    if (targetStock.getProfitChange() == null) {
                        purchasePrice.setTextColor(Color.parseColor("#80696969"));
                    } else if (targetStock.getProfitChange().equals("▲")) {
                        purchasePrice.setTextColor(Color.parseColor("#80FF0000"));
                    } else {
                        purchasePrice.setTextColor(Color.parseColor("#800000FF"));
                    }
                }
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        // Android O 이상일 경우 Foreground 서비스를 실행
        // Notification channel 설정.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String strId = getString(R.string.app_name);
            final String strTitle = getString(R.string.app_name);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(strId);
            if (channel == null) {
                channel = new NotificationChannel(strId, strTitle, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(this, strId).build();
            startForeground(1, notification);
        }

        // inflater 를 사용하여 layout 을 가져오자
        inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 윈도우매니저 설정
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        // Android O 이상의 버전에서는 터치리스너가 동작하지 않는다. (TYPE_APPLICATION_OVERLAY 터치 미지원)
        mView = inflate.inflate(R.layout.overlay_view, null);

        // ViewModel 초기화
        overlayViewModel = new OverlayViewModel();

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                // Android O 이상인 경우 TYPE_APPLICATION_OVERLAY 로 설정
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        // 위치 지정
        params.gravity = Gravity.CENTER | Gravity.TOP;

        // 윈도우에 layout 을 추가 한다.
        wm.addView(mView, params);

        // delay 시간 세팅
        if(getConfigValue(getApplicationContext(), "delayTime") != null) delayTime = Integer.parseInt(getConfigValue(getApplicationContext(), "delayTime"));
        else delayTime = 4000;

        // TextView 및 Button 초기화
        stockName = (TextView)mView.findViewById(R.id.stockboard_stockname);
        currentPrice = (TextView)mView.findViewById(R.id.stockboard_currentprice);
        changePrice = (TextView)mView.findViewById(R.id.stockboard_changeprice);
        changeRate = (TextView)mView.findViewById(R.id.stockboard_changerate);
        overlayCancle = (Button)mView.findViewById(R.id.overlay_cancle);
        purchasePrice = (TextView)mView.findViewById(R.id.stockboard_purchaseprice);

        /*
        // Down → (Move) → Up → onClick 순서로 작동
        ImageButton btn_img = (ImageButton) mView.findViewById(R.id.btn_overlay);
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","onClick ");
                // do something!
            }
        });

        페이스북 메세지처럼 쳇헤드 형식으로 만들 때 필요한 코드
        // btn_img 에 android:filterTouchesWhenObscured="true" 속성 추가하면 터치리스너가 동작한다.
        btn_img.setOnTouchListener(new View.OnTouchListener() {
            private float mTouchX, mTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    Log.d("Init X, Y", "X = " + mTouchX + ", Y = " + mTouchY);
                    Log.d("Init parms X, Y", "X = " + params.x + ", Y = " + params.y);
                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    int x = (int) (event.getRawX() - mTouchX);
                    int y = (int) (event.getRawY() - mTouchY);
                    Log.d("Move X, Y", "X = " + event.getRawX() + ", Y = " + event.getRawY());
                    Log.d("Move sub", x + " / " + y);

                    params.x = (int)mTouchX + x;
                    params.y = (int)mTouchY + y;

                    wm.updateViewLayout(mView, params);
                }else if(event.getAction() == MotionEvent.ACTION_UP){

                }
                return false;
            }
        });
        */

        // 쓰레드 시작
        OverlayThread overlayThread = new OverlayThread();
        overlayThread.setContext(getApplicationContext());
        priceTh = new Thread(overlayThread);
        priceTh.start();
    }

    /** onCreate 이후에 실행되는 메서드 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stocks = overlayViewModel.getStockList().getValue();
        if(stocks != null) iteratorStock = stocks.iterator();

        overlayCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopStockBoard();
            }
        });

        stockBoardTh = new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.postDelayed(this, delayTime);
                handler.sendMessage(msg);
            }
        };
        stockBoardTh.start();


        return super.onStartCommand(intent, flags, startId);
    }

    //  -------------- 스톡보드 스레드 및 서비스 종료에 필요한 메서드 --------------
    public void stopStockBoard() {
        handler.removeMessages(0);
        priceTh.interrupt();
        setConfigValue(getApplicationContext(),"stockBoardStart", "stop");
        stopService(new Intent(mView.getContext(), OverlayService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); // Foreground service 종료
        }

        if(wm != null) {
            if(mView != null) {
                wm.removeView(mView); // View 초기화
                mView = null;
            }
            wm = null;
        }
    }

    public void overServiceObserver(){
        if(iteratorStock != null) {
            if(!iteratorStock.hasNext()) stocks.iterator();
            Stock s = iteratorStock.next();

            stocks = overlayViewModel.getStockList().getValue();
            iteratorStock = stocks.iterator();

            while (!iteratorStock.next().getName().equals(s.getName()));
        }
    }

    // Preference 읽기
    public static String getConfigValue(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, null);
    }

    // Preference 쓰기
    public static void setConfigValue(Context context, String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //  -------------- 기타 메서드 --------------
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
