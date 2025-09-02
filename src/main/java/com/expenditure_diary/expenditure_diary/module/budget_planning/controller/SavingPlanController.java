package com.expenditure_diary.expenditure_diary.module.budget_planning.controller;

import com.expenditure_diary.expenditure_diary.module.budget_planning.dto.req.DepositRequest;
import com.expenditure_diary.expenditure_diary.module.budget_planning.dto.req.SavingPlanCreateReq;
import com.expenditure_diary.expenditure_diary.module.budget_planning.dto.resp.DepositResp;
import com.expenditure_diary.expenditure_diary.module.budget_planning.service.SavingPlanService;
import com.expenditure_diary.expenditure_diary.module.expense_tracking.dto.req.ExpenseDeleteRequest;
import com.expenditure_diary.expenditure_diary.module.expense_tracking.dto.resp.PaginatedResponse;
import com.expenditure_diary.expenditure_diary.module.model.secondary.DepositHistory;
import com.expenditure_diary.expenditure_diary.module.model.secondary.SavingPlan;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/03. saving_plan")
public class SavingPlanController {

    private final SavingPlanService savingPlanService;

    public SavingPlanController(SavingPlanService savingPlanService) {
        this.savingPlanService = savingPlanService;
    }

    @PostMapping("/create_plan")
    public void createSavingPlan(@RequestBody SavingPlanCreateReq req) {
        savingPlanService.createSavingPlan(req);
    }

    @GetMapping("/view_plans")
    public ResponseBuilder<PaginatedResponse<SavingPlan>> responseMonthlyExpenses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return savingPlanService.viewSavingPlans(page, size);
    }

    @PostMapping("/deposit")
    public ResponseBuilder<DepositResp> deposit(@RequestBody DepositRequest request) {
        return savingPlanService.deposit(request);
    }

    @GetMapping("/check_history")
    public ResponseBuilder<PaginatedResponse<DepositHistory>> history(@RequestParam(defaultValue = "0") int planId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10")int size) {
        return savingPlanService.history(planId, page, size);
    }

    @PostMapping("/delete_plan")
    public void delete(@RequestBody ExpenseDeleteRequest deleteRequest) {
        savingPlanService.deletePlan(deleteRequest);
    }
}
