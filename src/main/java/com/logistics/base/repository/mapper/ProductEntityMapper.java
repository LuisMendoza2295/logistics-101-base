package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.Product;
import com.logistics.base.repository.entity.ProductEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ProductEntityMapper {

    public ProductEntity toProductEntity(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.uuid = product.uuid().toString();
        productEntity.name = product.name();
        productEntity.width = product.dimensions().width();
        productEntity.height = product.dimensions().height();
        productEntity.length = product.dimensions().length();
        productEntity.netWeight = product.netWeight();
        productEntity.grossWeight = product.grossWeight();

        return productEntity;
    }

    public Product toProduct(ProductEntity productEntity) {
        return new Product(
            UUID.fromString(productEntity.uuid),
            productEntity.name,
            new Dimensions(
                productEntity.width,
                productEntity.height,
                productEntity.length),
            productEntity.netWeight,
            productEntity.grossWeight);
    }
}
