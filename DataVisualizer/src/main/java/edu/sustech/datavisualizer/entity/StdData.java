package edu.sustech.datavisualizer.entity;

import lombok.Data;

@Data
public class StdData {
    private String name;
    private int value;

    public StdData(String name, int value) {
        this.name = name;
        this.value = value;
    }
}