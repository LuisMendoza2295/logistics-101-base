package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.ProductDTO;
import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductControllerMapper {

    public ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
            product.uuid().toString(),
            product.name(),
            product.dimensions().height(),
            product.dimensions().width(),
            product.dimensions().length(),
            product.netWeight(),
            product.grossWeight());
    }

    public Product toProduct(ProductDTO productDTO) {
        return new Product(
            productDTO.uuid(),
            productDTO.name(),
            new Dimensions(
                productDTO.width(),
                productDTO.height(),
                productDTO.length()),
            productDTO.netWeight(),
            productDTO.grossWeight());
    }
}
