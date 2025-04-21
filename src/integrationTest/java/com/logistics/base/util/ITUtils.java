package com.logistics.base.util;

import java.io.IOException;
import java.io.InputStream;

public class ITUtils {

  private static final ClassLoader CLASS_LOADER = ITUtils.class.getClassLoader();

  private ITUtils() {
  }

  public static final String PRODUCT_UUID = "9957f784-74c5-46ed-92bb-22d0e8c9d281";

  public static String readFileAsString(String filePath) throws IOException {
    return new String(readFile(filePath));
  }

  public static byte[] readFile(String filePath) throws IOException {
    try (InputStream inputStream = CLASS_LOADER.getResourceAsStream(filePath)) {
      if (inputStream == null) {
        throw new IOException("File empty: " + filePath);
      }
      return inputStream.readAllBytes();
    }
  }
}
