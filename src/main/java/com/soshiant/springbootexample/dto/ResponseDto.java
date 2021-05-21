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

  private Object data;

  //@formatter:off
  public static final String RESPONSE_BODY =
       "\"status\": \"{0}\","
     + "\"error\": \"{1}\","
     + "\"data\": {2}";

  //@formatter:on

  @Override
  public String toString(){
    String dataStr = "\"\"";
    try {
      if (data != null) {
        dataStr = new ObjectMapper().writeValueAsString(data);
      }
    }catch (Exception e){
      log.error("ResponseDto failed to convert data to json: [{}]!",e.getMessage());
    }
    return "{ "
         +   MessageFormat.format( ResponseDto.RESPONSE_BODY, status, error, dataStr)
         + "}";
  }
}
