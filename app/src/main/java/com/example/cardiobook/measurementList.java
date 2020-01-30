package com.example.cardiobook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class measurementList {
    ArrayList<Measurement> myList;

    public measurementList(ArrayList<Measurement> myList){
        this.myList = myList;
    }

    public measurementList(){
        this.myList = new ArrayList<>();
        Measurement[] measurements = {};
        myList.addAll(Arrays.asList(measurements));

    }

    public ArrayList<Measurement> getMeasurementList(){
        return this.myList;
    }

    public void setMeasurementList(ArrayList<Measurement> myList){
        this.myList = myList;
    }
}
