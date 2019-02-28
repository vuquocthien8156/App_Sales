package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.model.DetailOrder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterDetailOrder extends
        RecyclerView.Adapter<AdapterDetailOrder.ViewHolder> {

    ArrayList<DetailOrder> detailOrders;
    Context context;

    public AdapterDetailOrder(ArrayList<DetailOrder> detailOrders, Context context) {
        this.detailOrders = detailOrders;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_sub_detail , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DetailOrder d = detailOrders.get(i);
        Picasso.get()
                .load(d.getProduct().getUrl())
                .into(viewHolder.img);
        viewHolder.txtName.setText(d.getProduct().getName());
        viewHolder.txtGia.setText(((new DecimalFormat("###,###,###")).format(d.getDonGia())) + "đ");
        viewHolder.txtSoLuong.setText("x" + d.getSoLuong());
    }

    @Override
    public int getItemCount() {
        return detailOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txtName , txtGia , txtTrangThai , txtSoLuong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgOrder);
            txtName = itemView.findViewById(R.id.txtNameOrder);
            txtGia = itemView.findViewById(R.id.txtGiaOrder);
            txtTrangThai = itemView.findViewById(R.id.txtTrangthaiOrder);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuongOrder);
        }
    }
}
