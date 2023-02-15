package com.soshiant.springbootexample.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.soshiant.springbootexample.Application;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import static com.soshiant.springbootexample.util.AppConstants.*;


/**
 *
 * @author Hamid.Ghorbani
 *
 */

@Slf4j
public class FileUtils {

  private FileUtils() {
  }

  public static String loadFile(String filePath) throws IOException {

    Path path = Paths.get(filePath);
    try {
      byte[] bytes = Files.readAllBytes(path);
      return new String(bytes, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error("couldn't read file [{}]", filePath);
      throw e;
    }
  }

  public static void writeToFile(String filePath, byte[] data) throws IOException {

    try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){

      fileOutputStream.write(data);
      fileOutputStream.flush();
    }
    catch (Exception e) {
      log.error("couldn't write to file, file path :[{}]", filePath);
      throw e;
    }
  }

  public static Map<String,String> loadErrorCodesFile(String processorName) {

    Map<String,String> errorsMap = new HashMap<>();
    try {
      String filePath = ERROR_CODE_FILE_PREFIX +
              processorName.toLowerCase() +
              ERROR_CODE_FILE_POSTFIX;

      errorsMap = loadErrorCodesFileWithFileReader(filePath);
      if(errorsMap == null){
        errorsMap = loadErrorCodesFileWithInputStream(filePath);
      }

    } catch (Exception e){
      log.error("loadErrorCodesFile for processor [{}] exception: [{}]",
              processorName, e.getMessage());
    }
    return errorsMap;
  }

  /*
   * this method work in Docker image, but doesn't work in regular jar file
   */
  public static Map<String,String> loadErrorCodesFileWithInputStream(String filePath) {

    try {
      Resource resource = new ClassPathResource(filePath);
      Map<String,String> errorsMap = new BufferedReader(
              new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
      ).lines()
              .collect(Collectors.toMap(k ->k.split("=")[0], v -> v.split("=")[1]));

      log.debug("loadErrorCodesFileWithInputStream for filePath [{}] loaded successfully. data [{}]",
              filePath, errorsMap);

      return errorsMap;

    } catch (FileNotFoundException e){
      log.error("loadErrorCodesFileWithInputStream for filePath [{}] FileNotFoundException : [{}]",
              filePath, e.getMessage());
      return null;
    } catch (IOException e) {
      log.error("loadErrorCodesFileWithInputStream for filePath [{}] IOException: [{}]",
              filePath, e.getMessage());
      return null;
    } catch (Exception e){
      log.error("loadErrorCodesFileWithInputStream for filePath [{}] exception: [{}]",
              filePath, e.getMessage());
      return null;
    }
  }

