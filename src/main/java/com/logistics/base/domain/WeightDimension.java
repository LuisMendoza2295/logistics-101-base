package com.logistics.base.domain;

import java.math.BigDecimal;

public record WeightDimension(
        BigDecimal netWeight,
        BigDecimal grossWeight) implements Dimension {

    @Override
    public BigDecimal calculate() {
        return grossWeight;
    }
}
