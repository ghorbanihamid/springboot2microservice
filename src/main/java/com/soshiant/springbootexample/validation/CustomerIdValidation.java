package com.soshiant.springbootexample.validation;

import com.soshiant.springbootexample.validation.validator.CustomerIdValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation Annotation defining constraint validation for CustomerId
 */
@Constraint(validatedBy = {CustomerIdValidator.class})
@Target({METHOD, FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface CustomerIdValidation {

  String message() default "CustomerId not found!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
