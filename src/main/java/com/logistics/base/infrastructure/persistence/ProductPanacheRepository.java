package com.logistics.base.infrastructure.persistence;

import com.logistics.base.infrastructure.persistence.entity.ProductEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductPanacheRepository implements PanacheRepository<ProductEntity> {

  public Optional<ProductEntity> findByUuid(String uuid) {
    return find("uuid", uuid).firstResultOptional();
  }

  public Optional<ProductEntity> findByName(String name) {
    return find("name", name).firstResultOptional();
  }

  public Set<ProductEntity> findByProductName(String productName) {
    return find("productName", productName).stream()
      .collect(Collectors.toSet());
  }

  public Set<ProductEntity> findByProductUUIDs(Set<String> productUUIDs) {
    return find("uuid in ?1", productUUIDs).stream()
      .collect(Collectors.toSet());
  }
}
