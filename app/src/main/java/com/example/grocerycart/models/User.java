package com.example.grocerycart.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String email;
    private String phone;
    private ArrayList<Product> products;

    public User(){}
    public User(String email, String phone) {
        this.email = email;
        this.phone = phone;
        this.products = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
