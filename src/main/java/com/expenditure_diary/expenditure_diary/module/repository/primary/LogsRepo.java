package com.expenditure_diary.expenditure_diary.module.repository.primary;

import com.expenditure_diary.expenditure_diary.module.model.secondary.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepo extends JpaRepository<Logs, Long> {
}
