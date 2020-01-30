package com.example.cardiobook;

import android.text.format.Time;

import java.time.LocalDate;
import java.util.Date;

public class Measurement {

    //attributes of the measurement
    private String date;
    private String time;
    private String sysPressure;
    private String diaPressure;
    private String BPM;
    private String comment;

    //constructor without the comment field
    public Measurement(String date, String time, String sysPressure, String diaPressure, String BPM){
        this.date = date;
        this.time = time;
        this.sysPressure = sysPressure;
        this.diaPressure = diaPressure;
        this.BPM = BPM;
    }

    //constructor with the comment field
    public Measurement(String date, String time, String sysPressure, String diaPressure, String BPM, String comment){
        this.date = date;
        this.time = time;
        this.sysPressure = sysPressure;
        this.diaPressure = diaPressure;
        this.BPM = BPM;
        this.comment = comment;
    }

    //getters and setters for the measurement attributes
    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTime(){
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSysPressure() {
        return this.sysPressure;
    }

    public void setSysPressure(String sysPressure) {
        this.sysPressure = sysPressure;
    }

    public String getDiaPressure() {
        return this.diaPressure;
    }

    public void setDiaPressure(String diaPressure) {
        this.diaPressure = diaPressure;
    }

    public String getBPM() {
        return this.BPM;
    }

    public void setBPM(String BPM) {
        this.BPM = BPM;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
