package Model.Thread;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FireStore_Thread extends Thread {
    // FireStore(Firebase) 접속용 Instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 해당 주식 관련 초기화 해야하는 변수 목록
    private String name; // 종목명
    private String stockCode; // 종목코드
    private String detailCode; // 업종코드

    /**
     * name, stockCode, detailCode 순으로 생성자의 매개변수에 입력하여 객체를 선언한다.
     * @param name = 종목명
     * @param stockCode = 종목코드
     * @param detailCode = 업종코드
     */
    public FireStore_Thread(String name, String stockCode, String detailCode) {
        this.name = name;
        this.stockCode = stockCode;
        this.detailCode = detailCode;
    }

    @Override
    public void run() {
        Log.d("start", "FireStore_Thread Start");
        // 종목명에 맞는 데이터를 FireStore DB 중 Stock 컬렉션에서 불러온다.
        final DocumentReference docRef = db.collection("Stock").document(name);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                // 불러오기 실패시
                if (e != null) {
                    Log.w("Failed", "Listen failed.", e);
                    return;
                }

                // 불러오기 성공시
                if (snapshot != null && snapshot.exists()) {
                    // 데이터가 null 이 아닐때
                    stockCode = snapshot.getData().get("code").toString();
                    detailCode = snapshot.getData().get("detail_code").toString();
                    Log.d("This stock's data", "Stock Code: " + stockCode + ", Detail Code: " + detailCode);
                } else {
                    // 데이터가 null 일때
                    Log.d("Null", "Current data: null");
                }
            }
        });
        Log.d("end", "FireStore_Thread end");
    }
}
