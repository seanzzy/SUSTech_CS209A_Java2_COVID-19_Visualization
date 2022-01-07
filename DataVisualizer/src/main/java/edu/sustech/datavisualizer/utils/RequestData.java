package edu.sustech.datavisualizer.utils;

import edu.sustech.datavisualizer.DataVisualizerApplication;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

public class RequestData {
    private static final String dataPath = "./data";
    private static Instant lastUpdate = Instant.now();
    private static final Map<String, String> linkMap = Map.of(
            "WHO", "https://covid19.who.int/WHO-COVID-19-global-data.csv",
            "JHU", "https://raw.githubusercontent.com/owid/covid-19-data/master/public/data/jhu/full_data.csv"
    );

    public static Set<String> getSources() {
        return linkMap.keySet();
    }

    public static Instant getLastUpdate() {
        return lastUpdate;
    }

    public static boolean bySource(String source) {
        String urlStr = linkMap.get(source);
        String fileName = source + ".csv";
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setConnectTimeout(20000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0");

            InputStream inputStream = conn.getInputStream();
            byte[] data = readInputStream(inputStream);
            inputStream.close();

            File saveDir = new File(dataPath);
            if (!saveDir.exists()) saveDir.mkdir();
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);

            fos.flush();
            fos.close();
            DataVisualizerApplication.log.info("Succees download csv file from: " + source);
            lastUpdate = Instant.now();
            return true;
        } catch (IOException e) {
            DataVisualizerApplication.log.error("Error download csv file from: " + source);
            return false;
        }
    }

    public static boolean getProvinceData() {
        String urlStr = "https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list?modules=provinceCompare";
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setConnectTimeout(20000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0");

            InputStream inputStream = conn.getInputStream();
            byte[] data = readInputStream(inputStream);
            inputStream.close();

            File saveDir = new File(dataPath);
            if (!saveDir.exists()) saveDir.mkdir();
            File file = new File(saveDir + File.separator + "province.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);

            fos.flush();
            fos.close();
            inputStream.close();
            DataVisualizerApplication.log.info("Succees download province JSON file");
            lastUpdate = Instant.now();
            return true;
        } catch (IOException e) {
            DataVisualizerApplication.log.error("Error download province JSON file");
            return false;
        }
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.flush();
        bos.close();
        return bos.toByteArray();
    }

}
