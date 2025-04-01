package com.logistics.base.domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DimensionsTest {

  @ParameterizedTest(name = "Given width {0}, height {1}, length {2}, then shouldPass?: {3}")
  @CsvSource({
    "0, 0, 0, true",
    "0, 1, 1, true",
    "1, 0, 1, true",
    "-1, 1, 1, false",
    "0.1, 0.1, 0.1, true",
    "0.9999999999, 0.9999999999, 0.9999999999, true",
    "1, 1, 1, true"
  })
  void testDimensions(String widthString, String heightString, String lengthString, String passString) {
    BigDecimal width = new BigDecimal(widthString);
    BigDecimal height = new BigDecimal(heightString);
    BigDecimal length = new BigDecimal(lengthString);
    boolean pass = Boolean.parseBoolean(passString);

    if (pass) {
      Dimensions dimensions = Dimensions.builder()
        .width(width)
        .height(height)
        .length(length)
        .build();
      BigDecimal volume = dimensions.getVolume();
      assertNotNull(dimensions);
      assertNotNull(volume);
    } else {
      Exception exception = assertThrows(IllegalArgumentException.class, () -> Dimensions.builder()
        .width(width)
        .height(height)
        .length(length)
        .build());
      assertEquals("Value cannot be negative", exception.getMessage());
    }
  }
}
