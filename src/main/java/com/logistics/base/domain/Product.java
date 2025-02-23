package com.logistics.base.domain;

import java.util.UUID;

public record Product(
        UUID uuid,
        String name,
        VolumeDimension volumeDimension,
        WeightDimension weightDimension) {

}
