package com.example.admin.app_sales.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private int maDOnHang;
    private Customer customer;
    private String ghiChu;
    private String ngayDat;
    private double phiShip;
    private double tongTien;
    private int trangThai;
    private String ngayThanhToan;
    private ArrayList<DetailOrder> chitiet;

    public Order(int maDOnHang, Customer customer, String ghiChu, String ngayDat, double phiShip, double tongTien, int trangThai, String ngayThanhToan, ArrayList<DetailOrder> chitiet) {
        this.maDOnHang = maDOnHang;
        this.customer = customer;
        this.ghiChu = ghiChu;
        this.ngayDat = ngayDat;
        this.phiShip = phiShip;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.ngayThanhToan = ngayThanhToan;
        this.chitiet = chitiet;
    }

    public int getMaDOnHang() {
        return maDOnHang;
    }

    public void setMaDOnHang(int maDOnHang) {
        this.maDOnHang = maDOnHang;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public double getPhiShip() {
        return phiShip;
    }

    public void setPhiShip(double phiShip) {
        this.phiShip = phiShip;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(String ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public ArrayList<DetailOrder> getChitiet() {
        return chitiet;
    }

    public void setChitiet(ArrayList<DetailOrder> chitiet) {
        this.chitiet = chitiet;
    }
}
