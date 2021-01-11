package Module;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Parsing {
    // 종목코드 파싱
    public String getStockCode(AssetManager assetManager, String name) {
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

            result = stockData.getString("code");
            if (result.length() < 6) for (int i=0; i<=6-(result.length()); i++) result = "0" + result;
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
