package com.expenditure_diary.expenditure_diary.module.repository.secondary;

import com.expenditure_diary.expenditure_diary.module.model.secondary.SavingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingPlanRepo extends JpaRepository<SavingPlan, Integer> {
}
