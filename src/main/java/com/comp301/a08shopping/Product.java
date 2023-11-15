package com.comp301.a08shopping;

public interface Product {
  /** Gets the product's name */
  String getName();

  /** Gets the product's base price in dollars */
  double getBasePrice();

  void setInventory(int amt);

  int getInventory();

  void setSalePrice(double pctOff);

  double getSalePrice();
}
