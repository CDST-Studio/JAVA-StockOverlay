package Module;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Parsing {
    /**
     * assetManager: getApplicationContext().getAssets(), name: stockName, codeType: "code"=종목코드 | "detail_code"=업종코드
     * @param assetManager
     * @param name
     * @param codeType
     * @return
     */
    // 종목코드 파싱
    public String getCode(AssetManager assetManager, String name, String codeType) {
        String result = null;
        try {
            InputStream is = assetManager.open("StockData.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONObject stockData = jsonObject.getJSONObject(name);

            result = stockData.getString(codeType);
            int repeatCount = 6-result.length();
            if (result.length() < 6) for (int i=0; i<repeatCount; i++) result = "0" + result;
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public ArrayList<String> searchName(AssetManager assetManager, String name) {
        ArrayList<String> names = new ArrayList<>();
        try {
            InputStream is = assetManager.open("StockData.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray namelist = jsonObject.names();

            for(int i=0; i<namelist.length(); i++) {
                try{
                    if((namelist.getString(i)).substring(0,name.length()).equals(name)) {
                        names.add(namelist.getString(i));
                    }
                }catch (Exception e) { }
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return names;
    }
}
