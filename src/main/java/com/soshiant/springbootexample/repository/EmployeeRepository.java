package com.soshiant.springbootexample.repository;

import com.soshiant.springbootexample.entity.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>  {

  /**
   * retrieves a Employee info from database based on his first name and last name
   *
   * @param firstName employee's first name
   * @param lastName  employee's last name
   */
  Optional<Employee> findByFirstNameAndLastName(@Param("firstName") String firstName,
                                                @Param("lastName")  String lastName);

  /**
   * retrieves a Employee info from database based on its email address
   *
   * @param emailAddress employee's email address
   */
  Optional<Employee> findByEmailAddress(@Param("emailAddress") String emailAddress);

}
