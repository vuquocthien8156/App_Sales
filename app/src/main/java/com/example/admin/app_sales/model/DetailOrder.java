package com.example.admin.app_sales.model;

import java.io.Serializable;

public class DetailOrder implements Serializable {
    private Product product;
    private double donGia;
    private int soLuong;
    private double thanhtien;
    private int trangthai;

    public DetailOrder(Product product, double donGia, int soLuong, double thanhtien, int trangthai) {
        this.product = product;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.thanhtien = thanhtien;
        this.trangthai = trangthai;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(double thanhtien) {
        this.thanhtien = thanhtien;
    }
}
