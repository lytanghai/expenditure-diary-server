package com.expenditure_diary.expenditure_diary.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "setting")
public class SettingProperties {
    private Boolean enableSaveLog;
    private boolean enableRegister;

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