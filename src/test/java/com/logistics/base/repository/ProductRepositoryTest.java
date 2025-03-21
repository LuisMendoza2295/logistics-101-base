package com.logistics.base.repository;

import com.logistics.base.domain.model.Product;
import com.logistics.base.domain.repository.ProductRepository;
import com.logistics.base.repository.entity.ProductEntity;
import com.logistics.base.repository.mapper.ProductDbMapper;
import com.logistics.base.repository.panache.ProductPanacheRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.logistics.base.util.TestUtils.PRODUCT;
import static com.logistics.base.util.TestUtils.PRODUCT_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository = new ProductRepositoryImpl();
    @Mock
    ProductPanacheRepository productPanacheRepository;
    @Mock
    ProductDbMapper productDbMapper;

    @BeforeEach
    void setUp() {
        when(productDbMapper.toProductEntity(any(Product.class))).thenCallRealMethod();
        when(productDbMapper.toProduct(any(ProductEntity.class))).thenCallRealMethod();
    }

    @Test
    @DisplayName("Given uuid then return product")
    void testGetProduct() {
        ProductEntity productEntity = productDbMapper.toProductEntity(PRODUCT);
        when(productPanacheRepository.findByUuid(PRODUCT_UUID)).thenReturn(Optional.of(productEntity));

        Optional<Product> productOptional = productRepository.findByUuid(PRODUCT_UUID);
        assertThat(productOptional).isPresent().hasValue(PRODUCT);
    }
}
