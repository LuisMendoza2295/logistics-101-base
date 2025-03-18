package com.logistics.base.controller.mapper;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.base.controller.dto.ProductDTO;
import com.logistics.base.domain.model.Dimensions;
import com.logistics.base.domain.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;

@ApplicationScoped
public class ProductWebMapper extends KeyDeserializer {

    @Inject
    ObjectMapper objectMapper;

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

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return objectMapper.readValue(key, ProductDTO.class);
    }
}
