package com.soshiant.springbootexample.service;


import com.soshiant.springbootexample.dto.SignupDto;
import com.soshiant.springbootexample.entity.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Hamid.Ghorbani
 *
 */
public interface UserService extends UserDetailsService {

  boolean signupUser(SignupDto signupDto) throws Exception;

  /**
   * @param username String
   * @return UserInfo
   */
  UserInfo findUserByUsername(String username) throws UsernameNotFoundException ;

  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

  Integer increasePasswordRetryAttempts(String username);

  Integer resetPasswordRetryAttempts(String username);

  boolean lockUser(String username);
}
