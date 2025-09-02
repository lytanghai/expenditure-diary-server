package com.expenditure_diary.expenditure_diary.module.repository.primary;

import com.expenditure_diary.expenditure_diary.annotate.PrimaryDbRepository;
import com.expenditure_diary.expenditure_diary.module.model.primary.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

@PrimaryDbRepository
public interface LogsRepo extends JpaRepository<Logs, Long> {
}
