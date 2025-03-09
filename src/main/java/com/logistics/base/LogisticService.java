package com.logistics.base;

import com.logistics.base.domain.LogisticAggregate;
import com.logistics.base.domain.Product;
import com.logistics.base.domain.StorageUnit;
import com.logistics.base.domain.Transfer;
import com.logistics.base.repository.ProductRepository;
import com.logistics.base.repository.StorageUnitRepository;
import com.logistics.base.repository.entity.ProductEntity;
import com.logistics.base.repository.entity.StorageUnitEntity;
import com.logistics.base.repository.mapper.ProductEntityMapper;
import com.logistics.base.repository.mapper.StorageUnitEntityMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class LogisticService implements LogisticAggregate {

    @Inject
    ProductRepository productRepository;

    @Inject
    StorageUnitRepository storageUnitRepository;

    @Inject
    ProductEntityMapper productEntityMapper;

    @Inject
    StorageUnitEntityMapper storageUnitEntityMapper;

    @Override
    public Product findByProductUuid(String uuid) {
        return productRepository.findByUuid(uuid)
            .map(productEntity -> productEntityMapper.toProduct(productEntity))
            .orElseThrow(() -> new NotFoundException("Product with uuid " + uuid + " not found"));
    }

    @Override
    @Transactional
    public Product persist(Product product) {
        if (productRepository.findByName(product.name()).isPresent()) {
            throw new RuntimeException("Product with name " + product.name() + " already exists");
        }
        ProductEntity productEntity = productEntityMapper.toProductEntity(product);
        productRepository.persist(productEntity);
        return productEntityMapper.toProduct(productEntity);
    }

    @Override
    @Transactional
    public StorageUnit addProducts(String storageUnitUUID, Map<String, Integer> productUUIDsWithQty) {
        Optional<StorageUnitEntity> currentStorageUnitOptional = storageUnitRepository.findByUuid(storageUnitUUID);
        if (currentStorageUnitOptional.isEmpty()) {
            throw new RuntimeException("Storage unit with uuid " + storageUnitUUID + " not found");
        }
        Set<ProductEntity> productEntities = new HashSet<>(productRepository.findByProductUUID(productUUIDsWithQty.keySet()));
        StorageUnit toUpdateStorageUnit = storageUnitEntityMapper.toStorageUnit(currentStorageUnitOptional.get())
            .addProducts(products);
        StorageUnitEntity toUpdateStorageUnitEntity = storageUnitEntityMapper.toStorageUnitEntity(toUpdateStorageUnit);
        storageUnitRepository.persist(toUpdateStorageUnitEntity);
        return storageUnitEntityMapper.toStorageUnit(toUpdateStorageUnitEntity);
    }

    @Override
    @Transactional
    public StorageUnit persist(StorageUnit storageUnit) {
        StorageUnitEntity storageUnitEntity = storageUnitEntityMapper.toStorageUnitEntity(storageUnit);
        storageUnitRepository.persist(storageUnitEntity);
        return storageUnitEntityMapper.toStorageUnit(storageUnitEntity);
    }

    @Override
    public Set<StorageUnit> findByType(String storageType) {
        return storageUnitRepository.findByType(storageType).stream()
            .map(storageUnitEntity -> storageUnitEntityMapper.toStorageUnit(storageUnitEntity))
            .collect(Collectors.toSet());
    }

    @Override
    public Transfer transferProduct(String sourceUUID, String targetUUID, Map<String, Integer> productUUIDsWithQty) {
        throw new UnsupportedOperationException();
    }
}
