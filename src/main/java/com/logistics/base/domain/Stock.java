package com.logistics.base.domain;

import java.time.LocalDate;

public record Stock(
    Long id,
    String barcode,
    LocalDate expirationDate,
    Product product,
    StorageUnit storageUnit) {

    public static final class Builder {
        private Long id;
        private String barcode;
        private LocalDate expirationDate;
        private Product product;
        private StorageUnit storageUnit;
        private Builder() {}

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }
        public Builder expirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }
        public Builder product(Product product) {
            this.product = product;
            return this;
        }
        public Builder storageUnit(StorageUnit storageUnit) {
            this.storageUnit = storageUnit;
            return this;
        }

        public Stock build() {
            return new Stock(
                id,
                barcode,
                expirationDate,
                product,
                storageUnit);
        }
    }

    public Builder toBuilder() {
        return new Builder()
            .id(id)
            .barcode(barcode)
            .expirationDate(expirationDate)
            .product(product)
            .storageUnit(storageUnit);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }
}
