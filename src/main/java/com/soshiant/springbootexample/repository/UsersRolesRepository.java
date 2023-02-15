package com.soshiant.springbootexample.repository;

import java.util.List;
import com.soshiant.springbootexample.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UsersRolesRepository extends JpaRepository<UserRole, Long>  {

    /**
     * @param userId userId
     */
    @Modifying(clearAutomatically = true)
    @Query(value= "SELECT R.ROLE_NAME  FROM CST.USERS_ROLES U " +
                  "INNER JOIN CST.ROLES R ON U.ROLE_ID = R.ID " +
                  "WHERE U.USER_ID = :userId AND U.ENABLED = TRUE AND R.ACTIVE = TRUE",
           nativeQuery = true)
    List<String> findByUserId(Long userId);
}
