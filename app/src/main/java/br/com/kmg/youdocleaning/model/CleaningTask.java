package br.com.kmg.youdocleaning.model;

import java.io.Serializable;

public class CleaningTask implements Serializable {
    String name;
    int duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
