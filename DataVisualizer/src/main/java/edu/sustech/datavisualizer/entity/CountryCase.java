package edu.sustech.datavisualizer.entity;


import lombok.Data;

@Data
public class CountryCase {
    private String country;
    private String date;
    private int totalCases;
    private int dailyCases;
    private int totalDeaths;
    private int dailyDeaths;

    public CountryCase(String country, String date, int totalCases, int dailyCases, int totalDeaths, int dailyDeaths) {
        this.country = country;
        this.date = date;
        this.totalCases = totalCases;
        this.dailyCases = dailyCases;
        this.totalDeaths = totalDeaths;
        this.dailyDeaths = dailyDeaths;
    }
}
