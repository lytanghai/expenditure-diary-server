package com.expenditure_diary.expenditure_diary.dto.resp;

import java.util.Map;

public class CommonResp {
    private String result;
    private String detail;
    private Map<String, Object> objectMap;

    public Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, Object> objectMap) {
        this.objectMap = objectMap;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
