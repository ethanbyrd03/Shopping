package com.comp301.a08shopping;

import com.comp301.a08shopping.events.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerImpl implements Customer {
  private final String _name;
  private double _budget;
  private List<ReceiptItem> _history;

  public CustomerImpl(String name, double budget) {
    if (name == null || budget < 0.00) {
      throw new IllegalArgumentException();
    }
    this._name = name;
    this._budget = budget;
    this._history = new ArrayList<>();
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public double getBudget() {
    return _budget;
  }

  @Override
  public void purchaseProduct(Product product, Store store) {
    if (product == null || store == null) {
      throw new IllegalArgumentException();
    }
    if (product.getBasePrice() > this._budget) {
      return;
    }
    ReceiptItem receipt = store.purchaseProduct(product);
    this._history.add(receipt);
    this._budget -= receipt.getPricePaid();
  }

  @Override
  public List<ReceiptItem> getPurchaseHistory() {
    return _history;
  }

  @Override
  public void update(StoreEvent event) {
    if (event.getClass() == SaleStartEvent.class) {
      System.out.println(
          "New sale for "
              + event.getProduct().getName()
              + " at "
              + event.getStore().getName()
              + "!");
    }
    if (event.getClass() == SaleEndEvent.class) {
      System.out.println(
          "The sale for "
              + event.getProduct().getName()
              + " at "
              + event.getStore().getName()
              + " has ended");
    }
    if (event.getClass() == PurchaseEvent.class) {
      System.out.println(
          "Someone purchased "
              + event.getProduct().getName()
              + " at "
              + event.getStore().getName());
    }
    if (event.getClass() == OutOfStockEvent.class) {
      System.out.println(
          event.getProduct().getName() + " is now out of stock at " + event.getStore().getName());
    }
    if (event.getClass() == BackInStockEvent.class) {
      System.out.println(
          event.getProduct().getName() + " is back in stock at " + event.getStore().getName());
    }
  }
}
