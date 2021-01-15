package View.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
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

import com.needfor.stockoverlay.R;

import java.util.ArrayList;
import java.util.Iterator;

import Model.Stock;
import View.MainActivity;

public class OverlayService extends Service {
    private ArrayList<Stock> stocks = new ArrayList<>();
    private Iterator<Stock> iteratorStock;
    private Stock stock;

    private WindowManager wm;
    private View mView;

    private Thread stockBoardTh;
    private TextView stockName;
    private TextView currentPrice;
    private TextView change;
    private TextView changePrice;
    private TextView changeRate;
    private Button overlayCancle;

    /** 스톡보드 스레드 실행용 핸들러 */
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(iteratorStock.hasNext() == false) iteratorStock = stocks.iterator();
            Stock stock = iteratorStock.next();

            // 텍스트 설정
            stockName.setText(stock.getName());
            currentPrice.setText(stock.getCurrentPrice());
            change.setText(stock.getChange());
            changePrice.setText(stock.getChangePrice());
            changeRate.setText(stock.getChangeRate());

            // 텍스트 애니매이션 설정
            Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stockboard_text);
            stockName.startAnimation(translate);
            currentPrice.startAnimation(translate);
            change.startAnimation(translate);
            changePrice.startAnimation(translate);
            changeRate.startAnimation(translate);

            // 텍스트 색상 변경
            stockName.setTextColor(Color.parseColor("#80000000"));
            if (change.getText().equals("▲")) {
                currentPrice.setTextColor(Color.parseColor("#80FF0000"));
                change.setTextColor(Color.parseColor("#80FF0000"));
                changePrice.setTextColor(Color.parseColor("#80FF0000"));
                changeRate.setTextColor(Color.parseColor("#80FF0000"));
            } else if (change.getText().equals("▼")) {
                currentPrice.setTextColor(Color.parseColor("#800000FF"));
                change.setTextColor(Color.parseColor("#800000FF"));
                changePrice.setTextColor(Color.parseColor("#800000FF"));
                changeRate.setTextColor(Color.parseColor("#800000FF"));
            } else {
                currentPrice.setTextColor(Color.parseColor("#80808080"));
                change.setTextColor(Color.parseColor("#80808080"));
                changePrice.setTextColor(Color.parseColor("#80808080"));
                changeRate.setTextColor(Color.parseColor("#80808080"));
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
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 윈도우매니저 설정
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        // Android O 이상의 버전에서는 터치리스너가 동작하지 않는다. (TYPE_APPLICATION_OVERLAY 터치 미지원)
        mView = inflate.inflate(R.layout.overlay_view, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
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

        // TextView 및 Button 초기화
        stockName = (TextView)mView.findViewById(R.id.stockboard_stockname);
        currentPrice = (TextView)mView.findViewById(R.id.stockboard_currentprice);
        change = (TextView)mView.findViewById(R.id.stockboard_change);
        changePrice = (TextView)mView.findViewById(R.id.stockboard_changeprice);
        changeRate = (TextView)mView.findViewById(R.id.stockboard_changerate);
        overlayCancle = (Button)mView.findViewById(R.id.overlay_cancle);

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
    }

    /** onCreate 이후에 실행되는 메서드 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            stocks = intent.getParcelableArrayListExtra("stocks");
            iteratorStock = stocks.iterator();
        }

        overlayCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("종료 테스트", "stockboard 종료");
                stopStockBoard();
            }
        });

        stockBoardTh = new Thread() {
            @Override
            public void run() {
                Log.d("실행 테스트", "stockboard 실행 중");
                Message msg = handler.obtainMessage();
                handler.postDelayed(this, 4000);
                handler.sendMessage(msg);
            }
        };
        stockBoardTh.start();

        return super.onStartCommand(intent, flags, startId);
    }

    //  -------------- 스톡보드 스레드 및 서비스 종료에 필요한 메서드 --------------
    public void stopStockBoard() {
        handler.removeMessages(0);
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

    //  -------------- 기타 메서드 --------------
    @Nullable @Override
    public IBinder onBind(Intent intent) { return null; }
}
