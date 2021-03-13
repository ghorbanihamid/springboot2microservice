package com.soshiant.springbootexample.service.impl;

import com.soshiant.springbootexample.config.properties.CryptographyProperties;
import com.soshiant.springbootexample.service.CryptographyService;
import com.soshiant.springbootexample.util.CipherKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.bc.BcPEMDecryptorProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

@Slf4j
@Service
public class CryptographyServiceImpl implements CryptographyService {

  private PrivateKey privateKey = null;

  private PublicKey publicKey = null;

  @Autowired
  private CryptographyProperties cryptographyProperties;

  protected PrivateKey loadPrivateKeyFromFile() throws IOException {
    String prvKeyString = loadPrivateKeyFile();
    if(StringUtils.isBlank(prvKeyString)){
      log.error("couldn't load private key file.");
      throw new IOException("couldn't load private key from specified path.");
    }
    return loadPrivateKey(prvKeyString);
  }

  protected PrivateKey loadPrivateKey(String encodedCertificate) throws IOException {

    try {
      JcaPEMKeyConverter converter =
          new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
      Security.addProvider(new BouncyCastleProvider());
      PEMParser pemParser = new PEMParser(new StringReader(encodedCertificate));
      Object pemParsedObject = pemParser.readObject();
      KeyPair keyPair;
      if (pemParsedObject instanceof PEMEncryptedKeyPair) { // "Encrypted key - we will use provided
                                                            // password"
        PEMDecryptorProvider pemProvider =
            new BcPEMDecryptorProvider(cryptographyProperties.getPemKeyPassword().toCharArray());
        keyPair = converter
            .getKeyPair(((PEMEncryptedKeyPair) pemParsedObject).decryptKeyPair(pemProvider));
      } else {
        keyPair = converter.getKeyPair((PEMKeyPair) pemParsedObject);
      }
      this.publicKey = keyPair.getPublic();
      log.debug("**** private key successfully loaded.");
      return keyPair.getPrivate();

    } catch (IOException e) {
      log.error("couldn't convert the input string to private key");
      throw e;
    }

  }

  public String encrypt(String rawData) {

    try {
      if(publicKey == null) {
        this.privateKey = loadPrivateKeyFromFile();
      }
      Cipher encrypt = Cipher.getInstance(cryptographyProperties.getCipherType());
      encrypt.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encrypted = encrypt.doFinal(rawData.getBytes());
      byte[] encodedData = Base64.getEncoder().encode(encrypted);
      return new String(encodedData, StandardCharsets.UTF_8);

    } catch (Exception e) {
      return null;
    }
  }

  public String decrypt(String encryptedData) {

    try {
      if (this.privateKey == null) {
        this.privateKey = loadPrivateKeyFromFile();
      } else {
        log.debug("*** private key already loaded.");
      }

      Cipher decrypt = Cipher.getInstance(cryptographyProperties.getCipherType());
      decrypt.init(Cipher.DECRYPT_MODE, privateKey);

      byte[] s = Base64.getDecoder().decode(encryptedData);

      return new String(decrypt.doFinal(s), StandardCharsets.UTF_8);

    } catch (Exception e) {
      log.error("error while trying to decrypt the input value:{}",e.getMessage());
      return null;
    }
  }

  protected String loadPrivateKeyFile() throws IOException {

    Path path = Paths.get(cryptographyProperties.getPrivateKeyPath());
    try {
      if(!Files.exists(path)){
        log.error("private key file not exist:{}",path.toString());
        return null;
      }
      log.debug("path for private key file is :{}", path.toString());
      byte[] bytes = Files.readAllBytes(path);
      return new String(bytes, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error("couldn't read the private key file: {}", e.getMessage());
      throw e;
    }
  }

  public boolean encryptAndDecrypt(String rawData,boolean generateKey) {
    if(generateKey){
      CipherKeyUtils.generatePEMKey(cryptographyProperties.getAlgorithm(),
          cryptographyProperties.getCipherType(),
          cryptographyProperties.getKeySize());
    }
    String encryptedData = encrypt(rawData);
    String decryptedData = decrypt(encryptedData);
    return encryptedData.equalsIgnoreCase(decryptedData);
  }

}
