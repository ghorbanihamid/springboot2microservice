package com.soshiant.springbootexample.validation.validator;

import com.soshiant.springbootexample.service.CustomerService;
import com.soshiant.springbootexample.validation.CustomerIdValidation;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Validation implementation linked to the annotation {@link CustomerIdValidation} executing
 * the logic to verify that a given CustomerId is existing.
 */
@Slf4j
public class CustomerIdValidator implements ConstraintValidator<CustomerIdValidation, Long> {


  @Autowired
  private CustomerService customerService;


  @Override
  public void initialize(CustomerIdValidation constraintAnnotation) {
    // nothing to do in this case
  }


  /**
   * Verify that the given CustomerId exist
   */
  @Override
  public boolean isValid(Long customerId, ConstraintValidatorContext context) {

    try {
      if(customerId == null){
        return false;
      }
      List<Long> customerIds = new ArrayList<>();
      customerIds.add(customerId);
      return !customerService.getCustomers(customerIds).isEmpty();

    } catch (Exception e) {
      log.error("Error occurred while validating CustomerId: {}", customerId, e);
      return false;
    }

  }

  public void setCustomerService(
      CustomerService customerService) {
    this.customerService = customerService;
  }
}
