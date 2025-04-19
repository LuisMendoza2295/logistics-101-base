package com.logistics.base.controller;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

@QuarkusIntegrationTest
@TestHTTPEndpoint(HelloWorldController.class)
public class HelloWorldControllerIT {

  @Test
  @DisplayName("Test Hello IT")
  void testHello() {
    when()
      .get()
      .then()
      .statusCode(200);
  }
}
