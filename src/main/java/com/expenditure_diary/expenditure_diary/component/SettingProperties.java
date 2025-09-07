package com.expenditure_diary.expenditure_diary.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "setting")
public class SettingProperties {
    private Boolean enableSaveLog;
    private boolean enableRegister;

    private String twelveDataUrl;
    private String twelveDataApiKey;

    private String forexFactoryThisWeekJson;


    public String getForexFactoryThisWeekJson() {
        return forexFactoryThisWeekJson;
    }

    public void setForexFactoryThisWeekJson(String forexFactoryThisWeekJson) {
        this.forexFactoryThisWeekJson = forexFactoryThisWeekJson;
    }

    public String getTwelveDataUrl() {
        return twelveDataUrl;
    }

    public void setTwelveDataUrl(String twelveDataUrl) {
        this.twelveDataUrl = twelveDataUrl;
    }

    public String getTwelveDataApiKey() {
        return twelveDataApiKey;
    }

    public void setTwelveDataApiKey(String twelveDataApiKey) {
        this.twelveDataApiKey = twelveDataApiKey;
    }

    public boolean isEnableRegister() {
        return enableRegister;
    }

    public void setEnableRegister(boolean enableRegister) {
        this.enableRegister = enableRegister;
    }

    public Boolean getEnableSaveLog() {
        return enableSaveLog;
    }

    public void setEnableSaveLog(Boolean enableSaveLog) {
        this.enableSaveLog = enableSaveLog;
    }
}