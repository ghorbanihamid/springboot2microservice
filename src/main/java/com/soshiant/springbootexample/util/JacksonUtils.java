package com.soshiant.springbootexample.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class JacksonUtils {

  public static final String JSON_START_TAG = "{";
  public static final String XML_START_TAG = "<";

  private JacksonUtils() {

  }

  public static String toJson(Object obj) {
    try {
      return toJsonString(obj, false);
    } catch (Exception e) {
      return null;
    }
  }

  public static String toJsonString(Object obj) throws Exception {
    return toJsonString(obj, false);
  }

  public static String toJsonString(Object obj,boolean excludeNullFields) throws Exception {
    String objJson = "";
    try {
      if (obj != null) {
        ObjectMapper mapper = new ObjectMapper();
        // configure ObjectMapper to exclude null fields while serializing
        if(excludeNullFields) {
          mapper.setSerializationInclusion(Include.NON_NULL);
        }
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
      }
      return objJson;

    } catch (Exception e) {
      log.error("JsonUtil.toJsonString(), Failed to convert input object to JSON String!" + obj.toString());
      throw e;
    }
  }

  public static Object toObject(String inputStr,Class<?> clazz) {
    Object resultObject = null;
    try {
      if (StringUtils.isBlank(inputStr)) {
        return null;
      }
      if(inputStr.startsWith(JSON_START_TAG)) { // means input is json
        resultObject = new ObjectMapper().readValue(inputStr, clazz);

      } else if(inputStr.startsWith(XML_START_TAG)) { // means input is xml
        resultObject = new XmlMapper().readValue(inputStr, Object.class);
      }

    } catch (Exception e) {
      log.error("toObject exception, input String [{}] to specified Object, exception: {}",
                inputStr,e);
      e.printStackTrace();
    }
    return resultObject;
  }

  public static Map<String, String> toMap(String inputStr) {

    try {
      if (StringUtils.isBlank(inputStr)) {
        return null;
      }
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode treeNode = objectMapper.readTree(inputStr);
      Map<String, String> map = new HashMap<>();
      treeNode=treeNode.findValue("sets");
      for (Iterator<JsonNode> it = treeNode.elements(); it.hasNext(); ) {
        JsonNode node = it.next();
        String theValueYouWant=node.findValue("translations").textValue();
      }
      return map;
    } catch (Exception e) {
      log.error("toObject exception, input String [{}] to specified Object, exception: {}",
                inputStr,e);
      e.printStackTrace();
      return null;
    }
  }

  public static String toXMLSting(Object obj) {
    try {
      if (obj == null) {
        return null;
      }
      ObjectMapper mapper = new XmlMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      String result =  mapper.writeValueAsString(obj);
      return AppConstants.XML_DECLARATION + result;

    } catch (Exception e) {
      log.error("toXML: Failed to convert input object [{}] to xml!", obj);
      return null;
    }

  }
}
