package com.expenditure_diary.expenditure_diary.module.repository;

import com.expenditure_diary.expenditure_diary.module.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query(name = "SELECT COUNT(1) FROM users " +
            "WHERE username = :username OR email = :email", nativeQuery = true)
    List<UserEntity> findByUsernameOrEmail(@Param("username") String username , @Param("email") String email);

    @Query(name = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    UserEntity findByEmail(@Param("email") String email);
}