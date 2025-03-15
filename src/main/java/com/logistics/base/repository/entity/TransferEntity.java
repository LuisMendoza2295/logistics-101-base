package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "transfers")
public class TransferEntity extends PanacheEntity {
    @Column(unique = true)
    public String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_storage_unit_id")
    public StorageUnitEntity sourceStorage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_storage_unit_id")
    public StorageUnitEntity targetStorage;

    @ManyToMany
    @JoinTable(
        name = "transfer_stocks",
        joinColumns = {@JoinColumn(name = "transfer_id")},
        inverseJoinColumns = {@JoinColumn(name = "stock_id")}
    )
    public Set<StockEntity> stocks;
}
