package com.logistics.base.controller;

import com.logistics.base.controller.mapper.ProductWebMapper;
import com.logistics.base.controller.mapper.StockWebMapper;
import com.logistics.base.domain.aggregate.LogisticAggregate;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.logistics.base.util.TestUtils.PRODUCT;
import static com.logistics.base.util.TestUtils.PRODUCT_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusComponentTest
public class ProductControllerTest {

  @Inject
  ProductController productController;

  @InjectMock
  LogisticAggregate logisticAggregate;
  @Inject
  ProductWebMapper productWebMapper;
  @Inject
  StockWebMapper stockWebMapper;

  @Test
  @DisplayName("Given uuid then return HTTP 200 and product")
  void testGetProduct200() {
    when(logisticAggregate.findByProductUuid(PRODUCT_UUID.toString())).thenReturn(PRODUCT);

    Response response = productController.getProduct(PRODUCT_UUID.toString());

    assertThat(response)
            .isNotNull()
            .extracting(Response::getStatus, Response::getEntity)
            .containsExactly(200, productWebMapper.toProductDTO(PRODUCT));
  }
}
