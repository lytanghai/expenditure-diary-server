package com.expenditure_diary.expenditure_diary.controller;

import com.expenditure_diary.expenditure_diary.dto.req.ExpenseSettingReq;
import com.expenditure_diary.expenditure_diary.dto.resp.CommonResp;
import com.expenditure_diary.expenditure_diary.service.ExpenseSettingService;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense_tracking/setting")
public class ExpenseSettingController {

    @Autowired private ExpenseSettingService expenseSettingService;

    @GetMapping("/list")
    public ResponseBuilder<CommonResp> listSettingProperties() {
        return expenseSettingService.responseSettingProperties();
    }

    @PostMapping("/update")
    public ResponseBuilder<String> updateSettingProperties(@RequestBody ExpenseSettingReq expenseSettingReq) {
        expenseSettingService.updateSetting(expenseSettingReq);
        return ResponseBuilder.success("success");
    }
}
