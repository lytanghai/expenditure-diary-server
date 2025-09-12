package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.component.SettingProperties;
import com.expenditure_diary.expenditure_diary.dto.req.ExpenseSettingReq;
import com.expenditure_diary.expenditure_diary.dto.resp.CommonResp;
import com.expenditure_diary.expenditure_diary.repository.ExpenseTrackerRepo;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExpenseSettingService {

    @Autowired private SettingProperties settingProperties;

    @Autowired private ExpenseTrackerRepo expenseTrackerRepo;

    private static BigDecimal monthlyBalance = new BigDecimal(150);

    public static BigDecimal getMonthlyBalance() {
        return monthlyBalance;
    }

    public static void setMonthlyBalance(BigDecimal monthlyBalance) {
        ExpenseSettingService.monthlyBalance = monthlyBalance;
    }

    public ResponseBuilder<CommonResp> responseSettingProperties() {
        Map<String, Object> propertiesMap = new HashMap<>();

        try {
            for (Method method : SettingProperties.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    String propertyName = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);

                    if(propertyName.equals("class"))
                        continue;

                    Object value = method.invoke(settingProperties);

                    if (value != null && (propertyName.toLowerCase().contains("key") || propertyName.toLowerCase().contains("api"))) {
                        String strValue = value.toString();
                        if (strValue.length() > 4) {
                            value = "****" + strValue.substring(strValue.length() - 4);
                        } else {
                            value = "****";
                        }
                    }
                    propertiesMap.put(propertyName, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read SettingProperties", e);
        } finally {
            propertiesMap.put("monthlyBalanceUpt", monthlyBalance);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("current_spending", expenseTrackerRepo.fetchCurrentMonthTotal().setScale(3, RoundingMode.HALF_UP) + " $");
        map.put("available_spending", new BigDecimal(propertiesMap.get("monthlyBalanceUpt").toString())
                .subtract(expenseTrackerRepo.fetchCurrentMonthTotal().setScale(3, RoundingMode.HALF_UP)) + " $");

        propertiesMap.put("metadata", map);

        CommonResp response = new CommonResp();
        response.setObjectMap(propertiesMap);

        return ResponseBuilder.success(response);
    }

    public void updateSetting(ExpenseSettingReq expenseSettingReq) {
        if(expenseSettingReq.getMonthlyBalanceUpt() != null) {
            monthlyBalance = new BigDecimal(expenseSettingReq.getMonthlyBalanceUpt());
        }
    }

}
