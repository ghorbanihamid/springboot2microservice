package com.soshiant.springbootexample.dto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import com.soshiant.springbootexample.service.CustomerService;
import com.soshiant.springbootexample.util.TestUtil;
import com.soshiant.springbootexample.util.ValidatorTestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class CustomerDtoTest {

  private static CustomerService customerService;

  private static MockServletContext servletContext;

  private static Validator validator;

  @BeforeAll
  public static void setupValidatorInstance() {
    servletContext =  new MockServletContext();
    customerService = mock(CustomerService.class);
    List<Object> servicesList = new ArrayList<>();
    servicesList.add(customerService);

    validator = ValidatorTestUtil.getCustomValidatorFactoryBean(servicesList,servletContext).getValidator();

  }

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
    validateMockitoUsage();
  }

  @Test
  void testValidationOfFirstName() throws Exception {
    when(customerService.getCustomers(ArgumentMatchers.anyList())).thenReturn(null);

    CustomerDto customerDto = TestUtil.buildCustomerDto();

    customerDto.setFirstName("Hamid");
    Set<ConstraintViolation<CustomerDto>> violations = validator.validate(customerDto);
    assertThat(violations.size(),comparesEqualTo(0));

    customerDto.setFirstName("H");
    violations = validator.validate(customerDto);
    assertThat(violations.size(),comparesEqualTo(1));
    violations.forEach(action ->
      assertThat(action.getMessage(),equalTo("First name must not be less than 2 characters!")));

    customerDto.setFirstName("");
    violations = validator.validate(customerDto);
    assertThat(violations.size(),greaterThanOrEqualTo(1));
  }

}