package com.logistics.base.domain;

import java.util.List;
import java.util.UUID;

public record StorageUnit(
        UUID uuid,
        StorageType storageType,
        VolumeDimension volumeDimension,
        WeightDimension weightDimension,
        int maxUnits,
        StorageStatus storageStatus,
        List<Product> products) {
}
