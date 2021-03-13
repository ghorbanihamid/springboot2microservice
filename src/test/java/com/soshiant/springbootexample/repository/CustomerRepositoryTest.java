package com.soshiant.springbootexample.repository;

import static com.soshiant.springbootexample.util.AppTestConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.validateMockitoUsage;

import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.util.TestUtil;
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
@EnableJpaRepositories(basePackageClasses = CustomerRepository.class)
@EntityScan(basePackageClasses = Customer.class)
class CustomerRepositoryTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private CustomerRepository customerRepository;

  private Customer customer;

  @BeforeEach
  void setUp() {
    customer = TestUtil.buildCustomerObject();
    // for persisting a new customer (testEntityManager.persist), Id must be null
    customer.setCustomerId(null);
  }

  @AfterEach
  void tearDown() {
    validateMockitoUsage();
  }

  @Test
  void testFindById() {

    Customer persistedCustomer = testEntityManager.persist(customer);
    Optional<Customer> queryResult = customerRepository.findById(persistedCustomer.getCustomerId());

    assertThat(queryResult.isPresent(),is(true));

    queryResult.ifPresent(result ->
        assertThat(result.getFirstName(), CoreMatchers.equalTo(FIRST_NAME)));

  }

  @Test
  void findByFirstNameAndLastName() {

    Customer persistedCustomer = testEntityManager.persist(customer);
    Optional<Customer> queryResult =
        customerRepository.findByFirstNameAndLastName(FIRST_NAME,LAST_NAME);

    assertThat(queryResult.isPresent(),is(true));

    queryResult.ifPresent(result ->
        assertThat(result.getCustomerId(), CoreMatchers.equalTo(persistedCustomer.getCustomerId())));

  }

}