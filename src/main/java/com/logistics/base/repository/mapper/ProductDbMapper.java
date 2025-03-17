package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.Product;
import com.logistics.base.repository.entity.ProductEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductDbMapper {

    public ProductEntity toProductEntity(Product product) {
        ProductEntity temp = new ProductEntity();
        temp.setId(product.id());

        ProductEntity productEntity = temp.getAttachedEntity();
        productEntity.setUuid(product.uuid().toString());
        productEntity.setName(product.name());
        productEntity.setWidth(product.dimensions().width());
        productEntity.setHeight(product.dimensions().height());
        productEntity.setLength(product.dimensions().length());
        productEntity.setNetWeight(product.netWeight());
        productEntity.setGrossWeight(product.grossWeight());
        return productEntity.getAttachedEntity();
    }

    public Product toProduct(ProductEntity productEntity) {
        return Product.builder()
            .id(productEntity.id())
            .uuid(productEntity.uuid())
            .name(productEntity.name())
            .dimensions(new Dimensions(
                productEntity.width(),
                productEntity.height(),
                productEntity.length()))
            .netWeight(productEntity.netWeight())
            .grossWeight(productEntity.grossWeight())
            .build();
    }
}
