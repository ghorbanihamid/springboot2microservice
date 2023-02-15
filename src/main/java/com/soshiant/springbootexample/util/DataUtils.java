package com.soshiant.springbootexample.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soshiant.springbootexample.config.AppStaticContext;
import com.soshiant.springbootexample.dto.*;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.entity.Employee;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.service.UserService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class DataUtils {

    public static LoginRequestDto buildLoginRequestDto() {

        return new LoginRequestDto(
                "username",
                "password"
        );
    }

    public static String buildLoginRequestDtoAsJson() {
        try {
            return new ObjectMapper().writeValueAsString(buildLoginRequestDto());

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Login object to JSON String!");
        }
    }

    public static LoginResponseDto buildLoginResponseDto() {

        return new LoginResponseDto(
                "username",
                "password",
                "Hamid",
                "7786362492",
                "a@yahoo.com",
                "aJWTToken"
        );
    }

    public static CustomerRequestDto buildCustomerDto() {

        return new CustomerRequestDto(
                AppConstants.FIRST_NAME,
                AppConstants.LAST_NAME,
                AppConstants.PHONE_NUMBER,
                AppConstants.EMAIL_ADDRESS,
                LocalDate.parse("1979-01-01",
                        DateTimeFormatter.ofPattern(AppConstants.BIRTH_DATE_FORMAT)),
                AppConstants.STREET_NUMBER,
                AppConstants.STREET_NAME,
                AppConstants.CITY,
                AppConstants.STATE,
                AppConstants.COUNTRY,
                AppConstants.ZIP_CODE,
                "test",
                "test"
        );
    }

    public static String buildCustomerDtoAsJson() {
        try {
            return new ObjectMapper().writeValueAsString(buildCustomerDto());

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Customer object to JSON String!");
        }
    }

    public static CustomerUpdateDto buildCustomerUpdateDto() {

        return new CustomerUpdateDto(
                1L,
                AppConstants.FIRST_NAME,
                AppConstants.LAST_NAME,
                AppConstants.PHONE_NUMBER,
                AppConstants.EMAIL_ADDRESS,
                LocalDate.parse("1979-01-01",
                        DateTimeFormatter.ofPattern(AppConstants.BIRTH_DATE_FORMAT))
        );
    }

    public static String buildCustomerUpdateDtoAsJson() {
        try {
            return new ObjectMapper().writeValueAsString(buildCustomerUpdateDto());

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Customer Update object to JSON String!");
        }
    }

    public static Customer buildCustomerObject() {

        ZoneId zoneId = ZoneId.of(AppConstants.PACIFIC_TIME_ZONE);
        Instant now = Instant.now();
        ZonedDateTime currentDateTimeInPST = now.atZone(zoneId);

        return new Customer(
                AppConstants.CUSTOMER_ID,
                AppConstants.FIRST_NAME,
                AppConstants.LAST_NAME,
                AppConstants.PHONE_NUMBER,
                AppConstants.EMAIL_ADDRESS,
                LocalDate.parse("1979-01-01",
                        DateTimeFormatter.ofPattern(AppConstants.BIRTH_DATE_FORMAT)),
                currentDateTimeInPST.toLocalDateTime(),
                null,
                //        buildUserObject(),
                new ArrayList<>()
        );
    }

    public static UserInfo buildUserObject() {

        ZoneId zoneId = ZoneId.of(AppConstants.PACIFIC_TIME_ZONE);
        Instant now = Instant.now();
        ZonedDateTime currentDateTimeInPST = now.atZone(zoneId);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("USER_NAME");
        userInfo.setPassword("Password");
        userInfo.setCreatedDate(currentDateTimeInPST.toLocalDateTime());
        return userInfo;
    }

    public static EmployeeRequestDto buildEmployeeDto() {

        return new EmployeeRequestDto(
                AppConstants.FIRST_NAME,
                AppConstants.LAST_NAME,
                AppConstants.PHONE_NUMBER,
                AppConstants.EMAIL_ADDRESS,
                LocalDate.parse("1979-01-01",
                        DateTimeFormatter.ofPattern(AppConstants.BIRTH_DATE_FORMAT)),
                AppConstants.STREET_NUMBER,
                AppConstants.STREET_NAME,
                AppConstants.CITY,
                AppConstants.STATE,
                AppConstants.COUNTRY,
                AppConstants.ZIP_CODE
        );
    }

    public static String buildEmployeeDtoAsJson() {
        try {
            return new ObjectMapper().writeValueAsString(buildEmployeeDto());

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Employee object to JSON String!");
        }
    }

    public static Employee buildEmployeeObject() {

        Employee employee =
                new Employee(
                        AppConstants.EMPLOYEE_ID,
                        AppConstants.FIRST_NAME,
                        AppConstants.LAST_NAME,
                        AppConstants.PHONE_NUMBER,
                        AppConstants.EMAIL_ADDRESS,
                        LocalDate.parse("1979-01-01",
                                DateTimeFormatter.ofPattern(AppConstants.BIRTH_DATE_FORMAT))
                );
        employee.setCreatedDate(Calendar.getInstance());
        return employee;
    }

    public static EmployeeAddress buildEmployeeAddressObject() {

        return new EmployeeAddress(
                1L,
                "1807",
                "Happy Street",
                "Happy City",
                "Happy State",
                "United States",
                "123456",
                buildEmployeeObject()
        );
    }

    public static void loadData() {
        try {
            UserService userService = AppStaticContext.getBean(UserService.class);

            SignupDto signupDto = new SignupDto();
            signupDto.setFirstName("hamid");
            signupDto.setLastName("Ghorbani");
            signupDto.setEmailAddress("hamid@gmail.com");
            signupDto.setUsername("hamid");
            signupDto.setPassword("hamid");
            signupDto.setUserRole("ROLE_ADMIN");
            userService.signupUser(signupDto);

            signupDto = new SignupDto();
            signupDto.setFirstName("Adrian");
            signupDto.setLastName("Ghorbani");
            signupDto.setEmailAddress("hamid@gmail.com");
            signupDto.setUsername("adrian");
            signupDto.setPassword("adrian");
            signupDto.setUserRole("ROLE_USER");
            userService.signupUser(signupDto);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}


