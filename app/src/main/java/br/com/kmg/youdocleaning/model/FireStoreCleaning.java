package br.com.kmg.youdocleaning.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class FireStoreCleaning implements Serializable {

    @PropertyName("sector_id")
    private String sector_id;

    @PropertyName("start_datetime")
    private Timestamp start_datetime;

    @PropertyName("end_datetime")
    private Timestamp end_datetime;

    @PropertyName("status")
    private String status;

    private String userId;

    public FireStoreCleaning() {
    }

    public FireStoreCleaning(Cleaning cleaning) {
        this.sector_id = cleaning.getIdDepartment();
        this.start_datetime = cleaning.getStartCleaning();
        this.end_datetime = cleaning.getFinishCleaning();
        this.status = cleaning.getStatus();
        this.userId = cleaning.getUserId();
    }

    public FireStoreCleaning(String idDepartment, Timestamp startCleaning, Timestamp finishCleaning, String status) {
        this.sector_id = idDepartment;
        this.start_datetime = startCleaning;
        this.end_datetime = finishCleaning;
        this.status = status;
    }

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public Timestamp getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(Timestamp start_datetime) {
        this.start_datetime = start_datetime;
    }

    public Timestamp getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(Timestamp end_datetime) {
        this.end_datetime = end_datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
