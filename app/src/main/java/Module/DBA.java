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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Model.Stock;
import Model.User;

public class DBA {
    // FireStore(Firebase) 접속용 Instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // ---------------------------- Add ----------------------------
    /**
     * 관심 종목 추가 메서드(단수)
     * DB: getDatabasePath("User"), user: User, name: StockName
     * @param DB
     * @param user
     * @param name
     */
    public void addInterestedStocks(File DB, String user, String name) {
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

        HashMap<String, String> setData = new HashMap<>();
        setData.put("매입가", null);
        db.collection("User").document(user).collection("interestedStocks").document(name)
                .set(setData)
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
    public void addInterestedStocks(File DB, String user, String[] names) { for(int i=0; i<names.length; i++) this.addInterestedStocks(DB, user, names[i]); }

    /**
     * DB: getDatabasePath("User"), user: User, name: StockName, price: PurchasePrice
     * @param DB
     * @param user
     * @param name
     * @param price
     */
    public void addPurchasePrice(File DB, String user, String name, String price) {
        ArrayList<String> stockList = new ArrayList<>();
        String fileDir = DB + "/InterestedStocks.txt";

        try {
            FileReader fr = new FileReader(fileDir); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) { stockList.add(line); }

            FileWriter fw = new FileWriter(new File(fileDir), false);
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i=0; i<stockList.size(); i++) {
                if(stockList.get(i).split(":")[0].equals(name)) {
                    bw.write(stockList.get(i) + ":" + price);
                    bw.newLine();
                }else {
                    bw.write(stockList.get(i));
                    bw.newLine();
                }
            }
            bw.flush();

            br.close();
            fr.close();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        HashMap<String, String> setData = new HashMap<>();
        setData.put("매입가", price);
        db.collection("User").document(user).collection("interestedStocks").document(name)
                .set(setData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Updata Success", "매입가 추가 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Update Failed", "매입가 추가 실패, ", e);
                    }
                });
    }

    // ---------------------------- Sub ----------------------------

    /**
     * 관심 종목 삭제 메서드(단수)
     * DB: getDatabasePath("User"), name: stockName
     * @param DB
     * @param name
     */
    public void subInterestedStocks(File DB, String user, String name) {
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
            }
            bw.flush();

            br.close();
            fr.close();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        db.collection("User").document(user).collection("interestedStocks").document(name)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Updata Success", "관심종목 삭제 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Update Failed", "관심종목 삭제 실패, ", e);
                    }
                });
    }
    // 관심 종목 삭제 메서드(복수)
    public void subInterestedStocks(File DB, String user, String[] names) { for(int i=0; i<names.length; i++) this.subInterestedStocks(DB, user, names[i]); };

    /**
     * DB: getDatabasePath("User"), user: User, name: StockName
     * @param DB
     * @param user
     * @param name
     */
    public void subPurchasePrice(File DB, User user, String name) {
        ArrayList<String> stockList = new ArrayList<>();
        String fileDir = DB + "/InterestedStocks.txt";

        try {
            FileReader fr = new FileReader(fileDir); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) { stockList.add(line); }

            FileWriter fw = new FileWriter(new File(fileDir), false);
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i=0; i<stockList.size(); i++) {
                if(stockList.get(i).split(":")[0].equals(name)) {
                    bw.write(stockList.get(i).split(":")[0]);
                    bw.newLine();
                }else {
                    bw.write(stockList.get(i));
                    bw.newLine();
                }
            }
            bw.flush();

            br.close();
            fr.close();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        db.collection("User").document(user.getNickName()).collection("interestedStocks").document(name)
                .update("매입가", null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Updata Success", "매입가 삭제 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Update Failed", "매입가 삭제 실패, ", e);
                    }
                });
    }

    // ---------------------------- Search ----------------------------

    /**
     * DB: getDatabasePath("User"), name: 종목명
     * @param assetManager
     * @param name
     * @return
     */
    public ArrayList<Stock> searchStocks(AssetManager assetManager, String name) {
        ArrayList<Stock> stocks = new ArrayList<>();
        Parsing parsing = new Parsing();

        ArrayList<String> names = parsing.searchName(assetManager,name);

        for(int i=0; i<names.size(); i++) {
            Stock stock = new Stock();
            stock.setName(names.get(i));
            stock.setStockCode(parsing.getCode(assetManager, names.get(i), "code"));
            stocks.add(stock);
        }

        return stocks;
    }

    // ---------------------------- Init, 처음 객체(모델) 생성할 때(Ex. LoginActivity에서 User나 Stock 객체 생성) 무조건 Init 메서드 사용하는 메서드 목록 ----------------------------

    /**
     * 관심 종목 초기화 메서드
     * DB: getDatabasePath("User"), user: new User()
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
            while((line = br.readLine()) != null) { result.add(line.split(":")[0]); }

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        user.setInterestedStocks(result);
    }

    /**
     * 닉네임 초기 설정 메서드
     * DB: getDatabasePath("User"), user: new User(), nickname: user.getNickname()
     * @param DB
     * @param user
     * @param nickname
     */
    public void initNickname(File DB, User user, String nickname) {
        String fileName = DB + "/Nickname.txt";

        try {
            File saveDir = new File(DB.toString());
            File saveFile = new File(fileName);
            if(!saveDir.exists()) {
                saveDir.mkdir();
            }
            if(!saveFile.exists()){
                saveFile.createNewFile();
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

        // 현재 시스템 시간 구하기
        long systemTime = System.currentTimeMillis();
        // 출력 형태를 위한 formmater
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        // format에 맞게 출력하기 위한 문자열 변환
        String dTime = formatter.format(systemTime);

        HashMap<String, Object> setDate = new HashMap<>();
        setDate.put("생성일", dTime);
        db.collection("User").document(nickname)
                .set(setDate)
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

    // ---------------------------- Get 모델 객체 생성 후 또는 이름으로만 주식 종목 찾을 때 사용하는 메서드 목록 ----------------------------

    /**
     * 관심 종목 불러오기 메서드
     * DB: getDatabasePath("User")
     * @param DB
     */
    public ArrayList<String> getInterestedStocks(File DB) {
        ArrayList<String> result = new ArrayList<>();

        try {
            FileReader fr = new FileReader(DB + "/InterestedStocks.txt"); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) { result.add(line.split(":")[0]); }

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
     * DB: getDatabasePath("User")
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
     * 관심 종목 불러오기 메서드
     * DB: getDatabasePath("User")
     * @param DB
     */
    public ArrayList<String> getPurchasePrice(File DB) {
        ArrayList<String> result = new ArrayList<>();

        try {
            FileReader fr = new FileReader(DB + "/InterestedStocks.txt"); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                String[] split = line.split(":");

                if(split.length == 1) result.add("-");
                else result.add(split[1]);
            }

            if(result.size() == 0) result.add("-");

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return result;
    }

    // ---------------------------- isInit ----------------------------

    /**
     * 닉네임이 있는지 없는지 체크하는 메서드
     * DB: getDatabasePath("User")
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
