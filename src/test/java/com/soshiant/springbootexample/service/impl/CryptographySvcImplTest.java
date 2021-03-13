package com.soshiant.springbootexample.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;

import com.soshiant.springbootexample.config.properties.CryptographyProperties;
import com.soshiant.springbootexample.util.CryptographyTestUtil;
import java.io.File;
import java.security.PrivateKey;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
/*
 * JUnit 5 doesn't support Power mock
 * also current version of Mockito(3.4) does not support private and just supports static methods
 */


@Slf4j
@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class CryptographyServiceImplTest {

  private static final String CIPHER_TYPE = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
  private static final String CIPHER_PASSWORD = "password";

  @MockBean
  CryptographyProperties cryptographyProperties;

  private String privateKeyPath = "src/test/resources/rsa_1024_priv.pem";

  @InjectMocks
  @Spy
  CryptographyServiceImpl cryptographyService;

  @BeforeEach
  void setUp() {
    when(cryptographyProperties.getCipherType()).thenReturn(CIPHER_TYPE);
    when(cryptographyProperties.getPemKeyPassword()).thenReturn(CIPHER_PASSWORD);
  }

  @AfterEach
  void tearDown() {
    validateMockitoUsage();
  }

  @Test
  @DisplayName("Test encryption and decryption would be success with generated in memory private")
  void test_decrypt_Success() {

    String originalMessage = "Test message";

    CryptographyTestUtil.generatePemKey("RSA","test",1024);
    ReflectionTestUtils.setField(cryptographyService,"privateKey",CryptographyTestUtil.getPrivateKey());
    ReflectionTestUtils.setField(cryptographyService,"publicKey",CryptographyTestUtil.getPublicKey());
    String encryptedMessage = cryptographyService.encrypt(originalMessage);
    String decryptedMessage = cryptographyService.decrypt(encryptedMessage);

    assertThat(originalMessage, CoreMatchers.equalTo(decryptedMessage));

  }

  @Test
  @DisplayName("Test encryption and decryption would be success with loading private key from file")
  void test_encrypt_decrypt_Success() {

    File file = new File(privateKeyPath);
    String absolutePath = file.getAbsolutePath();
    String originalMessage = "5365391482579704";
    when(cryptographyProperties.getPrivateKeyPath()).thenReturn(absolutePath);
    String encryptedMessage = cryptographyService.encrypt(originalMessage);
    String decryptedMessage = cryptographyService.decrypt(encryptedMessage);

    assertThat(originalMessage, CoreMatchers.equalTo(decryptedMessage));

  }

  @Test
  @DisplayName("Test encryption and decryption would be success with loading private key from file")
  void test_encryption_fails() {

    String originalMessage = "5365391482579704";
    when(cryptographyProperties.getPrivateKeyPath()).thenReturn("");
    String encryptedMessage = cryptographyService.encrypt(originalMessage);

    assertThat(encryptedMessage, CoreMatchers.is(nullValue()));

  }

  @Test
  @DisplayName("Test decryption fails with invalid encrypted message")
  void test_decrypt_fails_with_invalid_encrypted_message() {

    String message = "Test message";
    CryptographyTestUtil.generatePemKey("RSA","test",1024);
    ReflectionTestUtils.setField(cryptographyService,"privateKey",CryptographyTestUtil.getPrivateKey());
    String decryptedMessage = cryptographyService.decrypt(message);

    assertThat(decryptedMessage, is(nullValue()));

  }

  @Test
  @DisplayName("Test decryption fails with null privateKey")
  void test_decrypt_fails_with_null_privateKey() throws Exception {

    String message = "Test message";

    doReturn(null).when(cryptographyService).loadPrivateKeyFromFile();
    String decryptedMessage = cryptographyService.decrypt(message);

    assertThat(decryptedMessage, is(nullValue()));

  }

  @Test
  @DisplayName("Test read privateKey success")
  void test_read_privateKey_success() throws Exception {

    doReturn(CryptographyTestUtil.privateKeyStr).when(cryptographyService).loadPrivateKeyFile();
    PrivateKey prvKey = cryptographyService.loadPrivateKeyFromFile();
    assertThat(prvKey, is(notNullValue()));

  }

  @Test
  @DisplayName("Test read privateKey fails")
  void test_read_privateKey_fails() {
    when(cryptographyProperties.getPrivateKeyPath()).thenReturn("");
    assertThrows(Exception.class, () -> cryptographyService.loadPrivateKeyFromFile());
  }

  @Test
  @DisplayName("Test read privateKey with invalid string")
  void test_read_privateKey_fails_with_invalid_String() throws Exception {

    doReturn("Private Key").when(cryptographyService).loadPrivateKeyFile();
    assertThrows(Exception.class, () -> cryptographyService.loadPrivateKeyFromFile());

  }

  @Test
  @DisplayName("Test read privateKey with invalid string")
  void test_read_privateKey_fails_with_invalid_KeyString() throws Exception {

    assertThrows(Exception.class, () -> cryptographyService.loadPrivateKey("Private Key"));

  }

}