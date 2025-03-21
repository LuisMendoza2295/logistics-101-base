package com.logistics.base.repository.panache;

import com.logistics.base.repository.entity.ProductEntity;
import com.logistics.base.repository.mapper.ProductDbMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.logistics.base.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ProductPanacheRepositoryTest {

    @Inject
    ProductPanacheRepository repository;
    @Inject
    ProductDbMapper mapper;

    @Test
    @TestTransaction
    @DisplayName("Given default product insert successfully")
    void testInsertDefaultProduct() {
        ProductEntity productEntity = mapper.toProductEntity(PRODUCT_ID_NULL);
        repository.persist(productEntity);

        assertNotNull(productEntity);
        assertEquals(PRODUCT_UUID, productEntity.uuid());
    }

    @Test
    @TestTransaction
    @DisplayName("Given default product throw error when inserting duplicate name product")
    void testInsertDuplicateNameProduct() {
        ProductEntity productEntity = mapper.toProductEntity(PRODUCT_ID_NULL);
        repository.persist(productEntity);

        assertNotNull(productEntity);
        assertEquals(PRODUCT_UUID, productEntity.uuid());

        ProductEntity duplicateProductEntity = mapper.toProductEntity(PRODUCT_ID_NULL.toBuilder()
            .uuid(UUID.randomUUID())
            .build());
        assertThrows(ConstraintViolationException.class, () -> repository.persist(duplicateProductEntity));
    }

    @Test
    @TestTransaction
    @DisplayName("Insert default product and get it by uuid")
    void testGetProduct() {
        ProductEntity productEntity = mapper.toProductEntity(PRODUCT.toBuilder().id(null).build());
        repository.persist(productEntity);

        Optional<ProductEntity> fetchedProductEntity = repository.findByUuid(PRODUCT_UUID);
        assertThat(fetchedProductEntity)
            .isPresent()
            .hasValueSatisfying(
                entity -> assertThat(entity.uuid())
                    .isEqualTo(PRODUCT_UUID));
    }
}
