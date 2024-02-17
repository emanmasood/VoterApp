package com.example.polls.payload;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PollLength {

    @NotNull
    private String day;

    //@NotNull
    //private String format;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    /*public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }*/
}
