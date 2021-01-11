package Module;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import Model.Stock;

public class DBA {
    /**
     * DB: getDatabasePath("~"), name: StockName
     * @param DB
     * @param name
     */
    // 관심 종목 추가 메서드(단수)
    public void addInterestedStocks(File DB, String name) {
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
    }
    // 관심 종목 추가 메서드(복수)
    public void addInterestedStocks(File DB, String[] names) { for(int i=0; i<names.length; i++) this.addInterestedStocks(DB, names[i]); }

    /**
     * DB: getDatabasePath("~"), name: stockName
     * @param DB
     * @param name
     */
    // 관심 종목 삭제 메서드(단수)
    public void subInterestedStocks(File DB, String name) {
        ArrayList<String> stockList = new ArrayList<>();
        String fileDir = DB + "/InterestedStocks.txt";
        try {
            FileReader fr = new FileReader(fileDir); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                String[] stocks = line.split("\n");
                for(String stock : stocks) stockList.add(stock);
            }

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
    }
    // 관심 종목 삭제 메서드(복수)
    public void subInterestedStocks(File DB, String[] names) { for(int i=0; i<names.length; i++) this.subInterestedStocks(DB, names[i]); };

    /**
     * DB: getDatabasePath("~")
     * @param DB
     */
    // 관심 종목 불러오기 메서드
    public ArrayList<String> getInterestedStocks(File DB) {
        ArrayList<String> result = new ArrayList<>();

        try {
            FileReader fr = new FileReader(DB + "/InterestedStocks.txt"); // 파일 스트림 생성
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                String[] stocks = line.split("\n");
                for(String stock : stocks) result.add(stock);
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        
        return result;
    }

    /**
     * assetManager: getApplicationContext().getAssets(), stock: new Stock()
     * @param assetManager
     * @param stock
     */
    // 해당 종목 정보 초기화 메서드
    public void initStock(AssetManager assetManager, Stock stock) {
        Crawling crawer = new Crawling(stock);
        if(stock.getStockCode() == null) stock.setStockCode(new Parsing().getCode(assetManager, stock.getName(), "code"));
        if(stock.getDetailCode() == null) stock.setDetailCode(new Parsing().getCode(assetManager, stock.getName(), "detail_code"));
        if(stock.getCurrentPrice() == null) stock.setCurrentPrice(crawer.currentPrice());
        if(stock.getChange() == null) stock.setChange(crawer.change());
        if(stock.getChangeRate() == null) stock.setChangeRate(crawer.changeRate());
        if(stock.getCurrentPrice() == null) stock.setCurrentPrice(crawer.currentPrice());
    }

    /**
     * assetManager: getApplicationContext().getAssets(), name: stockName
     * @param assetManager
     * @param name
     * @return
     */
    // 종목명에 맞는 종목 정보 반환 메서드
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
}
