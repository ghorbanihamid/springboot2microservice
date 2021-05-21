package com.soshiant.springbootexample.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.soshiant.springbootexample.validation.validator.CustomerIdValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

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
