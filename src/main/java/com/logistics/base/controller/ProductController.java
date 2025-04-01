package com.logistics.base.controller;

import com.logistics.base.controller.dto.GenerateStockDTO;
import com.logistics.base.controller.dto.ProductDTO;
import com.logistics.base.controller.dto.StockDTO;
import com.logistics.base.controller.mapper.ProductWebMapper;
import com.logistics.base.controller.mapper.StockWebMapper;
import com.logistics.base.domain.aggregate.LogisticAggregate;
import com.logistics.base.domain.model.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;

@Path("/product")
public class ProductController {

  @Inject
  LogisticAggregate logisticAggregate;
  @Inject
  ProductWebMapper productWebMapper;
  @Inject
  StockWebMapper stockWebMapper;

  @GET
  @Path("{uuid}")
  @Produces(APPLICATION_JSON)
  public Response getProduct(String uuid) {
    Product product = logisticAggregate.findByProductUuid(uuid);
    return Response.ok(productWebMapper.toProductDTO(product)).build();
  }

  @POST
  @Produces(APPLICATION_JSON)
  @Consumes(APPLICATION_JSON)
  public Response createProduct(ProductDTO productDTO) {
    Product product = logisticAggregate.persistProduct(productWebMapper.toProduct(productDTO));
    return Response.status(CREATED).entity(productWebMapper.toProductDTO(product)).build();
  }

  @POST
  @Path("{uuid}/generate")
  @Produces(APPLICATION_JSON)
  @Consumes(APPLICATION_JSON)
  public Response generateStock(String uuid, GenerateStockDTO generateStockDTO) {
    Set<StockDTO> stockDTOs = logisticAggregate.generateStocks(
        uuid,
        generateStockDTO.expirationDate(),
        generateStockDTO.quantity())
      .stream()
      .map(stockWebMapper::toStockDTO).collect(Collectors.toSet());
    return Response.status(CREATED).entity(stockDTOs).build();
  }
}

