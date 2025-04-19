package com.logistics.base.controller;

import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/hello")
public class HelloWorldController {

  @ConfigProperty(name = "logistic.info")
  String info;

  @GET
  public Response hello() {
    Log.infof("Hello from HelloController %s!", info);
    String profiles = String.join(",", ConfigUtils.getProfiles());
    Log.infof("Profiles: %s", profiles);
    return Response.ok(info).build();
  }
}
