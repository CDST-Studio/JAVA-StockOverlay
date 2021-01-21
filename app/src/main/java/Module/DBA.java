package Module;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Model.Stock;
import Model.User;

public class DBA {
    // FireStore(Firebase) 접속용 Instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // ---------------------------- Add ----------------------------

    /**
     * 관심 종목 추가 메서드(단수)
     * DB: getDatabasePath("~"), user: User, name: StockName
     * @param DB
     * @param user
     * @param name
     */
    public void addInterestedStocks(File DB, User user, String name) {
        try {
            FileWriter fw = new FileWriter(new File(DB + "/InterestedStocks.txt"), true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(name);
            bw.newLine();
            bw.flush();

            bw.close();
            fw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        final DocumentReference washingtonRef = db.collection("User").document(user.getNickName());
        List<String> savedInterestedStocks = user.getInterestedStocks();
        savedInterestedStocks.add(name);

        washingtonRef
                .update("interestedStocks", savedInterestedStocks)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Updata Success", "관심종목 추가 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Update Failed", "관심종목 추가 실패, ", e);
                    }
                });
    }
    // 관심 종목 추가 메서드(복수)
    public void addInterestedStocks(File DB, User user, String[] names) { for(int i=0; i<names.length; i++) this.addInterestedStocks(DB, user, names[i]); }

    // ---------------------------- Sub ----------------------------

    /**
     * 관심 종목 삭제 메서드(단수)
     * DB: getDatabasePath("~"), name: stockName
     * @param DB
     * @param name
     */
    public void subInterestedStocks(File DB, User user, String name) {
        ArrayList<String> stockList = new ArrayList<>();
        String fileDir = DB + "/InterestedStocks.txt";

        try {
            FileReader fr = new FileReader(fileDir); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) { stockList.add(line); }

            stockList.remove(name);
            FileWriter fw = new FileWriter(new File(fileDir), false);
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i=0; i<stockList.size(); i++) {
                bw.write(stockList.get(i));
                bw.newLine();
                bw.flush();
            }

            br.close();
            fr.close();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        final DocumentReference washingtonRef = db.collection("User").document(user.getNickName());
        List<String> savedInterestedStocks = user.getInterestedStocks();
        savedInterestedStocks.remove(name);

        washingtonRef
                .update("interestedStocks", savedInterestedStocks)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Updata Success", "관심종목 추가 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Update Failed", "관심종목 추가 실패, ", e);
                    }
                });
    }
    // 관심 종목 삭제 메서드(복수)
    public void subInterestedStocks(File DB, User user, String[] names) { for(int i=0; i<names.length; i++) this.subInterestedStocks(DB, user, names[i]); };

    // ---------------------------- Init, 처음 객체(모델) 생성할 때(Ex. LoginActivity에서 User나 Stock 객체 생성) 무조건 Init 메서드 사용하는 메서드 목록 ----------------------------

    /**
     * 관심 종목 초기화 메서드
     * DB: getDatabasePath("~"), user: new User()
     * @param DB
     * @param user
     */
    public void initInterestedStocks(File DB, User user) {
        ArrayList<String> result = new ArrayList<>();
        String fileName = DB + "/InterestedStocks.txt";

        try {
            File saveFile = new File(fileName); // 저장 경로
            if(!saveFile.exists()){ // 파일 없을 경우
                saveFile.createNewFile(); // 파일 생성
            }

            FileReader fr = new FileReader(fileName); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) { result.add(line); }

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        user.setInterestedStocks(result);
    }

    /**
     * 닉네임 초기 설정 메서드
     * DB: getDatabasePath("~"), user: new User(), nickname: user.getNickname()
     * @param DB
     * @param user
     * @param nickname
     */
    public void initNickname(File DB, User user, String nickname) {
        String fileName = DB + "/Nickname.txt";

        try {
            File saveFile = new File(fileName); // 저장 경로
            if(!saveFile.exists()){ // 폴더 없을 경우
                saveFile.createNewFile(); // 폴더 생성
            }

            FileWriter fw = new FileWriter(new File(fileName), false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(nickname);
            bw.flush();

            bw.close();
            fw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("interestedStocks", user.getInterestedStocks());

        db.collection("User").document(nickname)
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success save", "환영합니다.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fail save", "회원가입 실패, Error: ", e);
                    }
                });

        user.setNickName(nickname);
    }

    /**
     * 해당 종목 정보 초기화 메서드
     * assetManager: getApplicationContext().getAssets(), stock: new Stock()
     * @param assetManager
     * @param stock
     */
    public void initStock(AssetManager assetManager, Stock stock) {
        if(stock.getStockCode() == null) stock.setStockCode(new Parsing().getCode(assetManager, stock.getName(), "code"));
        if(stock.getDetailCode() == null) stock.setDetailCode(new Parsing().getCode(assetManager, stock.getName(), "detail_code"));

        Crawling crawer = new Crawling(stock);
        if(stock.getCurrentPrice() == null) stock.setCurrentPrice(crawer.currentPrice());
        if(stock.getChange() == null) stock.setChange(crawer.change());
        if(stock.getChangeRate() == null) stock.setChangeRate(crawer.changeRate());
        if(stock.getChangePrice() == null) stock.setChangePrice(crawer.changePrice());
    }

    // ---------------------------- Get, Search 모델 객체 생성 후 또는 이름으로만 주식 종목 찾을 때 사용하는 메서드 목록 ----------------------------

    /**
     * 관심 종목 불러오기 메서드
     * DB: getDatabasePath("~"), nickname: user.getNickname()
     * @param DB
     */
    public ArrayList<String> getInterestedStocks(File DB) {
        ArrayList<String> result = new ArrayList<>();

        try {
            FileReader fr = new FileReader(DB + "/InterestedStocks.txt"); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) { result.add(line); }

            if(result.size() == 0) result.add("-");

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        
        return result;
    }

    /**
     * 닉네임 불러오는 메서드
     * DB: getDatabasePath("~")
     * @param DB
     * @return
     */
    public String getNickname(File DB) {
        String nickname = "";
        try {
            FileReader fr = new FileReader(DB + "/Nickname.txt"); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            nickname = br.readLine();

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return nickname;
    }
    
    /**
     * 종목명에 맞는 종목 정보 반환 메서드
     * assetManager: getApplicationContext().getAssets(), name: stockName
     * @param assetManager
     * @param name
     * @return
     */
    public Stock getStock(AssetManager assetManager, String name) {
        Stock stock = new Stock();
        stock.setName(name);
        if(stock.getStockCode() == null) stock.setStockCode(new Parsing().getCode(assetManager, stock.getName(), "code"));
        if(stock.getDetailCode() == null) stock.setDetailCode(new Parsing().getCode(assetManager, stock.getName(), "detail_code"));

        Crawling crawer = new Crawling(stock);
        stock.setCurrentPrice(crawer.currentPrice());
        stock.setChange(crawer.change());
        stock.setChangeRate(crawer.changeRate());
        stock.setChangePrice(crawer.changePrice());

        return stock;
    }

    /**
     * 종목명에 맞는 종목코드 포함된 Stock 모델 리턴
     * @param assetManager
     * @param name
     * @return
     */
    public Stock searchStock(AssetManager assetManager, String name) {
        Stock stock = new Stock();
        stock.setName(name);
        stock.setStockCode(new Parsing().getCode(assetManager, stock.getName(), "code"));

        return stock;
    }

    // ---------------------------- isExist ----------------------------

    public boolean isExistInterestedStock(File DB, String name) {
        boolean result = false;

        try {
            FileReader fr = new FileReader(DB + "/InterestedStocks.txt"); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                if(line.equals(name)) {
                    result = true;
                    break;
                }
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return result;
    }

    /**
     * 닉네임 중복체크
     * nickname: user.getNickname()
     * @param nickname
     * @return
     */
    public boolean isExistNickname(String nickname) {
        // FireStore(Firebase) 접속용 Instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("User").document(nickname);

        return !docRef.get().isSuccessful();
    }

    // ---------------------------- isInit ----------------------------

    /**
     * 닉네임이 있는지 없는지 체크하는 메서드
     * DB: getDatabasePath("~")
     * @param DB
     * @return
     */
    public boolean isInitNickname(File DB) {
        boolean result = true;

        try {
            FileReader fr = new FileReader(DB + "/Nickname.txt"); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            if ((line = br.readLine()) != null) result = false;

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return result;
    }
}
