package com.soshiant.springbootexample.validation.username;

import com.soshiant.springbootexample.validation.username.validator.UsernameValidator;

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
@Constraint(validatedBy = {UsernameValidator.class})
@Target({METHOD, FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface UsernameValidation {

  String message() default "Username not found!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
