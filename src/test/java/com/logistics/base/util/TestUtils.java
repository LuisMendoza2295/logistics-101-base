package com.logistics.base.util;

import com.logistics.base.domain.model.*;

import java.math.BigDecimal;
import java.util.UUID;

import static com.logistics.base.domain.model.StorageStatus.AVAILABLE;
import static com.logistics.base.domain.model.StorageType.NO_MOIST;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;

public class TestUtils {

  private TestUtils() {
  }

  public static final Long PRODUCT_ID = 1L;
  public static final UUID PRODUCT_UUID = UUID.randomUUID();
  public static final String PRODUCT_NAME = "Product A";
  public static final Dimensions PRODUCT_DIMENSIONS = Dimensions.builder()
    .width(ONE)
    .length(ONE)
    .height(ONE)
    .build();
  public static final BigDecimal NET_WEIGHT = ONE;
  public static final BigDecimal GROSS_WEIGHT = ONE;
  public static final Product PRODUCT = Product.builder()
    .id(PRODUCT_ID)
    .uuid(PRODUCT_UUID)
    .name(PRODUCT_NAME)
    .dimensions(PRODUCT_DIMENSIONS)
    .netWeight(NET_WEIGHT)
    .grossWeight(GROSS_WEIGHT)
    .build();
  public static final Product PRODUCT_ID_NULL = PRODUCT.toBuilder()
    .id(null)
    .build();
  public static final Long STORAGE_UNIT_ID = 1L;
  public static final UUID STORAGE_UNIT_UUID = UUID.randomUUID();
  public static final Dimensions STORAGE_UNIT_DIMENSIONS = Dimensions.builder()
    .width(TEN)
    .height(TEN)
    .length(TEN)
    .build();
  public static final BigDecimal STORAGE_UNIT_WEIGHT_CAPACITY = TEN;
  public static final int STORAGE_UNIT_MAX_UNITS = TEN.intValue();
  public static final StorageUnit EMPTY_STORAGE_UNIT = StorageUnit.builder()
    .id(STORAGE_UNIT_ID)
    .uuid(STORAGE_UNIT_UUID)
    .dimensions(STORAGE_UNIT_DIMENSIONS)
    .weightCapacity(STORAGE_UNIT_WEIGHT_CAPACITY)
    .maxUnits(STORAGE_UNIT_MAX_UNITS)
    .storageType(NO_MOIST)
    .storageStatus(AVAILABLE)
    .build();
}
