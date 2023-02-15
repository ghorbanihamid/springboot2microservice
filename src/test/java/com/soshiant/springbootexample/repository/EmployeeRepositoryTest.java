package com.soshiant.springbootexample.repository;

import static com.soshiant.springbootexample.util.AppTestConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.validateMockitoUsage;

import com.soshiant.springbootexample.entity.Employee;
import com.soshiant.springbootexample.util.DataUtils;
import com.soshiant.springbootexample.util.LocalDateSerializer;

import java.util.Optional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = EmployeeRepository.class)
@EntityScan(basePackageClasses = Employee.class)
class EmployeeRepositoryTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private EmployeeRepository employeeRepository;

  private Employee employee;

  @BeforeEach
  void setUp() {
    employee = DataUtils.buildEmployeeObject();
    // for persisting a new Employee (testEntityManager.persist), Id must be null
    employee.setEmployeeId(null);
  }

  @AfterEach
  void tearDown() {
    validateMockitoUsage();
  }

  @Test
  void testFindById() {

    Employee persistedEmployee = testEntityManager.persist(employee);
    Optional<Employee> queryResult = employeeRepository.findById(persistedEmployee.getEmployeeId());

    assertThat(queryResult.isPresent(),is(true));

    queryResult.ifPresent(
        result -> assertThat(result.getEmailAddress(), CoreMatchers.equalTo(EMAIL_ADDRESS)));

  }

  @Test
  void testFindByEmailAddress() {

    Employee persistedEmployee = testEntityManager.persist(employee);
    Optional<Employee> queryResult =
        employeeRepository.findByEmailAddress(EMAIL_ADDRESS);

    assertThat(queryResult.isPresent(),is(true));

    queryResult.ifPresent(result ->
        assertThat(result.getEmployeeId(), CoreMatchers.equalTo(persistedEmployee.getEmployeeId())));
  }
}