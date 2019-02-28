package com.example.admin.app_sales.model;

import java.io.Serializable;

public class Product implements Serializable{
    private int id;
    private String name;
    private String mota;
    private int Gia;
    private String url;
    private int loaisanpham;

    public Product(int id, String name, String mota, int gia, String url, int loaisanpham) {
        this.id = id;
        this.name = name;
        this.mota = mota;
        Gia = gia;
        this.url = url;
        this.loaisanpham = loaisanpham;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getGia() {
        return Gia;
    }

    public void setGia(int gia) {
        Gia = gia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLoaisanpham() {
        return loaisanpham;
    }

    public void setLoaisanpham(int loaisanpham) {
        this.loaisanpham = loaisanpham;
    }

    @Override
    public String toString() {
        return  name ;
    }
}
