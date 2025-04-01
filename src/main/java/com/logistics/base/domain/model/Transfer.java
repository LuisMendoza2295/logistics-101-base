package com.logistics.base.domain.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record Transfer(
  Long id,
  UUID uuid,
  StorageUnit source,
  StorageUnit target,
  Set<Stock> stocks) {

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    Builder builder = new Builder()
      .id(id)
      .uuid(uuid)
      .source(source)
      .target(target);
    stocks.forEach(builder::addStock);
    return builder;
  }

  public static final class Builder {
    private final Set<Stock> stocks = new HashSet<>();
    private Long id;
    private UUID uuid;
    private StorageUnit source;
    private StorageUnit target;

    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder uuid(String uuid) {
      this.uuid = Optional.ofNullable(uuid).map(UUID::fromString).orElse(UUID.randomUUID());
      return this;
    }

    public Builder uuid(UUID uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder source(StorageUnit source) {
      this.source = source;
      return this;
    }

    public Builder target(StorageUnit target) {
      this.target = target;
      return this;
    }

    public Builder addStock(Stock stock) {
      this.stocks.add(stock);
      return this;
    }

    public Transfer build() {
      return new Transfer(
        id,
        Optional.ofNullable(uuid).orElse(UUID.randomUUID()),
        source,
        target,
        stocks
      );
    }
  }
}
