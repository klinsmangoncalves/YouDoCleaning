package br.com.kmg.youdocleaning.model;

import br.com.kmg.youdocleaning.R;

public enum CleaningStatus {

    RUNNING(0, "running"), FINISHED(1, "finished");

    private int id;
    private String description;

    CleaningStatus(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getStringId() {
        switch (this){
            case RUNNING:
                return R.string.cleaning_status_running;
            case FINISHED:
                return R.string.cleaning_status_finished;
        }
        return -1;
    }
}