  /*
   * this method doesn't work in Docker image, but work in regular jar file
   */
  public static Map<String,String> loadErrorCodesFileWithFileReader(String filePath) {

    try {
      File file = ResourceUtils.getFile(filePath);
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
        Map<String, String> errorsMap =
                bufferedReader
                        .lines()
                        .collect(Collectors.toMap(k -> k.split("=")[0], v -> v.split("=")[1]));

        log.debug("loadErrorCodesFileWithFileReader for filePath [{}] loaded successfully. data [{}]",
                filePath, errorsMap);
        return errorsMap;
      }

    } catch (FileNotFoundException e){
      log.error("loadErrorCodesFileWithFileReader for filePath [{}] FileNotFoundException : [{}]",
              filePath, e.getMessage());
      return null;
    } catch (Exception e){
      log.error("loadErrorCodesFileWithFileReader for filePath [{}] exception: [{}]",
              filePath, e.getMessage());
      return null;
    }
  }

  public static String getPackageOfProcessor(String processorName, String folderName){
    try{
      String basePackageName = getBasePackageOfProcessor(processorName) ;
      if(StringUtils.isBlank(basePackageName)){
        log.error("getPackageOfProcessor didn't find any package for processor [{}]", processorName);
        return null;
      }
      return basePackageName + DOT + folderName;
    } catch (Exception e){
      log.error("getPackageOfProcessor exception, processorName [{}], message [{}].",
              processorName, e.getMessage());
      return null;
    }
  }

  public static String getBasePackageOfProcessor(String processorName){
    return Application.class.getPackage().getName() + DOT + PROCESSOR_FOLDER_NAME +
            DOT + processorName.toLowerCase();
  }

  public static String getPackageOfCoreTokenService(){
    return Application.class.getPackage().getName() + DOT + CORE + DOT +
            SERVICE_IMPL_FOLDER_NAME.toLowerCase();
  }

  public static List<Class<?>> findClass(String packageName, String wordInfileName){
    try{
      List<String> classList = findClassesInPackage(packageName);
      if(CollectionUtils.isEmpty(classList)){
        log.error("findClass, classList is empty for processor {}", packageName);
        return null;
      }
      List<Class<?>> selectedClassesList = new ArrayList<>();
      for (String className : classList) {
        if(className.toLowerCase().contains(wordInfileName.toLowerCase()))  {
          log.debug("findClass for package [{}] is [{}]", packageName, className );
          selectedClassesList.add(Class.forName(className));
        }
      }
      return selectedClassesList;

    } catch (Exception e){
      log.error("findClass exception {}",e.getMessage());
      return null;
    }

  }

  public static List<String> findClassesInPackage(String packageName) {
    try{
      log.debug("findClassesInPackage, package is:{}",packageName);
      if(StringUtils.isEmpty(packageName)){
        return Collections.emptyList();
      }
      return findAllClassesInPackageUsingGuava(packageName);

    } catch (Exception e){
      log.error("findClassesInPackage for packageName [{}], exception {}", packageName, e.getMessage());
    }
    return Collections.emptyList();
  }

  public static List<String> findAllClassesInPackageUsingGuava(String packageName) {

    try {
      ClassPath classPath = ClassPath.from(Application.class.getClassLoader());
      ImmutableSet<ClassPath.ClassInfo> classes = classPath.getAllClasses();
      if(classes == null || classes.isEmpty()){
        log.error("findAllClassesInPackageUsingGuava, getAllClasses is empty for package[{}]",
                packageName);
        return Collections.emptyList();
      }
      List<String> classList = new ArrayList<>();
      for (ClassPath.ClassInfo ci : classes) {
        if (ci.getPackageName().contains(packageName)) {
          log.debug("findAllClassesInPackageUsingGuava,class  found[{}]", ci.getPackageName());
          log.debug("findAllClassesInPackageUsingGuava,class  found[{}]", ci.getName());
          classList.add(packageName + "." + ci.getSimpleName());
        }
      }
      if (CollectionUtils.isEmpty(classList)) {
        log.error("findAllClassesInPackageUsingGuava, didn't find any class under package[{}]",
                packageName);
        return Collections.emptyList();
      }
      log.debug("findAllClassesInPackageUsingGuava,classes under package[{}] is [{}] ",
              packageName, classList);
      return classList;

    } catch (IOException e) {
      log.error("findAllClassesInPackageUsingGuava exception, package[{}], message[{}]",
              packageName, e.getMessage() );
      return Collections.emptyList();
    }
  }
  public static String getResourceRealPath(String resourceName) {
    try {
      return   System.getProperty("user.dir") + RESOURCES + resourceName;
    } catch (Exception e) {
      log.error("getResourceRealPath exception, resourceName[{}], message[{}]", resourceName, e.getMessage());
      return null;
    }
  }

  public static String getRealPath(String path) {
    try {
      path = StringUtils.replace(path,".","/");
      path = StringUtils.replace(path,"\\","/");
      if(!path.contains("src")){
        path = System.getProperty("user.dir") + "/src/main/java/" + path;
      }
      return Paths.get(path).toAbsolutePath().toString();

    } catch (Exception e) {
      log.error("getRealPath exception, path[{}], message[{}]", path, e.getMessage());
      return null;
    }
  }

  public static String getAbsolutePath(String path) {
    try {
      return Paths.get(path).toAbsolutePath().toString();

    } catch (Exception e) {
      log.error("getRealPath exception, path[{}], message[{}]", path, e.getMessage());
      return null;
    }
  }

  public static boolean createDirectory(String path) {
    try {
      Files.createDirectories(Paths.get(path));
      return true;
    } catch (IOException e) {
      log.error("createDirectory exception, path[{}], message[{}]", path, e.getMessage());
      return false;
    }
  }

  public static String readFile(String filePath) {

    final StringBuilder fileData = new StringBuilder();
    try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
      stream.forEach(line -> fileData.append(line).append(System.lineSeparator()));

    } catch (IOException e) {
      log.error("readFile exception, filePath[{}], message[{}]", filePath, e.getMessage());
      e.printStackTrace();
      return null;
    }
    return fileData.toString();
  }

  public static boolean writeToFile(String fileContent, String filePath) {
    try {
      if(StringUtils.isBlank(fileContent)){
        log.error("writeToFile content is empty, filePath[{}]", filePath);
        return false;
      }
      Files.write(Paths.get(filePath), fileContent.getBytes());
      return true;
    } catch (IOException e){
      log.error("writeToFile IOException, filePath[{}], message[{}]", filePath, e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

}
