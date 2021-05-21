package com.soshiant.springbootexample.service;


import com.soshiant.springbootexample.dto.EmployeeRequestDto;
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
   * @param employeeRequestDto employee data
   * @return EmployeeId EmployeeId
   */
  EmployeeAddress registerEmployee(EmployeeRequestDto employeeRequestDto) throws EmployeeServiceException;

  /**
   * 
   * @param employeeId employeeId
   * @return Employee Employee info
   */
  EmployeeRequestDto getEmployee(long employeeId);

}
