package com.soshiant.springbootexample.service;


import com.soshiant.springbootexample.entity.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Hamid.Ghorbani
 *
 */
public interface UserService {

  /**
   * @param username String
   * @return UserInfo
   */
  UserInfo findUserByUsername(String username) throws UsernameNotFoundException ;

  Integer increasePasswordRetryAttempts(String username);

  Integer resetPasswordRetryAttempts(String username);

  boolean lockUser(String username);
}
