package com.expenditure_diary.expenditure_diary.module.repository;

public interface ExpenseTrackerCustomRepo {
    void truncateAndResetSequence(String entity);
}
