package com.soshiant.springbootexample.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;

import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.bc.BcPEMDecryptorProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

/**
 * Helper class for loading private keys (PVK or PEM, encrypted or not).
 */

@Slf4j
public class CipherKeyUtils {

  private CipherKeyUtils() {}

  private static PrivateKey loadPEMPrivateKey(File file, String password)
              throws IOException, OperatorCreationException, PKCSException {

    try (FileReader reader = new FileReader(file)) {
      PEMParser parser = new PEMParser(reader);
      Object object = parser.readObject();

      if (object == null) {
        throw new IllegalArgumentException("No key found in " + file);
      }

      BouncyCastleProvider provider = new BouncyCastleProvider();
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(provider);

      if (object instanceof PEMEncryptedKeyPair) {
        // PKCS1 encrypted key
        PEMDecryptorProvider decryptionProvider =
            new JcePEMDecryptorProviderBuilder()
                .setProvider(provider)
                .build(password.toCharArray());
        PEMKeyPair keypair = ((PEMEncryptedKeyPair) object).decryptKeyPair(decryptionProvider);
        return converter.getPrivateKey(keypair.getPrivateKeyInfo());

      } else if (object instanceof PKCS8EncryptedPrivateKeyInfo) {
        // PKCS8 encrypted key
        InputDecryptorProvider decryptionProvider =
            new JceOpenSSLPKCS8DecryptorProviderBuilder()
                .setProvider(provider)
                .build(password.toCharArray());
        PrivateKeyInfo info =
            ((PKCS8EncryptedPrivateKeyInfo) object).decryptPrivateKeyInfo(decryptionProvider);
        return converter.getPrivateKey(info);

      } else if (object instanceof PEMKeyPair) {
        // PKCS1 unencrypted key
        return converter.getKeyPair((PEMKeyPair) object).getPrivate();

      } else if (object instanceof PrivateKeyInfo) {
        // PKCS8 unencrypted key
        return converter.getPrivateKey((PrivateKeyInfo) object);

      } else {
        throw new UnsupportedOperationException(
            "Unsupported PEM object: " + object.getClass().getSimpleName());
      }
    }
  }

  public static PublicKey getPEMPublicKey(String filePath,String password) {

    String cryptoKey = loadPEMKeysFile(filePath);
    KeyPair keyPair = getPEMKeysFromString(cryptoKey,password);
    return keyPair.getPublic();
  }
  public static PrivateKey getPEMPrivateKey(String filePath,String password) {
    String cryptoKey = loadPEMKeysFile(filePath);
    KeyPair keyPair = getPEMKeysFromString(cryptoKey,password);
    return keyPair.getPrivate();
  }

  private static String loadPEMKeysFile(String filePath) {
    String cryptoKey;
    try{
      cryptoKey = FileUtils.loadFile(filePath);
      if(StringUtils.isBlank(cryptoKey)){
        log.error("empty file");
        return cryptoKey;
      }
    } catch (IOException e){
      log.error("couldn't read file");
    }
    return null;
  }

  public static KeyPair getPEMKeysFromString(String cryptoKey, String password) {

    try {
      JcaPEMKeyConverter converter =
          new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
      Security.addProvider(new BouncyCastleProvider());
      PEMParser pemParser = new PEMParser(new StringReader(cryptoKey));
      Object pemParsedObject = pemParser.readObject();
      KeyPair keyPair;

      // "Encrypted key - we will use provided password"
      if (pemParsedObject instanceof PEMEncryptedKeyPair) {

        PEMDecryptorProvider pemProvider = new BcPEMDecryptorProvider(
            password.toCharArray());

        keyPair = converter.getKeyPair(((PEMEncryptedKeyPair) pemParsedObject)
            .decryptKeyPair(pemProvider));
      } else {
        keyPair = converter.getKeyPair((PEMKeyPair) pemParsedObject);
      }
      return keyPair;

    } catch (IOException e) {
      log.error("couldn't convert the input string to private key");
      return null;
    }
  }

  public static KeyPair generatePEMKey(String algorithm,String password, int keySize) {

    try {
      Security.addProvider(new BouncyCastleProvider());
      KeyPairGenerator keygen = KeyPairGenerator.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
      keygen.initialize(keySize, new SecureRandom());
      KeyPair keypair = keygen.generateKeyPair();
      if(!StringUtils.isBlank(password)){
        getPEMKeyEncryptedWithPassword(keypair.getPrivate().getEncoded(),algorithm,password);
      }
      return keypair;

    } catch (NoSuchAlgorithmException e) {
      System.out.println("error : No Such Algorithm");
      return null;

    } catch (NoSuchProviderException e) {
      System.out.println("error : No Such Provider");
      return null;
    }
  }

