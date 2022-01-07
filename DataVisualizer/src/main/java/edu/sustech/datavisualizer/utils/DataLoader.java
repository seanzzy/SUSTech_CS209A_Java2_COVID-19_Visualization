package edu.sustech.datavisualizer.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opencsv.CSVReader;
import edu.sustech.datavisualizer.DataVisualizerApplication;
import edu.sustech.datavisualizer.entity.CountryCase;
import edu.sustech.datavisualizer.entity.RawData;
import edu.sustech.datavisualizer.entity.StdData;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader {
    private static List<CountryCase> JHUData = new ArrayList<>();
    private static List<CountryCase> WHOData = new ArrayList<>();
    private static List<StdData> provinceData = new ArrayList<>();
    private static JSONObject chinaMap = new JSONObject();

    public static JSONObject getChinaMap() {
        return chinaMap;
    }

    public static List<CountryCase> getJHUData() {
        return JHUData;
    }

    public static List<CountryCase> getWHOData() {
        return WHOData;
    }

    public static List<StdData> getProvinceData() {
        return provinceData;
    }

    public static void readData(String source) {
        String path = "./data/" + source + ".csv";
        List<RawData> tmpData = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] dataSplit = reader.readNext();
            RawData rawData;
            while ((dataSplit = reader.readNext()) != null) {
                rawData = toData(source, dataSplit);
                if (rawData != null) tmpData.add(rawData);
            }
            if (source.equals("JHU")) {
                tmpData = processingJHUData(tmpData);
            }

            List<CountryCase> finalData = tmpData.stream()
                    .map(item -> new CountryCase(
                            item.getCountry(),
                            item.getDate(),
                            toInt(item.getTotalCases()),
                            toInt(item.getDailyCases()),
                            toInt(item.getTotalDeaths()),
                            toInt(item.getDailyDeaths())
                    ))
                    .collect(Collectors.toList());

            if (source.equals("JHU")) {
                JHUData = finalData;
            } else {
                WHOData = finalData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<RawData> processingJHUData(List<RawData> tmpData) {
        return tmpData.stream()
                .filter(DataLoader::isCountry)
                .collect(Collectors.toList());
    }

    private static boolean isCountry(RawData rawData) {
        String countryName = rawData.getCountry();
        return !(countryName.equals("World") ||
                countryName.equals("High income") ||
                countryName.equals("Europe") ||
                countryName.equals("European Union") ||
                countryName.equals("North America") ||
                countryName.equals("South America") ||
                countryName.equals("Upper middle income") ||
                countryName.equals("Asia") ||
                countryName.equals("Lower middle income") ||
                countryName.equals("Africa") ||
                countryName.equals("Oceania") ||
                countryName.equals("Low income"));
    }


    public static RawData toData(String source, String[] dataSplit) {
        if (source.equals("JHU")) {
            if (dataSplit.length < 6) return null;
            else return new RawData(
                    dataSplit[1],
                    dataSplit[0],
                    dataSplit[4],
                    dataSplit[2],
                    dataSplit[5],
                    dataSplit[3]
            );
        } else {
            if (dataSplit.length < 8) return null;
            else return new RawData(
                    dataSplit[2],
                    dataSplit[0],
                    dataSplit[5],
                    dataSplit[4],
                    dataSplit[7],
                    dataSplit[6]
            );
        }
    }

    public static void readProvinceData() {
        String path = "./data/province.json";
        List<StdData> tmpData = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject data = JSONObject.parseObject(sb.toString()).getJSONObject("data");
            JSONObject province = data.getJSONObject("provinceCompare");

            for (String pro : province.keySet()) {
                tmpData.add(new StdData(pro, province.getJSONObject(pro).getIntValue("confirmAdd")));
            }

            provinceData = tmpData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readChinaMap() {
        String path = "./data/chinageo.json";
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject tmpMap = JSONObject.parseObject(sb.toString());
            chinaMap = tmpMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int toInt(String str) {
        if (str.equals("")) return 0;
        else return (int) Math.floor(Double.parseDouble(str));
    }

}
