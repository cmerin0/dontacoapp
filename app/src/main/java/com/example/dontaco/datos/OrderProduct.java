package com.example.dontaco.datos;

public class OrderProduct {
    private String id;
    private String productId;
    private String productName;
    private String productPrice;
    private String quantity;
    private String total;

    public OrderProduct() {
    }

    public OrderProduct(String productId, String productName, String productPrice, String quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;

        this.total = String.valueOf(Double.parseDouble(this.productPrice) * Integer.parseInt(this.quantity));
    }

    public String toString() {
        return "Producto: " + this.productName + "\n" +
               "Precio unitario: $" + this.productPrice + "\n" +
               "Cantidad: " + this.quantity + "\n" +
               "Total: $" + this.total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() { return total; }
}
