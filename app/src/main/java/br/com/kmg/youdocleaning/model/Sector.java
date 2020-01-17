package br.com.kmg.youdocleaning.model;

import java.io.Serializable;
import java.util.List;

public class Sector implements Serializable {
    String name;
    List<CleaningTask> tasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CleaningTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<CleaningTask> tasks) {
        this.tasks = tasks;
    }
}
