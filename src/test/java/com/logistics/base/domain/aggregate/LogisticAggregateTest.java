package com.logistics.base.domain.aggregate;

import com.logistics.base.domain.model.Product;
import com.logistics.base.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.logistics.base.util.TestUtils.PRODUCT;
import static com.logistics.base.util.TestUtils.PRODUCT_UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogisticAggregateTest {

  @InjectMocks
  final LogisticAggregate logisticAggregate = new LogisticService();
  @Mock
  ProductRepository productRepository;

  @Test
  @DisplayName("Given uuid then return product")
  void testFindProductByProductUuid() {
    when(productRepository.findByUuid(PRODUCT_UUID.toString())).thenReturn(Optional.of(PRODUCT));

    Product result = logisticAggregate.findByProductUuid(PRODUCT_UUID.toString());

    assertNotNull(result);
    assertEquals(PRODUCT, result);
  }
}
