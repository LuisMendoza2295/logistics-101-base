package com.logistics.base.controller.healthcheck;

import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class SimpleHealthCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    String profiles = String.join(",", ConfigUtils.getProfiles());
    Log.infof("Profiles: %s", profiles);
    return HealthCheckResponse.named("Service is up")
      .withData("profiles", profiles)
      .up()
      .build();
  }
}
