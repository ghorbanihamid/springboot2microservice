package com.soshiant.springbootexample.util.validation.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;

import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.service.CustomerService;
import com.soshiant.springbootexample.validation.validator.CustomerIdValidator;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class CustomerIdValidatorTest {

  @MockBean
  private CustomerService customerService;

  @MockBean
  private ConstraintValidatorContext constraintValidatorContext;

  @InjectMocks
  private CustomerIdValidator customerIdValidator;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() { validateMockitoUsage(); }

  @Test
  void testIsValidSuccess() throws Exception {

    List<Customer> customerList = new ArrayList<>();
    customerList.add(new Customer());
    when(customerService.getCustomers(anyList())).thenReturn(customerList);

    CustomerIdValidator customerIdValidator = new CustomerIdValidator();
    customerIdValidator.setCustomerService(customerService);

    boolean result = customerIdValidator.isValid(123L, constraintValidatorContext);

    assertThat(result, CoreMatchers.is(true));
  }

  @Test
  void testIsValidCustomerIdIsEmpty() {

    CustomerIdValidator customerIdValidator = new CustomerIdValidator();
    boolean result = customerIdValidator.isValid(null, constraintValidatorContext);

    assertThat(result, CoreMatchers.is(false));
  }

  @Test
  void testIsValidWhenCustomerIdNotFound() throws Exception {

    List<Long> customerIds = new ArrayList<>();
    List<Customer> customerList = new ArrayList<>();
    when(customerService.getCustomers(customerIds)).thenReturn(customerList);

    CustomerIdValidator customerIdValidator = new CustomerIdValidator();
    customerIdValidator.setCustomerService(customerService);

    boolean result = customerIdValidator.isValid(null, constraintValidatorContext);

    assertThat(result, CoreMatchers.is(false));
  }

  @Test
  @DisplayName("Test isValid when throwing Exception in method")
  void testIsValidWhenMethodThrowsException() {

    CustomerIdValidator customerIdValidator = new CustomerIdValidator();

    boolean result = customerIdValidator.isValid(123L, constraintValidatorContext);

    assertThat(result, CoreMatchers.is(false));
  }

}