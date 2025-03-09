package com.logistics.base.repository;

import com.logistics.base.repository.entity.ProductEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<ProductEntity> {

  public Optional<ProductEntity> findByUuid(String uuid) {
    return find("uuid", uuid).firstResultOptional();
  }

  public Optional<ProductEntity> findByName(String name) {
    return find("name", name).firstResultOptional();
  }

  public List<ProductEntity> findByProductName(String productName) {
    return list("productName", productName);
  }

  public List<ProductEntity> findByProductUUID(Set<String> productUUIDs) {
    return list("uuid in ?1", productUUIDs);
  }
}
