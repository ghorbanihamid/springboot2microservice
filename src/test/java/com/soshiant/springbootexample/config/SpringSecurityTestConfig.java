package com.soshiant.springbootexample.config;

import com.soshiant.springbootexample.filter.AuthenticationFilter;
import com.soshiant.springbootexample.handler.CustomAuthenticationFailureHandler;
import com.soshiant.springbootexample.handler.CustomAuthenticationSuccessHandler;
import com.soshiant.springbootexample.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@EnableWebSecurity(debug = true)
@TestConfiguration
@Order(1)
public class SpringSecurityTestConfig extends WebSecurityConfigurerAdapter  {

//  @Autowired
//  private AuthenticationFilter authenticationFilter;
//
//  private static final String[] AUTH_WHITELIST = {
//      // -- Swagger UI v2
//      "/v2/api-docs",
//      "/swagger-resources",
//      "/swagger-resources/**",
//      "/configuration/ui",
//      "/configuration/security",
//      "/swagger-ui.html",
//      "/webjars/**",
//      // -- Swagger UI v3 (OpenAPI)
//      "/v3/api-docs/**",
//      "/swagger-ui/**",
//      "/actuator/**",
//      "/h2/**",
//      "/authenticate/**",
//      "/customer/register"
//  };
//
//  @Bean
//  @Override
//  public UserDetailsService userDetailsService(){
//    return new UserDetailsServiceImpl();
//
//  }
//  @Bean
//  public PasswordEncoder passwordEncoder(){
//    return new BCryptPasswordEncoder();
////    return NoOpPasswordEncoder.getInstance();
//  }
//
//  @Bean
//  public DaoAuthenticationProvider authProvider() {
//    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//    authProvider.setUserDetailsService(userDetailsService());
//    authProvider.setPasswordEncoder(passwordEncoder());
//    return authProvider;
//  }
//
//  @Bean
//  public AuthenticationManager customAuthenticationManager() throws Exception {
//    return authenticationManager();
//  }
//
//  @Bean
//  public AuthenticationFailureHandler authenticationFailureHandler() {
//    return new CustomAuthenticationFailureHandler();
//  }
//
//  @Bean
//  public AuthenticationSuccessHandler authenticationSuccessHandler() {
//    return new CustomAuthenticationSuccessHandler();
//  }
//
//  /*
//   *  enable basic authentication & disable anonymous access
//   */
//  @Override
//  public void configure(HttpSecurity httpSecurity) throws Exception {
//    httpSecurity
//        .authorizeRequests()
//        .antMatchers(AUTH_WHITELIST).permitAll() // whitelist public endpoints
//        .and()
//        .authorizeRequests()
//        .anyRequest().authenticated() // require authentication for any endpoint that's not whitelisted
//        .and()
//        .formLogin()
////            .usernameParameter("username")
//        .failureHandler(authenticationFailureHandler())
//        .successHandler(authenticationSuccessHandler())
//        .loginPage("/login")
//        .permitAll()
//        .and()
//        .logout()
//        .logoutUrl("/logout")
//        .permitAll()
//        .and()
//        .sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    httpSecurity.csrf().disable();
//    httpSecurity.headers().frameOptions().disable();
//
//    httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//  }

  /*
   * register test user with in memory authentication provider
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    auth.inMemoryAuthentication()
        .passwordEncoder(passwordEncoder)
        .withUser("test")
        .password(passwordEncoder.encode("test"))
        .roles("USER");
  }
}
