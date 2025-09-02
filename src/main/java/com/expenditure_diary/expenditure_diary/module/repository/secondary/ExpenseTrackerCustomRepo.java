package com.expenditure_diary.expenditure_diary.module.repository.secondary;

public interface ExpenseTrackerCustomRepo {
    void truncateAndResetSequence(String entity);
}
