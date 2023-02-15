package com.soshiant.springbootexample.validation.username.validator;

import com.soshiant.springbootexample.validation.username.UsernameValidation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Validation implementation linked to the annotation {@link UsernameValidation} executing
 * the logic to verify that a given CustomerId is existing.
 */
@Slf4j
public class UsernameValidator implements ConstraintValidator<UsernameValidation, String> {


  @Override
  public void initialize(UsernameValidation constraintAnnotation) {
    // nothing to do in this case
  }


  /**
   * Verify that the given CustomerId exist
   * regex for username : "^[a-zA-Z0-9._-]{3,20}$"
   * regex for password : "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
   * regex for both     : "^([a-zA-Z0-9._-]{3,20}|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$"
   */
  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {

    if(StringUtils.isBlank(username)){
      return false;
    }
    try {
      String pattern = "^([a-zA-Z0-9._-]{3,20}|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
      Validate.matchesPattern(username, pattern);
      return true;

    } catch (Exception e) {
      return false;
    }
  }

}
