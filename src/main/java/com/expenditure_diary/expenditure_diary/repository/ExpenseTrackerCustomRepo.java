package com.expenditure_diary.expenditure_diary.repository;

public interface ExpenseTrackerCustomRepo {
    void truncateAndResetSequence(String entity);
}
