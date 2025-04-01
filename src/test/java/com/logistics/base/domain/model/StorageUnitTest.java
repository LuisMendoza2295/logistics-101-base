package com.logistics.base.domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static com.logistics.base.util.TestUtils.EMPTY_STORAGE_UNIT;
import static com.logistics.base.util.TestUtils.PRODUCT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StorageUnitTest {

  @ParameterizedTest(name = "Given storage with Dimensions(10,10,10) and {0} Products with Dimensions({1},{2},{3}), then shouldPass?: {4}")
  @CsvSource({
    "10, 1, 1, 1, false",
    "10, 1, 1, 0.99, false",
    "10, 1, 0.99, 1, false",
    "10, 0.99, 1, 1, false",
    "101, 1, 1, 1, false",
    "5, 1, 1, 1, true",
    "10, 0.99, 0.99, 0.99, true"
  })
  void testAddProductByVolume(String quantityString,
                              String productWidthString, String productHeightString, String productLengthString,
                              String passString) {
    boolean pass = Boolean.parseBoolean(passString);
    BigDecimal productWidth = new BigDecimal(productWidthString);
    BigDecimal productHeight = new BigDecimal(productHeightString);
    BigDecimal productLength = new BigDecimal(productLengthString);
    int quantity = Integer.parseInt(quantityString);

    Product product = PRODUCT.toBuilder()
      .dimensions(Dimensions.builder()
        .width(productWidth)
        .height(productHeight)
        .length(productLength)
        .build())
      .build();

    StorageUnit storageUnit = EMPTY_STORAGE_UNIT;
    if (pass) {

      StorageUnit storageUnitWithProduct = storageUnit.addProduct(product, quantity);
      assertNotNull(storageUnitWithProduct);
    } else {
      Exception exception = assertThrows(RuntimeException.class, () -> storageUnit.addProduct(product, quantity));
      assertNotNull(exception);
    }
  }
}
