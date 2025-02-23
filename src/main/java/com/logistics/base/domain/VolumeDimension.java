package com.logistics.base.domain;

import java.math.BigDecimal;

public record VolumeDimension(
        BigDecimal width,
        BigDecimal height,
        BigDecimal length) implements Dimension {
    @Override
    public BigDecimal calculate() {
        return width.multiply(height).multiply(length);
    }
}
