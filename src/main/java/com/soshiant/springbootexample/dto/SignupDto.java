package com.soshiant.springbootexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupDto implements Serializable {

//  @JsonProperty("first-name")
//  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid First Name!")
//  @NotBlank(message="First name cannot be missing or empty!")
//  @Size(min=2, message="First name must not be less than 2 characters!")
  private String firstName;

//  @JsonProperty("last-name")
//  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid Last Name!")
//  @NotBlank(message="Last name cannot be missing or empty!")
//  @Size(min=2, message="Last name must not be less than 2 characters!")
  private String lastName;

//  @JsonProperty("phone-number")
//  @NotBlank(message="Phone number cannot be missing or empty!")
//  @Size(min=10, max = 15, message="Invalid Phone number!")
//  @Pattern(regexp = "^[0-9]*$", message="Invalid Phone number!")
  private String phoneNumber;

//  @JsonProperty("email-address")
//  @NotBlank(message="Email address cannot be missing or empty!")
//  @Email(message="Invalid Email address!")
  private String emailAddress;

  @JsonProperty("username")
  @Pattern(regexp = "^[A-Za-z ]*$", message="Invalid username!")
  @NotBlank(message="username cannot be missing or empty!")
  @Size(min=2, message="username must not be less than 2 characters!")
  private String username;

  @JsonProperty("password")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message="Invalid password!")
  @NotBlank(message="password cannot be missing or empty!")
  @Size(min=2, message="password must not be less than 2 characters!")
  private String password;

  @JsonProperty("user-role")
  @NotBlank(message="user-role cannot be missing or empty!")
  @Size(min=2, message="user-role must not be less than 2 characters!")
  private String userRole;
}
