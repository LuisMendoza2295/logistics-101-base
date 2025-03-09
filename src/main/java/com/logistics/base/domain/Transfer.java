package com.logistics.base.domain;

import java.util.Map;
import java.util.UUID;

public record Transfer(
    UUID uuid,
    StorageUnit source,
    StorageUnit target,
    Map<Product, Integer> productsWithQty) {
}
