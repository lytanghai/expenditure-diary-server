package com.expenditure_diary.expenditure_diary.module.repository;

import com.expenditure_diary.expenditure_diary.module.model.SavingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingPlanRepo extends JpaRepository<SavingPlan, Integer> {
}
