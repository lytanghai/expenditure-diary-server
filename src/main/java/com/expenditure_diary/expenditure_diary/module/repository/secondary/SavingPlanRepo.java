package com.expenditure_diary.expenditure_diary.module.repository.secondary;

import com.expenditure_diary.expenditure_diary.annotate.SecondaryDbRepository;
import com.expenditure_diary.expenditure_diary.module.model.secondary.SavingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

@SecondaryDbRepository
public interface SavingPlanRepo extends JpaRepository<SavingPlan, Integer> {
}
