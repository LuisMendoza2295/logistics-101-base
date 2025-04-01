package com.logistics.base.domain.aggregate;

import com.logistics.base.domain.model.Product;
import com.logistics.base.domain.model.Stock;
import com.logistics.base.domain.model.StorageUnit;
import com.logistics.base.domain.model.Transfer;
import com.logistics.base.domain.repository.ProductRepository;
import com.logistics.base.domain.repository.StockRepository;
import com.logistics.base.domain.repository.StorageUnitRepository;
import com.logistics.base.domain.repository.TransferRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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

  @Override
  public Product findByProductUuid(String uuid) {
    return productRepository.findByUuid(uuid)
      .orElseThrow(() -> new RuntimeException("Product with uuid " + uuid + " not found"));
  }

  @Override
  @Transactional
  public Product persistProduct(Product product) {
    if (productRepository.findByName(product.name()).isPresent()) {
      throw new RuntimeException("Product with name " + product.name() + " already exists");
    }
    return productRepository.save(product);
  }

  @Override
  @Transactional
  public StorageUnit addProducts(String storageUnitUUID, Set<String> barcodes) {
    StorageUnit storageUnit = findByStorageUnitUuid(storageUnitUUID);
    Set<Stock> stocks = stockRepository.findByBarcodes(barcodes);
    StorageUnit toUpdateStorageUnit = storageUnit.addStocks(stocks);
    StorageUnit updatedStorageUnit = storageUnitRepository.save(toUpdateStorageUnit);

    Set<Stock> toUpdateStocks = stocks.stream()
      .map(stock -> stock.setStorageUnit(updatedStorageUnit))
      .collect(Collectors.toSet());
    stockRepository.saveAll(toUpdateStocks);
    return updatedStorageUnit;
  }

  @Override
  public StorageUnit findByStorageUnitUuid(String storageUnitUUID) {
    return storageUnitRepository.findByUuid(storageUnitUUID)
      .orElseThrow(() -> new RuntimeException("Storage Unit with uuid " + storageUnitUUID + " not found"));
  }

  @Override
  @Transactional
  public StorageUnit persistStorageUnit(StorageUnit storageUnit) {
    return storageUnitRepository.save(storageUnit);
  }

  @Override
  public Set<StorageUnit> findByType(String storageType) {
    return storageUnitRepository.findWithProductsQtyByType(storageType);
  }

  @Override
  @Transactional
  public Transfer transferProduct(String sourceStorageUUID, String targetStorageUUID, Set<String> barcodes) {
    StorageUnit sourceStorageUnit = findByStorageUnitUuid(sourceStorageUUID);
    StorageUnit targetStorageUnit = findByStorageUnitUuid(targetStorageUUID);

    Set<Stock> stocks = stockRepository.findByStorageUuidAndBarcodes(sourceStorageUUID, barcodes);
    Set<String> foundBarcodes = stocks.stream()
      .map(Stock::barcode)
      .collect(Collectors.toSet());
    if (stocks.size() < barcodes.size()) {
      Set<String> notFoundBarcodes = new HashSet<>(barcodes);
      notFoundBarcodes.removeAll(foundBarcodes);
      throw new RuntimeException("Stock entities with barcodes [" + notFoundBarcodes + "] do not exist in source storage unit");
    }

    StorageUnit toUpdateSourceStorageUnit = sourceStorageUnit.removeStocks(stocks);
    StorageUnit toUpdateTargetStorageUnit = targetStorageUnit.addStocks(stocks);

    Set<Stock> toUpdateStocks = stocks.stream()
      .map(stock -> stock.setStorageUnit(toUpdateTargetStorageUnit))
      .collect(Collectors.toSet());
    Set<Stock> updatedStocks = stockRepository.saveAll(toUpdateStocks);

    var tranferBuilder = Transfer.builder()
      .source(toUpdateSourceStorageUnit)
      .target(toUpdateTargetStorageUnit);
    updatedStocks.forEach(tranferBuilder::addStock);
    Transfer transfer = tranferBuilder.build();
    return transferRepository.save(transfer);
  }

  @Override
  @Transactional
  public Set<Stock> generateStocks(String storageUnitUUID, String productUUID, LocalDate expirationDate, Integer quantity) {
    StorageUnit storageUnit = findByStorageUnitUuid(storageUnitUUID);
    Product product = findByProductUuid(productUUID);

    // Generate stocks for StorageUnit and Product
    Set<Stock> stocks = IntStream.range(0, quantity)
      .mapToObj(i -> storageUnit.generateStock(product, expirationDate))
      .collect(Collectors.toSet());

    return stockRepository.saveAll(stocks);
  }

  @Override
  @Transactional
  public Set<Stock> generateStocks(String productUUID, LocalDate expirationDate, Integer quantity) {
    Product product = findByProductUuid(productUUID);

    // Generate stocks for Product
    Set<Stock> stocks = IntStream.range(0, quantity)
      .mapToObj(i -> product.generateStock(expirationDate))
      .collect(Collectors.toSet());

    return stockRepository.saveAll(stocks);
  }
}
