package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.DetailOrderActivity;
import com.example.admin.app_sales.activity.HistoryActivity;
import com.example.admin.app_sales.model.DetailOrder;
import com.example.admin.app_sales.model.Order;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {

    ArrayList<Order> orders;
    Context context;

    public AdapterOrder(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recy_detailoder , viewGroup , false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Order o = orders.get(i);

        viewHolder.txtDonHang.setText("Đơn hàng: " + o.getMaDOnHang());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            viewHolder.txtTGDat.setText("Ngày đặt " + format.format(format.parse(o.getNgayDat())));
            viewHolder.txtTGThanhToan.setText("Ngày thanh toán " + o.getNgayThanhToan() != "" ? format.format(format.parse(o.getNgayThanhToan())) : "");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.txtTrangthai.setText(o.getTrangThai() == 0 ? "Chưa thanh toán" : "Đã thanh toán");
        int sl = 0;
        double tong = 0;
        for(DetailOrder d : o.getChitiet()){
            sl += d.getSoLuong();
            tong += d.getThanhtien();
        }
        viewHolder.txtSoLuong.setText(sl + "");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtTongTien.setText(decimalFormat.format(tong) + "đ");

        viewHolder.recyclerView.setAdapter(new AdapterDetailOrder(o.getChitiet() , context));
        viewHolder.recyclerView.addItemDecoration(new DividerItemDecoration(context , DividerItemDecoration.VERTICAL));
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false));


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView txtChiTiet ,  txtDonHang , txtTGDat , txtTGThanhToan , txtTrangthai , txtSoLuong , txtTongTien;
        LinearLayout ln;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyDetailOrder);
            txtDonHang = itemView.findViewById(R.id.txtDonHang);
            txtTGDat = itemView.findViewById(R.id.txtTGDat);
            txtTGThanhToan = itemView.findViewById(R.id.txtTGThanhToan);
            txtTrangthai = itemView.findViewById(R.id.txtTrangThaii);
            txtSoLuong = itemView.findViewById(R.id.txtTongSL);
            txtTongTien = itemView.findViewById(R.id.txtTongTienn);
            ln = itemView.findViewById(R.id.lnorder);
            txtChiTiet = itemView.findViewById(R.id.txtChitiet);
            txtChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , DetailOrderActivity.class);
                    intent.putExtra("donhang" , orders.get(getAdapterPosition()));
                    ((HistoryActivity)context).startActivity(intent);
                }
            });
        }
    }
}
