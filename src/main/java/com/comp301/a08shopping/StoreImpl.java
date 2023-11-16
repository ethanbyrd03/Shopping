package com.comp301.a08shopping;

import com.comp301.a08shopping.events.*;
import com.comp301.a08shopping.exceptions.OutOfStockException;
import com.comp301.a08shopping.exceptions.ProductNotFoundException;
import java.util.*;

public class StoreImpl implements Store {
  private final String _name;
  private final List<StoreObserver> _customers;
  private final List<Product> _products;

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
    if (observer == null) {
      throw new IllegalArgumentException();
    }
    this._customers.add(observer);
  }

  @Override
  public void removeObserver(StoreObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException();
    }
    this._customers.remove(observer);
  }

  @Override
  public List<Product> getProducts() {
    List<Product> a = new ArrayList<>(this._products);
    return a;
  }

  @Override
  public Product createProduct(String name, double basePrice, int inventory) {
    if (name == null || basePrice < 0.00 || inventory < 0) {
      throw new IllegalArgumentException();
    }
    Product result = new ProductImpl(name, basePrice);
    result.setInventory(inventory);
    _products.add(result);
    return result;
  }

  @Override
  public ReceiptItem purchaseProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int ind = _products.indexOf(product);
    int inv1 = _products.get(ind).getInventory();
    if (inv1 == 0) {
      throw new OutOfStockException();
    }
    _products.get(ind).setInventory(inv1 - 1);
    if (_products.get(ind).getInventory() == 0) {
      StoreEvent j = new OutOfStockEvent(product, this);
      notify(j);
    }
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
    if (product == null || numItems < 0) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int index = this._products.indexOf(product);
    if (this._products.get(index).getInventory() == 0) {
      StoreEvent i = new BackInStockEvent(product, this);
      notify(i);
    }
    this._products.get(index).setInventory(numItems);
  }

  @Override
  public void startSale(Product product, double percentOff) {
    if (product == null || percentOff > 1.00) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int index = this._products.indexOf(product);
    this._products.get(index).setSalePrice(percentOff);
    StoreEvent i = new SaleStartEvent(product, this);
    notify(i);
  }

  @Override
  public void endSale(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int index = this._products.indexOf(product);
    this._products.get(index).setSalePrice(0.00);
    StoreEvent i = new SaleEndEvent(product, this);
    notify(i);
  }

  @Override
  public int getProductInventory(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int index = this._products.indexOf(product);
    return this._products.get(index).getInventory();
  }

  @Override
  public boolean getIsInStock(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int inv = getProductInventory(product);
    return inv > 0;
  }

  @Override
  public double getSalePrice(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int ind = _products.indexOf(product);
    return _products.get(ind).getSalePrice();
  }

  @Override
  public boolean getIsOnSale(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!this._products.contains(product)) {
      throw new ProductNotFoundException();
    }
    int ind = _products.indexOf(product);
    double salePrice = _products.get(ind).getSalePrice();
    double basePrice = _products.get(ind).getBasePrice();
    return salePrice < basePrice;
  }

  private void notify(StoreEvent event) {
    for (StoreObserver o : _customers) {
      int ind = _customers.indexOf(o);
      _customers.get(ind).update(event);
    }
  }
}
