package com.logistics.base.repository;

import com.logistics.base.domain.model.Product;
import com.logistics.base.domain.repository.ProductRepository;
import com.logistics.base.repository.entity.ProductEntity;
import com.logistics.base.repository.mapper.ProductDbMapper;
import com.logistics.base.repository.panache.ProductPanacheRepository;
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
  ProductDbMapper productDbMapper;

  @Override
  public Optional<Product> findByUuid(String uuid) {
    return productPanacheRepository.findByUuid(uuid)
      .map(productDbMapper::toProduct);
  }

  @Override
  public Optional<Product> findByName(String name) {
    return productPanacheRepository.findByName(name)
      .map(productDbMapper::toProduct);
  }

  @Override
  public Set<Product> findByProductName(String productName) {
    return productPanacheRepository.findByProductName(productName).stream()
      .map(productDbMapper::toProduct)
      .collect(Collectors.toSet());
  }

  @Override
  public Set<Product> findByProductUUID(Set<String> productUUIDs) {
    return productPanacheRepository.findByProductUUID(productUUIDs).stream()
      .map(productDbMapper::toProduct)
      .collect(Collectors.toSet());
  }

  @Override
  public Product save(Product product) {
    ProductEntity productEntity = productDbMapper.toProductEntity(product);
    productPanacheRepository.persist(productEntity);
    return productDbMapper.toProduct(productEntity);
  }

  @Override
  public Set<Product> saveAll(Set<Product> products) {
    Set<ProductEntity> productEntities = products.stream()
      .map(product -> productDbMapper.toProductEntity(product))
      .collect(Collectors.toSet());
    productPanacheRepository.persist(productEntities);
    return productEntities.stream()
      .map(productEntity -> productDbMapper.toProduct(productEntity))
      .collect(Collectors.toSet());
  }
}
