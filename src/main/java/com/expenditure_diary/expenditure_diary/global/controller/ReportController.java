package com.expenditure_diary.expenditure_diary.global.controller;

import com.expenditure_diary.expenditure_diary.expense_tracking.dto.resp.ExpenseTrackerListResp;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.resp.PaginatedResponse;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.resp.ProExpTrackerResp;
import com.expenditure_diary.expenditure_diary.income_tracking.entity.ProfitTracker;
import com.expenditure_diary.expenditure_diary.expense_tracking.service.ExpenseService;
import com.expenditure_diary.expenditure_diary.income_tracking.dto.resp.ProfitTrackerListResp;
import com.expenditure_diary.expenditure_diary.income_tracking.service.ProfitTrackerService;
import com.expenditure_diary.expenditure_diary.expense_tracking.entity.ExpenseTracker;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/global/report")
public class ReportController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ProfitTrackerService profitTrackerService;
//    Expense
    /**
     * Fetch By daily
     * usage: manual - schedule
     * */
    @GetMapping("/expense/fetch-daily")
    public ResponseBuilder<ExpenseTrackerListResp> responseExpensesDaily() {
        return expenseService.fetchDaily(false); // do not enable this
    }

    /**
     * Fetch By monthly
     * month = 2025-07
     * usage: manual - schedule
     * */
    @GetMapping("/expense/fetch-monthly")
    public ResponseBuilder<ProExpTrackerResp> responseMonthlyExpenses(
            @RequestParam(defaultValue = "") String month) {
        return expenseService.fetchMonthlyExpenses(month);
    }

    /**
     * Fetch By date
     * date1 = 2025-07-23
     * date1 = 2025-07-23 && date2 = 2025-07-25
     * */
    @GetMapping("/expense/fetch-by-date")
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> responseExpensesByDate(
            @RequestParam(name = "date1", defaultValue = "") String date1,
            @RequestParam(name = "date2", defaultValue = "") String date2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return expenseService.fetchExpensesByDate(date1, date2, page, size);
    }

    //     * month = 2025-07
    //     * date1 && date2 = 2025-07-24
    @GetMapping("/expense/calculate")
    public ResponseBuilder<ProExpTrackerResp> getTotalAmountExpense(@RequestParam(name = "date1", defaultValue = "") String date1,
                                                             @RequestParam(name = "date2", defaultValue = "") String date2,
                                                             @RequestParam(name = "type", defaultValue = "") String type) {
        return expenseService.calculate(type, date1, date2);
    }

//    Income
    /**
     * Fetch By monthly
     * month = 2025-07
     * usage: manual - schedule
     * */
    @GetMapping("/income/fetch-monthly")
    public ResponseBuilder<PaginatedResponse<ProfitTracker>> responseMonthlyProfit(
            @RequestParam(defaultValue = "") String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return profitTrackerService.fetchMonthlyProfit(month, page, size);
    }

    /**
     * Fetch By date
     * date1 = 2025-07-23
     * date1 = 2025-07-23 && date2 = 2025-07-25
     * */
    @GetMapping("/income/fetch-by-date")
    public ResponseBuilder<PaginatedResponse<ProfitTracker>> responseProfitByDate(
            @RequestParam(name = "date1", defaultValue = "") String date1,
            @RequestParam(name = "date2", defaultValue = "") String date2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return profitTrackerService.fetchProfitByDate(date1, date2, page, size);
    }

    /**
     * Fetch By daily
     * usage: manual - schedule
     * */
    @GetMapping("/income/fetch-daily")
    public ResponseBuilder<ProfitTrackerListResp> responseProfitDaily() {
        return profitTrackerService.fetchDaily(false); // do not enable this
    }

    //     * month = 2025-07
    //     * date1 && date2 = 2025-07-24
    @GetMapping("/income/calculate")
    public ResponseBuilder<ProExpTrackerResp> getTotalAmount(@RequestParam(name = "date1", defaultValue = "") String date1,
                                                             @RequestParam(name = "date2", defaultValue = "") String date2,
                                                             @RequestParam(name = "type", defaultValue = "") String type) {
        return profitTrackerService.calculate(type, date1, date2);
    }

}
