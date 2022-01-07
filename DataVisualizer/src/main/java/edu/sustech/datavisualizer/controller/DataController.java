package edu.sustech.datavisualizer.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.sustech.datavisualizer.entity.CountryCase;
import edu.sustech.datavisualizer.entity.StdData;
import edu.sustech.datavisualizer.entity.TimeSeriesData;
import edu.sustech.datavisualizer.utils.DataLoader;
import edu.sustech.datavisualizer.utils.DataProcessor;
import edu.sustech.datavisualizer.utils.RequestData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DataController {

    @GetMapping("/pieChart")
    public JSONObject pieChart(@RequestParam String source,
                               @RequestParam int nDays,
                               @RequestParam int n
    ) {
        List<StdData> dataList = DataProcessor.newCasesInLastNDays(source, nDays, n);
        JSONObject result = new JSONObject();
        JSONArray legendData = new JSONArray();
        JSONArray seriesData = new JSONArray();

        legendData.addAll(dataList.stream()
                .map(StdData::getName)
                .collect(Collectors.toList()));

        seriesData.addAll(dataList);

        result.put("legendData", legendData);
        result.put("seriesData", seriesData);

        return result;
    }

    @GetMapping("/lineRace")
    public JSONObject lineRace(@RequestParam String source, @RequestParam int n) {
        List<TimeSeriesData> data = DataProcessor.lineRaceData(source);
        JSONObject result = new JSONObject();
        JSONArray legendData = new JSONArray();
        JSONArray seriesDate = new JSONArray();


        Map<String, Optional<Integer>> country_maxCases = data.stream()
                .collect(Collectors.groupingBy(
                        TimeSeriesData::getCountry,
                        Collectors.mapping(TimeSeriesData::getCases,
                                Collectors.maxBy(Comparator.comparingInt(i -> i)))));

        List<String> countries = country_maxCases.entrySet().stream()
                .sorted((i1, i2) -> i2.getValue().orElse(0) - i1.getValue().orElse(0))
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

//        legendData.addAll(data.stream().map(TimeSeriesData::getCountry).collect(Collectors.toSet()));

        legendData.addAll(countries);

        seriesDate.add(new String[]{"Country", "Month", "Cases"});

        seriesDate.addAll(data.stream()
                .map(TimeSeriesData::toObjArray)
                .collect(Collectors.toList()));

        result.put("legendData", legendData);
        result.put("source", seriesDate);
        return result;
    }

    @GetMapping("/mapChart")
    public JSONObject mapChart() {
        List<StdData> provinceData = DataLoader.getProvinceData();
        JSONObject result = new JSONObject();
        JSONObject chinaMap = DataLoader.getChinaMap();
        int max = provinceData.stream()
                .mapToInt(StdData::getValue)
                .max().orElse(1000);
        JSONArray seriesData = new JSONArray();
        seriesData.addAll(provinceData);

        result.put("map", chinaMap);
        result.put("seriesData", seriesData);
        result.put("max", max);
        return result;
    }

    @GetMapping("/allData")
    public JSONObject allData(@RequestParam String source) {
        List<CountryCase> data;
        if (source.equals("JHU")) data = DataLoader.getJHUData();
        else data = DataLoader.getWHOData();



        JSONObject result = new JSONObject();
        JSONArray tableData = new JSONArray();
        tableData.addAll(data.stream()
                .limit(1000)
                .collect(Collectors.toList())
        );
        result.put("tableData", tableData);
        return result;
    }


    @GetMapping("/lastUpdate")
    public Instant lastUpdate() {
        return RequestData.getLastUpdate();
    }

}

