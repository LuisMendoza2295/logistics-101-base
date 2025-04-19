package com.logistics.base.controller;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
@TestHTTPEndpoint(ProductController.class)
public class ProductControllerIT {

  static final String UUID = "9957f784-74c5-46ed-92bb-22d0e8c9d281";

  @Test
  @DisplayName("Product IT")
  void test() {
    given().pathParam("uuid", UUID)
      .when()
      .get("/{uuid}")
      .then()
      .statusCode(200);
  }
}
