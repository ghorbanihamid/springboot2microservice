package com.soshiant.springbootexample.config;

import com.soshiant.springbootexample.handler.CustomAuthenticationFailureHandler;
import com.soshiant.springbootexample.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Value("${spring.security.enabled:false}")
  private  boolean springSecurityEnabled;

  @Value("${server.servlet.context-path}")
  private  String appContextPath;

  private final String[] AUTH_WHITELIST = {
          "/css/**",
          "/img/**",
          "/js/**",
          "/webjars/**",
          "/actuator/**",
          "/h2/**",
          "h2/stylesheet.css",
          "/h2/login.do/**",
          "/h2/background.gif/**",
          "/login/**",
          "/authenticate/**",
          "/signup-page",
          "/signup",

          // -- Swagger UI v2
          "/v2/api-docs",
          "/swagger-resources",
          "/swagger-resources/**",
          "/configuration/ui",
          "/configuration/security",
          "/swagger-ui.html",
          "/webjars/**",

          // -- Swagger UI v3 (OpenAPI)
          "/v3/api-docs/**",
          "/swagger-ui/**"
  };

  @Autowired
  private UserDetailsService UserDetailsService;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private AccessDeniedHandler accessDeniedHandler;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(UserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);

    return authProvider;
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new CustomAuthenticationSuccessHandler();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    if(springSecurityEnabled) {
        http.authenticationProvider(authenticationProvider());

        http.headers().frameOptions().sameOrigin();
        HttpSecurity h2Console = http.antMatcher("/h2/**");
        h2Console.csrf().disable();
        h2Console.httpBasic();
        h2Console.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
            // URL matching for accessibility
            .antMatchers(AUTH_WHITELIST).permitAll()
            .antMatchers("/h2/**").permitAll()
//            .antMatchers("/soshiant/sbe/employee/**").hasAuthority("ADMIN")
//            .antMatchers("/soshiant/sbe/customer/**").hasAuthority("USER")
//            .antMatchers("/soshiant/sbe/signup-page/").hasAuthority("USER")
//            .antMatchers("/soshiant/sbe/dashboard/").hasAnyAuthority("USER")
//            .antMatchers("/soshiant/sbe/logout/").hasAnyAuthority("USER","ADMIN")
//            .antMatchers(HttpMethod.DELETE).hasAuthority("ADMIN")
            .anyRequest().authenticated()
            .and()
            // form login
            .csrf().disable().formLogin()
            .loginPage("/login").permitAll()
            .usernameParameter("username")
            .passwordParameter("password")
            .successHandler(authenticationSuccessHandler())
            .defaultSuccessUrl("/dashboard", true)
            .failureHandler(authenticationFailureHandler())
            .failureUrl("/login?error=true")
            .and()
            // logout
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login").permitAll()
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
            .accessDeniedPage("/login")
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
    else {
        http.authorizeRequests().anyRequest().permitAll();
    }
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    if(springSecurityEnabled) {
      return (web) -> web.ignoring().antMatchers(AUTH_WHITELIST);
    } else {
      return (web) -> web.ignoring().antMatchers("/**");
    }

  }
}
