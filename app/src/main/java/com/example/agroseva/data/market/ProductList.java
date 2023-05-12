package com.example.agroseva.data.market;

import java.util.List;

public class ProductList {
    List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "ProductList{" +
                "products=" + products +
                '}';
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}


