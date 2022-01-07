package edu.sustech.datavisualizer.utils;

import edu.sustech.datavisualizer.entity.RawData;
import edu.sustech.datavisualizer.entity.CountryCase;
import edu.sustech.datavisualizer.entity.StdData;
import edu.sustech.datavisualizer.entity.TimeSeriesData;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DataProcessor {
    private static final String regex = ".*-15$";
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static List<StdData> newCasesInLastNDays(String source, int nDay, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -(nDay + 1));
        Date ago = calendar.getTime();

        List<CountryCase> data;
        if (source.equals("JHU")) data = DataLoader.getJHUData();
        else data = DataLoader.getWHOData();

        Map<String, Integer> map = data.stream()
                .filter(d -> parseDate(d.getDate()).after(ago))
                .collect(Collectors.groupingBy(CountryCase::getCountry,
                        Collectors.summingInt(CountryCase::getDailyCases)));

        return map
                .entrySet()
                .stream()
                .map(item -> new StdData(item.getKey(), item.getValue()))
                .sorted(Comparator.comparingInt(StdData::getValue).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public static List<TimeSeriesData> lineRaceData(String source) {
        List<CountryCase> data;
        if (source.equals("JHU")) data = DataLoader.getJHUData();
        else data = DataLoader.getWHOData();

        return data.stream()
                .filter(d -> Pattern.matches(regex, d.getDate()))
                .map(item -> new TimeSeriesData(
                        item.getCountry(),
                        item.getDate().substring(0, item.getDate().length() - 3),
                        item.getTotalCases()))
                .collect(Collectors.toList());
    }

    public static Date parseDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
