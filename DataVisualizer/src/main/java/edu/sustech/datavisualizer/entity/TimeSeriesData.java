package edu.sustech.datavisualizer.entity;

import lombok.Data;

@Data
public class TimeSeriesData {
    private String country;
    private String month;
    private int cases;

    public TimeSeriesData(String country, String month, int cases) {
        this.country = country;
        this.month = month;
        this.cases = cases;
    }

    public Object[] toObjArray(){
        return new Object[]{country, month, cases};
    }
}
