package com.soshiant.springbootexample.util.validation;

import com.soshiant.springbootexample.service.CustomerService;
import com.soshiant.springbootexample.validation.validator.CustomerIdValidator;
import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ConstraintValidationFactoryTest extends SpringWebConstraintValidatorFactory {


  private final WebApplicationContext webApplicationContext;

  private List<Object> services;


  public ConstraintValidationFactoryTest(WebApplicationContext wac, List<Object> services) {

    this.webApplicationContext = wac;
    this.services = services;
  }

  @Override
  public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {

    ConstraintValidator instance = super.getInstance(key);

    if (instance instanceof CustomerIdValidator) {
      CustomerIdValidator customerIdValidator = (CustomerIdValidator) instance;
      customerIdValidator.setCustomerService(
          services.stream()
              .filter(service -> service instanceof CustomerService)
              .map(CustomerService.class::cast)
              .findFirst()
              .orElseThrow(
                () ->
                  new IllegalArgumentException("CustomerService not found in passed services list"))
          );
      instance = customerIdValidator;
    }

    return (T) instance;
  }

  @Override
  protected WebApplicationContext getWebApplicationContext() {

    return webApplicationContext;
  }
}
