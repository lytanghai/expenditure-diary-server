package com.expenditure_diary.expenditure_diary.module.activity_log.service;

import com.expenditure_diary.expenditure_diary.component.SettingProperties;
import com.expenditure_diary.expenditure_diary.module.model.secondary.Logs;
import com.expenditure_diary.expenditure_diary.module.repository.primary.LogsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityLogService {

    @Autowired private LogsRepo repo;
    @Autowired private SettingProperties settingProperties;

    public void insert(String operation, Long userId, String result, String resultDetail) {
        if(settingProperties.getEnableSaveLog()) {
            Logs newLog = new Logs();
            newLog.setCreatedAt(LocalDateTime.now());
            newLog.setOperation(operation);
            newLog.setUserId(userId);
            newLog.setResult(result);
            newLog.setResultDetail(resultDetail);

            repo.save(newLog);
        }
    }
}
