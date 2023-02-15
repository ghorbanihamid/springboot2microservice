package com.soshiant.springbootexample;

import com.soshiant.springbootexample.controller.UserController;
import com.soshiant.springbootexample.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 * Spring Boot 2.7 moves to new versions of several Spring projects:
 *    Spring Data 2021.2
 *    Spring HATEOAS 1.5
 *    Spring LDAP 2.4
 *    Spring Security 5.7
 *    Spring Session 2021.2
 */
@SpringBootApplication
public class Application {

    @Autowired
    UserController userController;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        DataUtils.loadData();
    }
}
