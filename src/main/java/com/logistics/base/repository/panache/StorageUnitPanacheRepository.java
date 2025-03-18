package com.logistics.base.repository.panache;

import com.logistics.base.repository.entity.ProductEntity;
import com.logistics.base.repository.entity.StorageUnitEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class StorageUnitPanacheRepository implements PanacheRepository<StorageUnitEntity> {

    public Optional<StorageUnitEntity> findByUuid(String uuid) {
        var query = getEntityManager().createQuery("SELECT su, p, count(p.id) " +
            "FROM storageUnits su " +
            "LEFT JOIN stocks s ON su.id = s.storageUnit.id " +
            "LEFT JOIN products p ON p.id = s.product.id " +
            "WHERE su.uuid = :uuid " +
            "GROUP BY su.id, p.id", Object[].class);
        query.setParameter("uuid", uuid);

        return parseRows(query.getResultList()).stream().findFirst();
    }

    public Set<StorageUnitEntity> findWithProductsQtyByType(String storageType) {
        var query = getEntityManager().createQuery("SELECT su, p, count(p.id) " +
            "FROM storageUnits su " +
            "LEFT JOIN stocks s ON su.id = s.storageUnit.id " +
            "LEFT JOIN products p ON p.id = s.product.id " +
            "WHERE su.storageType = :type " +
            "GROUP BY su.id, p.id", Object[].class);
        query.setParameter("type", storageType);

        return parseRows(query.getResultList());
    }

    private Optional<StorageUnitEntity> parseRow(Object[] row) {
        List<Object[]> rows = new ArrayList<>();
        rows.add(row);
        return parseRows(rows).stream().findFirst();
    }

    private Set<StorageUnitEntity> parseRows(List<Object[]> rows) {
        Map<Long, StorageUnitEntity> storageUnitMap = new HashMap<>();
        for (Object[] row : rows) {
            StorageUnitEntity storageUnitEntity = (StorageUnitEntity) row[0];
            if (storageUnitMap.containsKey(storageUnitEntity.id())) {
                storageUnitEntity = storageUnitMap.get(storageUnitEntity.id());
            }
            ProductEntity productEntity = (ProductEntity) row[1];
            if (productEntity != null) {
                storageUnitEntity.addProductWithQty(productEntity, ((Long) row[2]).intValue());
            }
            storageUnitMap.put(storageUnitEntity.id(), storageUnitEntity);
        }
        return new HashSet<>(storageUnitMap.values());
    }
}
