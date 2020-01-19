package br.com.kmg.youdocleaning.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class Cleaning implements Serializable {

    @PropertyName("sector_id")
    private String idDepartment;

    @PropertyName("start_datetime")
    private Timestamp startCleaning;

    @PropertyName("end_datetime")
    private Timestamp finishCleaning;

    @PropertyName("status")
    private String status;

    private String departmentName;

    private String companyLogoUrl;

    private String userId;

    public Cleaning(String idDepartment, Timestamp startCleaning, Timestamp finishCleaning, String status) {
        this.idDepartment = idDepartment;
        this.startCleaning = startCleaning;
        this.finishCleaning = finishCleaning;
        this.status = status;
    }

    public Cleaning(String idDepartment, Timestamp startCleaning, Timestamp finishCleaning, String status, String companyLogoUrl) {
        this.idDepartment = idDepartment;
        this.startCleaning = startCleaning;
        this.finishCleaning = finishCleaning;
        this.status = status;
        this.companyLogoUrl = companyLogoUrl;
    }



    public Cleaning() {
    }

    public String getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(String idDepartment) {
        this.idDepartment = idDepartment;
    }

    public Timestamp getStartCleaning() {
        return startCleaning;
    }

    public void setStartCleaning(Timestamp startCleaning) {
        this.startCleaning = startCleaning;
    }

    public Timestamp getFinishCleaning() {
        return finishCleaning;
    }

    public void setFinishCleaning(Timestamp finishCleaning) {
        this.finishCleaning = finishCleaning;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
