package com.example.admin.app_sales.model;

public class Catelory {
    int maLoaiCuThe;
    String tenLoaiCuThe;
    String url;

    public Catelory(int maLoaiCuThe, String tenLoaiCuThe, String url) {
        this.maLoaiCuThe = maLoaiCuThe;
        this.tenLoaiCuThe = tenLoaiCuThe;
        this.url = url;
    }

    public int getMaLoaiCuThe() {
        return maLoaiCuThe;
    }

    public void setMaLoaiCuThe(int maLoaiCuThe) {
        this.maLoaiCuThe = maLoaiCuThe;
    }

    public String getTenLoaiCuThe() {
        return tenLoaiCuThe;
    }

    public void setTenLoaiCuThe(String tenLoaiCuThe) {
        this.tenLoaiCuThe = tenLoaiCuThe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
