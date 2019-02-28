package com.example.admin.app_sales.model;

import java.io.Serializable;

public class Customer implements Serializable {
    int ID;
    String TenKH;
    String Email = null;
    String DiaChi = "";
    String SDT;
    String Avata;
    int khach_chinh;
    int macDinh;

    public Customer(int id , String tenKH, String email, String SDT, String avata , int khach_chinh , String diaChi , int macdinh) {
        ID = id;
        TenKH = tenKH;
        Email = email;
        this.SDT = SDT;
        Avata = avata;
        this.khach_chinh = khach_chinh;
        this.DiaChi = diaChi;
        this.macDinh = macdinh;
    }

    public int getMacDinh() {
        return macDinh;
    }

    public void setMacDinh(int macDinh) {
        this.macDinh = macDinh;
    }
    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String tenKH) {
        TenKH = tenKH;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getAvata() {
        return Avata;
    }

    public void setAvata(String avata) {
        Avata = avata;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getKhach_chinh() {
        return khach_chinh;
    }

    public void setKhach_chinh(int khach_chinh) {
        this.khach_chinh = khach_chinh;
    }

    public Customer(){

    }

    public Customer(Customer cus){
        setID(cus.getID());
        setTenKH(cus.getTenKH());
        setEmail(cus.getEmail());
        setSDT(cus.getSDT());
        setDiaChi(cus.getDiaChi());
        setKhach_chinh(cus.getKhach_chinh());
        setAvata(cus.getAvata());
        setMacDinh(cus.getMacDinh());
    }
}
