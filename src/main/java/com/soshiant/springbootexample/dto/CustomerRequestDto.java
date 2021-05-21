package com.soshiant.springbootexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class CustomerRequestDto {

  @JsonProperty("first-name")
  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid First Name!")
  @NotBlank(message="First name cannot be missing or empty!")
  @Size(min=2, message="First name must not be less than 2 characters!")
  private String firstName;

  @JsonProperty("last-name")
  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid Last Name!")
  @NotBlank(message="Last name cannot be missing or empty!")
  @Size(min=2, message="Last name must not be less than 2 characters!")
  private String lastName;

  @JsonProperty("phone-number")
  @NotBlank(message="Phone number cannot be missing or empty!")
  @Size(min=10, max = 15, message="Invalid Phone number!")
  @Pattern(regexp = "^[0-9]*$", message="Invalid Phone number!")
  private String phoneNumber;

  @JsonProperty("email-address")
  @NotBlank(message="Email address cannot be missing or empty!")
  @Email(message="Invalid Email address!")
  private String emailAddress;

  @JsonProperty("birth-date")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @Past(message="Invalid birth date!")
  @NotNull(message="Birth date cannot be missing or empty!")
  private LocalDate birthDate;

  @JsonProperty("street-number")
  @NotBlank(message="street number cannot be missing or empty!")
  private String streetNumber;

  @JsonProperty("street-name")
  @NotBlank(message="street name cannot be missing or empty!")
  @Size(min=2, message="Invalid street name!")
  private String streetName;

  @JsonProperty("city")
  @NotBlank(message="City name cannot be missing or empty!")
  @Size(min=2, message="Invalid City name!")
  private String city;

  @JsonProperty("state")
  @NotBlank(message="state name cannot be missing or empty!")
  @Size(min=2, message="Invalid state name!")
  private String state;

  @JsonProperty("country")
  @NotBlank(message="country name cannot be missing or empty!")
  @Size(min=2, max=60, message="Invalid country name!")
  private String country;

  @JsonProperty("zip-code")
  @NotBlank(message="zip code name cannot be missing or empty!")
  @Size(min=5, message="zip code must not be less than 2 characters!")
  private String zipCode;

  @JsonProperty("username")
  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid username!")
  @NotBlank(message="username cannot be missing or empty!")
  @Size(min=2, message="username must not be less than 2 characters!")
  private String username;

  @JsonProperty("password")
  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid password!")
  @NotBlank(message="password cannot be missing or empty!")
  @Size(min=2, message="password must not be less than 2 characters!")
  private String password;
}
