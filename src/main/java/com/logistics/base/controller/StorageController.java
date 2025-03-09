package com.logistics.base.controller;

import com.logistics.base.controller.dto.ProductDTO;
import com.logistics.base.controller.dto.StorageUnitDTO;
import com.logistics.base.controller.mapper.ProductControllerMapper;
import com.logistics.base.controller.mapper.StorageControllerMapper;
import com.logistics.base.domain.LogisticAggregate;
import com.logistics.base.domain.Product;
import com.logistics.base.domain.StorageUnit;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.OK;

@Path("/storage")
public class StorageController {

    @Inject
    LogisticAggregate logisticAggregate;

    @Inject
    StorageControllerMapper storageControllerMapper;
    @Inject
    ProductControllerMapper productControllerMapper;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getStorageUnits(@QueryParam("type") String type) {
        Set<StorageUnitDTO> storageUnitDTOs = logisticAggregate.findByType(type).stream()
            .map(storageUnit -> storageControllerMapper.toStorageUnitDTO(storageUnit))
            .collect(Collectors.toSet());
        return Response.status(OK).entity(storageUnitDTOs).build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response createStorageUnit(StorageUnitDTO storageUnitDTO) {
        StorageUnit storageUnit = logisticAggregate.persist(storageControllerMapper.toStorageUnit(storageUnitDTO));
        return Response.status(CREATED).entity(storageControllerMapper.toStorageUnitDTO(storageUnit)).build();
    }

    @POST
    @Path("{uuid}/add-product")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response addProducts(String uuid, Set<ProductDTO> productDTOs) {
        Set<Product> products = productDTOs.stream()
            .map(productControllerMapper::toProduct)
            .collect(Collectors.toSet());
        StorageUnit storageUnit = logisticAggregate.addProducts(uuid, products);
        return Response.status(OK).entity(storageControllerMapper.toStorageUnitDTO(storageUnit)).build();
    }
}
