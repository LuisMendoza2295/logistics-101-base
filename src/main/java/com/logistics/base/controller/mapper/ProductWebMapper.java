package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.ProductDTO;
import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductWebMapper {

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
        return Product.builder()
            .uuid(productDTO.uuid())
            .name(productDTO.name())
            .dimensions(new Dimensions(
                productDTO.width(),
                productDTO.height(),
                productDTO.length()))
            .netWeight(productDTO.netWeight())
            .grossWeight(productDTO.grossWeight())
            .build();
    }
}
