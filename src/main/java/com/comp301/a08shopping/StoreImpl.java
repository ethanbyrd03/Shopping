package com.comp301.a08shopping;

import com.comp301.a08shopping.events.*;
import java.util.*;

public class StoreImpl implements Store {
  private final String _name;
  private List<StoreObserver> _customers;
  private List<Product> _products;

  public StoreImpl(String name) {
    if (name == null) {
      throw new IllegalArgumentException();
    }
    this._name = name;
    this._customers = new ArrayList<>();
    this._products = new ArrayList<>();
  }

  @Override
  public String getName() {
    return this._name;
  }

  @Override
  public void addObserver(StoreObserver observer) {
    this._customers.add(observer);
  }

  @Override
  public void removeObserver(StoreObserver observer) {
    this._customers.remove(observer);
  }

  @Override
  public List<Product> getProducts() {
    return this._products;
  }

  @Override
  public Product createProduct(String name, double basePrice, int inventory) {
    Product result = new ProductImpl(name, basePrice);
    result.setInventory(inventory);
    _products.add(result);
    return result;
  }

  @Override
  public ReceiptItem purchaseProduct(Product product) {
    int ind = _products.indexOf(product);
    int inv1 = _products.get(ind).getInventory();
    _products.get(ind).setInventory(inv1 - 1);
    StoreEvent i = new PurchaseEvent(product, this);
    notify(i);
    if (getIsOnSale(product)) {
      return new ReceiptItemImpl(product.getName(), product.getSalePrice(), this._name);
    } else {
      return new ReceiptItemImpl(product.getName(), product.getBasePrice(), this._name);
    }
  }

  @Override
  public void restockProduct(Product product, int numItems) {
    int index = this._products.indexOf(product);
    this._products.get(index).setInventory(numItems);
    StoreEvent i = new BackInStockEvent(product, this);
    notify(i);
  }

  @Override
  public void startSale(Product product, double percentOff) {
    int index = this._products.indexOf(product);
    this._products.get(index).setSalePrice(percentOff);
    StoreEvent i = new SaleStartEvent(product, this);
    notify(i);
  }

  @Override
  public void endSale(Product product) {
    int index = this._products.indexOf(product);
    this._products.get(index).setSalePrice(1.00);
    StoreEvent i = new SaleEndEvent(product, this);
    notify(i);
  }

  @Override
  public int getProductInventory(Product product) {
    int index = this._products.indexOf(product);
    return this._products.get(index).getInventory();
  }

  @Override
  public boolean getIsInStock(Product product) {
    int inv = getProductInventory(product);
    if (inv > 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public double getSalePrice(Product product) {
    int ind = _products.indexOf(product);
    return _products.get(ind).getSalePrice();
  }

  @Override
  public boolean getIsOnSale(Product product) {
    int ind = _products.indexOf(product);
    double salePrice = _products.get(ind).getSalePrice();
    double basePrice = _products.get(ind).getBasePrice();
    if (salePrice < basePrice) {
      return true;
    } else {
      return false;
    }
  }

  private void notify(StoreEvent event) {
    for (StoreObserver o : _customers) {
      int ind = _customers.indexOf(o);
      _customers.get(ind).update(event);
    }
  }
}
