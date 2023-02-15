package com.soshiant.springbootexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.text.MessageFormat;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto implements Serializable {

  private static final long serialVersionUID = -8091879091924078944L;

  @JsonProperty("status")
  private String status;

  @JsonProperty("error")
  private String error;

  @JsonProperty("data")
  @JsonIgnore
  private String data;

  @JsonProperty("token")
  private String token;

  //@formatter:off
  public static final String RESPONSE_BODY =
       "\"status\": \"{0}\","
     + "\"error\": \"{1}\","
     + "\"data\": {2}\","
     + "\"token\": {3}";

  //@formatter:on

  @Override
  public String toString(){
    return "{ "
         +   MessageFormat.format( ResponseDto.RESPONSE_BODY, status, error, data, token)
         + "}";
  }
}
