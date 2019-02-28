package com.example.admin.app_sales.model;

import java.io.Serializable;

public class Cart  implements Serializable{
    String Masp;
    String name;
    String gia;
    String url;
    int soluong;

    public Cart(String Masp , String name, String gia, String url, int soluong) {
        this.Masp = Masp;
        this.name = name;
        this.gia = gia;
        this.url = url;
        this.soluong = soluong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasp() {
        return Masp;
    }

    public void setMasp(String masp) {
        Masp = masp;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
