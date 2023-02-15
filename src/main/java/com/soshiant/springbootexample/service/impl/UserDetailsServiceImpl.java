package com.soshiant.springbootexample.service.impl;


import com.soshiant.springbootexample.dto.SignupDto;
import com.soshiant.springbootexample.entity.Role;
import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.entity.UserRole;
import com.soshiant.springbootexample.entity.UserRolePrimaryKey;
import com.soshiant.springbootexample.mapper.UserMapper;
import com.soshiant.springbootexample.repository.RoleRepository;
import com.soshiant.springbootexample.repository.UserRepository;
import com.soshiant.springbootexample.repository.UsersRolesRepository;
import com.soshiant.springbootexample.service.UserService;
import com.soshiant.springbootexample.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserService {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsersRolesRepository usersRolesRepository;


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
     * @return userDetails
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo userInfo = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User: %s, not found", username)));

        UserRolePrimaryKey userRolePrimaryKey = new UserRolePrimaryKey();
        userRolePrimaryKey.setUserInfo(userInfo);
        List<String> userRoles = usersRolesRepository.findByUserId(userInfo.getId());


        return new User(username,
                userInfo.getPassword(),
                userInfo.isEnabled(),
                true,
                true,
                !userInfo.isLocked(),
                getAuthorities(userRoles));
    }

    public Set<GrantedAuthority> getAuthorities(List<String> roles) {

        Set<GrantedAuthority> authorities = new HashSet<>();
        if(roles != null) {
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }

    /**
     * @param signupDto SignupDto
     * @return UserInfo
     */
    public boolean signupUser(SignupDto signupDto) throws Exception {

        try {
            UserInfo userInfo = userMapper.toUser(signupDto);
            userInfo.setPassword(passwordEncoder.encode(signupDto.getPassword()));
            userInfo.setEnabled(true);
            userInfo.setLocked(false);
            UserInfo savedUserInfo = userRepository.save(userInfo);

            if(!StringUtils.isBlank(signupDto.getUserRole())) {
                Role role = roleRepository.findByRoleName(signupDto.getUserRole()).orElse(null);
                if (role != null) {
                    UserRolePrimaryKey primaryKey = new UserRolePrimaryKey();
                    primaryKey.setUserInfo(savedUserInfo);
                    primaryKey.setRole(role);

                    UserRole userRole = new UserRole();
                    userRole.setId(primaryKey);
                    userRole.setEnabled(true);
                    userRole.setCreatedDate(DateTimeUtils.getCurrentLocalDateTime());
                    usersRolesRepository.save(userRole);

                }
            }
            return true;
        } catch (Exception e) {
            log.error("Exception occurred during signupUser method for signupDto : {} with message :{} ",
                    signupDto, e.getCause());
            throw new Exception(e.getMessage());
        }

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
