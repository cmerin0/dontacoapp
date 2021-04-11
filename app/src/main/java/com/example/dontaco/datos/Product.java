package com.example.dontaco.datos;

public class Product {
    private String id;
    private String name;
    private String price;

    public Product() {
    }

    public Product(String name, String price) {
        this.id = "";
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return this.name + " - $" + this.price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
