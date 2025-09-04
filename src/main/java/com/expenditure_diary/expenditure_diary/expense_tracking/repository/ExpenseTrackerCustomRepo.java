package com.expenditure_diary.expenditure_diary.expense_tracking.repository;

public interface ExpenseTrackerCustomRepo {
    void truncateAndResetSequence(String entity);
}
