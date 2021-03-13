package com.soshiant.springbootexample.controller;

import com.soshiant.springbootexample.service.CryptographyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Hamid.Ghorbani
 *
 */
@Slf4j
@Controller
public class CryptographyController {

  @Autowired
  private CryptographyService cryptographyService;

  @PostMapping(value = "test-cryptography",consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> testCryptography(@RequestBody String rawData, @RequestParam("generateKey") boolean generateKey) {

    try {
      log.info("input raw data : {}", rawData);
      boolean result = cryptographyService.encryptAndDecrypt(rawData,generateKey);
      return ResponseEntity.ok( "result : " + result);


    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
  }
  }

  @PostMapping(value = "encrypt",consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> encrypt(@RequestBody String rawData) {

    try {
      log.info("input encrypted data : {}", rawData);
      String encryptedData = cryptographyService.encrypt(rawData);
      return ResponseEntity.ok(encryptedData);


    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
  }
  }

  @PostMapping(value = "decrypt",consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> decrypt(@RequestBody String encryptedData) {

    try {
      log.info("input data : {}", encryptedData);
      String decrypted = cryptographyService.decrypt(encryptedData);
      return ResponseEntity.ok(decrypted);


    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
    }
  }

}
