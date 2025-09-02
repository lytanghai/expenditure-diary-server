package com.expenditure_diary.expenditure_diary.module.income_tracking.dto.req;

public class ProfitDeleteRequest {
    private Integer id;
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
