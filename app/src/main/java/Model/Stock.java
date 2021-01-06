package Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

import Module.Thread.Thread.CrawlingThread;

public class Stock implements Serializable {
    // FireStore(Firebase) 접속용 Instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 스레드
    private CrawlingThread CrawlingTh;

    // 해당 주식 관련 클래스 변수
    private String name; // 종목명
    private String stockCode; // 종목코드
    private String detailCode; // 업종코드

    /**
     * 생성자의 매개변수에 종목명을 입력하여 객체를 선언한다.
     * @param name = 종목명
     */
    public Stock(String name) {
        this.name = name;
        Log.d("start", "Init Start");

        // 종목명에 맞는 데이터를 FireStore DB 중 Stock 컬렉션에서 불러온다.
        final DocumentReference docRef = db.collection("Stock").document(this.name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        stockCode = document.getData().get("code").toString();
                        detailCode = document.getData().get("detail_code").toString();
                        Log.d("This stock's data", "Stock Code: " + stockCode + ", Detail Code: " + detailCode);

                        CrawlingTh = new CrawlingThread(stockCode);
                        CrawlingTh.start();
                    } else { Log.d("No Search", "No such document"); }
                } else { Log.d("Failed", "get failed with ", task.getException()); }
            }
        });

        Log.d("end", "Init end");
    }

    // 해당 종목의 현재가 메서드
    public int currentPrice() {
        return 0;
    }

    // 전일 종가 대비 등락 동향(▲ 또는 - 또는 ▼) 메서드
    public int change() {
        // 1 = ▲, 0 = -, -1 = ▼
        int cFlag = 0;

        return cFlag;
    }

    // 전일 종가 대비 등락 가격 계산 메서드
    public int changePrice() {
        int cPrice = 0;

        return cPrice;
    }

    // 전일 종가 대비 등락률 계산 메서드
    public double changeRate() {
        double cRate = 0.0;

        return cRate;
    }

    // 종목명에서 종목코드로 바꾸는 메서드
    public String nameToCode() {
        String code = "";

        return code;
    }

    // 종목코드에서 종목명으로 바꾸는 메서드
    public String codeToName() {
        String name = "";

        return name;
    }

    // Getter, Setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStockCode() {
        return stockCode;
    }
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getDetailCode() {
        return detailCode;
    }
    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }
}