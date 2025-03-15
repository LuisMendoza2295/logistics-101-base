package com.logistics.base;

import com.logistics.base.domain.*;
import com.logistics.base.repository.ProductRepository;
import com.logistics.base.repository.StockRepository;
import com.logistics.base.repository.StorageUnitRepository;
import com.logistics.base.repository.entity.ProductEntity;
import com.logistics.base.repository.entity.StockEntity;
import com.logistics.base.repository.entity.StorageUnitEntity;
import com.logistics.base.repository.mapper.ProductEntityMapper;
import com.logistics.base.repository.mapper.StockEntityMapper;
import com.logistics.base.repository.mapper.StorageUnitEntityMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class LogisticService implements LogisticAggregate {

    @Inject
    ProductRepository productRepository;
    @Inject
    StorageUnitRepository storageUnitRepository;
    @Inject
    StockRepository stockRepository;

    @Inject
    ProductEntityMapper productEntityMapper;
    @Inject
    StorageUnitEntityMapper storageUnitEntityMapper;
    @Inject
    StockEntityMapper stockEntityMapper;

    @Override
    public Product findByProductUuid(String uuid) {
        return productRepository.findByUuid(uuid)
            .map(productEntity -> productEntityMapper.toProduct(productEntity))
            .orElseThrow(() -> new RuntimeException("Product with uuid " + uuid + " not found"));
    }

    @Override
    @Transactional
    public Product persistProduct(Product product) {
        if (productRepository.findByName(product.name()).isPresent()) {
            throw new RuntimeException("Product with name " + product.name() + " already exists");
        }
        ProductEntity productEntity = productEntityMapper.toProductEntity(product);
        productRepository.persist(productEntity);
        return productEntityMapper.toProduct(productEntity);
    }

    @Override
    @Transactional
    public StorageUnit addProducts(String storageUnitUUID, Set<String> barcodes) {
        Optional<StorageUnitEntity> currentStorageUnitEntityOptional = storageUnitRepository.findByUuid(storageUnitUUID);
        if (currentStorageUnitEntityOptional.isEmpty()) {
            throw new RuntimeException("Storage unit with uuid " + storageUnitUUID + " not found");
        }
        Set<Stock> stocks = stockRepository.findByBarcodes(new ArrayList<>(barcodes)).stream()
            .map(stockEntityMapper::toStock)
            .collect(Collectors.toSet());
        StorageUnit toUpdateStorageUnit = storageUnitEntityMapper.toStorageUnit(currentStorageUnitEntityOptional.get())
            .addProducts(stocks);
        StorageUnitEntity toUpdateStorageUnitEntity = storageUnitEntityMapper.toStorageUnitEntity(toUpdateStorageUnit);
        storageUnitRepository.persist(toUpdateStorageUnitEntity);
        return storageUnitEntityMapper.toStorageUnit(toUpdateStorageUnitEntity);
    }

    @Override
    @Transactional
    public StorageUnit persistStorageUnit(StorageUnit storageUnit) {
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
    public Transfer transferProduct(String sourceStorageUUID, String targetStorageUUID, Set<String> barcodes) {
        StorageUnitEntity sourceStorageUnitEntity = storageUnitRepository.findByUuid(sourceStorageUUID)
            .orElseThrow(() -> new RuntimeException("Storage Unit with uuid " + sourceStorageUUID + " not found"));
        StorageUnitEntity targetStorageUnitEntity = storageUnitRepository.findByUuid(targetStorageUUID)
            .orElseThrow(() -> new RuntimeException("Storage Unit with uuid " + sourceStorageUUID + " not found"));

        List<StockEntity> stockEntities = stockRepository.findByBarcodes(new ArrayList<>(barcodes));

        StorageUnit sourceStorageUnit = storageUnitEntityMapper.toStorageUnit(sourceStorageUnitEntity);
        StorageUnit targetStorageUnit = storageUnitEntityMapper.toStorageUnit(targetStorageUnitEntity);
        return new Transfer(0L, UUID.randomUUID(), sourceStorageUnit, targetStorageUnit, new HashSet<>());
    }

    @Override
    @Transactional
    public Set<Stock> generateStocks(String storageUnitUUID, String productUUID, LocalDate expirationDate, Integer quantity) {
        ProductEntity productEntity = productRepository.findByUuid(productUUID)
            .orElseThrow(() -> new RuntimeException("Product with uuid " + productUUID + " not found"));
        StorageUnitEntity storageUnitEntity = storageUnitRepository.findByUuid(storageUnitUUID)
            .orElseThrow(() -> new RuntimeException("Storage Unit with uuid " + storageUnitUUID + " not found"));

        StorageUnit storageUnit = storageUnitEntityMapper.toStorageUnit(storageUnitEntity);
        Product product = productEntityMapper.toProduct(productEntity);
        Set<Stock> stocks = new HashSet<>();
        for (int i = 0; i < quantity; i++) {
            Log.infof("iteration: %d of %d", i, quantity);
            Stock stock = storageUnit.generateStock(product, expirationDate);
            Log.infof("generated stock: %s", stock);
            stocks.add(stock);
        }

        Set<StockEntity> stockEntities = stocks.stream()
            .map(stock -> stockEntityMapper.toStockEntity(stock))
            .collect(Collectors.toSet());
        stockEntities.forEach(stockEntity -> {
            stockEntity.product = productEntity;
            stockEntity.storageUnit = storageUnitEntity;
        });
        stockRepository.persist(stockEntities);
        return stocks;
    }
}
