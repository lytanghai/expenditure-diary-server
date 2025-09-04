package com.expenditure_diary.expenditure_diary.activity_log.controller;

import com.expenditure_diary.expenditure_diary.expense_tracking.dto.req.ExpenseAddRequest;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.req.ExpenseDeleteRequest;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.req.ExpenseFilterRequest;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.resp.ExpenseResponse;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.resp.ExpenseTrackerListResp;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.resp.PaginatedResponse;
import com.expenditure_diary.expenditure_diary.expense_tracking.dto.resp.ProExpTrackerResp;
import com.expenditure_diary.expenditure_diary.expense_tracking.entity.ExpenseTracker;
import com.expenditure_diary.expenditure_diary.expense_tracking.service.ExpenseService;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense")
public class ExpenseTrackerController {

    private final ExpenseService expenseService;

    public ExpenseTrackerController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/create")
    public void addNewExpense(@RequestBody ExpenseAddRequest expenseAddRequest) {
        expenseService.addNewExpenseRecord(expenseAddRequest);
    }

    @PatchMapping("/update/{id}")
    public void updateExpenseById(@PathVariable("id") Integer id, @RequestBody ExpenseAddRequest expenseUpdateRequest) {
        expenseService.updateExpenseById(id, expenseUpdateRequest);
    }

    @PostMapping("/delete")
    public void deleteExpenseByIdOrDate(@RequestBody ExpenseDeleteRequest expenseDeleteRequest) {
        expenseService.deleteByIdOrDate(expenseDeleteRequest);  // e.g., "2025-07-12" for date
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ExpenseResponse>> filterExpenses(@RequestBody ExpenseFilterRequest request) {
        return ResponseEntity.ok(expenseService.getFilteredExpenses(request));
    }

    /**
     * Fetch By monthly
     * month = 2025-07
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-monthly")
    public ResponseBuilder<ProExpTrackerResp> responseMonthlyExpenses(
            @RequestParam(defaultValue = "") String month) {
        return expenseService.fetchMonthlyExpenses(month);
    }

    /**
     * Fetch By date
     * date1 = 2025-07-23
     * date1 = 2025-07-23 && date2 = 2025-07-25
     * */
    @GetMapping("/fetch-by-date")
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> responseExpensesByDate(
            @RequestParam(name = "date1", defaultValue = "") String date1,
            @RequestParam(name = "date2", defaultValue = "") String date2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return expenseService.fetchExpensesByDate(date1, date2, page, size);
    }

    //     * month = 2025-07
    //     * date1 && date2 = 2025-07-24
    @GetMapping("/calculate")
    public ResponseBuilder<ProExpTrackerResp> getTotalAmount(@RequestParam(name = "date1", defaultValue = "") String date1,
                                                             @RequestParam(name = "date2", defaultValue = "") String date2,
                                                             @RequestParam(name = "type", defaultValue = "") String type) {
        return expenseService.calculate(type, date1, date2);
    }

    /**
     * Fetch By daily
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-daily")
    public ResponseBuilder<ExpenseTrackerListResp> responseExpensesDaily() {
        return expenseService.fetchDaily(false); // do not enable this
    }

    @PostMapping("/cleanup")
    public void cleanup() {
        expenseService.cleanup();
    }

}
