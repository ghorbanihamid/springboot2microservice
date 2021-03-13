package com.soshiant.springbootexample.repository;

import com.soshiant.springbootexample.entity.Employee;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface EmployeeAddressesRepository extends JpaRepository<EmployeeAddress, Long>  {

  /**
   * retrieves a Employee info from database based on employeeId
   *
   * @param employee employee info
   */
  Optional<EmployeeAddress> findByEmployee(Employee employee);

}
