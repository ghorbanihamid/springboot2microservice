package com.soshiant.springbootexample.util;

import com.soshiant.springbootexample.util.validation.ConstraintValidationFactoryTest;
import com.soshiant.springbootexample.validation.validator.CustomerIdValidator;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.mock.web.MockServletContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.List;

public class ValidatorTestUtil {

  public static LocalValidatorFactoryBean getCustomValidatorFactoryBean (List<Object> servicesUsedByValidators, MockServletContext servletContext) {

    final GenericWebApplicationContext context = new GenericWebApplicationContext( servletContext );
    final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

    beanFactory.registerSingleton( CustomerIdValidator.class.getCanonicalName(), new CustomerIdValidator() );

    context.refresh();

    LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
    validatorFactoryBean.setApplicationContext( context );

    ConstraintValidationFactoryTest constraintFactory =
        new ConstraintValidationFactoryTest( context, servicesUsedByValidators );

    validatorFactoryBean.setConstraintValidatorFactory( constraintFactory );
    validatorFactoryBean.setProviderClass( HibernateValidator.class );
    validatorFactoryBean.afterPropertiesSet();
    return validatorFactoryBean;
  }

}
