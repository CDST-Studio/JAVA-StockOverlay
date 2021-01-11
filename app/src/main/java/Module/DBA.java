package Module;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DBA {
    public void initInterestedStocks(File DB, String name) {
        try {
            BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream(DB + "/InterestedStocks.txt"));
            bs.write(name.getBytes()); //Byte형으로만 넣을 수 있음
            bs.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public ArrayList<String> readInterestedStocks(File DB) {
        ArrayList<String> result = new ArrayList<>();

        try {
            // 바이트 단위로 파일읽기
            FileInputStream fileStream = new FileInputStream(DB + "/InterestedStocks.txt");// 파일 스트림 생성
            
            //버퍼 선언
            byte[] readBuffer = new byte[fileStream.available()];
            
            while (fileStream.read(readBuffer) != -1) { Log.d("Loding","Data Loding"); }
            result.add(new String(readBuffer)); // 버퍼에 입력된 것을 출력 목록에 추가

            fileStream.close(); //스트림 닫기
        } catch (Exception e) {
            e.getStackTrace();
        }
        
        return result;
    }
}
