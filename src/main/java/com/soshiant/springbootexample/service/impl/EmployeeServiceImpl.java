package com.soshiant.springbootexample.service.impl;

import com.soshiant.springbootexample.dto.EmployeeRequestDto;
import com.soshiant.springbootexample.entity.Employee;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import com.soshiant.springbootexample.exception.EmployeeServiceException;
import com.soshiant.springbootexample.mapper.EmployeeMapper;
import com.soshiant.springbootexample.repository.EmployeeAddressesRepository;
import com.soshiant.springbootexample.repository.EmployeeRepository;
import com.soshiant.springbootexample.service.EmployeeService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hamid.Ghorbani
 *
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EmployeeAddressesRepository employeeAddressesRepository;

  @Override
  public EmployeeAddress registerEmployee(EmployeeRequestDto employeeRequestDto) throws EmployeeServiceException {
    log.info("register new employee {}", employeeRequestDto);

    EmployeeAddress employeeInfo;
    try {
      employeeInfo = employeeMapper.toEmployeeWithAddress(employeeRequestDto);
      EmployeeAddress savedEmployee = employeeAddressesRepository.save(employeeInfo);
      return savedEmployee;

    } catch (Exception e) {
      log.error(
          "Exception occurred during onboardEmployee method for employee : {} with message :{} ",
          employeeRequestDto, e.getCause());
      throw new EmployeeServiceException(e.getMessage());
    }
  }

  @Override
  public EmployeeRequestDto getEmployee(long employeeId) {

    try {
      Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
      Employee employee = employeeOptional.orElse(null);
      Optional<EmployeeAddress> employeeOptional1 = employeeAddressesRepository.findByEmployee(employee);
      EmployeeAddress employeeAddress = employeeOptional1.orElse(null);
      EmployeeRequestDto employeeRequestDto = employeeMapper.toEmployeeDto(employee,employeeAddress);
      return employeeRequestDto;

    } catch (Exception e){
      log.error("Exception in getEmployee, username: {}, message: {}. ", employeeId, e.getMessage());
      return null;
    }

  }

}
