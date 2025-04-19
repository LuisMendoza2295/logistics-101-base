package com.logistics.base.infrastructure.persistence;

import com.logistics.base.infrastructure.persistence.entity.ProductEntity;
import com.logistics.base.infrastructure.persistence.mapper.ProductEntityMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.logistics.base.util.TestUtils.PRODUCT_ID_NULL;
import static com.logistics.base.util.TestUtils.PRODUCT_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ProductPanacheRepositoryIT {

  @Inject
  ProductPanacheRepository repository;
  @Inject
  ProductEntityMapper mapper;

  @Test
  @TestTransaction
  @DisplayName("Given default product insert successfully")
  void testInsertDefaultProduct() {
    ProductEntity productEntity = mapper.toProductEntity(PRODUCT_ID_NULL);
    repository.persist(productEntity);

    assertNotNull(productEntity);
    assertEquals(PRODUCT_UUID.toString(), productEntity.uuid());
  }

  @Test
  @TestTransaction
  @DisplayName("Given default product throw error when inserting duplicate name product")
  void testInsertDuplicateNameProduct() {
    ProductEntity productEntity = mapper.toProductEntity(PRODUCT_ID_NULL);
    repository.persist(productEntity);

    assertNotNull(productEntity);
    assertEquals(PRODUCT_UUID.toString(), productEntity.uuid());

    ProductEntity duplicateProductEntity = mapper.toProductEntity(PRODUCT_ID_NULL.toBuilder()
      .uuid(UUID.randomUUID())
      .build());
    assertThrows(ConstraintViolationException.class, () -> repository.persist(duplicateProductEntity));
  }

  @Test
  @TestTransaction
  @DisplayName("Insert default product and get it by uuid")
  void testGetProduct() {
    ProductEntity productEntity = mapper.toProductEntity(PRODUCT_ID_NULL);
    repository.persist(productEntity);

    Optional<ProductEntity> fetchedProductEntity = repository.findByUuid(PRODUCT_UUID.toString());
    assertThat(fetchedProductEntity)
      .isPresent()
      .hasValueSatisfying(
        entity -> assertThat(entity.uuid())
          .isEqualTo(PRODUCT_UUID.toString()));
  }
}
