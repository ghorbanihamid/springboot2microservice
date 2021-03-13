package com.soshiant.springbootexample.service;


import com.soshiant.springbootexample.dto.EmployeeDto;
import com.soshiant.springbootexample.entity.Employee;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import com.soshiant.springbootexample.exception.EmployeeServiceException;

/**
 *
 * @author Hamid.Ghorbani
 *
 */
public interface EmployeeService {

  /**
   *
   * @param employeeDto employee data
   * @return EmployeeId EmployeeId
   */
  EmployeeAddress registerEmployee(EmployeeDto employeeDto) throws EmployeeServiceException;

  /**
   * 
   * @param employeeId employeeId
   * @return Employee Employee info
   */
  EmployeeDto getEmployee(long employeeId);

}
