package com.expenditure_diary.expenditure_diary.activity_log.repository;

import com.expenditure_diary.expenditure_diary.activity_log.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepo extends JpaRepository<Logs, Long> {
}
