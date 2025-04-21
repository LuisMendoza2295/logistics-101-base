package com.logistics.base.controller;

import io.quarkus.logging.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.logistics.base.util.ITUtils.PRODUCT_UUID;
import static com.logistics.base.util.ITUtils.readFileAsString;
import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
@TestHTTPEndpoint(ProductController.class)
public class ProductControllerIT {

  @Test
  @DisplayName("Get product by UUID")
  void test() {
    given()
      .pathParam("uuid", PRODUCT_UUID)
      .when()
      .get("/{uuid}")
      .then()
      .statusCode(200);
  }

  @Test
  @DisplayName("Insert product")
  void testInsert() throws IOException {
    String productJson = readFileAsString("payload/insert-product.json");
    Log.infof("Product JSON: %s", productJson);
    given()
      .contentType(ContentType.JSON)
      .body(productJson)
      .when()
      .post("/")
      .then()
      .statusCode(201);
  }
}
