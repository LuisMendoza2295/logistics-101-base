package com.logistics.base.repository;

import com.logistics.base.domain.model.Product;
import com.logistics.base.domain.repository.ProductRepository;
import com.logistics.base.infrastructure.persistence.entity.ProductEntity;
import com.logistics.base.infrastructure.persistence.mapper.ProductEntityMapper;
import com.logistics.base.infrastructure.persistence.ProductPanacheRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.logistics.base.util.TestUtils.*;
import static com.logistics.base.util.TestUtils.PRODUCT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

  @InjectMocks
  ProductRepository productRepository = new ProductRepositoryImpl();
  @Mock
  ProductPanacheRepository productPanacheRepository;
  @Mock
  ProductEntityMapper productEntityMapper;

  @BeforeEach
  void setUp() {
    when(productEntityMapper.toProductEntity(any(Product.class))).thenCallRealMethod();
    when(productEntityMapper.toProduct(any(ProductEntity.class))).thenCallRealMethod();
  }

  @Test
  @DisplayName("Given uuid then return product")
  void testGetProduct() {
    ProductEntity productEntity = productEntityMapper.toProductEntity(PRODUCT);
    when(productPanacheRepository.findByUuid(PRODUCT_UUID.toString())).thenReturn(Optional.of(productEntity));

    Optional<Product> productOptional = productRepository.findByUuid(PRODUCT_UUID.toString());
    assertThat(productOptional).isPresent().hasValue(PRODUCT);
  }

  @Test
  @DisplayName("Given default product insert successfully")
  void testInsertDefaultProduct() {
    doAnswer(invocation -> {
      ProductEntity argProductEntity = invocation.getArgument(0);
      argProductEntity.setId(PRODUCT_ID);
      return null;
    }).when(productPanacheRepository).persist(any(ProductEntity.class));

    Product product = productRepository.save(PRODUCT_ID_NULL);

    assertThat(product).isNotNull().isEqualTo(PRODUCT);
  }
}
