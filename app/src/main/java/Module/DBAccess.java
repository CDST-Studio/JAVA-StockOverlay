package Module;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import Model.Stock;
import Model.User;

public class DBAccess {
    // FireStore(Firebase) 접속용 Instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // -------------- 생성자 --------------
    /**
     * Google FireStore DB 내에서 Stock 컬렉션 Access 해서 데이터 받아오는 생성자
     * @param stock
     */
    public DBAccess(Stock stock) {
        Log.d("start", "DB access start");
        // 종목명에 맞는 데이터를 FireStore DB 중 Stock 컬렉션에서 불러온다.
        final DocumentReference docRef = db.collection("Stock").document(stock.getName());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        stock.setStockCode(document.getData().get("code").toString());
                        stock.setDetailCode(document.getData().get("detail_code").toString());
                        Log.d("This stock's data", "Stock Code: " + stock.getStockCode() + ", Detail Code: " + stock.getDetailCode());
                    } else {
                        Log.d("No Search", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
        Log.d("end", "DB access end");
    }

    // 기본 생성자
    public DBAccess() { }

    // -------------- 기타 메서드 --------------
    // 회원가입 메서드
    public void signUp(User user) {
        HashMap<String, Object> userData = new HashMap<>();
        HashMap<String, Object> encryptPwd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encryptPwd = encrypt(user.getPwd());
        }

        userData.put("pwd", encryptPwd.get("pwd"));
        userData.put("salt", encryptPwd.get("salt"));
        userData.put("interestedStocks", Arrays.asList("삼성전자", "LG전자", "센트리온", "포항제철", "현대자동차", "LG건강"));

        db.collection("User").document(user.getId())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success save", "회원가입 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fail save", "회원가입 실패, Error: ", e);
                    }
                });
    }

    // 로그인 메서드
    public void signIn(User user) {
        final DocumentReference docRef = db.collection("User").document(user.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String pwd = document.getData().get("pwd").toString();
                        String salt = document.getData().get("salt").toString();

                        if (pwd.equals(decrypt(user.getPwd(), salt))) {
                            Log.d("Success SignIn", "로그인 성공");
                        }else {
                            Log.d("Failed SignIn", "로그인 실패, assistant = " + salt + ", DBpwd: " + pwd + ", Decryptpwd: " + decrypt(user.getPwd(), salt));
                        }
                    } else {
                        Log.d("No Search", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
    }

    // 관심종목 추가 메서드
    public void addInterestedStock(User user, String name) {
        final DocumentReference washingtonRef = db.collection("User").document(user.getId());
        washingtonRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public synchronized void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String[] loadedInterestedStocks = document.getData().get("interestedStocks").toString().replaceAll(" ", "").replace("[", "").replace("]", "").split(",");
                        List<String> savedInterestedStocks = new ArrayList<>(Arrays.asList(loadedInterestedStocks));
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
                    } else {
                        Log.d("No Search", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
    }

    // 관심종목 삭제 메서드
    public void subInterestedStock(User user, String name) {
        final DocumentReference washingtonRef = db.collection("User").document(user.getId());
        washingtonRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public synchronized void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String[] loadedInterestedStocks = document.getData().get("interestedStocks").toString().replaceAll(" ", "").replace("[", "").replace("]", "").split(",");
                        for(String s : loadedInterestedStocks) Log.d(s.toUpperCase(), s);
                        List<String> savedInterestedStocks = new ArrayList<>(Arrays.asList(loadedInterestedStocks));
                        for(String s : savedInterestedStocks) Log.d("savedElement", s + " " + s.getClass().getName());
                        Log.d("indexOf", Integer.toString(savedInterestedStocks.indexOf(name)));
                        savedInterestedStocks.remove(name);

                        washingtonRef
                                .update("interestedStocks", savedInterestedStocks)
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
                    } else {
                        Log.d("No Search", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
    }

    // 모든 관심종목 읽어오는 메서드
    public String[] readAllInterestedStock(User user) {
        final String[][] interestedStocks = new String[1][1];
        final DocumentReference docRef = db.collection("User").document(user.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public synchronized void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        interestedStocks[0] = document.getData().get("interestedStocks").toString().replaceAll(" ", "").replace("[", "").replace("]", "").split(",");
                        for (String s : interestedStocks[0]) Log.d("test", s);
                    } else {
                        Log.d("No Search", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
        return interestedStocks[0];
    }

    // -------------- 암호화, 복호화 메서드 --------------
    public HashMap<String, Object> encrypt(String plainText) {
        HashMap<String, Object> pwdData = new HashMap<>();

        String encryptResult = "";
        String salt = "";
        SecureRandom random = null;
        try {
            byte[] bytes = new byte[16];
            random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(bytes);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                salt = new String(Base64.getEncoder().encode(bytes));
            }
            pwdData.put("salt", salt);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            md.update(plainText.getBytes());

            encryptResult = String.format("%0128x", new BigInteger(1, md.digest()));
            pwdData.put("pwd", encryptResult);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return pwdData;
    }

    public String decrypt(String encryptedText, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            md.update(encryptedText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return String.format("%0128x", new BigInteger(1, md.digest()));
    }
}
