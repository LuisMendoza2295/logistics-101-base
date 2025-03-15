package com.logistics.base.controller;

import com.logistics.base.controller.dto.CreateTransferDTO;
import com.logistics.base.controller.mapper.TransferControllerMapper;
import com.logistics.base.domain.LogisticAggregate;
import com.logistics.base.domain.Transfer;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;

@Path("/transfer")
public class TransferController {

    @Inject
    LogisticAggregate logisticAggregate;

    @Inject
    TransferControllerMapper transferControllerMapper;

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response transferStock(CreateTransferDTO createTransferDTO) {
        Transfer transfer = logisticAggregate.transferProduct(createTransferDTO.sourceStorageUUID(), createTransferDTO.targetStorageUUID(), createTransferDTO.barcodes());
        return Response.status(CREATED).entity(transferControllerMapper.toTransferDTO(transfer)).build();
    }
}
