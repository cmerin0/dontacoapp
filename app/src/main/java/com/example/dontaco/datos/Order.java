package com.example.dontaco.datos;

import java.util.Calendar;
import java.util.Date;

public class Order {
    private String productsQuantity;
    private String productsTotal;
    private String date;

    public Order() {
    }

    public Order(String productsQuantity, String productsTotal) {
        this.productsQuantity = productsQuantity;
        this.productsTotal = productsTotal;

        Date currentTime = Calendar.getInstance().getTime();
        this.date = currentTime.toString();
    }

    public String toString() {
        return "Fecha de compra: " + this.date + "\n" +
               "Cantidad de productos: " + this.productsQuantity + "\n" +
               "Total pagado: $" + this.productsTotal + "\n";
    }

    public String getProductsQuantity() {
        return productsQuantity;
    }

    public void setProductsQuantity(String productsQuantity) {
        this.productsQuantity = productsQuantity;
    }

    public String getProductsTotal() {
        return productsTotal;
    }

    public void setProductsTotal(String productsTotal) {
        this.productsTotal = productsTotal;
    }

    public String getDate() {
        return date;
    }
}
