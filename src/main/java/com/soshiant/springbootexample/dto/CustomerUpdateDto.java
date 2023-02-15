package com.soshiant.springbootexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soshiant.springbootexample.validation.CustomerIdValidation;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerUpdateDto implements Serializable {

  @JsonProperty("customer-id")
  @CustomerIdValidation
  @NotNull(message="Customer ID is missing!")
  private Long customerId;

  @JsonProperty("first-name")
  @Pattern(regexp = "^[A-Za-z]*$", message="Invalid First Name!")
  @Size(min=2, max = 20, message="First Name should be minimum 2 and maximum 20 character!")
  private String firstName;

  @JsonProperty("last-name")
  @Pattern(regexp = "^[A-Za-z]*$", message="Invalid Last Name!")
  @Size(min=2, max = 20, message="Last Name should be minimum 2 and maximum 20 character!")
  private String lastName;

  @JsonProperty("phone-number")
  @Pattern(regexp = "^[0-9]*$", message="Invalid Phone number!")
  @Size(min=10, max = 15, message="Invalid Phone number!")
  private String phoneNumber;

  @JsonProperty("email-address")
  @Email(message="Invalid Email address!")
  private String emailAddress;

  @JsonProperty("birth-date")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @Past(message="Invalid birth date!")
  private LocalDate birthDate;


}
