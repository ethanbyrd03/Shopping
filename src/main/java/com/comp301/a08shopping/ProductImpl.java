package com.comp301.a08shopping;

public class ProductImpl implements Product {
  private final String _name;
  private final double _basePrice;
  private int _inventory;
  private double _salePrice;

  public ProductImpl(String name, double basePrice) {
    if (name == null || basePrice <= 0.00) {
      throw new IllegalArgumentException();
    }
    this._name = name;
    this._basePrice = basePrice;
    this._inventory = 0;
    this._salePrice = basePrice;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public double getBasePrice() {
    return _basePrice;
  }

  @Override
  public void setInventory(int amt) {
    if (amt < 0) {
      throw new IllegalArgumentException();
    }
    this._inventory = amt;
  }

  @Override
  public int getInventory() {
    return this._inventory;
  }

  @Override
  public void setSalePrice(double pctOff) {
    double sale = this._basePrice * pctOff;
    this._salePrice = this._basePrice - sale;
  }

  @Override
  public double getSalePrice() {
    return this._salePrice;
  }
}
