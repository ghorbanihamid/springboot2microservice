package com.soshiant.springbootexample.service.impl;

import static com.soshiant.springbootexample.util.AppTestConstants.EMPLOYEE_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.soshiant.springbootexample.dto.EmployeeRequestDto;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import com.soshiant.springbootexample.exception.EmployeeServiceException;
import com.soshiant.springbootexample.repository.EmployeeAddressesRepository;
import com.soshiant.springbootexample.repository.EmployeeRepository;
import com.soshiant.springbootexample.util.TestUtil;
import java.util.Optional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class EmployeeServiceImplTest {

  @Mock
  private EmployeeRepository employeeRepository;
  @Mock
  private EmployeeAddressesRepository employeeAddressesRepository;

  @InjectMocks
  private EmployeeServiceImpl employeeService;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  @DisplayName("Test Register new employee is success")
  void testRegisterNewEmployeeWithAddressIsSuccess() throws Exception {

    Mockito.when(employeeAddressesRepository.save(any(EmployeeAddress.class)))
        .thenReturn(TestUtil.buildEmployeeAddressObject());

    EmployeeAddress result = employeeService.registerEmployee(TestUtil.buildEmployeeDto());
    assertThat(result, is(notNullValue()));
    assertThat(result.getEmployee().getEmployeeId(), CoreMatchers.equalTo(EMPLOYEE_ID));

  }

  @Test
  @DisplayName("Test Register new employee")
  void testRegisterNewEmployeeThrowsException() throws EmployeeServiceException {

    Mockito.when(employeeAddressesRepository.save(any(EmployeeAddress.class)))
        .thenThrow(new QueryTimeoutException("QueryTimeout Exception"));

    Throwable thrown = assertThrows(
        EmployeeServiceException.class,
        () -> employeeService.registerEmployee(TestUtil.buildEmployeeDto())
    );
    assertEquals("QueryTimeout Exception",thrown.getMessage());
  }


  @Test
  void testGetEmployeeByValidId() {

    Mockito.when(employeeRepository.findById(EMPLOYEE_ID))
        .thenReturn(Optional.of(TestUtil.buildEmployeeObject()));


    EmployeeRequestDto employee = employeeService.getEmployee(EMPLOYEE_ID);

    assertThat(employee, is(notNullValue()));

  }

  @Test
  void testGetEmployeeByInvalidId() {

    Mockito.when(employeeRepository.findById(EMPLOYEE_ID))
        .thenReturn(Optional.empty());

    EmployeeRequestDto employee = employeeService.getEmployee(EMPLOYEE_ID);

    assertThat(employee, is(nullValue()));

  }


}