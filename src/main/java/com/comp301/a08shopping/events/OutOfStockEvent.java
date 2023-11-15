package com.comp301.a08shopping.events;

import com.comp301.a08shopping.Product;
import com.comp301.a08shopping.Store;

public class OutOfStockEvent implements StoreEvent {
  private final Product _product;
  private final Store _store;

  public OutOfStockEvent(Product product, Store store) {
    this._product = product;
    this._store = store;
  }

  @Override
  public Product getProduct() {
    return this._product;
  }

  @Override
  public Store getStore() {
    return this._store;
  }
}
