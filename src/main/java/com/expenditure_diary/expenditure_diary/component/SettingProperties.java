package com.expenditure_diary.expenditure_diary.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "setting")
public class SettingProperties {
    private Boolean enableSaveLog;

    public Boolean getEnableSaveLog() {
        return enableSaveLog;
    }

    public void setEnableSaveLog(Boolean enableSaveLog) {
        this.enableSaveLog = enableSaveLog;
    }
}