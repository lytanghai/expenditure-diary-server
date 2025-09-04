package com.expenditure_diary.expenditure_diary.expense_tracking.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ExpenseTrackerCustomRepoImpl implements ExpenseTrackerCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void truncateAndResetSequence(String entity) {
        if ("EXPENSE_TRACKER".equals(entity)) {
            entityManager.createNativeQuery("DELETE FROM expense_tracker").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE expense_tracker_id_seq RESTART WITH 1").executeUpdate();
        } else if ("PROFIT_TRACKER".equals(entity)) {
            entityManager.createNativeQuery("DELETE FROM profit_tracker").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE profit_tracker_id_seq RESTART WITH 1").executeUpdate();
        }
    }
}
