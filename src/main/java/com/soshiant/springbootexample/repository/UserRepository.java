package com.soshiant.springbootexample.repository;

import com.soshiant.springbootexample.entity.UserInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long>  {

  /**
   * retrieves a customer info from database based on username
   *
   * @param username userName
   */
  Optional<UserInfo> findByUsername(@Param("username") String username);

  @Query("UPDATE UserInfo u SET u.failedAttempt = u.failedAttempt + 1 WHERE u.username = :username")
  Integer increasePasswordRetryAttempts(String username);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE UserInfo u SET u.failedAttempt = 0 WHERE u.username = :username")
  int resetPasswordRetryAttempts(@Param("username") String username);

}
