package com.logistics.base.repository;

import com.logistics.base.domain.model.Product;
import com.logistics.base.domain.repository.ProductRepository;
import com.logistics.base.infrastructure.persistence.entity.ProductEntity;
import com.logistics.base.infrastructure.persistence.mapper.ProductEntityMapper;
import com.logistics.base.infrastructure.persistence.ProductPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository {

  @Inject
  ProductPanacheRepository productPanacheRepository;
  @Inject
  ProductEntityMapper productEntityMapper;

  @Override
  public Optional<Product> findByUuid(String uuid) {
    return productPanacheRepository.findByUuid(uuid)
      .map(productEntityMapper::toProduct);
  }

  @Override
  public Optional<Product> findByName(String name) {
    return productPanacheRepository.findByName(name)
      .map(productEntityMapper::toProduct);
  }

  @Override
  public Set<Product> findByProductName(String productName) {
    return productPanacheRepository.findByProductName(productName).stream()
      .map(productEntityMapper::toProduct)
      .collect(Collectors.toSet());
  }

  @Override
  public Set<Product> findByProductUUID(Set<String> productUUIDs) {
    return productPanacheRepository.findByProductUUIDs(productUUIDs).stream()
      .map(productEntityMapper::toProduct)
      .collect(Collectors.toSet());
  }

  @Override
  public Product save(Product product) {
    ProductEntity productEntity = productEntityMapper.toProductEntity(product);
    productPanacheRepository.persist(productEntity);
    return productEntityMapper.toProduct(productEntity);
  }

  @Override
  public Set<Product> saveAll(Set<Product> products) {
    Set<ProductEntity> productEntities = products.stream()
      .map(product -> productEntityMapper.toProductEntity(product))
      .collect(Collectors.toSet());
    productPanacheRepository.persist(productEntities);
    return productEntities.stream()
      .map(productEntity -> productEntityMapper.toProduct(productEntity))
      .collect(Collectors.toSet());
  }
}
