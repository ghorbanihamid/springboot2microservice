package com.soshiant.springbootexample.service;

public interface CryptographyService {

  String encrypt(String rawData);

  String decrypt(String encryptedData);

  boolean encryptAndDecrypt(String rawData,boolean generateKey);

}