  public static String getPEMKeyEncryptedWithPassword(byte[] encodedPrivateKey, String algorithm,String password) {
      return "";
  }

  public static boolean encryptKeyWithPassword(byte[] encodedPrivateKey, String algorithm,String password) {

    try {
      // We must use a PasswordBasedEncryption algorithm in order to encrypt the private key,
      // you may use any common algorithm supported by openssl,
      // you can check them in the openssl documentation http://www.openssl.org/docs/apps/pkcs8.html
      String passBasedEncAlgorithm = "PBEWithSHA1AndDESede";

      int count = 20;// hash iteration count
      SecureRandom random = new SecureRandom();
      byte[] salt = new byte[8];
      random.nextBytes(salt);

      // Create PBE parameter set
      PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
      PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
      SecretKeyFactory keyFac = SecretKeyFactory.getInstance(passBasedEncAlgorithm);
      SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

      Cipher pbeCipher = Cipher.getInstance(passBasedEncAlgorithm);

      // Initialize PBE Cipher with key and parameters
      pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

      // Encrypt the encoded Private Key with the PBE key
      byte[] ciphertext = pbeCipher.doFinal(encodedPrivateKey);

      // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
      AlgorithmParameters algparms = AlgorithmParameters.getInstance(passBasedEncAlgorithm);
      algparms.init(pbeParamSpec);
      javax.crypto.EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

      // and here we have it! a DER encoded PKCS#8 encrypted key!
      byte[] encryptedPkcs8 = encinfo.getEncoded();


    } catch (NoSuchAlgorithmException e) {
      System.out.println("error : No Such Algorithm");
      return false;

    } catch (Exception e) {
      System.out.println("error : No Such Provider");
      return false;
    }
    return true;
  }

//  public static PrivateKey loadPVKPrivateKeyFromFile(String file, String password) throws KeyException {
//    try {
//      return PVK.parse(file, password);
//    } catch (Exception e) {
//      throw new KeyException("Failed to load the private key from " + file, e);
//    }
//  }


//  private static PrivateKey readPrivateKeyDER(File file, String password)
//      throws IOException, GeneralSecurityException, OperatorCreationException, PKCSException {
//    try (FileInputStream in = new FileInputStream(file)) {
//      BouncyCastleProvider provider = new BouncyCastleProvider();
//      JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(provider);
//
//      // PKCS8 unencrypted key
//      PrivateKeyInfo info = PrivateKeyInfo.getInstance(new ASN1InputStream(in));
//      converter.getPrivateKey(info);
//
//      // PKCS8 encrypted key
//      EncryptedPrivateKeyInfo info2 = EncryptedPrivateKeyInfo.getInstance(new ASN1InputStream(in));
//      PKCS8EncryptedPrivateKeyInfo pkcsinfo = new PKCS8EncryptedPrivateKeyInfo(info2);
//
//      /// PKCS1 unencrypted key
//      RSAPrivateKey rsapk = RSAPrivateKey.getInstance(new ASN1InputStream(in));
//      AlgorithmIdentifier algId =
//          new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
//      new PrivateKeyInfo(algId, rsapk);
//
//      if (object instanceof PEMEncryptedKeyPair) {
//        // PKCS1 encrypted key
//        PEMDecryptorProvider decryptionProvider =
//            new JcePEMDecryptorProviderBuilder()
//                .setProvider(provider)
//                .build(password.toCharArray());
//        PEMKeyPair keypair = ((PEMEncryptedKeyPair) object).decryptKeyPair(decryptionProvider);
//        return converter.getPrivateKey(keypair.getPrivateKeyInfo());
//
//      } else if (object instanceof PKCS8EncryptedPrivateKeyInfo) {
//        // PKCS8 encrypted key
//        InputDecryptorProvider decryptionProvider =
//            new JceOpenSSLPKCS8DecryptorProviderBuilder()
//                .setProvider(provider)
//                .build(password.toCharArray());
//        PrivateKeyInfo info =
//            ((PKCS8EncryptedPrivateKeyInfo) object).decryptPrivateKeyInfo(decryptionProvider);
//        return converter.getPrivateKey(info);
//
//      } else if (object instanceof PEMKeyPair) {
//        // PKCS1 unencrypted key
//        return converter.getKeyPair((PEMKeyPair) object).getPrivate();
//
//      } else if (object instanceof PrivateKeyInfo) {
//        // PKCS8 unencrypted key
//        return converter.getPrivateKey((PrivateKeyInfo) object);
//
//      } else {
//        throw new UnsupportedOperationException(
//            "Unsupported PEM object: " + object.getClass().getSimpleName());
//      }
//    }
//  }
}
