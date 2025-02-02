package com.example.grocerycart.models;

public class Product {
    private String ProductName;
    private int quantity;

    public Product(){}

    public Product(String productName, int quantity) {
        ProductName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "Product{name='" + ProductName + "', quantity='" + quantity + "'}";
    }

}
