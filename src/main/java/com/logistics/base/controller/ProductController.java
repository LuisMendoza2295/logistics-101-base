package com.logistics.base.controller;

import com.logistics.base.controller.dto.ProductDTO;
import com.logistics.base.controller.mapper.ProductControllerMapper;
import com.logistics.base.domain.LogisticAggregate;
import com.logistics.base.domain.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;

@Path("/product")
public class ProductController {

    @Inject
    LogisticAggregate logisticAggregate;
    @Inject
    ProductControllerMapper productControllerMapper;

    @GET
    @Path("{uuid}")
    @Produces(APPLICATION_JSON)
    public Response getProduct(String uuid) {
        Product product = logisticAggregate.findByProductUuid(uuid);
        return Response.ok(productControllerMapper.toProductDTO(product)).build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response createProduct(ProductDTO productDTO) {
        Product product = logisticAggregate.persistProduct(productControllerMapper.toProduct(productDTO));
        return Response.status(CREATED).entity(productControllerMapper.toProductDTO(product)).build();
    }
}

