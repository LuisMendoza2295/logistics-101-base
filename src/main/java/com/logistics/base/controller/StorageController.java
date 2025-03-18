package com.logistics.base.controller;

import com.logistics.base.controller.dto.GenerateStorageStockDTO;
import com.logistics.base.controller.dto.StockDTO;
import com.logistics.base.controller.dto.StorageUnitDTO;
import com.logistics.base.controller.mapper.StockWebMapper;
import com.logistics.base.controller.mapper.StorageWebMapper;
import com.logistics.base.domain.aggregate.LogisticAggregate;
import com.logistics.base.domain.model.StorageUnit;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

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
    StorageWebMapper storageWebMapper;
    @Inject
    StockWebMapper stockWebMapper;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getStorageUnits(@QueryParam("type") String type) {
        Set<StorageUnitDTO> storageUnitDTOs = logisticAggregate.findByType(type).stream()
            .map(storageUnit -> storageWebMapper.toStorageUnitDTO(storageUnit))
            .collect(Collectors.toSet());
        return Response.status(OK).entity(storageUnitDTOs).build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response createStorageUnit(StorageUnitDTO storageUnitDTO) {
        StorageUnit storageUnit = logisticAggregate.persistStorageUnit(storageWebMapper.toStorageUnit(storageUnitDTO));
        return Response.status(CREATED).entity(storageWebMapper.toStorageUnitDTO(storageUnit)).build();
    }

    @POST
    @Path("{uuid}/add-product")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response addProducts(String uuid, Set<String> barcodes) {
        StorageUnit storageUnit = logisticAggregate.addProducts(uuid, barcodes);
        return Response.status(OK).entity(storageWebMapper.toStorageUnitDTO(storageUnit)).build();
    }

    @POST
    @Path("{uuid}/generate")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response generateStock(String uuid, GenerateStorageStockDTO generateStorageStockDTO) {
        Set<StockDTO> stockDTOs = logisticAggregate.generateStocks(
                uuid,
                generateStorageStockDTO.productUUID(),
                generateStorageStockDTO.expirationDate(),
                generateStorageStockDTO.quantity())
            .stream()
            .map(stockWebMapper::toStockDTO).collect(Collectors.toSet());
        return Response.status(CREATED).entity(stockDTOs).build();
    }
}
