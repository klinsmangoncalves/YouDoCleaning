package br.com.kmg.youdocleaning.model;

import java.io.Serializable;
import java.util.Date;

public class Cleaning implements Serializable {
    private String idDepartment;
    private Date startCleaning;
    private Date finishCleaning;
    private String status;

    public Cleaning(String idDepartment, Date startCleaning, Date finishCleaning, String status) {
        this.idDepartment = idDepartment;
        this.startCleaning = startCleaning;
        this.finishCleaning = finishCleaning;
        this.status = status;
    }

    public Cleaning() {
    }

    public String getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(String idDepartment) {
        this.idDepartment = idDepartment;
    }

    public Date getStartCleaning() {
        return startCleaning;
    }

    public void setStartCleaning(Date startCleaning) {
        this.startCleaning = startCleaning;
    }

    public Date getFinishCleaning() {
        return finishCleaning;
    }

    public void setFinishCleaning(Date finishCleaning) {
        this.finishCleaning = finishCleaning;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
