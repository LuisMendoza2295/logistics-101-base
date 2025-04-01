package com.logistics.base.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.logistics.base.util.TestUtils.PRODUCT;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

  @Test
  @DisplayName("Given now date then generate stock")
  void testGenerateStock() {
    LocalDate now = LocalDate.now();

    Stock stock = PRODUCT.generateStock(now);

    assertNotNull(stock);
    assertEquals(PRODUCT, stock.product());
    assertEquals(now, stock.expirationDate());
  }

  @Test
  @DisplayName("Given null dimensions then throw exception")
  void testNullDimensions() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> PRODUCT.toBuilder()
      .dimensions(null)
      .build());
    assertEquals("Product dimensions cannot be null", exception.getMessage());
  }

  @ParameterizedTest(name = "Given product with Dimensions({0},{1},{2}), then shouldPass?: {3}")
  @CsvSource({
    "0, 1, 1, false",
    "1, 0, 1, false",
    "0.1, 0.1, 0.1, true",
    "0.9999999999, 0.9999999999, 0.9999999999, true",
    "1, 1, 1, true"
  })
  void testProductDimensions(String widthString, String heightString, String lengthString, String passString) {
    BigDecimal width = new BigDecimal(widthString);
    BigDecimal height = new BigDecimal(heightString);
    BigDecimal length = new BigDecimal(lengthString);
    boolean pass = Boolean.parseBoolean(passString);

    Dimensions dimensions = Dimensions.builder()
      .width(width)
      .height(height)
      .length(length)
      .build();
    if (pass) {
      Product product = PRODUCT.toBuilder()
        .dimensions(dimensions)
        .build();
      assertNotNull(product);
    } else {
      Exception exception = assertThrows(IllegalArgumentException.class, () -> PRODUCT.toBuilder()
        .dimensions(dimensions)
        .build());
      assertEquals("Value cannot be zero or negative", exception.getMessage());
    }
  }
}
