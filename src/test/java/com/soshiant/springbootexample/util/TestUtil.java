package com.soshiant.springbootexample.util;


import static com.soshiant.springbootexample.util.AppTestConstants.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soshiant.springbootexample.dto.CustomerDto;
import com.soshiant.springbootexample.dto.EmployeeDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.entity.Employee;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestUtil {



  public static CustomerDto buildCustomerDto() {

    return new CustomerDto(
        FIRST_NAME,
        LAST_NAME,
        PHONE_NUMBER,
        EMAIL_ADDRESS,
        LocalDate.parse("1979-01-01",
            DateTimeFormatter.ofPattern(AppTestConstants.BIRTH_DATE_FORMAT)),
        STREET_NUMBER,
        STREET_NAME,
        CITY,
        STATE,
        COUNTRY,
        ZIP_CODE
    );
  }

  public static String buildCustomerDtoAsJson() {
    try {
      return new ObjectMapper().writeValueAsString(buildCustomerDto());

    } catch (Exception e) {
      throw new RuntimeException("Failed to convert Customer object to JSON String!");
    }
  }

  public static Customer buildCustomerObject() {

    ZoneId zoneId = ZoneId.of(AppTestConstants.PACIFIC_TIME_ZONE);
    Instant now = Instant.now();
    ZonedDateTime currentDateTimeInPST = now.atZone( zoneId );

    return new Customer(
        CUSTOMER_ID,
        FIRST_NAME,
        LAST_NAME,
        PHONE_NUMBER,
        EMAIL_ADDRESS,
        LocalDate.parse("1979-01-01",
            DateTimeFormatter.ofPattern(AppTestConstants.BIRTH_DATE_FORMAT)),
        currentDateTimeInPST.toLocalDateTime(),
        null,
        new ArrayList<>()
    );
  }

  public static EmployeeDto buildEmployeeDto() {

    return new EmployeeDto(
        FIRST_NAME,
        LAST_NAME,
        PHONE_NUMBER,
        EMAIL_ADDRESS,
        LocalDate.parse("1979-01-01",
            DateTimeFormatter.ofPattern(AppTestConstants.BIRTH_DATE_FORMAT)),
        STREET_NUMBER,
        STREET_NAME,
        CITY,
        STATE,
        COUNTRY,
        ZIP_CODE
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

    return new Employee(
        EMPLOYEE_ID,
        FIRST_NAME,
        LAST_NAME,
        PHONE_NUMBER,
        EMAIL_ADDRESS,
        LocalDate.parse("1979-01-01",
            DateTimeFormatter.ofPattern(AppTestConstants.BIRTH_DATE_FORMAT))
    );
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

}
