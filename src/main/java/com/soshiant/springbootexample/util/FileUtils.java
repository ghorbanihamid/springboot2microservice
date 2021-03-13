package com.soshiant.springbootexample.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

  public static String loadFile(String filePath) throws IOException {

    Path path = Paths.get(filePath);
    try {
      byte[] bytes = Files.readAllBytes(path);
      return new String(bytes, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error("couldn't read file");
      throw e;
    }
  }

  public static void writeToFile(String filePath, byte[] data) throws IOException {

    try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){

      fileOutputStream.write(data);
      fileOutputStream.flush();
    }
    catch (Exception e) {
      log.error("couldn't write to file");
      throw e;
    }
  }

}
