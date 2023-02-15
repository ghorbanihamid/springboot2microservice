package com.soshiant.springbootexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.soshiant.springbootexample.validation.username.UsernameValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestDto implements Serializable {

  @JsonProperty("username")
  @NotBlank(message="username cannot be missing or empty!")
  @UsernameValidation
  private String username;

  @JsonProperty("password")
  @NotBlank(message="password cannot be missing or empty!")
  @Size(min=2, message="password must not be less than 2 characters!")
  private String password;
}
