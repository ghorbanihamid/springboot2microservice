package com.soshiant.springbootexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
public class LoginResponseDto {

  @JsonProperty("username")
  private String username;

  @JsonProperty("first-name")
  private String firstName;

  @JsonProperty("last-name")
  private String lastName;

  @JsonProperty("phone-number")
  private String phoneNumber;

  @JsonProperty("email-address")
  private String emailAddress;

  @JsonProperty("token")
  private String jwtToken;
}
