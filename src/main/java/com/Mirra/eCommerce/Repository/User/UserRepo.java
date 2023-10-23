package com.Mirra.eCommerce.Repository.User;

import com.Mirra.eCommerce.Models.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.accountNonLocked = false AND u.lockTime < :currentTime")
    List<User> findExpiredLockedUsers(@Param("currentTime") LocalDateTime currentTime);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = :failedAttempt WHERE u.email = :email")
    void updateFailedAttempt(@Param("failedAttempt") int failedAttempt, @Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") String role);
}
