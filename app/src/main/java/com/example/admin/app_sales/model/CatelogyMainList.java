package com.example.admin.app_sales.model;

import java.util.ArrayList;

public class CatelogyMainList {
    int loaiThoiTrang;
    int maLoai;
    String tenLoai;
    ArrayList<Catelory> catelories;

    public CatelogyMainList(int loaiThoiTrang, int maLoai, String tenLoai, ArrayList<Catelory> catelories) {
        this.loaiThoiTrang = loaiThoiTrang;
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.catelories = catelories;
    }

    public int getLoaiThoiTrang() {
        return loaiThoiTrang;
    }

    public void setLoaiThoiTrang(int loaiThoiTrang) {
        this.loaiThoiTrang = loaiThoiTrang;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public ArrayList<Catelory> getCatelories() {
        return catelories;
    }

    public void setCatelories(ArrayList<Catelory> catelories) {
        this.catelories = catelories;
    }
}
