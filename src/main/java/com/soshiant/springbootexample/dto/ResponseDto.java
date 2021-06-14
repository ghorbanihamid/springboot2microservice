package com.soshiant.springbootexample.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.MessageFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

  private String status;

  private String error;

  private String data;

  //@formatter:off
  public static final String RESPONSE_BODY =
       "\"status\": \"{0}\","
     + "\"error\": \"{1}\","
     + "\"data\": {2}";

  //@formatter:on

  @Override
  public String toString(){
    return "{ "
         +   MessageFormat.format( ResponseDto.RESPONSE_BODY, status, error, data)
         + "}";
  }
}
