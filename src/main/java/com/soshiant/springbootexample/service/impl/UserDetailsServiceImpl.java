package com.soshiant.springbootexample.service.impl;


import static java.lang.String.format;

import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


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

}
