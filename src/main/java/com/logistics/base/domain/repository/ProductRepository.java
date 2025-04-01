package com.logistics.base.domain.repository;

import com.logistics.base.domain.model.Product;

import java.util.Optional;
import java.util.Set;

public interface ProductRepository {

  Optional<Product> findByUuid(String uuid);

  Optional<Product> findByName(String name);

  Set<Product> findByProductName(String productName);

  Set<Product> findByProductUUID(Set<String> productUUIDs);

  Product save(Product product);

  Set<Product> saveAll(Set<Product> products);
}
