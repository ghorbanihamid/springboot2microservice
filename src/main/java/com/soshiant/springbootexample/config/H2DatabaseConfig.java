package com.soshiant.springbootexample.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //  Enabling JPA Auditing
public class H2DatabaseConfig {

  @Bean
  ServletRegistrationBean h2ServletRegistrationBean(){
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new WebServlet());
    servletRegistrationBean.addUrlMappings("/h2/*");
    return servletRegistrationBean;
  }
}
