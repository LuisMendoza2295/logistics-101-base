package com.logistics.base;

import com.logistics.base.domain.*;
import com.logistics.base.repository.ProductRepository;
import com.logistics.base.repository.StockRepository;
import com.logistics.base.repository.StorageUnitRepository;
import com.logistics.base.repository.TransferRepository;
import com.logistics.base.repository.entity.ProductEntity;
import com.logistics.base.repository.entity.StockEntity;
import com.logistics.base.repository.entity.StorageUnitEntity;
import com.logistics.base.repository.entity.TransferEntity;
import com.logistics.base.repository.mapper.ProductDbMapper;
import com.logistics.base.repository.mapper.StockDbMapper;
import com.logistics.base.repository.mapper.StorageUniDbMapper;
import com.logistics.base.repository.mapper.TransferDbMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class LogisticService implements LogisticAggregate {

    @Inject
    ProductRepository productRepository;
    @Inject
    StorageUnitRepository storageUnitRepository;
    @Inject
    StockRepository stockRepository;
    @Inject
    TransferRepository transferRepository;

    @Inject
    ProductDbMapper productDbMapper;
    @Inject
    StorageUniDbMapper storageUniDbMapper;
    @Inject
    StockDbMapper stockDbMapper;
    @Inject
    TransferDbMapper transferDbMapper;

    @Override
    public Product findByProductUuid(String uuid) {
        return productRepository.findByUuid(uuid)
            .map(productEntity -> productDbMapper.toProduct(productEntity))
            .orElseThrow(() -> new RuntimeException("Product with uuid " + uuid + " not found"));
    }

    @Override
    @Transactional
    public Product persistProduct(Product product) {
        if (productRepository.findByName(product.name()).isPresent()) {
            throw new RuntimeException("Product with name " + product.name() + " already exists");
        }
        ProductEntity productEntity = productDbMapper.toProductEntity(product);
        productRepository.persist(productEntity);
        return productDbMapper.toProduct(productEntity);
    }

    @Override
    @Transactional
    public StorageUnit addProducts(String storageUnitUUID, Set<String> barcodes) {
        StorageUnit storageUnit = findByStorageUnitUuid(storageUnitUUID);
        Set<Stock> stocks = stockRepository.findByBarcodes(new ArrayList<>(barcodes)).stream()
            .map(stockDbMapper::toStock)
            .collect(Collectors.toSet());
        StorageUnit toUpdateStorageUnit = storageUnit.addProducts(stocks);
        StorageUnitEntity toUpdateStorageUnitEntity = storageUniDbMapper.toStorageUnitEntity(toUpdateStorageUnit);
        storageUnitRepository.persist(toUpdateStorageUnitEntity);

        Set<StockEntity> stockEntities = stocks.stream()
            .map(stock -> stock.setStorageUnit(toUpdateStorageUnit))
            .map(stockDbMapper::toStockEntity)
            .collect(Collectors.toSet());
        stockRepository.persist(stockEntities);
        return toUpdateStorageUnit;
    }

    @Override
    public StorageUnit findByStorageUnitUuid(String storageUnitUUID) {
        return storageUnitRepository.findByUuid(storageUnitUUID)
            .map(storageUnitEntity -> storageUniDbMapper.toStorageUnit(storageUnitEntity))
            .orElseThrow(() -> new RuntimeException("Storage Unit with uuid " + storageUnitUUID + " not found"));
    }

    @Override
    @Transactional
    public StorageUnit persistStorageUnit(StorageUnit storageUnit) {
        StorageUnitEntity storageUnitEntity = storageUniDbMapper.toStorageUnitEntity(storageUnit);
        storageUnitRepository.persist(storageUnitEntity);
        return storageUniDbMapper.toStorageUnit(storageUnitEntity);
    }

    @Override
    public Set<StorageUnit> findByType(String storageType) {
        return storageUnitRepository.findWithProductsQtyByType(storageType).stream()
            .map(storageUnitEntity -> storageUniDbMapper.toStorageUnit(storageUnitEntity))
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Transfer transferProduct(String sourceStorageUUID, String targetStorageUUID, Set<String> barcodes) {
        StorageUnit sourceStorageUnit = findByStorageUnitUuid(sourceStorageUUID);
        StorageUnit targetStorageUnit = findByStorageUnitUuid(targetStorageUUID);

        Set<Stock> stocks = stockRepository.findByStorageUuidAndBarcodes(sourceStorageUUID, new ArrayList<>(barcodes))
            .stream()
            .map(stockEntity -> stockDbMapper.toStock(stockEntity))
            .collect(Collectors.toSet());
        Set<String> foundBarcodes = stocks.stream()
            .map(Stock::barcode)
            .collect(Collectors.toSet());
        if (stocks.size() < barcodes.size()) {
            Set<String> notFoundBarcodes = new HashSet<>(barcodes);
            notFoundBarcodes.removeAll(foundBarcodes);
            throw new RuntimeException("Stock entities with barcodes [" + notFoundBarcodes + "] do not exist in source storage unit");
        }

        Set<Stock> toUpdateStocks = stocks.stream()
            .map(stock -> stock.setStorageUnit(sourceStorageUnit))
            .collect(Collectors.toSet());
        stockRepository.persist(toUpdateStocks.stream()
            .map(stock -> stockDbMapper.toStockEntity(stock))
            .collect(Collectors.toSet()));

        var tranferBuilder = Transfer.builder()
            .source(sourceStorageUnit)
            .target(targetStorageUnit);
        stocks.forEach(tranferBuilder::addStock);
        TransferEntity transferEntity = transferDbMapper.toTransferEntity(tranferBuilder.build());
        transferRepository.persist(transferEntity);

        return transferDbMapper.toTransfer(transferEntity);
    }

    @Override
    @Transactional
    public Set<Stock> generateStocks(String storageUnitUUID, String productUUID, LocalDate expirationDate, Integer quantity) {
        StorageUnit storageUnit = findByStorageUnitUuid(storageUnitUUID);
        Product product = findByProductUuid(productUUID);

        // Generate stocks for StorageUnit and Product
        Set<StockEntity> stockEntities = IntStream.range(0, quantity)
            .mapToObj(i -> storageUnit.generateStock(product, expirationDate))
            .map(stock -> stockDbMapper.toStockEntity(stock))
            .collect(Collectors.toSet());

        stockRepository.persist(stockEntities);
        return stockEntities.stream()
            .map(stockDbMapper::toStock)
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<Stock> generateStocks(String productUUID, LocalDate expirationDate, Integer quantity) {
        Product product = findByProductUuid(productUUID);

        // Generate stocks for Product
        Set<StockEntity> stockEntities = IntStream.range(0, quantity)
            .mapToObj(i -> product.generateStock(expirationDate))
            .map(stock -> stockDbMapper.toStockEntity(stock))
            .collect(Collectors.toSet());

        stockRepository.persist(stockEntities);
        return stockEntities.stream()
            .map(stockDbMapper::toStock)
            .collect(Collectors.toSet());
    }
}
