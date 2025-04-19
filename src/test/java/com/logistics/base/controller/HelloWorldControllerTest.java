package com.logistics.base.controller;

import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusComponentTest
public class HelloWorldControllerTest {

    @Inject
    HelloWorldController helloWorldController;

    @Test
    @DisplayName("Return HTTP 200")
    void testHello() {
        Response response = helloWorldController.hello();
        assertThat(response)
                .isNotNull()
                .extracting(Response::getStatus)
                .isEqualTo(200);
    }
}
