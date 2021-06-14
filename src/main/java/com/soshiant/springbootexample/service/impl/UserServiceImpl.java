package com.soshiant.springbootexample.service.impl;


import static java.lang.String.format;

import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.repository.UserRepository;
import com.soshiant.springbootexample.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


  @Autowired
  private UserRepository userRepository;

  /**
   * @param username String
   * @return userDetails
   */
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      UserInfo userInfo = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(format("User: %s, not found", username)));

      return new User(username,
                      userInfo.getPassword(),
                      userInfo.isEnabled(),
                      true,
                      true,
                      !userInfo.isLocked(),
                      AuthorityUtils.createAuthorityList("ROLE_USER"));
  }

  /**
   * @param username String
   * @return UserInfo
   */
  public UserInfo findUserByUsername(String username) throws UsernameNotFoundException {

      UserInfo userInfo = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(format("User: %s, not found", username)));

      log.debug("user {} found, ",userInfo.getUsername());
      return userInfo;
  }

  /**
   * @param username String
   * @return UserInfo
   */
  public Integer increasePasswordRetryAttempts(String username) throws UsernameNotFoundException {

      Integer count = userRepository.increasePasswordRetryAttempts(username);

      log.debug("updatePasswordRetryAttempts {} found, ",username);
      return count;
  }

  /**
   * @param username String
   * @return UserInfo
   */
  public Integer resetPasswordRetryAttempts(String username) throws UsernameNotFoundException {

      Integer count = userRepository.increasePasswordRetryAttempts(username);

      log.debug("updatePasswordRetryAttempts {} found, ",username);
      return count;
  }

  /**
   * @param username String
   * @return UserInfo
   */
  public boolean lockUser(String username) throws UsernameNotFoundException {

      Integer count = userRepository.increasePasswordRetryAttempts(username);

      log.debug("updatePasswordRetryAttempts {} found, ",username);
      return true;
  }

}
