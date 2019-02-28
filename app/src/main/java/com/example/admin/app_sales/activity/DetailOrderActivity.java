package com.example.admin.app_sales.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.model.DetailOrder;
import com.example.admin.app_sales.model.Order;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailOrderActivity extends AppCompatActivity {
    TextView txtDonHang , txtNgayDat , txtTrangThai , txtNgayThanhToan , txtNameUser
            , txtPhone , txtAddress , txtSoLuong , txtTongTien , txtTienVanChuyen , txtTongCong;

    TableLayout tableLayout;
    int sl = 0;
    double tongtien = 0;
    double tongcong = 0;
    TableRow.LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Chi Tiết đơn hàng");
        setContentView(R.layout.activity_detail_order);

        tableLayout = findViewById(R.id.tbSanPham);
        params = new TableRow.LayoutParams();
        params.setMargins(2 , 3 , 2 , 3);
        Order o = (Order) getIntent().getSerializableExtra("donhang");
        map();
        loadRowHead();
        DecimalFormat dec = new DecimalFormat("###,###,###");
        for (DetailOrder de : o.getChitiet()){
            TableRow row = new TableRow(this);
            TextView txtTen = new TextView(this);
            txtTen.setText(de.getProduct().getName());
            txtTen.setGravity(Gravity.CENTER);
            txtTen.setLayoutParams(params);
            txtTen.setBackgroundColor(Color.WHITE);
            row.addView(txtTen);

            TextView txtGia = new TextView(this);
            txtGia.setText(dec.format(de.getDonGia()));
            txtGia.setGravity(Gravity.CENTER);
            txtGia.setBackgroundColor(Color.WHITE);
            txtGia.setLayoutParams(params);
            row.addView(txtGia);

            TextView txtSoLuong = new TextView(this);
            txtSoLuong.setText(de.getSoLuong() + "");
            txtSoLuong.setGravity(Gravity.CENTER);
            txtSoLuong.setBackgroundColor(Color.WHITE);
            txtSoLuong.setLayoutParams(params);
            row.addView(txtSoLuong);

            TextView txtThanhTien = new TextView(this);
            txtThanhTien.setText(dec.format(de.getThanhtien()) + "");
            txtThanhTien.setGravity(Gravity.END);
            txtThanhTien.setBackgroundColor(Color.WHITE);
            txtThanhTien.setLayoutParams(params);
            row.addView(txtThanhTien);

            tableLayout.addView(row);

            sl += de.getSoLuong();
            tongtien += de.getThanhtien();
        }

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        txtDonHang.setText("Đơn hàng : " + o.getMaDOnHang());
        try {
            txtNgayDat.setText("Ngày đặt " + f.format(f.parse(o.getNgayDat())));
            txtNgayThanhToan.setText(!o.getNgayThanhToan().equals("") ? "Ngày thanh toán " + f.format(f.parse(o.getNgayThanhToan())) : "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtNameUser.setText(o.getCustomer().getTenKH());
        txtPhone.setText(o.getCustomer().getSDT());
        txtAddress.setText(o.getCustomer().getDiaChi());
        txtSoLuong.setText(sl+"");
        txtTongTien.setText(dec.format(tongtien) + "đ");
        txtTongCong.setText(dec.format(tongtien) + "đ");
    }

    void map(){
        txtDonHang = findViewById(R.id.txtDonHangg);
        txtNgayDat = findViewById(R.id.txtNgayDat);
        txtTrangThai = findViewById(R.id.txtTrangthaiOrder);
        txtNgayThanhToan = findViewById(R.id.txtNgayThanhToan);
        txtNameUser = findViewById(R.id.txtNameUser);
        txtPhone = findViewById(R.id.txtPhoneUser);
        txtAddress = findViewById(R.id.txtAddressUser);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtTienVanChuyen = findViewById(R.id.txtTienShip);
        txtSoLuong = findViewById(R.id.txtSoLuongDetailOrder);
        txtTongCong = findViewById(R.id.txtTongCongTien);

    }

    private void loadRowHead() {
        TableRow tableRow = new TableRow(this);
        TextView txt = new TextView(this);
        txt.setText("Tên");
        txt.setBackgroundColor(Color.WHITE);
        txt.setGravity(Gravity.CENTER);
        txt.setLayoutParams(params);
        tableRow.addView(txt);
        txt = new TextView(this);
        txt.setText("Giá");
        txt.setBackgroundColor(Color.WHITE);
        txt.setGravity(Gravity.CENTER);
        txt.setLayoutParams(params);
        tableRow.addView(txt);
        txt = new TextView(this);
        txt.setText("Số lượng");
        txt.setBackgroundColor(Color.WHITE);
        txt.setGravity(Gravity.CENTER);
        txt.setLayoutParams(params);
        tableRow.addView(txt);
        txt = new TextView(this);
        txt.setText("Thành tiền");
        txt.setBackgroundColor(Color.WHITE);
        txt.setGravity(Gravity.CENTER);
        txt.setLayoutParams(params);
        tableRow.addView(txt);

        tableLayout.addView(tableRow);
    }
}
