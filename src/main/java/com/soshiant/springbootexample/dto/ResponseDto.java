package com.soshiant.springbootexample.dto;

import java.text.MessageFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

  private String status;

  private String description;

  private String detail;

  //@formatter:off
  public static final String RESPONSE_BODY = ""
      + "\"status\": \"{0}\","
      + "\"description\": \"{1}\","
      + "\"detail\": {2}";
  //@formatter:on

  @Override
  public String toString(){

    return
        "{" +
            MessageFormat.format(
                ResponseDto.RESPONSE_BODY,
                status,
                description,
                detail
            )
            + "}";
  }
}
