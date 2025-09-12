package com.expenditure_diary.expenditure_diary.dto.req;

import java.util.List;

public class RecurringExpenseReq {
    private List<ExpenseAddRequest> expenseAddRequests;

    public List<ExpenseAddRequest> getExpenseAddRequests() {
        return expenseAddRequests;
    }

    public void setExpenseAddRequests(List<ExpenseAddRequest> expenseAddRequests) {
        this.expenseAddRequests = expenseAddRequests;
    }
}
