package com.soshiant.springbootexample.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cipher", ignoreUnknownFields = true)
public class CryptographyProperties {

  private String publicKeyPath;
  private String privateKeyPath;
  private String pemKeyPassword;
  private String algorithm;
  private String cipherType;
  private int    keySize;

}
