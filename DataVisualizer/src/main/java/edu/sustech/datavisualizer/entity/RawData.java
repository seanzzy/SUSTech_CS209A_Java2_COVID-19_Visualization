package edu.sustech.datavisualizer.entity;


import lombok.Data;

@Data
public class RawData {
    private String country;
    private String date;
    private String totalCases;
    private String dailyCases;
    private String totalDeaths;
    private String dailyDeaths;

    public RawData(String country, String date, String totalCases, String dailyCases, String totalDeaths, String dailyDeaths) {
        this.country = country;
        this.date = date;
        this.totalCases = totalCases;
        this.dailyCases = dailyCases;
        this.totalDeaths = totalDeaths;
        this.dailyDeaths = dailyDeaths;
    }
}
