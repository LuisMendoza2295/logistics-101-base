package com.logistics.base.util;

import com.logistics.base.domain.model.Dimensions;
import com.logistics.base.domain.model.Product;

import java.math.BigDecimal;
import java.util.UUID;

import static java.math.BigDecimal.ONE;

public class TestUtils {

    public static final Long PRODUCT_ID = 1L;
    public static final String PRODUCT_UUID = UUID.randomUUID().toString();
    public static final String PRODUCT_NAME = "Product A";
    public static final Dimensions PRODUCT_DIMENSIONS = new Dimensions(ONE, ONE, ONE);
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
}
