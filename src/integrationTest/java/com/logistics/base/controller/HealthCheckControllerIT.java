package com.logistics.base.controller;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusIntegrationTest
public class HealthCheckControllerIT {

  @Test
  @DisplayName("Health check IT")
  void testServiceUp() {
    when()
      .get("/q/health/live")
      .then()
      .statusCode(200)
      .body("status", equalTo("UP"))
      .body("checks[0].data.profiles", equalTo("integration"));
  }
}
