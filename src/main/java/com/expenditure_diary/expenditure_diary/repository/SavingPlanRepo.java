package com.expenditure_diary.expenditure_diary.repository;

import com.expenditure_diary.expenditure_diary.entity.SavingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingPlanRepo extends JpaRepository<SavingPlan, Integer> {
}
