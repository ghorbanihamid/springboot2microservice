package com.soshiant.springbootexample.util;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class CryptographyTestUtil {

  public static String privateKeyStr =
          "-----BEGIN RSA PRIVATE KEY-----\n" +
          "MIICXQIBAAKBgQDWdzlqlgHK4Dnt51U0/RiXMtt1SwiDPnAgyVe+1UrQ9Eebx6Aw\n" +
          "0e5oq65p/N5SUsbg2z+zIQmXX2aTzJ8kHKRCAdS47fLNwdAr7U904WppOp7fdblX\n" +
          "1T/pohvtZlFjj1kErhtRiKlrgpOTSF810kAH6jjCfmQctISvu0kO/EJEAQIDAQAB\n" +
          "AoGAQddocSiMdFRSdI9IwXPSUNj0NxZKDsDke6xUm+mvhmb4dQLcUyCgKNJMQY24\n" +
          "U13GYNHuxHeoEijfvRBc6yE/69PZkwOd4Cotlz54UmLekieWWfgOJB7Yh+R69JoL\n" +
          "wVHBsWOogfSwHBPiO/L7umz/4xq4m9+AilXlFuXcoPT/kmECQQDzfund6upAZOCa\n" +
          "YXXpnr3K3ae1cBoPxCig8w3TpR4U+GUFNMKX0B3TYT07r+FB+Sx+9A8mUpZ71da6\n" +
          "alJguXtvAkEA4XqrxwleDyNptQ9fOFFbWhE0T4CPwAeG0bCLbm5xR3HJOTayZi+C\n" +
          "XEKF0vl3Q7OrI0GQH5787XfSC086mho/jwJAKuTrm+1bMNbUfj23AN22/3rGpX7H\n" +
          "CIn8wkWu5N7MitMrMJyXvxipeGhD2jTkkLOtc/vYg16/JbeI6TP7qRBjvwJBAI7b\n" +
          "/UgvTDQFOO1p3ue8zEdljSIuD2YYnrfuxnjias1cb6TbXe7WfR7dWxLP0lvH00+U\n" +
          "M6+FOTVv7FoUCconhUsCQQCH81IH9lllFWmhwsH9Qikunug2zWO3uksQZucj2RN2\n" +
          "tQ8JhKfoKlWq8FWmGiKmM+U00GOr7bRaZIoBEYXDFl6P\n" +
          "-----END RSA PRIVATE KEY-----\n";

  private static PrivateKey privateKey = null;

  private static PublicKey publicKey = null;

  public static boolean generateAndSavePemKeys(int keySize,String cipherType,String algorithm,String password,
      String privateKeyPath, String publicKeyPath) {
    try{
      generatePemKey(algorithm,password,keySize);

      /*
       * write private key pem to file
       */
      byte[] privateKeyBytes = privateKey.getEncoded();
      //    Convert private key to PKCS1:
      PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
      ASN1Encodable encodable = pkInfo.parsePrivateKey();
      ASN1Primitive primitive = encodable.toASN1Primitive();
      byte[] privateKeyPKCS1 = primitive.getEncoded();
      //    Convert private key in PKCS1 to PEM:
      PemObject pemObject = new PemObject(algorithm + " PRIVATE KEY", privateKeyPKCS1);
      StringWriter stringWriter = new StringWriter();
      PemWriter pemWriter = new PemWriter(stringWriter);
      pemWriter.writeObject(pemObject);
      pemWriter.close();
      String pemStringPrivateKey = stringWriter.toString();
      writeToFile(privateKeyPath, pemStringPrivateKey.getBytes());

      /*
       * write public key pem to file
       */
      byte[] pubBytes = publicKey.getEncoded();
      //    Convert public key to PKCS1:
      SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(pubBytes);
      ASN1Primitive primitivePub = spkInfo.parsePublicKey();
      byte[] publicKeyPKCS1 = primitivePub.getEncoded();
      //    Convert public key in PKCS1 to PEM:
      PemObject pemObjectPub = new PemObject(algorithm + " PUBLIC KEY", publicKeyPKCS1);
      StringWriter stringWriterPub = new StringWriter();
      PemWriter pemWriterPub = new PemWriter(stringWriterPub);
      pemWriterPub.writeObject(pemObjectPub);
      pemWriterPub.close();
      String pemStringPublicKey = stringWriterPub.toString();
      writeToFile(publicKeyPath, pemStringPublicKey.getBytes());

    }
    catch (Exception e){
      System.out.println("can not write keys to file.");
      return false;
    }
    return true;
  }
  public static boolean generatePemKey(String algorithm,String password, int keySize) {

    try {
      Security.addProvider(new BouncyCastleProvider());
      KeyPairGenerator keygen = KeyPairGenerator.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
      keygen.initialize(keySize, new SecureRandom());
      KeyPair keypair = keygen.generateKeyPair();
      setPrivateKey(keypair.getPrivate());
      setPublicKey(keypair.getPublic());
    } catch (NoSuchAlgorithmException e) {
      System.out.println("error : No Such Algorithm");
      return false;

    } catch (NoSuchProviderException e) {
      System.out.println("error : No Such Provider");
      return false;
    }
    return true;
  }

  public static String encrypt(String cipherType,String rawData) {

    try {
      if(publicKey == null) {
        return null;
      }
      Cipher encrypt = Cipher.getInstance(cipherType);
      encrypt.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encrypted = encrypt.doFinal(rawData.getBytes());
      return new String(Base64.getEncoder().encode(encrypted), StandardCharsets.UTF_8);

    } catch (Exception e) {
      return null;
    }
  }

  public static PrivateKey getPrivateKey() {
    return privateKey;
  }

  private static void setPrivateKey(PrivateKey prvKey) {
    privateKey = prvKey;
  }

  public static PublicKey getPublicKey() {
    return publicKey;
  }
  private static void setPublicKey(PublicKey pubKey) {
    publicKey = pubKey;
  }

  public static void writeToFile(String filePath, byte[] data) {

    try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){

      fileOutputStream.write(data);
      fileOutputStream.flush();
    }
    catch (Exception e) {
      return;
    }
  }
}
