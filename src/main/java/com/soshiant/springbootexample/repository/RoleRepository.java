package com.soshiant.springbootexample.repository;

import com.soshiant.springbootexample.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>  {

  /**
   * retrieves a Role info from database based on roleName
   *
   * @param roleName roleName
   */
  Optional<Role> findByRoleName(@Param("roleName") String roleName);

}
