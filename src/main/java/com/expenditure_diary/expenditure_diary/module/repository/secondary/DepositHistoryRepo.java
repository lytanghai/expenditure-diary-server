package com.expenditure_diary.expenditure_diary.module.repository.secondary;

import com.expenditure_diary.expenditure_diary.module.model.secondary.DepositHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositHistoryRepo extends JpaRepository<DepositHistory, Integer> {

    @Query(value = "SELECT * FROM deposit_history WHERE plan_id = :planId", nativeQuery = true)
    Page<DepositHistory> findAllByPlanId(@Param("planId") Integer planId, Pageable pageable);

}
