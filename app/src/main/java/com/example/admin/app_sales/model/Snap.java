package com.example.admin.app_sales.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Snap implements Serializable {
    String SnapText;
    ArrayList<Product> products;
    int orientation;

    public Snap(String snapText, ArrayList<Product> products, int orientation) {
        SnapText = snapText;
        this.products = products;
        this.orientation = orientation;
    }

    public String getSnapText() {
        return SnapText;
    }

    public void setSnapText(String snapText) {
        SnapText = snapText;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
