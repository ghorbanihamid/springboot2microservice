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
public class EmployeeRequestDto {

  @JsonProperty("first-name")
  @NotBlank(message="First name cannot be missing or empty!")
  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid First Name!")
  @Size(min=2, max = 20, message="First Name should be minimum 2 and maximum 20 character!")
  private String firstName;

  @JsonProperty("last-name")
  @NotBlank(message="Last name cannot be missing or empty!")
  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid Last Name!")
  @Size(min=2, max = 20, message="Last Name should be minimum 2 and maximum 20 character!")
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
  @Positive(message="Invalid Amount!")
  @Digits(integer=5, fraction=0,message="Invalid street number!")
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
  @Size(min=4, max = 60, message="Invalid country name!")
  private String country;

  @JsonProperty("zip-code")
  @NotBlank(message="zip code name cannot be missing or empty!")
  @Size(min=5, message="zip code must not be less than 2 characters!")
  private String zipCode;

}
